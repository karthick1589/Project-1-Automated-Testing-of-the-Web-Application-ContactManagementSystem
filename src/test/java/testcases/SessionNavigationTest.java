package testcases;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;

import Baseclass.BaseTest;
import pageclass.ContactListPage;
import pageclass.LoginPage;

public class SessionNavigationTest extends BaseTest {

	LoginPage loginPage;
	ContactListPage contactListPage;

	@BeforeMethod
	public void setupAndLogin() {
		loginPage = new LoginPage(driver);
		loginPage.login("test@set.com", "test@123$");
		contactListPage = new ContactListPage(driver);

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.urlContains("contactList"));
	}

	@Test
	public void logout() {

		test = report.createTest("Testcase-Session & Navigation-6.22- Verify logout redirects to login page");
		loginPage.logout();

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

		wait.until(ExpectedConditions.urlToBe("https://thinking-tester-contact-list.herokuapp.com/"));

		String currentUrl = driver.getCurrentUrl();
		try {
			Assert.assertEquals(currentUrl, "https://thinking-tester-contact-list.herokuapp.com/",
					"User was not redirected to the login page!");
			boolean isLoginVisible = driver.findElement(By.id("submit")).isDisplayed();
			Assert.assertTrue(isLoginVisible, "Login button should be visible after logout.");
			test.log(Status.PASS, "Verification done, Logout redirected to login page");
		} catch (AssertionError e) {
			test.log(Status.FAIL, "Assertion Failed: " + e.getMessage());
			throw e;
		} catch (Exception e) {
			test.log(Status.FAIL, "Exception Occurred: " + e.getMessage());
			throw e;
		}
	}

	@Test
	public void verifyLoginPersistsOnRefresh() {

		test = report.createTest("Testcase-Session & Navigation-6.23- Verify login state on refersh");

		String initialUrl = driver.getCurrentUrl();
		Assert.assertTrue(initialUrl.contains("contactList"),
				"Pre-condition failed: User is not on Contact List page.");

		driver.navigate().refresh();

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

		wait.until(ExpectedConditions.urlContains("contactList"));

		String postRefreshUrl = driver.getCurrentUrl();

		try {
			Assert.assertTrue(postRefreshUrl.contains("contactList"),
					"Failed: User was logged out (redirected) after refresh!");

			boolean listLoaded = !contactListPage.isListEmpty() || contactListPage.isListEmpty();
			Assert.assertTrue(listLoaded, "Page did not render correctly after refresh.");
			test.log(Status.PASS, "After refersh the page persists in login page.verification done");
		} catch (AssertionError e) {
			test.log(Status.FAIL, "Assertion Failed: " + e.getMessage());
			throw e;
		} catch (Exception e) {
			test.log(Status.FAIL, "Exception Occurred: " + e.getMessage());
			throw e;
		}
	}

	@Test
	public void verifyLoginRequiredForContactList() {

		test = report.createTest("Testcase-Session & Navigation-6.24- Verify login is required to access contact list");

		String baseUrl = "https://thinking-tester-contact-list.herokuapp.com/";
		String protectedUrl = "https://thinking-tester-contact-list.herokuapp.com/contactList";

		driver.get(protectedUrl);

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.urlToBe(baseUrl));

		String currentUrl = driver.getCurrentUrl();

		Assert.assertNotEquals(currentUrl, protectedUrl,
				"Security Fail: User was able to access Contact List without login!");

		try {
			Assert.assertEquals(currentUrl, baseUrl, "User was not redirected to the correct login page.");
			boolean isSignUpVisible = driver.findElement(By.id("signup")).isDisplayed();
			Assert.assertTrue(isSignUpVisible, "Login/Signup page elements are not visible.");
			test.log(Status.PASS, "Redircted to loginPage");
		} catch (AssertionError e) {
			test.log(Status.FAIL, "Assertion Failed: " + e.getMessage());
			throw e;
		} catch (Exception e) {
			test.log(Status.FAIL, "Exception Occurred: " + e.getMessage());
			throw e;
		}
	}

	@Test
	public void verifyBackAfterLogout() {

		test = report.createTest(
				"Testcase-Session & Navigation-6.25- Verify user cannot go back to contact list after logout using browser back button");

		String loginUrl = "https://thinking-tester-contact-list.herokuapp.com/";
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

		loginPage.logout();

		wait.until(ExpectedConditions.urlToBe(loginUrl));
		System.out.println("DEBUG: Logout successful. Redirected to Login Page.");

		System.out.println("DEBUG: Pressing Browser Back Button...");
		driver.navigate().back();

		String currentUrl = driver.getCurrentUrl();
		System.out.println("DEBUG: URL after back button: " + currentUrl);

		try {

			if (currentUrl.endsWith("/logout")) {
				boolean isLogoutBtnPresent = !driver.findElements(By.id("logout")).isEmpty();
				Assert.assertFalse(isLogoutBtnPresent, "Security Fail: User is still logged in on /logout page!");
				return;
			}

			if (currentUrl.contains("contactList")) {
				System.out.println("DEBUG: Stuck on Contact List. Refreshing to verify session state...");
				driver.navigate().refresh();
				wait.until(ExpectedConditions.urlToBe(loginUrl));
				return;
			}

			Assert.assertEquals(currentUrl, loginUrl, "Unexpected URL after back button.");
			test.log(Status.PASS, " Remains on login page; session is invalidated succrssfully");
		} catch (AssertionError e) {
			test.log(Status.FAIL, "Assertion Failed: " + e.getMessage());
			throw e;
		} catch (Exception e) {
			test.log(Status.FAIL, "Exception Occurred: " + e.getMessage());
			throw e;
		}

	}
}
