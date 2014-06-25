// New Server code
//package se.kaguwa.quicksend.server;
import java.net.*;
import java.io.IOException;
import java.util.ArrayList;
//import se.kaguwa.quicksend.utilities.*;

public class Server {
	
	private ServerSocket serverSocket;
	private int port = 4449;
	private String uploadDir = "/home/karl/uploads";
	//private boolean running = true;
	private Logger srvLog = new Logger("server");
	private ArrayList<Thread> connections = null;

	static volatile boolean running = true;

	public Server() {}

	public void init() {
		try {
			// Other initiation methods
			serverSocket = new ServerSocket(port);
			connections = new ArrayList<Thread>();
		} catch (Exception ex) {
			System.err.println("Could not create socket.");
		}
	}

	public void start() {
		// Starts the server
		srvLog.write("Staring server...");
		Socket clientSocket = null;
		String remoteAddress = null;
		try {
			
			ShutdownHook hook = new ShutdownHook();
			hook.addShutdownHook();
			
			while(running) {
				clientSocket = serverSocket.accept();
				if (!serverSocket.isClosed()) {
					remoteAddress = clientSocket.getRemoteSocketAddress().toString().substring(1);
					ConnectionHandler client = new ConnectionHandler(clientSocket, uploadDir);
					Thread connection = new Thread(client);
					connection.setName(remoteAddress);
					connections.add(connection);
					connection.start();
				}
			}
		} catch (SocketException ex) {
			// Do nothing.
		} catch (IOException ex) {
			System.err.println("Could not open socket for listening.");
		}
	}

	public void stop() {
		try {

			if (serverSocket != null) {
				serverSocket.close();
				if (serverSocket.isClosed()) {
					System.out.println("Server socket closed.");
				}
			}

			System.out.println("Shutdown tasks completed.");
		} catch (Exception ex) {
			System.err.println("Trouble stopping.");
			ex.printStackTrace();
		}
	}

	private class ShutdownHook {
		public void addShutdownHook() {
			Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
				@Override
				public void run() {
					running = false;
					try {
						Thread.sleep(500);
					} catch (InterruptedException ex) {
						//
					}
					stop();
				}
			}));
		}
	}
}