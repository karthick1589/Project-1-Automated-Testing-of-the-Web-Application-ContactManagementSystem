package testcases;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;

import Baseclass.BaseTest;
import pageclass.AddContactPage;
import pageclass.ContactDetailsPage;
import pageclass.ContactListPage;
import pageclass.LoginPage;

@Listeners(Baseclass.TestListener.class)

public class InputValidationTest extends BaseTest {

	LoginPage loginPage;
	ContactListPage contactListPage;
	AddContactPage addContactPage;
	ContactDetailsPage contactDetailsPage;

	@BeforeMethod
	public void setup() {
		loginPage = new LoginPage(getDriver());
		contactListPage = new ContactListPage(getDriver());

		loginPage.login("test@set.com", "test@123$");

		WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10));
		wait.until(ExpectedConditions.urlContains("contactList"));
	}

	@Test(priority = 1)
	public void testMaxCharacterLimit() {

		test.set(report.createTest(
				"Testcase- Input Validation & Security-6.28- Verify max character limit for contact fields"));

		String longName = "A".repeat(300);

		addContactPage = contactListPage.clickAddContact();

		addContactPage.fillContactForm(longName, "LimitTest", "1990-01-01", "maxchar@test.com", "1234567890", "Street",
				"City", "St", "123", "USA");
		addContactPage.clickSubmit();

		String actualError = addContactPage.getErrorMessage();
		System.out.println("Validation Error Received: " + actualError);

		try {
			boolean isErrorCorrect = actualError.contains("is longer than the maximum allowed length")
					|| actualError.contains("Validation failed");

			Assert.assertTrue(isErrorCorrect, "Expected validation error for 300+ chars, but got: " + actualError);

			getTest().log(Status.PASS, "Field restricts or shows error successfully");
		} catch (AssertionError e) {
			getTest().log(Status.FAIL, "Assertion Failed: " + e.getMessage());
			throw e;
		} catch (Exception e) {
			getTest().log(Status.FAIL, "Exception Occurred: " + e.getMessage());
			throw e;
		}

	}

	@Test(priority = 2)
	public void testUnicodeAndEmojis() throws InterruptedException {

		test.set(report.createTest("Testcase- Input Validation & Security-6.29- Verify Unicode and emojis in address field"));
		String specialAddress = "123 Rainbow Rd 🌈, Tokyo 東京, España ñ";
		String uniqueName = "Unicode" + System.currentTimeMillis();
		String uniqueEmail = "unicode" + System.currentTimeMillis() + "@test.com";

		addContactPage = contactListPage.clickAddContact();

		addContactPage.fillContactForm(uniqueName, "User", "1990-01-01", uniqueEmail, "1234567890", "", "City", "St",
				"123", "USA");

		WebElement addressField = getDriver().findElement(By.id("street1"));

		JavascriptExecutor js = (JavascriptExecutor) getDriver();
		js.executeScript("let input = arguments[0];" + "let lastValue = input.value;" + "input.value = arguments[1];"
				+ "let event = new Event('input', { bubbles: true });" + "let tracker = input._valueTracker;"
				+ "if (tracker) { tracker.setValue(lastValue); }" + // Tell React the value changed
				"input.dispatchEvent(event);" + "input.dispatchEvent(new Event('change', { bubbles: true }));"
				+ "input.dispatchEvent(new Event('blur', { bubbles: true }));", addressField, specialAddress);

		Thread.sleep(500);

		addContactPage.clickSubmit();

		WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10));
		wait.until(ExpectedConditions.urlContains("contactList"));

		try {
			contactDetailsPage = contactListPage.clickContactByName(uniqueName + " User");

			System.out.println("Original: " + specialAddress);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("street1")));

			String displayedAddress = getDriver().findElement(By.id("street1")).getText();
			System.out.println("Displayed: " + displayedAddress);

			Assert.assertEquals(displayedAddress, specialAddress, "Unicode/Emoji characters were not saved correctly!");
			getTest().log(Status.PASS, "System accepts and displays them correctly.");
		} catch (AssertionError e) {
			getTest().log(Status.FAIL, "Assertion Failed: " + e.getMessage());
			throw e;
		} catch (Exception e) {
			getTest().log(Status.FAIL, "Exception Occurred: " + e.getMessage());
			throw e;
		}
	}
}
