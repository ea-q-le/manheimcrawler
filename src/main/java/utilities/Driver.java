package utilities;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.safari.SafariDriver;

import io.github.bonigarcia.wdm.WebDriverManager;

/**
 * A singleton class that initializes the Driver based on the application
 * configurations (see: 'application.config' file). Provides 'getDriver()' and
 * 'closeDriver()' for initialization and closing.
 * It is lazily loaded and thread safe.
 *
 * @author Shahin 'Sean' Gadimov
 */
public class Driver {

	// avoid any Reflection access to the private Constructor
	private Driver() {
		if (driver != null) {
			throw new RuntimeException("Use getDriver() to create an instance");
		}
	}

	private static volatile WebDriver driver = null;

	// ThreadSafe measurements through synchronization
	public static WebDriver getDriver() {
		// lazily loading logic
		if (driver == null) {
			
			// additional null check within the synchronization
			//  is necessary to make sure that another thread
			//  has not already created an instance of the driver
			synchronized (Driver.class) {
				if (driver == null) {
					switch (ConfigReader.getProperty("browser")) {

					case "chrome":
						WebDriverManager.chromedriver().setup();
						driver = new ChromeDriver();
						break;
					case "chromeHeadless":
						WebDriverManager.chromedriver().setup();
						driver = new ChromeDriver(new ChromeOptions().setHeadless(true));
						break;

					case "firefox":
						WebDriverManager.firefoxdriver().setup();
						driver = new FirefoxDriver();
						break;
					case "firefoxHeadless":
						WebDriverManager.firefoxdriver().setup();
						driver = new FirefoxDriver(new FirefoxOptions().setHeadless(true));
						break;

					case "ie":
						if (BrowserUtils.ISMAC)
							throw new WebDriverException(
									"You are operating Mac OS which doesn't support Internet Explorer");
						WebDriverManager.iedriver().setup();
						driver = new InternetExplorerDriver();
						break;

					case "edge":
						WebDriverManager.edgedriver().setup();
						driver = new EdgeDriver();
						break;

					case "safari":
						if (BrowserUtils.ISWINDOWS)
							throw new WebDriverException("You are operating Windows OS which doesn't support Safari");
						WebDriverManager.getInstance(SafariDriver.class).setup();
						driver = new SafariDriver();
						break;
					}
				}
			}
			
		}

		driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
		return driver;
	}

	public static void closeDriver() {
		if (driver != null) {
			driver.quit();
			driver = null;
		}
	}
}
