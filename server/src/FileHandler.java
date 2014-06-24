//package se.kaguwa.quicksend.utilities;

import java.io.*;

public class FileHandler {

	private boolean writeResult;
	private boolean sendResult;

	public FileHandler() {}

	// Method to receive and write the file
	public void writeFile(File uploadDir, DataInputStream dis, DataOutputStream dos) {

		FileOutputStream fos;

		try {
			// Check if upload directory exists
			if (! uploadDir.exists()) {
				uploadDir.mkdir();
			}

			// Get the filename and specify it's path
			String filename = dis.readUTF();
			File file = new File(uploadDir, filename);
			fos = new FileOutputStream(file);
			long filesize = dis.readLong();
			byte[] buffer = new byte[4096];
			int bytesRead = 0;
			int totalBytes = 0;

			while (totalBytes < filesize && (bytesRead = dis.read(buffer, 0, (int) Math.min(buffer.length, filesize - totalBytes))) > 0){
				fos.write(buffer, 0, bytesRead);
				totalBytes += bytesRead;
			}
			fos.close();

			if (file.length() == filesize) {
				writeResult = true;
			} else {
				writeResult = false;
			}

			dos.writeBoolean(writeResult);
			dos.flush();
			
		} catch (Exception ex) {
			System.err.println("Error writing file.");
		}
	}

	public void sendFile(File file, DataInputStream dis, DataOutputStream dos) {
		FileInputStream fin;
		try {
			String filename = file.getName();
			dos.writeUTF(filename);
			dos.flush();

			// Get the filesize and send it to the DataOutputStream
			long filesize = (long) file.length();
			dos.writeLong(filesize);
			fin = new FileInputStream(file);
			byte[] buffer = new byte[4096];
			int bytesRead = 0;
			int totalBytes = 0;
			String percent;
			// Read from the FileInputStream and write it to the DataOutputStream
			while (totalBytes < filesize && (bytesRead = fin.read(buffer, 0, (int) Math.min(buffer.length, filesize - totalBytes)))> 0) {
				dos.write(buffer, 0, bytesRead);
				totalBytes += bytesRead;
				dos.flush();
				percent = String.valueOf(Math.round((double) totalBytes / (double) filesize * 100));
				System.out.print("\r" + percent + "%");
			}
			System.out.println();

			sendResult = dis.readBoolean();
		}
		catch (Exception ex) { ex.printStackTrace(); }
	}

		// Used to get the results from the writeFile() method
	public boolean getWriteResult()
	{
		return writeResult;
	}

	// Used to get the results from the sendFile() method
	public boolean getSendResult()
	{
		return sendResult;
	}
}