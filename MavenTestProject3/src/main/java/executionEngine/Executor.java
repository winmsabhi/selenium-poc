package executionEngine;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Properties;

import config.ActionKeywords;
import config.Constants;

public class Executor {
	public static Method method[];
	public static ActionKeywords actionkeywords;
	public static Properties OR;
	public String actionkeyword;
	public String pageObject;
	
	static {
		try {
			prepareOr();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		actionkeywords = new ActionKeywords();
		method = actionkeywords.getClass().getMethods();
	}
	
	/*public Executor(String actionKeyword, String pageObject) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, Exception {
		for (int i = 0; i < method.length; i++) {
			if (method[i].getName().equals(actionKeyword)) {
				method[i].invoke(actionkeywords, OR.getProperty(pageObject));
				break;
			}
		}
		
	}*/

	protected void executeAction(String actionKeyword, String pageObject)
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
		String Path_OR = Constants.Path_OR;/*
											 * String datasheetpath = Constants.Path_TestData;
											 * ExcelUtils.setExcelFile(datasheetpath);
											 */
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
