package pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import utilities.ConfigReader;
import utilities.Driver;

public class HomePage {
	
	public HomePage() {
		PageFactory.initElements(Driver.getDriver(), this);
	}
	
	@FindBy (linkText = "Sign In")
	public WebElement signInButton;
	
	public static void goToHomePage() {
		Driver.getDriver().get(
				ConfigReader.getProperty("homePageURL"));
	}
	
	public static void goToSignInPage() {
		new HomePage().signInButton.click();
	}

}
