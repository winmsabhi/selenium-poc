


package executionEngine;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Properties;


import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.MediaEntityModelProvider;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.model.Log;
import com.aventstack.extentreports.model.Media;
import com.aventstack.extentreports.model.ScreenCapture;
import com.aventstack.extentreports.model.Screencast;

import config.Constants;
import utilities.ActionReporter;
import utilities.ExcelUtils;
import utilities.HelperMethods;
import utilities.KioskActions;

public class Executor {
public WebDriver driver = null;
public Method method[];
public KioskActions actions;
public LinkedHashMap<Integer, String> testTracker= new LinkedHashMap<Integer, String>();
public Boolean isTestExecutionComplete = false;
public ActionReporter actionReporter = new ActionReporter();
public ExtentTest logger;

public Executor() {
try {
HelperMethods.prepareOr();
actions = new KioskActions();
method = actions.getClass().getMethods();
} catch (Exception e) {
// TODO Auto-generated catch block
e.printStackTrace();
}
}
public Executor(String test) throws Exception {
this();
actionReporter.setCurrentTestDetails(test.split(","));
actionReporter.setPNR(actionReporter.getCurrentTestDetails()[1]);
actionReporter.setLastName(actionReporter.getCurrentTestDetails()[2]);
String[][] data = {
    { "<h5>Pre-Requisite</h5>", "<h5>Description</h5>"},
    { "<h6>" + ExcelUtils.getCellData(ExcelUtils
.getRowContains(actionReporter.getCurrentTestDetails()[0],
Constants.Sheet_TestCases),Constants.Col_PreRequisites,Constants.Sheet_TestCases).trim().replace("\n", "<br>")+"</h6>", "<h6>" + ExcelUtils.getCellData(ExcelUtils
.getRowContains(actionReporter.getCurrentTestDetails()[0],
Constants.Sheet_TestCases),Constants.Col_TestDescription,Constants.Sheet_TestCases).trim().replace("\n", "<br>")+"</h6>"}
};
Markup m = MarkupHelper.createTable(data);
logger = DriverScriptTest.extent.createTest(actionReporter.getCurrentTestDetailsAsString(), m.getMarkup() );
logger.assignCategory(ExcelUtils.getCellData(ExcelUtils
.getRowContains(actionReporter.getCurrentTestDetails()[0],
Constants.Sheet_TestCases),Constants.Col_Category,Constants.Sheet_TestCases).trim());
ExcelUtils.setCellData((new Date()).toString(), ExcelUtils
.getRowContains(actionReporter.getCurrentTestDetails()[0],
Constants.Sheet_TestCases),
Constants.Col_LastExecutionTime,
Constants.Sheet_TestCases);
//actions.currentTestDetails=this.currentTestDetails;
// String[] PNRLN = testCaseDetails[1].split(":");
System.out
.println("**************************************************************************************************");
System.out.println("Test Case No " + actionReporter.getCurrentTestDetails()[0]
+ " PNR: " + actionReporter.getCurrentTestDetails()[1] + " Last Name: "
+ actionReporter.getCurrentTestDetails()[2]);
actionReporter.setTestPass(true);

int stepStart = ExcelUtils.getRowContains(actionReporter.getCurrentTestDetails()[0],
Constants.Sheet_TestSteps);
int stepEnd = ExcelUtils.getTestStepsCount(
Constants.Sheet_TestSteps, actionReporter.getCurrentTestDetails()[0]);
System.out.println("Step Start at: " + stepStart
+ " Steps End At: " + stepEnd + " Total Steps: "
+ (stepEnd - stepStart));
System.out
.println("**************************************************************************************************");
for (int irow = stepStart; irow <= stepEnd; irow++) {
testTracker.put(irow, "Not Run");
}
/*This loop runs through each step for the test case as defined in the TestSuite*/
for (int irow = stepStart; irow <= stepEnd; irow++) {
actionReporter.setScreenShotPath("");
actionReporter.setActionkeyword(ExcelUtils.getCellData(irow,
Constants.Col_KioskActionKeyword,
Constants.Sheet_TestSteps).trim());
actionReporter.setPageObject(ExcelUtils.getCellData(irow,
Constants.Col_PageObject, Constants.Sheet_TestSteps)
.trim());
actionReporter.setActionData(ExcelUtils.getCellData(irow,
Constants.Col_ActionData, Constants.Sheet_TestSteps)
.trim());
if (actionReporter.getActionData().equalsIgnoreCase("PNR")){
actionReporter.setActionData(actionReporter.getCurrentTestDetails()[1].trim());
} else if (actionReporter.getActionData().equalsIgnoreCase("LastName")){
actionReporter.setActionData(actionReporter.getCurrentTestDetails()[2].trim());
}
actionReporter.setInternal("");
StringBuilder actionDesc = new StringBuilder();
actionDesc.append(actionReporter.getActionkeyword());
if (actionReporter.getPageObject() != null && !actionReporter.getPageObject().trim().isEmpty()){
actionDesc.append(" on object " + actionReporter.getPageObject().toString() );
}
if (actionReporter.getActionData() != null && !actionReporter.getActionData().trim().isEmpty()){
actionDesc.append(" with parameters "
+ actionReporter.getActionData().toString());
}
actionDesc.append(".");
logger.log(Status.INFO, "["+test.toString()+"] "+" ["+ ExcelUtils.getCellData(irow,Constants.Col_TestStepID,Constants.Sheet_TestSteps).trim()
+"] "+ "Executing Action " + actionDesc.toString());
System.out.println("["+test.toString()+"] "+" Executing Action " + actionDesc.toString());
execute_Actions();
//TODO
if (!actionReporter.isTestPass()) {
testTracker.put(irow, Constants.KEYWORD_FAIL);
DriverScriptTest.testCaseTracker.put(actionReporter.getCurrentTestDetails()[0], new LinkedHashMap<Integer, String>(testTracker));
System.out.println("["+test.toString()+"] "+" Test Execution Halted.");
testTracker.clear();
isTestExecutionComplete = true;
String[][] screenHelper;
if (!actionReporter.getScreenShotPath().isEmpty()) {
screenHelper = new String[][] {{"Pre-Execution", "Post-Execution"},{HelperMethods.imageMarkupHelper(actionReporter.getScreenShotPath()), HelperMethods.imageMarkupHelper(actionReporter.getErrorScreenShotPath()) }};
logger.log(Status.FAIL, MarkupHelper.createLabel("["+test.toString()+"] "+"["+ ExcelUtils.getCellData(irow,Constants.Col_TestStepID,Constants.Sheet_TestSteps).trim()
+"]"+ " Test Case Failed. <br> " + actionReporter.getInternal()+"<br>", ExtentColor.RED).getMarkup() + MarkupHelper.createTable(screenHelper).getMarkup());
} else {
logger.log(Status.FAIL, MarkupHelper.createLabel("["+test.toString()+"] "+"["+ ExcelUtils.getCellData(irow,Constants.Col_TestStepID,Constants.Sheet_TestSteps).trim()
+"]"+ " Test Case Failed. <br> " + actionReporter.getInternal()+"<br>", ExtentColor.RED).getMarkup(), MediaEntityBuilder.createScreenCaptureFromPath(actionReporter.getErrorScreenShotPath()).build());
}
//logger.log(Status.ERROR, MarkupHelper.createTable(screenHelper).getMarkup());
//logger.log(Status.FAIL, MarkupHelper.createLabel("["+test.toString()+"] "+"["+ ExcelUtils.getCellData(irow,Constants.Col_TestStepID,Constants.Sheet_TestSteps).trim()
// +"]"+ " Test Case Failed. <br> " + actionReporter.getInternal()+"<br>", ExtentColor.RED).getMarkup() + MarkupHelper.createTable(screenHelper).getMarkup(), MediaEntityBuilder.createScreenCaptureFromPath(actionReporter.getErrorScreenShotPath()).build());
//logger.fail("["+actionReporter.getCurrentTestDetailsAsString()+"]"  + " Test Case Failed.", MediaEntityBuilder.createScreenCaptureFromPath(actionReporter.getErrorScreenShotPath()).build());
//logger.fail(details, provider);
break;
}
testTracker.put(irow, Constants.KEYWORD_PASS);
logger.log(Status.PASS, "["+test.toString()+"] "+"["+ ExcelUtils.getCellData(irow,Constants.Col_TestStepID,Constants.Sheet_TestSteps).trim()
+"]"+ " Action Complete. " + actionDesc.toString());
}
if (actionReporter.isTestPass()) {
DriverScriptTest.testCaseTracker.put(actionReporter.getCurrentTestDetails()[0], new LinkedHashMap<Integer, String>(testTracker));
logger.log(Status.PASS, MarkupHelper.createLabel("["+actionReporter.getCurrentTestDetailsAsString()+"]"  + " Test Case Passed.", ExtentColor.GREEN));
System.out.println("Test Execution Complete. Marked as Pass.");
testTracker.clear();
isTestExecutionComplete = true;
}
}

private void execute_Actions() throws IllegalAccessException,
IllegalArgumentException, InvocationTargetException {
String Path_OR = Constants.Path_OR;
//DOMConfigurator.configure("log4j.xml");
FileInputStream fs;
Properties OR = null;
try {
fs = new FileInputStream(Path_OR);

OR = new Properties(System.getProperties());
// Loading all the properties from Object Repository property file
// in to OR
// object

OR.load(fs);
} catch (FileNotFoundException e) {
// TODO Auto-generated catch block
e.printStackTrace();
} catch (IOException e) {
// TODO Auto-generated catch block
e.printStackTrace();
}

try {
if (!actionReporter.getActionkeyword().trim().isEmpty()) {
for (int i = 0; i < method.length; i++) {
if (method[i].getName().equalsIgnoreCase(actionReporter.getActionkeyword())) {
method[i].invoke(actions, actionReporter);
break;
/*if (actionReporter.getPageObject() != "" && actionReporter.getActionData() != "") {
method[i].invoke(actions, OR.getProperty(actionReporter.getPageObject()),
actionReporter.getActionData());
break;
} else if (actionReporter.getPageObject() == "" && actionReporter.getActionData() != "") {
method[i].invoke(actions, actionReporter.getActionData());
break;
} else if (actionReporter.getActionData() == "" && actionReporter.getPageObject() != "") {
method[i].invoke(actions, OR.getProperty(actionReporter.getPageObject()));
break;
} else {
method[i].invoke(actions);
break;
}*/
} else {

if (i == method.length - 1) {
Assert.fail("Method with name " + actionReporter.getActionkeyword()
+ " not found. Kindly review the test case.");
throw new Exception("Method with name " + actionReporter.getActionkeyword()
+ " not found. Kindly review the test case.");
}
}
} 
} else {
actionReporter.setInternal("["+actionReporter.getCurrentTestDetailsAsString()+"]" + " The action name is empty. Please check the test suite.");
actionReporter.setTestPass(false);
}
} catch (Exception e) {
// TODO Auto-generated catch block
//Assert.fail(String.join(", ",currentTestDetails) + "Execute Action Failure. ");
actionReporter.setTestPass(false); 
e.printStackTrace();
}
}
}


