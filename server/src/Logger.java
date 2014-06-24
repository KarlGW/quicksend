import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Logger
{
	private String dirName;
	private String filename;
	private File logFile;

	BufferedWriter writer;

	StringWriter sw = new StringWriter();
	PrintWriter pw = new PrintWriter(sw);

	public Logger(String type)
	{
		String appDirName = getAppPath();
		File appDir = new File(appDirName);
		dirName = "logs";
		filename = type + ".log";
		
		File dir = new File(appDir, dirName);
		if (! dir.exists())
		{
			dir.mkdirs();
		}
			
		logFile = new File(dir, filename);
		if (! logFile.exists())
		{
			try
			{
				logFile.createNewFile();	
			}
			catch (IOException ex)
			{
				ex.printStackTrace();
			}	
		}		
	}

	public void write(String logMsg)
	{
		try
		{
			writer = new BufferedWriter(new FileWriter(logFile, true));
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Calendar cal = Calendar.getInstance();
			String timestamp = dateFormat.format(cal.getTime());
			String logMessage = timestamp + ": " + logMsg + "\n";
			writer.write(logMessage);
			writer.close();
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
		}
	}

	public String getAppPath()
	{
		String firstString = new File(
			Logger.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getParent();
		File firstTier = new File(firstString);
		String path = firstTier.getParent();
		return path;
	}
}