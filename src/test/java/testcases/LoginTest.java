package testcases;

import java.time.Duration;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;

import Baseclass.BaseTest;
import pageclass.LoginPage;

@Listeners(Baseclass.TestListener.class)

public class LoginTest extends BaseTest {

	@Test
	public void testValidLogin() {

		test.set(report.createTest("Testcase-Login Functionality-1.1- Verify login with valid credentials "));

		LoginPage loginPage = new LoginPage(getDriver());
		loginPage.login("test@set.com", "test@123$");

		try {
			new WebDriverWait(getDriver(), Duration.ofSeconds(10)).until(ExpectedConditions.urlContains("contactList"));
			getTest().log(Status.PASS, "Login validation done successfully");
		} catch (AssertionError e) {
			getTest().log(Status.FAIL, "Test Failed: Assertion Error -" + e.getMessage());
			throw e;
		} catch (Exception e) {
			getTest().log(Status.FAIL, "Test Failed due to Exception- " + e.getMessage());
			throw e;
		}

	}

	@Test
	public void testLoginWithIncorrectPassword() {

		test.set(report.createTest("Testcase-Login Functionality-1.2- Verify login with incorrect password"));

		LoginPage loginPage = new LoginPage(getDriver());
		loginPage.login("test1234@gmail.com", "test@123");

		try {
			String actualError = loginPage.getGlobalErrorMessage();

			Assert.assertEquals(actualError, "Incorrect username or password");
			getTest().log(Status.PASS,
					"Test Passed: Error message verified successfully as 'Incorrect username or password'");

		} catch (AssertionError e) {

			getTest().log(Status.FAIL, "Test Failed: Assertion Error - " + e.getMessage());
			throw e;

		} catch (Exception e) {
			getTest().log(Status.FAIL, "Test Failed due to Exception: " + e.getMessage());
			throw e;
		}
	}

	@Test
	public void testLoginWithEmptyFields() {

		test.set(report.createTest("Testcase-Login Functionality-1.3- Verify login with empty fields"));

		LoginPage loginPage = new LoginPage(getDriver());
		loginPage.enterEmail(" ");
		loginPage.enterPassword("");
		loginPage.clickLogin();

		try {
			String actualError = loginPage.getGlobalErrorMessage();
			Assert.assertEquals(actualError, "Incorrect username or password");
			getTest().log(Status.PASS,
					"Test Passed: Error message verified successfully as 'Incorrect username or password'");

		} catch (AssertionError e) {

			getTest().log(Status.FAIL, "Test Failed: Assertion Error - " + e.getMessage());
			throw e;

		} catch (Exception e) {
			getTest().log(Status.FAIL, "Test Failed due to Exception: " + e.getMessage());
			throw e;
		}

	}

	@Test
	public void testInvalidEmailFormat() {

		test.set(report.createTest("Testcase-Login Functionality-1.4- Verify login with invalid email format"));

		LoginPage loginPage = new LoginPage(getDriver());
		loginPage.enterEmail("user.com");
		loginPage.enterPassword("password");
		loginPage.clickLogin();

		try {
			String actualError = loginPage.getGlobalErrorMessage();
			Assert.assertEquals(actualError, "Incorrect username or password");
			getTest().log(Status.PASS,
					"Test Passed: Error message verified successfully as 'Incorrect username or password'");
		} catch (AssertionError e) {
			getTest().log(Status.FAIL, "Test Failed: Assertion Error - " + e.getMessage());
			throw e;

		} catch (Exception e) {
			getTest().log(Status.FAIL, "Test Failed due to Exception: " + e.getMessage());
			throw e;
		}
	}

	@Test
	public void testPasswordMasking() {

		test.set(report.createTest("Testcase-Login Functionality-1.5- Verify password field masks input"));

		LoginPage loginPage = new LoginPage(getDriver());

		try {
			Assert.assertEquals(loginPage.getPasswordFieldType(), "password",
					"Password field is not masked (type attribute is not 'password')");
			getTest().log(Status.PASS,
					"Test Passed: User typed password into the field and those Characters are hidden (displayed as dots/bullets)");
		} catch (AssertionError e) {
			getTest().log(Status.FAIL, "Test Failed: Assertion Error - " + e.getMessage());
			throw e;

		} catch (Exception e) {
			getTest().log(Status.FAIL, "Test Failed due to Exception: " + e.getMessage());
			throw e;

		}

	}
}
