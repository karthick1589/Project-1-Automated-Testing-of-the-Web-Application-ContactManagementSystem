package Baseclass;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import utilityclass.ConfigReader;

public class BaseTest {

	public WebDriver driver;
	public static ExtentSparkReporter html;
	public static ExtentReports report;
	public static ExtentTest test;

	@BeforeSuite
	public void setupReport() {
		html = new ExtentSparkReporter(System.getProperty("user.dir") + "/CMSTestReport.html");
		report = new ExtentReports();
		report.attachReporter(html);
		System.out.println("DEBUG: Report Initialized");
	}

	@BeforeMethod
	@Parameters("browser") // Read the "browser" parameter from testng.xml
	public void setup(@Optional String browserFromXML) {
		String browser;
		if (browserFromXML != null && !browserFromXML.isEmpty()) {
			browser = browserFromXML;
		} else {
			browser = ConfigReader.getProperty("browser");
		}

		System.out.println("Launching Browser: " + browser);

		switch (browser.toLowerCase()) {
		case "chrome":
			driver = new ChromeDriver();
			break;
		case "firefox":
			driver = new FirefoxDriver();
			break;
		case "edge":
			driver = new EdgeDriver();
			break;
		default:
			throw new IllegalArgumentException("Browser not supported: " + browser);
		}

		driver.manage().window().maximize();
		String waitTime = ConfigReader.getProperty("implicitWait");
		long wait = (waitTime != null) ? Long.parseLong(waitTime) : 10;

		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(wait));

		String appUrl = ConfigReader.getProperty("testurl");
		if (appUrl != null)
			driver.get(appUrl);
		;
	}

	@AfterMethod
	public void tearDown() {
		if (driver != null) {
			driver.quit();
		}
	}

	@AfterSuite
	public void saveReport() {
		if (report != null) {
			report.flush();
			System.out.println("DEBUG: Report Flushed (Saved)");
		}
	}

}
