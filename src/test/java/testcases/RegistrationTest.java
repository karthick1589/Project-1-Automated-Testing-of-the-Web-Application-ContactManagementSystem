package testcases;

import java.time.Duration;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;

import Baseclass.BaseTest;
import pageclass.LoginPage;
import pageclass.RegistrationPage;
import utilityclass.ConfigReader;
import utilityclass.DataUtils;

public class RegistrationTest extends BaseTest {

	@Test
	public void testValidSignup() {

		test = report.createTest("Testcase-Sign Up / Registration Functionality-2.6- Verify sign-up with valid inputs");

		LoginPage loginPage = new LoginPage(driver);

		RegistrationPage registrationPage = loginPage.clickSignUp();

		String fname = ConfigReader.getProperty("reg.firstname");
		String lname = ConfigReader.getProperty("reg.lastname");
		String password = ConfigReader.getProperty("reg.password");

		String Email = "newuser" + System.currentTimeMillis() + "@example.com";

		registrationPage.registerUser(fname, lname, Email, password);

		registrationPage.clickSubmit();

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
		wait.until(ExpectedConditions.urlContains("/contactList"));
		try {
			Assert.assertTrue(driver.getCurrentUrl().contains("contactList"),
					"User was not redirected to Contact List after signing up.");
			test.log(Status.PASS, "User was redirected to Contact List after signing up");
		} catch (AssertionError e) {
			test.log(Status.FAIL, "Test Failed: Assertion Error -" + e.getMessage());
			throw e;
		} catch (Exception e) {
			test.log(Status.FAIL, "Test Failed due to Exception-" + e.getMessage());
			throw e;
		}

	}

	@Test
	public void testRegistrationWithExistingEmail() {

		test = report.createTest(
				"Testcase-Sign Up / Registration Functionality-2.7- Verify registration with already registered email");

		LoginPage loginPage = new LoginPage(driver);
		RegistrationPage registrationPage = loginPage.clickSignUp();

		String duplicateEmail = "dup" + System.currentTimeMillis() + "@test.com";
		String password = "Password123!";

		registrationPage.registerUser("Test", "User", duplicateEmail, password);

		registrationPage.clickSubmit();

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.urlContains("contactList"));

		loginPage.logout();

		loginPage.clickSignUp();

		registrationPage.registerUser("Test", "User", duplicateEmail, password);

		registrationPage.clickSubmit();

		String actualError = registrationPage.getErrorMessage();

		System.out.println("Actual Error Message: " + actualError);

		try {

			Assert.assertTrue(actualError.contains("Email address is already in use")
					|| actualError.contains("User validation failed"), "Expected 'Email already in use' error");
			test.log(Status.PASS, "Email address is already in use");
		} catch (AssertionError e) {
			test.log(Status.FAIL, "Test Failed: Assertion Error- " + e.getMessage());
			throw e;
		} catch (Exception e) {
			test.log(Status.FAIL, "Test Failed due to Exception- " + e.getMessage());
			throw e;
		}
	}

	@Test(dataProvider = "invalidRegistrationData", dataProviderClass = DataUtils.class)
	public void testInvalidRegistrationScenarios(String fName, String lName, String email, String password,
			String expectedError) {

		test = report
				.createTest("Testcase-Sign Up / Registration Functionality-2.8- Verify registration with blank fields");

		LoginPage loginPage = new LoginPage(driver);
		RegistrationPage regPage = loginPage.clickSignUp();

		regPage.registerUser(fName, lName, email, password);

		regPage.clickSubmit();

		String actualError = regPage.getErrorMessage();

		System.out.println("Testing Case: " + expectedError);

		try {
			boolean isMatch = actualError.contains(expectedError);

			Assert.assertTrue(isMatch,
					"Error mismatch! Expected part: [" + expectedError + "] but Found: [" + actualError + "]");

			test.log(Status.PASS, "Validation successful. Found expected error: " + expectedError);

		} catch (AssertionError e) {
			test.log(Status.FAIL, "Test Failed: " + e.getMessage());
			throw e;
		} catch (Exception e) {
			test.log(Status.FAIL, "Test Failed due to Exception- " + e.getMessage());
			throw e;
		}
	}
}
