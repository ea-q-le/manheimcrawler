package utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Simple config reader file that returns property values
 * by calling 'getProperty()' and passing the requested key.
 * Assumes the properties to be stored in 'application.config' file.
 *
 * @author Shahin 'Sean' Gadimov
 */
public class ConfigReader {
	private static Properties properties;

	static {
		try {
			String path = "application.config";

			FileInputStream stream = new FileInputStream(path);

			properties = new Properties();
			properties.load(stream);

			stream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getProperty(String key) {
		return properties.getProperty(key);
	}
}
