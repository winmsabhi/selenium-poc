package executionEngine;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Properties;

import org.openqa.selenium.WebDriver;
import config.ActionKeywords;
import config.Constants;
import utilities.ExcelUtils;

public class DriverScript {
	public static WebDriver driver = null;
	public static Method method[];
	public static ActionKeywords actionkeywords;
	public static String actionkeyword;
	public static String pageObject;
	public static Properties OR;

	static {
		actionkeywords = new ActionKeywords();
		method = actionkeywords.getClass().getMethods();
		String Path_OR = Constants.Path_OR;
		FileInputStream fs;
		try {
			fs = new FileInputStream(Path_OR);
		
		OR= new Properties(System.getProperties());
		//Loading all the properties from Object Repository property file in to OR object
		
			OR.load(fs);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public static void main(String[] args) throws Exception {

		String datasheetpath = Constants.Path_TestData;
		ExcelUtils.setExcelFile(datasheetpath, Constants.Sheet_TestSteps);
		for (int irow = 1; irow <= 9; irow++) {
			actionkeyword = ExcelUtils.getCellData(irow, Constants.Col_ActionKeyword);
			pageObject = ExcelUtils.getCellData(irow, Constants.Col_PageObject);
			execute_Actions();
		}
	}

	private static void execute_Actions()
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		for (int i = 0; i < method.length; i++) {
			if (method[i].getName().equals(actionkeyword)) {
				method[i].invoke(actionkeywords,OR.getProperty(pageObject));
				break;
			}
		}
	}
}
