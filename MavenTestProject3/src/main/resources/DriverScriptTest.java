

package executionEngine;


import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReporter;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.ChartLocation;
import com.aventstack.extentreports.reporter.configuration.Theme;

import config.Constants;
import utilities.ExcelUtils;
import utilities.OffLoad;

import java.io.File;
import java.io.FileFilter;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.TestRunner;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;

@Listeners(utilities.Listener.class)

public class DriverScriptTest {

public static ExtentReports extent;
public static ExtentHtmlReporter extentReporter;



public static LinkedHashMap<String, LinkedHashMap<Integer, String>> testCaseTracker = new LinkedHashMap<>();
List<String> testList;
String datasheetpath;
public DriverScriptTest() throws Exception {
String datasheetpath = Constants.Path_TestData;
ExcelUtils.setExcelFile(datasheetpath);
}

@BeforeSuite
public void prepareSuite() throws Exception {
extent= new ExtentReports();
createReportPath(Constants.ExtentReportPath);
extentReporter = new ExtentHtmlReporter(Constants.ExtentReportPath + Constants.ExtentReportName);
extentReporter.loadXMLConfig("extent-config.xml");
//extent.addSystemInfo("Environment","Environment Name")
extent.setSystemInfo("Host Name", System.getProperty("user.name")+"'s Desktop");
extent.setSystemInfo("Environment", "Automation Testing");
extent.setSystemInfo("User Name", System.getProperty("user.name"));
extent.setSystemInfo("Suite ", " <a href=\"" +Constants.Path_TestData+"\" target=\"_blank\">Click here to open TestSuite</a>");

//loading the external xml file (i.e., extent-config.xml) which was placed under the base directory
//You could find the xml file below. Create xml file in your project and copy past the code mentioned below
// extentReporter.config().setDocumentTitle("KioskAutomationSuite");
// extentReporter.config().setReportName("Automation");
// extentReporter.config().setTestViewChartLocation(ChartLocation.TOP);
// extentReporter.config().setTheme(Theme.STANDARD);
extentReporter.loadXMLConfig(Constants.ExtentConfigFile);

extent.attachReporter(extentReporter);

datasheetpath = Constants.Path_TestData;
ExcelUtils.setExcelFile(datasheetpath);



}

public void offloadPNR(Map<Object, Object> map) throws Exception {
OffLoad testPNR= new OffLoad();
try {
testPNR.openBrowser();
String[] currentTestDetails = map.values().toArray()[0].toString().split(",");
if (ExcelUtils.getCellData(
ExcelUtils.getRowContains(currentTestDetails[0],
Constants.Sheet_TestCases),
Constants.Col_OffLoadNeeded, Constants.Sheet_TestCases)
.equalsIgnoreCase("Yes") || ExcelUtils.getCellData(
ExcelUtils.getRowContains(currentTestDetails[0],
Constants.Sheet_TestCases),
Constants.Col_OffLoadNeeded, Constants.Sheet_TestCases)
.equalsIgnoreCase("Bag") ) {

if (testPNR.offloadPNR(currentTestDetails[1], currentTestDetails[2], ExcelUtils.getCellData(
ExcelUtils.getRowContains(currentTestDetails[0],
Constants.Sheet_TestCases),
Constants.Col_OffLoadNeeded, Constants.Sheet_TestCases))) {
ExcelUtils.setCellData("Success", ExcelUtils
.getRowContains(currentTestDetails[0],
Constants.Sheet_TestCases),
Constants.Col_OffloadResult,
Constants.Sheet_TestCases);
} else {
ExcelUtils.setCellData("Failed", ExcelUtils.getRowContains(
currentTestDetails[0], Constants.Sheet_TestCases),
Constants.Col_OffloadResult,
Constants.Sheet_TestCases);
}

} else {
ExcelUtils.setCellData("NA", ExcelUtils.getRowContains(
currentTestDetails[0], Constants.Sheet_TestCases),
Constants.Col_OffloadResult, Constants.Sheet_TestCases);
}

testPNR.closeBrowser();
} catch (Exception e) {
// TODO Auto-generated catch block
e.printStackTrace();

testPNR.closeBrowser();
}
}

@DataProvider(name = "TestsToRun", parallel = true)
public Object[][] TestsToRun() throws Exception {
// prepareOr();

//ExcelUtils.setExcelFile(datasheetpath);
List<String> testList = new ArrayList<>(ExcelUtils.getRunableTests(Constants.Sheet_TestCases).values());
Object[][] obj = new Object[testList.size()][1];
for (int i = 0; i < testList.size(); i++) {
LinkedHashMap<Object, Object> datamap = new LinkedHashMap<Object, Object>();
datamap.put(i, (Object) testList.get(i));
obj[i][0] = datamap;
}
return obj;
}

@Test(dataProvider = "TestsToRun")
public void runTest(Map<Object, Object> map)
throws Exception, IllegalAccessException, InvocationTargetException {
System.out.println("The Thread id is "+ Thread.currentThread().getId());
System.out.println("Running Test"+map.toString());
offloadPNR(map);
Executor executor = new Executor(map.values().toArray()[0].toString());
Thread.sleep(10);
if (executor.isTestExecutionComplete) {
if (!executor.actionReporter.isTestPass()) {
Assert.fail(String.join(", ", executor.actionReporter.getCurrentTestDetails()) + " - Test Failed. \n" + executor.actionReporter.getInternal());
} 

}
}

@AfterTest
public void endReport(){
File fl = new File(Constants.localDir+"/logs");
File[] files = fl.listFiles(new FileFilter() {          
public boolean accept(File file) {
return file.isFile();
}
});
long lastMod = Long.MIN_VALUE;
File choice = null;
for (File file : files) {
if (file.lastModified() > lastMod) {
choice = file;
lastMod = file.lastModified();
}
}
extent.setSystemInfo("Log File location", " <a href=\"" +choice+"\" target=\"_blank\">Click here to open execution logs</a>");

extent.flush();
}

@AfterSuite
public void writeResults() throws Exception {
Boolean isFailed;
System.out.print("Writing Test Results.");
for(String test: testCaseTracker.keySet()) {
isFailed=false;
//System.out.println(test.toString());
LinkedHashMap<Integer, String> testSteps=testCaseTracker.get(test);
for (Integer testStep: testSteps.keySet()) {
ExcelUtils.setCellData(testSteps.get(testStep), testStep.intValue(),
Constants.Col_TestStepResult,
Constants.Sheet_TestSteps);
if(testSteps.get(testStep).equalsIgnoreCase(Constants.KEYWORD_FAIL)) {
ExcelUtils.setCellData(Constants.KEYWORD_FAIL, ExcelUtils
.getRowContains(test.toString(),
Constants.Sheet_TestCases),
Constants.Col_TestCaseResult,
Constants.Sheet_TestCases);
isFailed=true;
}
System.out.print(".");
}
if (!isFailed) {
ExcelUtils.setCellData(Constants.KEYWORD_PASS, ExcelUtils
.getRowContains(test.toString(),
Constants.Sheet_TestCases),
Constants.Col_TestCaseResult,
Constants.Sheet_TestCases);
System.out.print(".");
}
}
ExcelUtils.closeExcel();
System.out.println();
System.out.println("Test Results written successfully to Excel.");

} 

@BeforeClass
public void setupClassName(ITestContext context) {
context.getCurrentXmlTest().getSuite().setDataProviderThreadCount(5);
//context.getCurrentXmlTest().getSuite().setPreserveOrder(false);
}

private static void createReportPath (String path) {
File testDirectory = new File(path);
if (!testDirectory.exists()) {
if (testDirectory.mkdir()) {
System.out.println("Directory: " + path + " is created!" );
} else {
System.out.println("Failed to create directory: " + path);
}
} else {
System.out.println("Directory already exists: " + path);
}
}
}


