package testcases;

import java.time.Duration;

import org.openqa.selenium.By;
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
import pageclass.ContactListPage;
import pageclass.LoginPage;

@Listeners(Baseclass.TestListener.class)

public class UITest extends BaseTest {
	LoginPage loginPage;
	ContactListPage contactListPage;
	AddContactPage addContactPage;

	@BeforeMethod
	public void setup() {
		loginPage = new LoginPage(getDriver());
		contactListPage = new ContactListPage(getDriver());
		addContactPage = new AddContactPage(getDriver());

		loginPage.login("test@set.com", "test@123$");

		WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10));
		wait.until(ExpectedConditions.urlContains("contactList"));
	}

	@Test(priority = 1)
	public void testContactFormAlignment() {
		
		test.set(report.createTest("Testcase-UI & Responsiveness-6.26- Verify alignment of fields on contact form"));

		addContactPage = contactListPage.clickAddContact();

		WebElement fName = getDriver().findElement(By.id("firstName"));
		WebElement lName = getDriver().findElement(By.id("lastName"));
		WebElement dob = getDriver().findElement(By.id("birthdate"));
		WebElement email = getDriver().findElement(By.id("email"));
		WebElement phone = getDriver().findElement(By.id("phone"));
		WebElement sadd1 = getDriver().findElement(By.id("street1"));
		WebElement sadd2 = getDriver().findElement(By.id("street2"));
		WebElement city = getDriver().findElement(By.id("city"));
		WebElement state = getDriver().findElement(By.id("stateProvince"));
		WebElement poscode = getDriver().findElement(By.id("postalCode"));
		WebElement country = getDriver().findElement(By.id("country"));
		WebElement submit = getDriver().findElement(By.id("submit"));
		WebElement cancel = getDriver().findElement(By.id("cancel"));

		int x1 = fName.getLocation().getX();
		int x2 = lName.getLocation().getX();
		int x3 = dob.getLocation().getX();
		int x4 = email.getLocation().getX();
		int x5 = phone.getLocation().getX();
		int x6 = sadd1.getLocation().getX();
		int x7 = sadd2.getLocation().getX();
		int x8 = city.getLocation().getX();
		int x9 = state.getLocation().getX();
		int x10 = poscode.getLocation().getX();
		int x11 = country.getLocation().getX();
		int x12 = submit.getLocation().getX();
		int x13 = cancel.getLocation().getX();

		System.out.println("DEBUG: Coordinates -> FName:" + x1 + " LName:" + x2 + " DoB:" + x3 + " Email:" + x4
				+ " Phone:" + x5 + " sadd1:" + x6 + " sadd2:" + x7 + " city:" + x8 + " state:" + x9 + " poscode:" + x10
				+ " country:" + x11 + " submit:" + x12 + " cancel:" + x13);
		try {
			Assert.assertEquals(x3, x1, "Left Column Alignment Failed (DoB vs FirstName)");

			Assert.assertEquals(x5, x2, "Right Column Alignment Failed (Phone vs LastName)");

			Assert.assertEquals(x6, x1, "Left Column Alignment Failed (Sadd1 vs FirstName )");

			Assert.assertEquals(x2, x1, "Layout Error: Last Name should be to the right of First Name");
			getTest().log(Status.PASS, "All fields and buttons are partially aligned.");
		} catch (AssertionError e) {
			getTest().log(Status.FAIL, "Assertion Failed: " + e.getMessage());
			throw e;
		} catch (Exception e) {
			getTest().log(Status.FAIL, "Exception Occurred: " + e.getMessage());
			throw e;
		}
	}

	@Test(priority = 2)
	public void testActionSuccessIndicator() {
		
		test.set(report.createTest("Testcase-UI & Responsiveness-6.27- Verify toast messages or success indicators"));

		addContactPage = contactListPage.clickAddContact();

		addContactPage.fillContactForm("UI", "Test", "1990-01-01", "ui@test.com", "1234567890", "St", "City", "St",
				"123", "USA");
		addContactPage.clickSubmit();

		WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10));

		try {
			wait.until(ExpectedConditions.urlContains("contactList"));
			System.out.println("DEBUG: Success indicator (Redirection) observed.");
			getTest().log(Status.PASS, "Brief confirmation message or status update is shown");
		} catch (Exception e) {
			Assert.fail("Success Indicator Failed: User was not redirected after adding contact.");
		}

		String Email = "ui@test.com";
		try {
			WebElement newRow = wait.until(
					ExpectedConditions.visibilityOfElementLocated(By.xpath("//td[normalize-space()='" + Email + "']")));
			Assert.assertTrue(newRow.isDisplayed(), "New contact row is present but not visible.");
		} catch (Exception e) {
			Assert.fail("Success Indicator Failed: New contact '" + Email + "' did not appear in the list.");
		}
	}
}
