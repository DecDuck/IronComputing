use crate::{IRONSERVER_PATH, handler::FirmwareUpload};
use std::{fs::create_dir_all, path::PathBuf};
use tokio::{fs::OpenOptions, io::AsyncWriteExt};

const FIRMWARE_DIR_PATH: &'static str = "firmware";

pub fn get_firmware_path(firmware_id: &str) -> Result<PathBuf, anyhow::Error> {
    let firmware_path = IRONSERVER_PATH.join(FIRMWARE_DIR_PATH);
    create_dir_all(&firmware_path)?;

    let firmware_path = firmware_path.join(format!("{}.bin", firmware_id));
    Ok(firmware_path)
}

pub async fn create_firmware(firmware: FirmwareUpload) -> Result<String, anyhow::Error> {
    let binary = firmware.parts.into_iter().flatten().collect::<Vec<u8>>();
    let firmware_id = uuid::Uuid::new_v4().to_string();

    let firmware_path = get_firmware_path(&firmware_id)?;
    let mut firmware_file = OpenOptions::new()
        .write(true)
        .truncate(true)
        .open(firmware_path)
        .await?;

    firmware_file.write_all(&binary).await?;

    Ok(firmware_id)
}
