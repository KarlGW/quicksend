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
	
	private volatile boolean connEstablished;

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

		ShutdownHook clientHook = new ShutdownHook();
		clientHook.addShutdownHook();
		// Set a boolean to control the loop
		connEstablished = true;
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
			if (dos != null) dos.close();
			if (socket != null) socket.close();
		} catch (IOException ex) {
			System.err.println("Could not close streams and socket.");
		}
		System.out.println("Ending run....");
	}

	public int receiveCommand() {
		int command = -1;
		try {
			command = dis.readInt();
		} catch (IOException ex) {
			return 0;
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

	public void close() {
		try {
			if (dis != null) dis.close();
			if (dos != null) dos.close();
			if (socket != null) socket.close();
		} catch (IOException ex) {
				System.err.println("Borked while closing streams.");
		}
	}

	private class ShutdownHook {
		public void addShutdownHook() {
			Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
				@Override
				public void run() {
					connEstablished = false;
					close();
				}
			}));
		}
	}
}