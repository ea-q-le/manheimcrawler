package runner;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import beans.Vehicle;
import pages.*;
import utilities.*;

import static utilities.BrowserUtils.*;
import static utilities.AnnouncementSpecsAnalyzer.matchesAnnouncementSpecs;;

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

		// iterate through each auction vehicle
		crawler();
		
System.out.println(Vehicle.getMatches());
		// send an email the output String with run date
		SendEmail.sendEmailTo(
				"sean.gadimoff@gmail.com",
				"Manheim vehicles for " + todaysDate("MM/dd/yyyy"), 
				Vehicle.getMatches().toString());
		// TODO -> email should contain Auction + Lane + Vehicle info

		// quit the driver
		BrowserUtils.wait(10);
		Driver.getDriver().quit();
	}

	/** The main method that contains the crawling logic */
	private static void crawler() {
		// store all auction names
		List<String> auctionNames = SimulcastPage.getAuctionNamesList();
System.out.println(auctionNames);

		// store the links to each auction vehicle lists
		List<WebElement> viewVehiclesLinkList = new ArrayList<WebElement>();

		// for each auction
		for (int i = 0; i < auctionNames.size(); i++) {
			String current = auctionNames.get(i);
System.out.println(current);

			// ignore if the auction contains 0 vehicles
			if (current.contains("View all 0 vehicles"))
				continue;

			// refresh the vehicle links to avoid stale element exception
			waitForLoad();
			viewVehiclesLinkList = SimulcastPage.getAuctionVehiclesLinkList();

			// click on the link to view all vehicles within the auction
			viewVehiclesLinkList.get(i).click();
			waitForLoad();

			// analyze each CR within a given auction
			iterateAuctionCRs();

System.out.println("Currently in : " + current);

			// go back to the Simulcast page to continue the loop
			Driver.getDriver().navigate().back();
		}
	}

	private static void iterateAuctionCRs() {
		// create variables to store vehicle title and announcements
		Vehicle vehicle = new Vehicle();
		
		// fetch the list of all CR buttons (vehicles with CR)
		List<WebElement> crLinks = AuctionPage.getCrLinkList();

		// for each CR link
		for (int j = 0; j < crLinks.size(); j++) {
			// store current window information
			String parentWindow = Driver.getDriver().getWindowHandle();

			// click on the current CR link
			crLinks.get(j).click();
			BrowserUtils.wait(1);

			// fetch the newly opened window handle and switch to it
			Set<String> handles = Driver.getDriver().getWindowHandles();
			for (String windowHandle : handles) {
				if (windowHandle.equals(parentWindow)) continue;
				
				// switch to newly opened Condition Report window
				Driver.getDriver().switchTo().window(windowHandle);

				// wait for the new window to finish loading
				waitForLoad(Driver.getDriver());

				// switch to the Frame where vehicle info is stored
				// this frame ID is true for both NEW and OLD CR windows
				Driver.getDriver().switchTo().frame("ecrFrame");

				// create TRY-CATCH block for new CR && the old CR windows
				try {
					// fetch the vehicle information
					vehicle.setTitle(Driver.getDriver()
							.findElement(By.cssSelector("h2[class='ymmt-headline']"))
								.getText());
System.out.println("Opened the CR window for: " + vehicle.getTitle());

					// fetch the vehicle ANNOUCEMENTS
					vehicle.setAnnouncement(Driver.getDriver()
							.findElement(By.id("cr_announcements"))
								.getText());
System.out.println("The ANNOUCEMENTS are: " + vehicle.getAnnouncement());
				} catch (NoSuchElementException e) {
					System.err.println("Element not found, encountered OLD Condition Report.");

					// fetch the vehicle information
					vehicle.setTitle(Driver.getDriver()
							.findElement(By.className("vehicleSummary"))
								.getText());
System.out.println("Opened the old version CR window for: " + vehicle.getTitle());
					// fetch the vehicle ANNOUNCEMENTS
					vehicle.setAnnouncement(Driver.getDriver()
							.findElement(By.cssSelector(".announcements>td"))
								.getText());
System.out.println("The ANNOUCEMENTS are: " + vehicle.getAnnouncement());
				}
				
				// analyze vehicle announcements and add
				if (matchesAnnouncementSpecs(vehicle))
					Vehicle.addAMatch(vehicle);				

				// close the CR window
				Driver.getDriver().close();

				// switch back to the parent window
				Driver.getDriver().switchTo().window(parentWindow);
				
			}
			//TODO - this line is for testing, remove when done
			if (Vehicle.getMatches().size() > 3) break;
		}
	}

}
