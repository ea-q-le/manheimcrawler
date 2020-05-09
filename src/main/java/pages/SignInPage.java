package pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import utilities.Driver;

public class SignInPage {
	
	public SignInPage() {
		PageFactory.initElements(Driver.getDriver(), this);
	}
	
	@FindBy (id = "user_username")
	public WebElement usernameInput;
	
	@FindBy (id = "user_password")
	public WebElement passwordInput;
	
	@FindBy (id = "submit")
	public WebElement signInButton;
		
	public static void signIn() {
		SignInPage page = new SignInPage();
		page.usernameInput.sendKeys(System.getenv("username"));
		page.passwordInput.sendKeys(System.getenv("password"));
		page.signInButton.click();
	}

}
