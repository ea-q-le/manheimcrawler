package runner;

import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import pages.*;
import utilities.*;

import static utilities.BrowserUtils.*;

public class Main {
	
	public static void main(String[] args) {
		
		// go to manheim.com
		HomePage.goToHomePage();
		
		// maximize window to avoid hidden element behavior
		Driver.getDriver().manage().window().maximize();
		
		// go to sign in page
		HomePage.goToSignInPage();
		
		// sign in to the system
		SignInPage.signIn();
		
		// go to Simulcast page
		MyManheimPage.goToSimulcast();
		
		// wait for the page to load
		waitForLoad();
		
		// store all auction names
		List<String> auctionNames = SimulcastPage.getAuctionNamesList();
System.out.println(auctionNames);

		// store the links to each auction vehicle lists
		List<WebElement> viewVehiclesLinkList = Driver.getDriver().findElements(By.cssSelector("tr>th>a"));

		// for each auction
		for (int i = 0; i < auctionNames.size(); i++) {
			String current = auctionNames.get(i);
System.out.println(current);

			// move on if the auction contains 0 vehicles
			if (current.contains("View all 0 vehicles")) continue;
			
			// refresh the vehicle links to avoid stale element exception
System.out.println("Waiting for page to load...");
			waitForLoad(Driver.getDriver());
			viewVehiclesLinkList = Driver.getDriver().findElements(By.cssSelector("tr>th>a"));
			
			// click on the link to view all vehicles within the auction
			viewVehiclesLinkList.get(i).click();
			
			// wait for the page to complete loading
			waitForLoad();
			
			// fetch the list of all CR buttons (vehicles with CR)
			List<WebElement> crLinks = Driver.getDriver().findElements(By.cssSelector("tr>td.make>div[name='sticker']>a.icon-cr"));
			
			// for each CR link
			for (int j = 0; j < crLinks.size(); j++) {
				// store current window information
				String parentWindow = Driver.getDriver().getWindowHandle();
				
System.out.println("Current window is: " + Driver.getDriver().getTitle());
				
				// click on the current CR link
				crLinks.get(j).click();
				try { Thread.sleep(1000); } catch(InterruptedException e) {}
								
				// fetch the newly opened window handle and switch to it
				Set<String> handles =  Driver.getDriver().getWindowHandles();
				for(String windowHandle  : handles) {
					if(!windowHandle.equals(parentWindow)) {
						Driver.getDriver().switchTo().window(windowHandle);			
System.out.println("Current window after switch is: " + Driver.getDriver().getTitle());
						
						// wait for the new window to finish loading
						waitForLoad(Driver.getDriver());
						
						// switch to the Frame where vehicle info is stored
						// this frame ID is true for both NEW and OLD CR windows
						Driver.getDriver().switchTo().frame("ecrFrame");
						
						// create variables to store vehicle title and announcements
						String vehicleTitle = "";
						String announcement = "";
						
						// create TRY-CATCH block for new CR && the old CR windows
						try {
							// fetch the vehicle information
							vehicleTitle = Driver.getDriver().findElement(By.cssSelector("h2[class='ymmt-headline']")).getText();
System.out.println("Opened the CR window for: " + vehicleTitle);						
						
							// fetch the vehicle ANNOUCEMENTS
							announcement = Driver.getDriver().findElement(By.id("cr_announcements")).getText();
System.out.println("The ANNOUCEMENTS are: " + announcement);	
						} catch (NoSuchElementException e) {
							System.err.println("Element not found, encountered OLD Condition Report.");
							
							// fetch the vehicle information
							vehicleTitle = Driver.getDriver().findElement(By.className("vehicleSummary")).getText();
System.out.println("Opened the old version CR window for: " + vehicleTitle);								
							// fetch the vehicle ANNOUNCEMENTS
							announcement = Driver.getDriver().findElement(By.cssSelector(".announcements>td")).getText();
System.out.println("The ANNOUCEMENTS are: " + announcement);							
						}
						
						// close the CR window
						Driver.getDriver().close();
						
						// switch back to the parent window
						Driver.getDriver().switchTo().window(parentWindow);
					}
				}
				
				
				
			}
			
			// TODO -> check for conditions statement of the announcement text
			// TODO -> IF A MATCH, fetch vehicle info + auction info + concatenate the info to output String
			// TODO -> AT THE END, email the output String with run date
			
System.out.println("Currently in : " + current);
			
			// go back to the Simulcast page to continue the loop
			Driver.getDriver().navigate().back();
		}
		
		// quit the driver
		try { Thread.sleep(10000); } catch(InterruptedException e) {}
		Driver.getDriver().quit();
	}

}
