/** Description of Client
 *
 * @author Karl Wallenius
 * @version 0.2 Jun 20, 2014
*/

import java.io.*;
import java.net.*;

public class Client {

	private String address;
	private int port;
	private boolean sendResult;
	private boolean connected;

	private Socket socket;
	private OutputStream os;
	private InputStream is;
	private DataOutputStream dos;
	private DataInputStream dis;

	public Client() {
	}

	public Client(String address, int port) {
		this.address = address;
		this.port = port;
	}

	// Häääääääääääääääär lever jag. Det är bra. Helt okej.
	public void connect() {

		try {
			socket = new Socket(address, port);
			os = socket.getOutputStream();
			is = socket.getInputStream();
			dis = new DataInputStream(is);
			dos = new DataOutputStream(os);
			connected = true;
		} catch (Exception ex) {
			// Remove this
			System.err.println("Could not connect.");
			connected = false;
		}
	}

	// Miiiiiiiiiiiiiiiiiiiiiljonprogramslägenhet, byggd för mig
	public void disconnect() {

		try {
			sendCommand(0);
			if (dos != null) { dos.close(); }
			if (dis != null) { dis.close(); }
			if (socket != null) { socket.close(); }
			connected = false;
		} catch (Exception ex) {
			// remove this
			System.err.println("Could not disconnect.");
		}
	}

	public boolean isConnected() {
		return connected;
	} 

	public String getAddress() {
		return address;
	}

	public int getPort() {
		return port;
	}

	public boolean getSendResult() {
		return sendResult;
	}

	public void send(File file) {
		sendCommand(2);
		FileHandler fileHandler = new FileHandler();
		fileHandler.sendFile(file, dis, dos);
		sendResult = fileHandler.getSendResult();
	}

	public void sendCommand(int command) {
		try {
			dos.writeInt(command);
			dos.flush();
		}
		catch (Exception ex) {
			// Remove this after testing
			System.err.println("Could not send command.");
		}
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setPort(int port) {
		this.port = port;
	}
}