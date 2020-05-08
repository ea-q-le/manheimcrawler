package runner;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.bonigarcia.wdm.WebDriverManager;

public class Main {
	
	public static void main(String[] args) {
		
		// setup the driver
		WebDriverManager.chromedriver().setup();
		WebDriver driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
		driver.manage().window().maximize();
		
		// go to manheim.com
		driver.get("https://www.manheim.com/");
		
		// go to sign in page
		driver.findElement(By.linkText("Sign In")).click();
		
		// add username and password
		driver.findElement(By.id("user_username")).sendKeys("amerimond");
		driver.findElement(By.id("user_password")).sendKeys("ziadalnmir7777");
		driver.findElement(By.id("submit")).click();
		
		// click on Simulcast
		driver.findElement(By.linkText("Simulcast")).click();
		
		// wait for the page to load
		waitForLoad(driver);
		
		// store the names of all auctions
		List<WebElement> auctionElements = driver.findElements(By.cssSelector("tr>th"));
		List<String> auctions = new ArrayList<String>();
		for (WebElement auction : auctionElements)
			auctions.add(auction.getText());
System.out.println(auctions);

		// store the links to each auction vehicle lists
		List<WebElement> viewVehicleLinks = driver.findElements(By.cssSelector("tr>th>a"));

		// for each auction
		for (int i = 0; i < auctions.size(); i++) {
			String current = auctions.get(i);
System.out.println(current);

			// move on if the auction contains 0 vehicles
			if (current.contains("View all 0 vehicles")) continue;
			
// refresh the vehicle links
System.out.println("Waiting for page to load...");
waitForLoad(driver);

			viewVehicleLinks = driver.findElements(By.cssSelector("tr>th>a"));
			
			// click on the link to view all vehicles
			viewVehicleLinks.get(i).click();
			
System.out.println("Currently in : " + current);
			
			// go back to the Simulcast page to continue the loop
			driver.navigate().back();
		}
		
		// quit the driver
		try { Thread.sleep(10000); } catch(InterruptedException e) {}
		driver.quit();
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
        WebDriverWait wait = new WebDriverWait(driver, 30);
        wait.until(pageLoadCondition);
    }

}
