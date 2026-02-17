use std::{fs::create_dir_all, path::PathBuf, sync::LazyLock};

use log::{info, warn};
use protobuf::Message;
use tokio::{
    io::{AsyncReadExt, AsyncWriteExt, BufReader, BufWriter},
    join,
    net::TcpListener,
};

use crate::{
    handler::Handler,
    proto::main::{JavaRust, RustJava},
};
use simple_logger::SimpleLogger;

mod bindgen;
mod firmware;
mod handler;
mod proto;
mod vm;

static IRONSERVER_PATH: LazyLock<PathBuf> = LazyLock::new(|| PathBuf::from("./ironserver"));

async fn real_main() -> Result<(), anyhow::Error> {
    let tcp_listener = TcpListener::bind("127.0.0.1:10450").await?;

    let (stream, _) = tcp_listener.accept().await?;
    let (read, write) = stream.into_split();

    info!("socket connected");

    let mut read = BufReader::new(read);
    let mut write = BufWriter::new(write);

    let (sender, mut receiver) = tokio::sync::mpsc::channel::<RustJava>(16);

    let send_task = tokio::spawn(async move {
        loop {
            let message = receiver.recv().await;
            let message = if let Some(message) = message {
                message
            } else {
                continue;
            };

            let mut message_buffer = Vec::new();
            if let Err(err) = message.write_to_vec(&mut message_buffer) {
                warn!("failed to serialize rust-java message: {:?}", err);
                continue;
            }

            let length = message_buffer.len();
            if let Err(err) = write.write_i64_le(length as i64).await {
                warn!("failed to write message length: {:?}", err);
                continue;
            }

            if let Err(err) = write.write_all(&message_buffer).await {
                warn!("failed to write message data: {:?}", err);
                panic!("corrupted stream");
            }

            if let Err(err) = write.flush().await {
                warn!("failed to flush buffer");
                panic!("corrupted stream");
            }
        }
    });

    let recieve_task = tokio::spawn(async move {
        let handler = Box::leak(Box::new(Handler::new()));
        loop {
            let length = match read.read_u64_le().await {
                Ok(v) => v,
                Err(err) => {
                    warn!("failed to read packet length: {:?}", err);
                    continue;
                }
            };

            let mut read_buf = vec![0u8; length as usize];
            if let Err(err) = read.read_exact(&mut read_buf).await {
                warn!("failed to read packet: {:?}", err);
                panic!("corrupted stream");
            }

            let message = match JavaRust::parse_from_bytes(&read_buf) {
                Ok(v) => v,
                Err(err) => {
                    warn!("failed to decode packet: {:?}", err);
                    continue;
                }
            };

            let sender = sender.clone();
            tokio::spawn(async {
                if let Err(err) = handler.handle(message, sender).await {
                    warn!("handle failed: {:?}", err);
                };
            });
        }
    });

    let handle_errors = join!(send_task, recieve_task);
    handle_errors.0?;
    handle_errors.1?;

    Ok(())
}

#[tokio::main]
async fn main() -> ! {
    SimpleLogger::new().init().unwrap();
    create_dir_all(&*IRONSERVER_PATH).expect("failed to create data dir");
    info!("starting ironserver...");

    loop {
        match real_main().await {
            Ok(_) => (),
            Err(err) => warn!("error while running app: {:?}", err),
        }
    }
}
