//package se.kaguwa.quicksend.server;
import java.io.*;
import java.util.HashMap;

public class Config {

	private String address;
	private String port;
	private String uploadDirectory;
	private String applicationDirectory;
	private String logDirectory;
	private String cfgFileName = "quicksend.cfg";
	private String[] allowedParams = {"tcpport", "tcpaddress", "upload_directory", 
									  "application_directory", "log_directory"};
	private HashMap<String, String> configParams;

	private File cfgFile;

	public String getAddress() {
		return address;
	}

	public String getApplicationDirectory() {
		return applicationDirectory;
	}

	public String getLogDirectory() {
		return logDirectory;
	}

	public String getPort() {
		return port;
	}

	public String getUploadDirectory() {
		return uploadDirectory;
	}

	// Method to read a configuration file
	public void readConfigFile() {
		
		try {
			//String appDirName = getAppPath();
			String appDirName = new File(".").getPath();
			File appDir = new File(appDirName);
			cfgFile = new File(appDir, cfgFileName);
			if (!cfgFile.exists()) {
				System.out.println("File was not found. Creating empty file.");
				cfgFile.createNewFile();
			}

			parseConfig(cfgFile);
			for (int i = 0; i < allowedParams.length; i++) {
				if (configParams.containsKey(allowedParams[i])) {
					String value = configParams.get(allowedParams[i]);
					if (allowedParams[i] == allowedParams[0]) {
						this.port = value;
					} else if (allowedParams[i] == allowedParams[1]) {
						this.address = value;
					} else if (allowedParams[i] == allowedParams[2]) {
						this.uploadDirectory = value;
					} else if (allowedParams[i] == allowedParams[3]) {
						this.applicationDirectory = value;
					} else if (allowedParams[i] == allowedParams[4]) {
						this.logDirectory = value;
					}
				}
			}

			if (logDirectory == null) {
				this.logDirectory = applicationDirectory + "/logs";
			}

		} catch (IOException ex) {
			System.err.println("Could not read configuration file.");
		}
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setApplicationDirectory(String applicationDirectory) {
		this.applicationDirectory = applicationDirectory;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public void setLogDirectory(String logDirectory) {
		this.logDirectory = logDirectory;
	}

	public void setUploadDirectory(String uploadDirectory) {
		this.uploadDirectory = uploadDirectory;
	}

	// Helper method to get the applications directory.
	private String getAppPath()
	{
		String firstString = new File(
			Config.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getParent();
		File firstTier = new File(firstString);
		String path = firstTier.getParent();
		return path;
	}

	public void parseConfig(File cfgFile) {
		try {
			configParams = new HashMap<String, String>();
			BufferedReader reader = new BufferedReader(new FileReader(cfgFile));
			String line = null;

			while((line = reader.readLine()) != null) {
				if (! line.startsWith("#") && ! line.equals("")) {
					String[] entry = line.split("=");
					configParams.put(entry[0], entry[1]);
				}
			}

		} catch (IOException ex) {
			System.err.println("Could not parse configuration file.");
		}
	}

	// Temporary
	public void printConfigParams() {
		System.out.println(configParams);
	}
}