package pages;

import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.PageFactory;

import utilities.BrowserUtils;
import utilities.Driver;

public class SimulcastPage {
	
	public SimulcastPage() {
		PageFactory.initElements(Driver.getDriver(), this);
	}
	
	@FindBys ({
		@FindBy (css = "tr>th")
	})
	private List<WebElement> auctionNameElementsList;
	
	@FindBys ({
		@FindBy (css = "tr>th>a")
	})
	private List<WebElement> viewAuctionVehiclesLinkList;
	
	public static List<WebElement> getAuctionVehiclesLinkList() {
		return new SimulcastPage().viewAuctionVehiclesLinkList;
	}
	
	public static List<WebElement> getAuctionElementsList() {
		return new SimulcastPage().auctionNameElementsList;
	}
	
	public static List<String> getAuctionNamesList() {
		return BrowserUtils
				.webelementTextList(
						new SimulcastPage().auctionNameElementsList);
	}
			

}
