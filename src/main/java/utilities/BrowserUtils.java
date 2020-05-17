package utilities;

import java.text.*;
import java.util.*;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Consists of common utility functions that can be handy for
 * browser related manipulations.
 *
 * @author Shahin 'Sean' Gadiomov
 */
public class BrowserUtils {
	public static final boolean ISWINDOWS;
	public static final boolean ISMAC;

	static {
		ISWINDOWS = System.getProperty("os.name").toLowerCase().contains("windows");
		ISMAC = System.getProperty("os.name").toLowerCase().contains("mac");
	}

	public static boolean verifyTextMatches(String str1, String str2) {
		return str1.equals(str2);
	}

	public static boolean verifyTextContains(String str1, String str2) {
		return str1.contains(str2);
	}

	public static void wait(int secs) {
		try {
			Thread.sleep(1000 * secs);
		} catch (InterruptedException e) {
		}
	}
	public static void waitQt() {
		try { Thread.sleep(250); } catch (InterruptedException e) { }
	}

	/**
	 * Given List<WebElement>, returns the List<String> of texts of the
	 * elements by .getText()
	 * 
	 * @param elements as List<WebElement>
	 * @return List<String>
	 */
	public static List<String> webelementTextList(List<WebElement> elements) {
		List<String> retList = new ArrayList<String>();
		for (WebElement each : elements)
			retList.add( each.getText() );
		return retList;
	}
	
	/**
	 * Wait for the page to completely load
	 * @param driver
	 */
	public static void waitForLoad(WebDriver driver) {
        ExpectedCondition<Boolean> pageLoadCondition = new
                ExpectedCondition<Boolean>() {
                    public Boolean apply(WebDriver driver) {
                        return ((JavascriptExecutor)driver).executeScript("return document.readyState").equals("complete");
                    }
                };
        WebDriverWait wait = new WebDriverWait(driver, 240);
        wait.until(pageLoadCondition);
    }
	public static void waitForLoad() {
		WebDriver driver = Driver.getDriver();
        ExpectedCondition<Boolean> pageLoadCondition = new
                ExpectedCondition<Boolean>() {
                    public Boolean apply(WebDriver driver) {
                        return ((JavascriptExecutor)driver).executeScript("return document.readyState").equals("complete");
                    }
                };
        WebDriverWait wait = new WebDriverWait(driver, 240);
        wait.until(pageLoadCondition);
    }
	
	/**
	 * Given a DateFormat, returns today's date per the format.
	 * @param dateFormat the String format to be used
	 * @return today's date in a given format
	 */
	public static String todaysDate(String dateFormat) {
		DateFormat df = new SimpleDateFormat(dateFormat);
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        return df.format(date);
	}

	/**
	 * Given a text parameter fetched from the Auction page as
	 * 'WALDORF MOBILE SALE (OPEN) 46 vehicles  May 18, 2020  10:20 AM, EDT'
	 * this specific method extracts the date as
	 * 'May 18 2020  10:20 AM' as the time format of 'MMM dd yyyy hh:mm'
	 * in order to be further parsed into a Timestamp
	 * @param webText as a special String format shown above
	 * @return String of date extracted from the text in the given format
	 */
	public static String runDateTimeExtractor(String webText) {
		if (webText.toLowerCase().contains("vehicles")) {
			webText = webText.substring(
					webText.toLowerCase().indexOf("vehicles")
					+ "vehicles".length())
					.trim();
			
			if (webText.toLowerCase().contains(" am,"))
				webText = webText.substring(0, webText.toLowerCase().indexOf(" am,") + 3)
						.trim();
			else if (webText.toLowerCase().contains(" pm,"))
				webText = webText.substring(0, webText.toLowerCase().indexOf(" pm,") + 3)
						.trim();
			else return null;
			
			return webText.replace(",", "");
		}
		
		return null;
	}
}
