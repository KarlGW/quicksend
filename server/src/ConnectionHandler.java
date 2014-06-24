// New ConnectionHandler code
//package se.kaguwa.quicksend.server;
import java.net.*;
import java.io.*;
//import se.kaguwa.quicksend.utilities;

public class ConnectionHandler implements Runnable {

	private Socket socket;
	private String uploadDir;
	private InputStream is;
	private OutputStream os;
	private DataInputStream dis;
	private DataOutputStream dos;

	public ConnectionHandler(Socket socket, String uploadDir) {
		// Establish the socket
		try {
			this.socket = socket;
			this.uploadDir = uploadDir;
		} catch (Exception ex) {
			System.err.println("Could not create socket for client @ ConnectionHandler");
		}
	}

	@Override
	public void run() {
		//Overrided run() method, this is the thread job
		try {
			is = socket.getInputStream();
			os = socket.getOutputStream();
			dis = new DataInputStream(new BufferedInputStream(is));
			dos = new DataOutputStream(new BufferedOutputStream(os));
		} catch (IOException ex) {
			System.err.println("Could not create streams.");
		}

		// Set a boolean to control the loop
		boolean connEstablished = true;
		while(connEstablished) {
			int command = receiveCommand();
			switch(command) {
				case 0:
					connEstablished = false;
					break;
				case 1:
					/*boolean auth = authorize();
					if (!auth) {
						connEstablished = false;
						System.out.println("Not authorized.");
					}*/
					break;
				case 2:
					receive();
					break;
				default:
					connEstablished = false;
					break;
			}
		}

		try {
			if (dis != null) dis.close();
		} catch (IOException ex) {
			System.err.println("Could not close streams and socket.");
		}
	}

	public int receiveCommand() {
		int command = -1;
		try {
			command = dis.readInt();
		} catch (IOException ex) {
			System.err.println("Could not read command");
		}
		return command;
	}

	public void receive() {
		try {
			File upDir = new File(uploadDir);
			FileHandler fileHandler = new FileHandler();
			fileHandler.writeFile(upDir, dis, dos);
		} catch (Exception ex) {
			System.err.println("Problem with writing file.");
		}
	}
}