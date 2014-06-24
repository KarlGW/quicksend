import java.io.*;

public class TestConfig {
	public static void main(String[] args) {
		Config config = new Config();
		
		try {
			File file = new File("quicksend.cfg");
			config.parseConfig(file);
			config.readConfigFile();
			System.out.println(config.getPort());
			System.out.println(config.getAddress());
			System.out.println(config.getUploadDirectory());
			System.out.println(config.getApplicationDirectory());
		} catch (Exception ex) {
			ex.printStackTrace();
			System.err.println("Wat");
		}
	}
}