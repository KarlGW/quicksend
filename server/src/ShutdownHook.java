import java.net.*;
import java.util.ArrayList;

public final class ShutdownHook {

	private Socket socket = null;
	private ServerSocket serverSocket = null;
	private Socket clientSocket = null;
	private ArrayList<Thread> connections = null;

	private Logger srvLog = new Logger("server");

	public ShutdownHook(Socket socket) {
		this.socket = socket;
	}

	public ShutdownHook(ServerSocket serverSocket, Socket clientSocket) {
		this.serverSocket = serverSocket;
		this.socket = clientSocket;
		srvLog.write("Creating shutdown hook.");
	}

	public ShutdownHook(ServerSocket serverSocket, Socket clientSocket, ArrayList<Thread> connections) {
		this.serverSocket = serverSocket;
		this.socket = clientSocket;
		this.connections = connections;
	}
	
	public synchronized void addShutdownHook() {
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			@Override
			public void run() {
				srvLog.write("Shutting down...");

				if (!connections.isEmpty()) {
					srvLog.write("Trying to interrupt client threads...");
					for (Thread t : connections) {
						t.interrupt();
					}

					for (Thread t : connections) {
						if (t.isInterrupted()) {
							// Do something
						}
					}
					// Code to verify all threads interruption
				}
				try {
					if (socket != null) { 
						socket.close();
						if (socket.isClosed()) {
							srvLog.write("Connection with client closed.");
						} 
					}

					if (serverSocket != null) { 
						serverSocket.close(); 
						if (serverSocket.isClosed()) {
							srvLog.write("Server socket closed.");
						}
					}
					srvLog.write("Shutdown tasks completed.");
				} catch (Exception ex) {
					ex.printStackTrace();
				}	
			}
		}));	
	}
	
}