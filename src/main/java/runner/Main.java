package runner;

import java.util.*;

import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;

import analyzers.CRAnalyzer;
import beans.*;
import pages.*;
import utilities.*;

import static utilities.BrowserUtils.*;
import static analyzers.AnnouncementSpecsAnalyzer.matchesAnnouncementSpecs;
import static analyzers.AuctionAnalyzer.matchesAuctionList;

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
		waitForLoad();
		
		// go to the next 7 days
		MyManheimPage.goToNext7Days();
		waitForLoad();

		// iterate through each auction vehicle
		crawler();
		
System.out.println(Vehicle.getMatches());
		// send an email the output String with run date
		SendEmail.sendEmailTo(
				"sean.gadimoff@gmail.com",
				"Manheim vehicles for " + todaysDate("MM/dd/yyyy"), 
				Vehicle.printMatches());
		// TODO -> email should contain Auction + Lane + Vehicle info
		// LIMIT to 2010 or newer

		// quit the driver
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

			// ignore if the auction contains 0 vehicles or not in the list
			if (current.contains("View all 0 vehicles") || 
					!matchesAuctionList(new Auction(current)))
				continue;

			// refresh the vehicle links to avoid stale element exception
			waitForLoad();
			viewVehiclesLinkList = SimulcastPage.getAuctionVehiclesLinkList();

			// click on the link to view all vehicles within the auction
			viewVehiclesLinkList.get(i).click();
			waitForLoad();

			// analyze each CR within a given auction
			iterateAuctionCRs(current);

System.out.println("Currently in: " + current);

			// go back to the Simulcast page to continue the loop
			Driver.getDriver().navigate().back();
		}
	}

	private static void iterateAuctionCRs(String auction) {
		// create variables to store vehicle auction, title & announcements
		Vehicle vehicle = new Vehicle();
		vehicle.setAuction(auction);
System.out.println("Vehicle auction is: " + vehicle.getAuction());
		
		// fetch the list of all CR buttons (vehicles with CR)
		List<WebElement> crLinks = AuctionPage.getCrLinkList();

		// for each CR link
		for (int j = 0; j < crLinks.size(); j++) {
			// store current window information
			String parentWindow = Driver.getDriver().getWindowHandle();

			// click on the current CR link
			crLinks.get(j).click();
			BrowserUtils.waitQt();

			// fetch the newly opened window handle and switch to it
			Set<String> handles = Driver.getDriver().getWindowHandles();
			for (String windowHandle : handles) {
				if (windowHandle.equals(parentWindow)) continue;
				
				// switch to newly opened Condition Report window
				Driver.getDriver().switchTo().window(windowHandle);

				// wait for the new window to finish loading
				waitForLoad(Driver.getDriver());

				// switch to the Frame where vehicle info is stored
				Driver.getDriver().switchTo().frame("ecrFrame");

				// if vehicle announcement can't be pulled, set it undefined
				if (!CRAnalyzer.isVersion1(vehicle) &&
						!CRAnalyzer.isVersion2(vehicle) &&
							!CRAnalyzer.isVersion3(vehicle))
					vehicle.setAnnouncement("Undefined");
				
System.out.println("Opened the CR window for: " + vehicle.getTitle());
System.out.println("The ANNOUCEMENTS are: " + vehicle.getAnnouncement());
				
				// analyze vehicle announcements and add
				if (matchesAnnouncementSpecs(vehicle))
					Vehicle.addAMatch(vehicle);				

				// close the CR window
				Driver.getDriver().close();

				// switch back to the parent window
				Driver.getDriver().switchTo().window(parentWindow);
				
			}
			
			//TODO -> remove this limitation for PROD run
			if (Vehicle.getMatches().size() > 3) break;
		}
	}

}
