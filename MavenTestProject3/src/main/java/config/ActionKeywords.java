package config;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import net.bytebuddy.utility.RandomString;

public class ActionKeywords {
	public WebDriver driver;
	
	public void openBrowser(String obj) throws Exception {
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
		createDriver();
	}

	private void createDriver() throws MalformedURLException {
		DesiredCapabilities capability = DesiredCapabilities.firefox();
		capability.setCapability("session", RandomString.make(3));
	    driver = new RemoteWebDriver(new URL("http://192.168.0.104:4444/wd/hub"), capability);
	}

	public void navigate(String obj) {
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.get(Constants.URL);
		WebDriverWait wait = new WebDriverWait(driver, 30);
		WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(".//*[@id='account']/a")));
		
		
	}

	public void click(String obj) {
		/*WebDriverWait wait = new WebDriverWait(driver, 30);
		WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(obj)));*/
		driver.findElement(By.id("account")).click();
	}

	public void input_Username(String obj) {
		driver.findElement(By.id("log")).sendKeys(Constants.UserName);
	}

	public void input_Password(String obj) {
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

	public void closeBrowser(String obj) {
		driver.quit();
	}

}
