package com.soywiz.vitaorganizer.tasks

import com.soywiz.vitaorganizer.GameEntry
import com.soywiz.vitaorganizer.PsvitaDevice
import com.soywiz.vitaorganizer.VitaTaskQueue
import java.util.zip.ZipFile
import javax.swing.JOptionPane

class SendDataToVitaTask(val entry: GameEntry) : VitaTask() {
	override fun perform() {
		updateStatus("Sending game ${entry.id}...")
		//val zip = ZipFile(entry.vpkFile)
		try {
			PsvitaDevice.uploadGame(entry.id, ZipFile(entry.vpkFile), filter = { path ->
				// Skip files already installed in the VPK
				if (path == "eboot.bin" || path.startsWith("sce_sys/")) {
					false
				} else {
					true
				}
			}) { status ->
				//println("$status")
				updateStatus("Uploading ${entry.id} :: ${status.fileRange} :: ${status.sizeRange}")
			}
			//statusLabel.text = "Processing game ${vitaGameCount + 1}/${vitaGameIds.size} ($gameId)..."
		} catch (e: Throwable) {
			error(e.toString())
		}
		updateStatus("Sent game data ${entry.id}")
		info("Game ${entry.id} sent successfully")
	}
}