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
	public WebElement simulcastLink;
	
	public static void goToSimulcast() {
		new MyManheimPage().simulcastLink.click();
	}

}
