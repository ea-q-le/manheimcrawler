package pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.PageFactory;

import utilities.Driver;

public class AuctionPage {
	
	public AuctionPage() {
		PageFactory.initElements(Driver.getDriver(), this);
	}
	
	@FindBys ({
		@FindBy (css = "tr>td.make>div[name='sticker']>a.icon-cr")
	})
	private List<WebElement> crLinkList;
	
	public static List<WebElement> getCrLinkList() {
		return new AuctionPage().crLinkList;
	}
	
	/**
	 * Given a current CR Link Element that is in the focus
	 * the method will go to its main parent that stores the year value
	 * and then will fetch the text and return it as a short.
	 * Additional internal condition is added if year box is empty or null
	 * in which case the method returns -1.
	 * @param crLinkElement that is currently in focus
	 * @return short value of the vehicle year
	 */
	public static short getVehicleYear(WebElement crLinkElement) {
		WebElement yearElement = crLinkElement.findElement(By.xpath("../../../td[2]"));
		String yearText = yearElement.getText();
		
		if (yearText == null || yearText.isEmpty()) 
			return (short)-1;
		
		short year = -1;
		try {
			year = Short.parseShort(yearText); 
		} catch (NumberFormatException e) {
			System.err.println("Couldn't fetch the year of the vehicle.");
		}
		return year;
	}
	
	/**
	 * Given a current CR Link Element that is in the focus
	 * the method will go to its main parent that stores the vehicle title
	 * and then will fetch the text and return it as a String.
	 * Additional internal condition is added if year box is empty or null
	 * in which case the method returns Unknown.
	 * @param crLinkElement that is currently in focus
	 * @return String value of the vehicle title
	 */
	public static String getVehicleTitle(WebElement crLinkElement) {
		WebElement titleElement = crLinkElement.findElement(By.xpath("../../a"));
		String title = titleElement.getText();
		
		if (title == null || title.isEmpty()) 
			return "Unknown";
		
		return title;
	}
	
	/**
	 * Given a current CR Link Element that is in the focus
	 * the method will go to its main parent that stores the odometer information
	 * and then will fetch the text and return it as an int.
	 * Additional internal condition is added if lane box is empty or null
	 * in which case the method returns -1.
	 * @param crLinkElement that is currently in focus
	 * @return int value of the vehicle odometer
	 */
	public static int getVehicleOdometer(WebElement crLinkElement) {
		WebElement odoElement = crLinkElement.findElement(By.xpath("../../../td[5]"));
		String odoText = odoElement.getText();
		
		if (odoText == null || odoText.isEmpty()) 
			return -1;
		
		int odometer = -1;
		try {
			odometer = Integer.parseInt(odoText); 
		} catch (NumberFormatException e) {
			System.err.println("Couldn't fetch the odometer of the vehicle.");
		}
		return odometer;
	}
	
	/**
	 * Given a current CR Link Element that is in the focus
	 * the method will go to its main parent that stores the lane information
	 * and then will fetch the text and return it as a String.
	 * Additional internal condition is added if lane box is empty or null
	 * in which case the method returns 'NOT found'.
	 * @param crLinkElement that is currently in focus
	 * @return String value of the vehicle lane
	 */
	public static String getVehicleLane(WebElement crLinkElement) {
		WebElement laneElement = crLinkElement.findElement(By.xpath("../../../td[6]"));
		String lane = laneElement.getText();
		
		if (lane == null || lane.isEmpty()) return "NOT found";
		
		return lane;
	}

}
