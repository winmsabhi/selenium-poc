package config;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import net.bytebuddy.utility.RandomString;
import utilities.Log;

public class ActionKeywords {
	public WebDriver driver = null;
	static int count = 0;
	BrowserEnum browser;

	public ActionKeywords() {
		count += 1;
		System.out.println(count);
	}

	public void openBrowser(String obj) throws Exception {
		/*
		 * System.setProperty("webdriver.gecko.driver",
		 * "C:\\\\Users\\\\winms\\\\eclipse-workspace\\\\MavenTestProject3\\\\src\\\\main\\\\java\\\\exes\\geckodriver.exe"
		 * ); driver = new FirefoxDriver();
		 */
		/*
		 * System.setProperty("webdriver.edge.driver",
		 * "C:\\Users\\winms\\eclipse-workspace\\MavenTestProject3\\src\\main\\java\\exes\\MicrosoftWebDriver.exe"
		 * ); driver = new EdgeDriver();
		 */
		/*
		 * System.setProperty("webdriver.ie.driver",
		 * "C:\\Users\\winms\\eclipse-workspace\\MavenTestProject3\\src\\main\\java\\exes\\IEDriverServer.exe"
		 * ); DesiredCapabilities caps = DesiredCapabilities.internetExplorer();
		 * caps.setCapability("ignoreZoomSetting", true);
		 * 
		 * driver = new InternetExplorerDriver();
		 */

		// driver = new RemoteWebDriver(remoteAddress, capabilities);

		/* To be used for grid */
		this.driver = createDriver(obj);
		this.driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}

	private WebDriver createDriver(String obj) throws MalformedURLException, Exception {
		/*
		 * System.setProperty("webdriver.gecko.driver",
		 * "C:\\\\Users\\\\winms\\\\eclipse-workspace\\\\MavenTestProject3\\\\src\\\\main\\\\java\\\\exes\\geckodriver.exe"
		 * ); driver = new FirefoxDriver();
		 */
		DesiredCapabilities capability;
		switch (BrowserEnum.valueOf(obj.toUpperCase())) {
		case CHROME:
			capability = DesiredCapabilities.chrome();
			break;
		case EDGE:
			capability = DesiredCapabilities.edge();
			break;
		case FIREFOX:
			capability = DesiredCapabilities.firefox();
			break;
		case INTERNETEXPLORER:
			capability = DesiredCapabilities.internetExplorer();
			break;
		case OPERA:
			capability = DesiredCapabilities.opera();
			break;
		default:
			capability = DesiredCapabilities.firefox();
			break;
		}

		capability.setCapability("session", RandomString.make(3));
		driver = new RemoteWebDriver(new URL("http://192.168.0.106:4444/wd/hub"), capability);
		System.out.println("Creating Browser" + capability.getCapability("session").toString());
		System.out.println(driver.getWindowHandle());

		return driver;
	}

	public void navigate(String obj) {
		this.driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		this.driver.get(Constants.URL);
		WebDriverWait wait = new WebDriverWait(this.driver, 30);
		wait.until(ExpectedConditions.elementToBeClickable(By.id("account")));
		// Capabilities cap = ((RemoteWebDriver) this.driver).getCapabilities();
		// Log.info("Navigating on Browser" + cap.getCapability("session").toString());

	}

	public void click(String obj) {
		WebDriverWait wait = new WebDriverWait(this.driver, 30);
		WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.id("account")));
		// Capabilities cap = ((RemoteWebDriver) this.driver).getCapabilities();
		// Log.info("Clicking on Browser" + cap.getCapability("session").toString());
		element.click();
	}

	public void input_Username(String obj) {
		this.driver.findElement(By.id("log")).sendKeys(Constants.UserName);
		// Capabilities cap = ((RemoteWebDriver) this.driver).getCapabilities();
		// Log.info("Entering Data on Browser" +
		// cap.getCapability("session").toString());
	}

	public void input_Password(String obj) {
		this.driver.findElement(By.id("pwd")).sendKeys(Constants.Password);
		// Capabilities cap = ((RemoteWebDriver) this.driver).getCapabilities();
		// Log.info("Entering Data on Browser" +
		// cap.getCapability("session").toString());
	}

	/*
	 * public static void click_Login() {
	 * driver.findElement(By.id("login")).click(); }
	 */

	public static void waitFor(String obj) throws Exception {
		Thread.sleep(5000);
	}

	/*
	 * public static void click_Logout() {
	 * driver.findElement(By.xpath(".//*[@id='account_logout']/a")).click(); }
	 */

	public void closeBrowser(String obj) {
		this.driver.quit();
		// Capabilities cap = ((RemoteWebDriver) this.driver).getCapabilities();
		// Log.info("Entering Data on Browser" +
		// cap.getCapability("session").toString());
	}

}
