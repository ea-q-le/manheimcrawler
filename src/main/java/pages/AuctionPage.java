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
			return -1;
		
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
