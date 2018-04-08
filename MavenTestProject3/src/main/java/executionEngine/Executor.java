package executionEngine;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Properties;

import org.apache.log4j.xml.DOMConfigurator;
import org.openqa.selenium.WebDriver;

import config.ActionKeywords;
import config.Constants;

public class Executor {
	public Method[] method;
	// public static String methodList[];
	public ActionKeywords actionkeywords;
	public static Properties OR;
	public String actionkeyword;
	public String pageObject;
	static int count = 0;
	public WebDriver driver = null;

	public Executor(){
		count += 1;
		System.out.println("Created Executor Object " + count);
		try {
			prepareOr();
			actionkeywords = new ActionKeywords();
			method = actionkeywords.getClass().getMethods();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void executeAction(String actionKeyword, String pageObject)
			throws IllegalAccessException, InvocationTargetException {
		System.out.println(actionkeywords.driver == null ? "null" : actionkeywords.driver.getWindowHandle().toString() + " action "+ actionKeyword);
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
