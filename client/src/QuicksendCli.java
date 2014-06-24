import java.io.*;
//import se.kaguwa.quicksendclient.*;

class QuicksendCli {

	public static void main(String[] args) {

		if (args.length < 1) {
			System.out.println("Please specify an address/hostname and a port");
			System.out.println("Usage: quicksend-cli <ip>:<port> <file>");
			System.exit(1);
		} else if (args.length > 1 && ! args[0].contains(":")) {
			System.out.println("Please specify port with seperator \":\"");
			System.out.println("Usage: quicksend-cli <ip>:<port> <file>");
			System.exit(1);
		} else if (args[0].equals("int") && args.length == 1) {
			System.out.println("Starting interactive mode.");
			System.exit(0);
		} else if (args.length == 1) {
			System.out.println("Please specify files");
			System.out.println("Usage: quicksend-cli <ip>:<port> <file>");
			System.exit(1);
		}

		QuicksendCli clientCli = new QuicksendCli();
		clientCli.go(args);
	}

	public void go(String[] args) {

		String address;
		int port;
		File[] files;
		
		String[] destinationArgs = args[0].split(":");
		address = destinationArgs[0];
		port = Integer.parseInt(destinationArgs[1]);

		int nrOfFiles = args.length - 1;
		files = new File[nrOfFiles];

		int fileCount = 0;
		for (int i = 1; i < args.length; i++) {
			files[fileCount] = new File(args[i]);
			fileCount++;
		}

		Client client = new Client(address, port);
		client.connect();
		if (! client.isConnected()) {
			System.err.println("Could not connect to " + address + ".");
			System.exit(1);
		}

		for (File file : files) {
			System.out.println("Sending " + file.getName() + "...");
			client.send(file);
			boolean sendResult = client.getSendResult();
			if (sendResult) {
				System.out.println(file.getName() + " was successfully uploaded.");
			}
			else {
				System.err.println("Upload of " + file.getName() + " failed.");
			}
		}
		client.disconnect();
	}
}