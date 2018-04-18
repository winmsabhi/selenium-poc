package executionEngine;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import config.ActionKeywords;
import config.Constants;
import utilities.ExcelUtils;

public class DriverScriptTest {

	//public static WebDriver driver;
	public Executor actionExecutor;
	public String actionkeyword;
	public String pageObject;
	public static Properties OR;

	/*@BeforeSuite
	private void prepareOr() throws Exception {
		System.out.println(Constants.Path_TestData);
		
		String Path_OR = Constants.Path_OR;
											 * String datasheetpath = Constants.Path_TestData;
											 * ExcelUtils.setExcelFile(datasheetpath);
											 
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
			, parallel = true
		}

	}*/

	@DataProvider(name = "TestsToRun", parallel = true)
	public Object[][] TestsToRun() throws Exception {
//		prepareOr();
		Map<Integer, String> testList = ExcelUtils.getRunableTests(Constants.Sheet_TestCases);
		Object[][] obj = new Object[testList.size()][1];
		for (int i = 0; i < testList.size(); i++) {
			String[] fn = testList.get(i + 1).split(",");
			Map<Object, Object> datamap = new HashMap<Object, Object>();
			datamap.put(fn[0].toString(), fn[1].toString());
			obj[i][0] = datamap;
		}

		return obj;
	}

	@Test(dataProvider = "TestsToRun", threadPoolSize = 5, invocationCount = 1)
	public void runTest(Map<Object, Object> map)
			throws Exception, IllegalAccessException, InvocationTargetException {
		String datasheetpath = Constants.Path_TestData;
		System.out.println(map.toString());
		ExcelUtils.setExcelFile(datasheetpath);
		actionExecutor = new Executor();
		for (int irow = 1; irow <= 9; irow++) {
			actionkeyword = ExcelUtils.getCellData(irow, Constants.Col_ActionKeyword, Constants.Sheet_TestSteps);
			pageObject = ExcelUtils.getCellData(irow, Constants.Col_PageObject, Constants.Sheet_TestSteps);
			
			actionExecutor.executeAction(actionkeyword, pageObject);
		}
	}

}
