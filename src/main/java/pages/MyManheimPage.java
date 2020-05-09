package pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import utilities.Driver;

public class MyManheimPage {
	
	public MyManheimPage() {
		PageFactory.initElements(Driver.getDriver(), this);
	}
	
	@FindBy (linkText = "Simulcast")
	private WebElement simulcastLink;
	
	@FindBy (linkText = "Next 7 Days")
	private WebElement next7DaysLink;
	
	public static void goToSimulcast() {
		new MyManheimPage().simulcastLink.click();
	}
	
	public static void goToNext7Days() {
		new MyManheimPage().next7DaysLink.click();
	}

}
