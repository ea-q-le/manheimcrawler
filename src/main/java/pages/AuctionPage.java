package pages;

import java.util.List;

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

}
