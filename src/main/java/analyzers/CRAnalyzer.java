package analyzers;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;

import beans.Vehicle;
import utilities.Driver;

public class CRAnalyzer {
	
	/**
	 * The method will 'try' to fetch the Announcements from
	 * the CR window that the Driver is currently focused on.
	 * It will set the announcement to the given Vehicle object.
	 * This method is designed to work with certain version of CR windows.
	 * 
	 * @param vehicle to which the announcement will be set to
	 * @return boolean value whether the method was successful in
	 * 		   finding and setting the announcements
	 */
	public static boolean isVersion1(Vehicle vehicle) {
		try {
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
	
	/**
	 * The method will 'try' to fetch the Announcements from
	 * the CR window that the Driver is currently focused on.
	 * It will set the announcement to the given Vehicle object.
	 * This method is designed to work with certain version of CR windows.
	 * 
	 * @param vehicle to which the announcement will be set to
	 * @return boolean value whether the method was successful in
	 * 		   finding and setting the announcements
	 */
	public static boolean isVersion2(Vehicle vehicle) {
		try {			
			// fetch the vehicle ANNOUNCEMENTS
			vehicle.setAnnouncement(Driver.getDriver()
					.findElement(By.cssSelector(".announcements>.mainfont"))
						.getText());
			
			return true;
		} catch (NoSuchElementException e) {
			System.err.println("Element not found, CRAnalyzer v2 failed.");
			return false;
		}
	}
	
	/**
	 * The method will 'try' to fetch the Announcements from
	 * the CR window that the Driver is currently focused on.
	 * It will set the announcement to the given Vehicle object.
	 * This method is designed to work with certain version of CR windows.
	 * 
	 * @param vehicle to which the announcement will be set to
	 * @return boolean value whether the method was successful in
	 * 		   finding and setting the announcements
	 */
	public static boolean isVersion3(Vehicle vehicle) {
		try {
			// fetch the vehicle ANNOUNCEMENTS
			vehicle.setAnnouncement(Driver.getDriver()
					.findElement(By.cssSelector(".announcements>td"))
						.getText());
			
			return true;
		} catch (NoSuchElementException ex) {
			System.err.println("Element not found, CRAnalyzer v3 failed.");
			return false;
		}
	}
	
	

}
