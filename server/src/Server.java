// New Server code
//package se.kaguwa.quicksend.server;
import java.net.*;
import java.util.ArrayList;
//import se.kaguwa.quicksend.utilities.*;

public class Server {
	
	private ServerSocket serverSocket;
	private int port = 4449;
	private String uploadDir = "/home/karl/uploads";
	private boolean running = true;
	private Logger srvLog = new Logger("server");
	private ArrayList<Thread> connections = null;

	public Server() {
	}

	public void init() {
		try {
			// Other initiation methods
			serverSocket = new ServerSocket(port);
		} catch (Exception ex) {
			System.err.println("Could not create socket.");
		}
	}

	public void start() {
		// Starts the server
		srvLog.write("Staring server...");
		Socket clientSocket = null;
		connections = new ArrayList<Thread>();
		try {
			
			ShutdownHook hook = new ShutdownHook(serverSocket, clientSocket, connections);
			hook.addShutdownHook();
			
			while(running) {
				//ShutdownHook hook = new ShutdownHook(serverSocket, clientSocket);
				//hook.addShutdownHook()
				clientSocket = serverSocket.accept();
				Thread connection = new Thread(new ConnectionHandler(clientSocket, uploadDir));
				connections.add(connection);
				connection.start();
			}
		} catch (Exception ex) {
			System.out.println("Socket was closed.");
		}
	}
}