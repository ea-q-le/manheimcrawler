package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import utilities.BrowserUtils;
import utilities.ConfigReader;
import utilities.Driver;

public class HomePage {
	
	public HomePage() {
		PageFactory.initElements(Driver.getDriver(), this);
	}
	
	@FindBy (linkText = "Sign In")
	private WebElement signInButton;
	
	@FindBy (id = "skipbutton")
	private WebElement skipEmailVerificationButton;
	
	public static void goToHomePage() {
		Driver.getDriver().get(
				ConfigReader.getProperty("homePageURL"));
	}
	
	public static void goToSignInPage() {
//		BrowserUtils.wait(3);
		Driver.getDriver()
			.findElement(By.className("uhf-menu__trigger"))
			.click();
		Driver.getDriver()
			.findElement(By.cssSelector("[href=\"https://members.manheim.com/gateway/login\"]"))
			.click();
		JavascriptExecutor jse = (JavascriptExecutor) Driver.getDriver();
		jse.executeScript("arguments[0].click()", new HomePage().signInButton);
//		new HomePage().signInButton.click();
	}
	
	public static void tryToSkipEmailVerification() {
		try {
			JavascriptExecutor jse = (JavascriptExecutor) Driver.getDriver();
			jse.executeScript("arguments[0].click()", 
					new HomePage().skipEmailVerificationButton);
		} catch (Exception e) {
			System.err.println("Email verification page did not appear.");
		}
	}

}
