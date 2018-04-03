package config;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ActionKeywords {
	public static WebDriver driver;
	
	public static void openBrowser(String obj) throws Exception {
		/*System.setProperty("webdriver.gecko.driver",
				"C:\\\\Users\\\\winms\\\\eclipse-workspace\\\\MavenTestProject3\\\\src\\\\main\\\\java\\\\exes\\geckodriver.exe");
		driver = new FirefoxDriver();*/
		/*System.setProperty("webdriver.edge.driver", "C:\\Users\\winms\\eclipse-workspace\\MavenTestProject3\\src\\main\\java\\exes\\MicrosoftWebDriver.exe");
		driver = new EdgeDriver();*/
		/*System.setProperty("webdriver.ie.driver", "C:\\Users\\winms\\eclipse-workspace\\MavenTestProject3\\src\\main\\java\\exes\\IEDriverServer.exe");
		DesiredCapabilities caps = DesiredCapabilities.internetExplorer();
		caps.setCapability("ignoreZoomSetting", true);
		
		driver = new InternetExplorerDriver();*/
		
//		driver = new RemoteWebDriver(remoteAddress, capabilities);
		
/*		To be used for grid*/
		DesiredCapabilities capability = DesiredCapabilities.firefox();
	    driver = new RemoteWebDriver(new URL("http://192.168.43.42:4444/wd/hub"), capability);
	}

	public static void navigate(String obj) {
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.get(Constants.URL);
		WebDriverWait wait = new WebDriverWait(driver, 30);
		WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(".//*[@id='account']/a")));
		
		
	}

	public static void click(String obj) {
		/*WebDriverWait wait = new WebDriverWait(driver, 30);
		WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(obj)));*/
		driver.findElement(By.id("account")).click();
	}

	public static void input_Username(String obj) {
		driver.findElement(By.id("log")).sendKeys(Constants.UserName);
	}

	public static void input_Password(String obj) {
		driver.findElement(By.id("pwd")).sendKeys(Constants.Password);
	}

	/*public static void click_Login() {
		driver.findElement(By.id("login")).click();
	}*/

	public static void waitFor(String obj) throws Exception {
		Thread.sleep(5000);
	}

	/*public static void click_Logout() {
		driver.findElement(By.xpath(".//*[@id='account_logout']/a")).click();
	}*/

	public static void closeBrowser(String obj) {
		driver.quit();
	}

}
