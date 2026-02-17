use anyhow::anyhow;
use dashmap::DashMap;
use log::info;
use protobuf::Message;
use tokio::sync::mpsc::Sender;
use waitmap::WaitMap;

use crate::{
    firmware::create_firmware,
    proto::{
        main::{JavaRust, MessageType, RustJava},
        upload::MultipartUpload,
    },
};

pub struct FirmwareUpload {
    pub current_length: u64,
    pub parts: Vec<Vec<u8>>,
}

pub struct Handler {
    messages: WaitMap<String, (JavaRust, Sender<RustJava>)>,
    firmware_uploads: DashMap<String, FirmwareUpload>,
}

const HARD_FIRMWARE_MAX: u64 = 1024 * 1024 * 64;

impl Handler {
    pub fn new() -> Self {
        Handler {
            messages: WaitMap::new(),
            firmware_uploads: DashMap::new(),
        }
    }

    pub async fn handle(
        &self,
        message: JavaRust,
        sender: Sender<RustJava>,
    ) -> Result<(), anyhow::Error> {
        let enum_value = message
            .message_type
            .enum_value()
            .map_err(|err| anyhow!("failed to parse enum, got value: {}", err))?;

        match enum_value {
            MessageType::UPLOAD_FIRMWARE => {
                let upload_message = MultipartUpload::parse_from_bytes(&message.data)?;
                let existing_upload = self
                    .firmware_uploads
                    .contains_key(&upload_message.upload_id);
                if !existing_upload {
                    let upload = FirmwareUpload {
                        current_length: upload_message.data.len() as u64,
                        parts: vec![upload_message.data],
                    };
                    self.firmware_uploads
                        .insert(upload_message.upload_id, upload);
                    return Ok(());
                }

                let mut existing_upload = self
                    .firmware_uploads
                    .get_mut(&upload_message.upload_id)
                    .unwrap();
                existing_upload.current_length += upload_message.data.len() as u64;

                if existing_upload.current_length > HARD_FIRMWARE_MAX {
                    drop(existing_upload);
                    self.firmware_uploads.remove(&upload_message.upload_id);

                    let mut rt_msg = RustJava::new();
                    rt_msg.id = message.id;
                    rt_msg.message_type = MessageType::ERROR.into();
                    rt_msg.data = format!("upload failed").as_bytes().to_vec();
                    sender.send(rt_msg).await?;

                    return Ok(());
                }

                existing_upload.parts.push(upload_message.data);
                if upload_message.next {
                    return Ok(());
                }

                drop(existing_upload);
                let (_, firmware_upload) = self
                    .firmware_uploads
                    .remove(&upload_message.upload_id)
                    .unwrap();

                let firmware_id = create_firmware(firmware_upload).await?;

                let mut rt_msg = RustJava::new();
                rt_msg.id = message.id;
                rt_msg.message_type = MessageType::UPLOAD_FIRMWARE.into();
                rt_msg.data = firmware_id.into_bytes();
                sender.send(rt_msg).await?;
            },
            MessageType::PING => {
                let mut rt_msg = RustJava::new();
                rt_msg.message_type = MessageType::PING.into();
                rt_msg.id = message.id;
                rt_msg.data = message.data;
                sender.send(rt_msg).await?;
            }
            _ => {
                self.messages.insert(message.id.clone(), (message, sender));
            }
        }

        Ok(())
    }
}
