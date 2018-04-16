package executionEngine;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import config.Constants;
import utilities.ExcelUtils;


public class DriverThreaded implements Runnable {

	public Executor actionExecutor;
	public String actionkeyword;
	public String pageObject;
	public Properties OR;
	public String testCaseName;

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		String datasheetpath = Constants.Path_TestData;
		// System.out.println(test.toString());
		try {
			ExcelUtils.setExcelFile(datasheetpath);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Map<Integer, String> testList = ExcelUtils.getRunableTests(Constants.Sheet_TestCases);
		for (String test : testList.values()) {
			(new Thread(new DriverThreaded(test))).start();
		}
	}

	public void run() {
		String[] testCaseDetails = testCaseName.split(",");
		
		System.out.println("Starting Thread for "+ testCaseDetails[0] + " on " +testCaseDetails[1]);
		
		actionExecutor = new Executor(testCaseName);
		/*for (int irow = 1; irow <= 9; irow++) {
			actionkeyword = ExcelUtils.getCellData(irow, Constants.Col_ActionKeyword, Constants.Sheet_TestSteps);
			pageObject = ExcelUtils.getCellData(irow, Constants.Col_PageObject, Constants.Sheet_TestSteps);
			try {
				actionExecutor.executeAction(actionkeyword, pageObject);
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}*/
	}

	public DriverThreaded(String test) {
		testCaseName = test;
	}

}
