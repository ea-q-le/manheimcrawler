package runner;

import java.sql.Timestamp;
import java.util.*;

import org.openqa.selenium.*;

import analyzers.*;
import beans.*;
import pages.*;
import tables.VehiclesTable;
import utilities.*;

import static utilities.BrowserUtils.*;
import static analyzers.AnnouncementSpecsAnalyzer.matchesAnnouncementSpecs;
import static analyzers.AuctionAnalyzer.matchesAuctionList;

public class Main {
	
	private static int processedVehicleCount;

	public static void main(String[] args) {
		
		// fetch the start time
		long startTime = System.currentTimeMillis();
		
		// establish DB connection
		DBUtils.createDBConnection();

		// go to manheim.com
		HomePage.goToHomePage();

		// maximize window to avoid hidden element behavior
		Driver.getDriver().manage().window().maximize();

		BrowserUtils.wait(5);
		// go to sign in page
		// as of 7/08, this navigation has been @Deprecated
		// need repetitive testing to validate and remove the step
//		HomePage.goToSignInPage();
		/**
		 * As of 6/12/2021 the utility above started throwing errors
		 * when being executed in Chrome Headless mode.
		 * Further analysis discovered that the sign-in page is
		 * directly accessible via the link within the get method below.
		 * Hence the code base needs to be refactored accordingly
		 * in order to remove redundant steps of fetching the Home Page
		 * and steps that follow until the step below.
		 */
		Driver.getDriver().get("https://members.manheim.com/gateway/login");

		// sign in to the system
		SignInPage.signIn();

		// if email verification page comes up, try to SKIP
		HomePage.tryToSkipEmailVerification();
		
		// go to Simulcast page
		MyManheimPage.goToSimulcast();
		waitForLoad();
		
		// go to the next 7 days
		MyManheimPage.goToNext7Days();
		waitForLoad();
		
		// iterate through each auction vehicle
		crawler();
		
		// destroy the DB connection
		DBUtils.destroyDBConnection();
		
		// fetching logic completion time
		long endTime = System.currentTimeMillis();
		
		// fetching duration in total seconds
		long runTimeSeconds = (endTime - startTime) / 1000;
		
		// send an email the output String with run date
		// if the 'emailToBeSent' value in application.config
		// is set to 'true'
		if (SendEmail.EMAIL_TO_BE_SENT)
			SendEmail.sendEmailTo(
				ConfigReader.getProperty("recipients"),
				"Manheim vehicles > BOT RUN on " + todaysDate("MM/dd/yyyy")
					+ "\tTime taken: " + runTimeSeconds + " seconds"
					+ "\tVehicles processed: " + processedVehicleCount, 
				Vehicle.printMatches());

		// quit the driver
		Driver.getDriver().quit();
		
System.out.println("LIST OF VEHICLES TO BE EMAILED:\n" + Vehicle.getMatches());
	}

	/** The main method that contains the crawling logic */
	private static void crawler() {
		// store all auction names
		List<String> auctionNames = SimulcastPage.getAuctionNamesList();

		// store the links to each auction vehicle lists
		List<WebElement> viewVehiclesLinkList = new ArrayList<WebElement>();

		// for each auction
		for (int i = 0; i < auctionNames.size(); i++) {
			String current = auctionNames.get(i);

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
			
			// limit vehicle count per the email limitation
			if (Vehicle.getMatches().size() >= SendEmail.EMAIL_LIMIT) 
				return;

			// go back to the Simulcast page to continue the loop
			Driver.getDriver().navigate().back();
		}
	}

	private static void iterateAuctionCRs(String auction) {		
		// fetch the list of all CR buttons (vehicles with CR)
		List<WebElement> crLinks = 
				new ArrayList<WebElement>(AuctionPage.getCrLinkList());

		// for each CR link
		for (int j = 0; j < crLinks.size(); j++) {
			// record the number of vehicles the program has analyzed
			processedVehicleCount++;
			
			// create variables to store vehicle auction, title & announcements
			Vehicle vehicle = new Vehicle();
			vehicle.setAuction(auction);
			
			// performance enhancements, get element once and remove it right away
			// decrement to avoid OutOfBounds
			WebElement currentCRLink = crLinks.get(j);
			crLinks.remove(j);
			j--;

			// fetch the vehicle year for compatibility
			short year = AuctionPage.getVehicleYear(currentCRLink); 
			if (year < Vehicle.YEAR_OLDEST ||
					year > Vehicle.YEAR_YOUNGEST) 
				continue;
			vehicle.setYear(year);
						
			// fetch the vehicle odometer information for compatibility
			int odometer = AuctionPage.getVehicleOdometer(currentCRLink);
			if (odometer > Vehicle.ODOMETER_MAX) continue;
			vehicle.setOdometer(odometer);

			// fetch the VIN of the vehicle
			String vin = AuctionPage.getVehicleVIN(currentCRLink);
			vehicle.setVIN(vin);
			if (VehiclesTable.vehicleExistsByVIN(vehicle.getVIN()))
				continue;
			
			// fetch vehicle availability status and continue if SOLD
			boolean isAvailable = AuctionPage.getVehicleIsAvailable(currentCRLink);
			vehicle.setIsAvailable(isAvailable);
			if (!isAvailable) continue;

			// fetch the vehicle run date and time
			String runDateTime = AuctionPage.getVehicleRunDateTime(currentCRLink);
			vehicle.setRunTimestamp(runDateTime);
			
			// fetch the vehicle title information
			String title = AuctionPage.getVehicleTitle(currentCRLink);
			vehicle.setTitle(title);
			
			// fetch vehicle lane information
			String lane = AuctionPage.getVehicleLane(currentCRLink);
			vehicle.setLane(lane);
			
			// store current window information
			String parentWindow = Driver.getDriver().getWindowHandle();

			// click on the current CR link
			currentCRLink.click();
			BrowserUtils.waitQt();

			// fetch the newly opened window handle and switch to it
			Set<String> handles = Driver.getDriver().getWindowHandles();
			for (String windowHandle : handles) {
				if (windowHandle.equals(parentWindow)) continue;
				
				// switch to newly opened Condition Report window
				Driver.getDriver().switchTo().window(windowHandle);

				try {
					// wait for the new window to finish loading
					waitForLoad();
	
					// switch to the Frame where vehicle info is stored
					Driver.getDriver().switchTo().frame("ecrFrame");
	
					// if vehicle announcement can't be pulled, set it undefined
					if (!CRAnalyzer.isVersion1(vehicle) &&
							!CRAnalyzer.isVersion2(vehicle) &&
								!CRAnalyzer.isVersion3(vehicle))
						vehicle.setAnnouncement("Undefined");
					
					// analyze vehicle announcements and add
					if (matchesAnnouncementSpecs(vehicle))
						Vehicle.addAMatch(vehicle);				
				} catch (Exception e) {
					vehicle.setAnnouncement("Something went wrong...");
				}
				
System.out.println("Current vehicle info: " + vehicle.toString());
				
				// close the CR window
				Driver.getDriver().close();

				// switch back to the parent window
				Driver.getDriver().switchTo().window(parentWindow);
				
				// add the found current timestamp as 
				// the found_date into the DB table
				vehicle.setFoundTimestamp(
						new Timestamp( new Date().getTime() ));
				
				// insert the vehicle into the DB table
				VehiclesTable.insertIntoVehicles(vehicle);
			}
			
			// limit vehicle count per the email limitation
			if (Vehicle.getMatches().size() >= SendEmail.EMAIL_LIMIT) 
				return;
		}
	}

}
