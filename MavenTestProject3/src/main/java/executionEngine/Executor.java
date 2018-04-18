package executionEngine;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.xml.DOMConfigurator;
import org.openqa.selenium.WebDriver;

import config.ActionKeywords;
import config.Constants;
import utilities.ExcelUtils;

public class Executor {
	public Method[] method;
	// public static String methodList[];
	public ActionKeywords actionkeywords;
	public static Properties OR;
	public String actionkeyword;
	public String pageObject;
	private String testCaseName = null;
	private String browser = null;
	private int testStepCount = 0;
	private int testStartRowNumber = 0;
	public WebDriver driver = null;

	public Executor() {
		try {
			prepareOr();
			actionkeywords = new ActionKeywords();
			method = actionkeywords.getClass().getMethods();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Executor(String testList) {
		this();
		testCaseName = testList.split(",")[0];
		browser=testList.split(",")[1];
		testStartRowNumber = ExcelUtils.getRowContains(testCaseName, Constants.Sheet_TestSteps);
		testStepCount = ExcelUtils.getTestStepsCount(Constants.Sheet_TestSteps, testCaseName);
		System.out.println("Starting Executor for "+ testCaseName + " on " +browser);
		try {
			actionkeywords.openBrowser(browser);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//ExcelUtils.getTestStepsCount(Constants.Sheet_TestSteps, testCaseName);
		for (int irow = testStartRowNumber; irow <= testStepCount-1; irow++) {
			actionkeyword = ExcelUtils.getCellData(irow, Constants.Col_ActionKeyword, Constants.Sheet_TestSteps);
			pageObject = ExcelUtils.getCellData(irow, Constants.Col_PageObject, Constants.Sheet_TestSteps);
			System.out.println("Performing action " + actionkeyword + " on "+ pageObject);

			try {

				executeAction(actionkeyword, pageObject);
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			/*finally {
				if (driver!=null) driver.quit();
				System.out.println("Closing the browser as " + actionkeyword + " on "+ pageObject +" failed");
				break;
			}*/
		}
		
	}

	public void executeAction(String actionKeyword, String pageObject)
			throws IllegalAccessException, InvocationTargetException {

		for (int i = 0; i < method.length; i++) {
			if (method[i].getName().equals(actionKeyword)) {
				method[i].invoke(actionkeywords, OR.getProperty(pageObject));
				break;
			}
		}
	}

	public static void prepareOr() throws Exception {
		System.out.println(Constants.Path_TestData);
		String Path_OR = Constants.Path_OR;
		DOMConfigurator.configure("log4j.xml");
		FileInputStream fs;
		try {
			fs = new FileInputStream(Path_OR);

			OR = new Properties(System.getProperties());
			// Loading all the properties from Object Repository property file in to OR
			// object

			OR.load(fs);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
