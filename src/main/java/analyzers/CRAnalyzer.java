package analyzers;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;

import beans.Vehicle;
import utilities.Driver;

public class CRAnalyzer {
	
	public static boolean isVersion1(Vehicle vehicle) {
		try {
			// fetch the vehicle information
			vehicle.setTitle(Driver.getDriver()
					.findElement(By.cssSelector("h2[class='ymmt-headline']"))
						.getText());

			// fetch the vehicle ANNOUCEMENTS
			vehicle.setAnnouncement(Driver.getDriver()
					.findElement(By.id("cr_announcements"))
						.getText());
			
			return true;
		} catch (NoSuchElementException e) {
			System.err.println("Element not found, CRAnalyzer v1 failed.");
			return false;
		}
	}
	
	public static boolean isVersion2(Vehicle vehicle) {
		try {
			// fetch the vehicle information
			vehicle.setTitle(Driver.getDriver()
					.findElement(By.className("vehicleSummary"))
						.getText());

			// fetch the vehicle ANNOUNCEMENTS
			vehicle.setAnnouncement(Driver.getDriver()
					.findElement(By.cssSelector(".announcements>td"))
						.getText());
			
			return true;
		} catch (NoSuchElementException ex) {
			System.err.println("Element not found, CRAnalyzer v2 failed.");
			return false;
		}
	}
	
	public static boolean isVersion3(Vehicle vehicle) {
		try {
			// fetch the vehicle information
			vehicle.setTitle(Driver.getDriver()
					.findElement(By.cssSelector("td[colspan='3']"))
						.getText());
			
			// fetch the vehicle ANNOUNCEMENTS
			vehicle.setAnnouncement(Driver.getDriver()
					.findElement(By.cssSelector(".announcements>.mainfont"))
						.getText());
			
			return true;
		} catch (NoSuchElementException e) {
			System.err.println("Element not found, CRAnalyzer v3 failed.");
			return false;
		}
	}

}
