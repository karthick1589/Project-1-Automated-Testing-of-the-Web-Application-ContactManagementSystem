package Baseclass;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.io.FileHandler;
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

	public static ThreadLocal<WebDriver> driver =new ThreadLocal<>();
	public static ThreadLocal<ExtentTest> test = new ThreadLocal<>();
	public static ExtentSparkReporter html;
	public static ExtentReports report;

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
		WebDriver localDriver;

		switch (browser.toLowerCase()) {
		case "chrome":
			localDriver = new ChromeDriver();
			break;
		case "firefox":
			localDriver = new FirefoxDriver();
			break;
		case "edge":
			localDriver = new EdgeDriver();
			break;
		default:
			throw new IllegalArgumentException("Browser not supported: " + browser);
		}
		
		driver.set(localDriver);
		getDriver().manage().window().maximize();
		String waitTime = ConfigReader.getProperty("implicitWait");
		long wait = (waitTime != null) ? Long.parseLong(waitTime) : 10;

		getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(wait));

		String appUrl = ConfigReader.getProperty("testurl");
		if (appUrl != null)
			getDriver().get(appUrl);
		;
	}

	@AfterMethod
	public void tearDown() {
		if (getDriver() != null) {
			getDriver().quit();
			driver.remove();
		}
	}

	@AfterSuite
	public void saveReport() {
		if (report != null) {
			report.flush();
			System.out.println("DEBUG: Report Flushed (Saved)");
		}
	}
	
	public static WebDriver getDriver() {
        return driver.get();
    }
    
    public static ExtentTest getTest() {
        return test.get();
    }

	public String takeScreenshot(String testName) {

		TakesScreenshot ts = (TakesScreenshot) getDriver();

		File source = ts.getScreenshotAs(OutputType.FILE);

		String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new java.util.Date());
		String fileName = testName + "_" + timestamp + ".png";

		File destination = new File("./screenshots/" + fileName);

		try {
			FileHandler.copy(source, destination);
			System.out.println("Screenshot taken: " + destination.getAbsolutePath());
		} catch (IOException e) {
			System.out.println("Failed to save screenshot: " + e.getMessage());
		}
		return fileName;
	}
}
