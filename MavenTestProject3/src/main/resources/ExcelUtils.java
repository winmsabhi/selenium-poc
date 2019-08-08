package utilities;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import config.Constants;

public class ExcelUtils {
private static XSSFWorkbook Workbook;
private static XSSFSheet Sheet;
private static XSSFRow ERow;
private static XSSFCell ECell;
private static String CellData;

public static synchronized void setExcelFile(String path) throws Exception {
FileInputStream file = new FileInputStream(path);
ZipSecureFile.setMinInflateRatio(0);
Workbook = new XSSFWorkbook(file);
}

public static synchronized String getCellData(int RowNum, int ColNum, String SheetName) {
try {
Sheet = Workbook.getSheet(SheetName);
ERow = Sheet.getRow(RowNum);
ECell = ERow.getCell(ColNum, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
CellData = "";
if (ECell != null) {
switch (ECell.getCellTypeEnum()) {
case BLANK:
CellData = ECell.getStringCellValue().toString();
break;
case NUMERIC:
CellData = String.valueOf(ECell.getNumericCellValue());
break;
case BOOLEAN:
CellData = ECell.getStringCellValue().toString();
break;
case STRING:
CellData = ECell.getStringCellValue().toString();
break;
case _NONE:
CellData = ECell.getStringCellValue().toString();
break;
default:
CellData = "Error";
break;
}
} else{
CellData = "";
}
return CellData;
} catch (Exception e) {
// TODO Auto-generated catch block
//Executor.currentTestResult = false;
System.exit(1);
System.out.println("Unable to read Excel at row: " + RowNum + " column: "+ColNum+ " in sheet "+SheetName);
e.printStackTrace();
}
return CellData;
}

public static synchronized int getRowContains(String sTestCaseName, String sheetname) {
// TODO Auto-generated method stub
int i;
Sheet = Workbook.getSheet(sheetname);
int rowCount = getRowCount(sheetname);
for (i = 0; i < rowCount; i++) {
if (ExcelUtils.getCellData(i, Constants.Col_TestCaseID, sheetname).equalsIgnoreCase(sTestCaseName)) {
break;
}
}
return i;

}

private static synchronized int getRowCount(String sheetname) {
// TODO Auto-generated method stub
Sheet = Workbook.getSheet(sheetname);
int number = Sheet.getLastRowNum() + 1;
return number;
}

public static synchronized int getTestStepsCount(String sheetname, String testcasename) {
int number=0;
try {
for (int i = getRowContains(testcasename, sheetname); i < getRowCount(sheetname); i++) {
if (!testcasename.equals(getCellData(i, Constants.Col_TestCaseID, sheetname))) {
number = i;
return number-1;
}
}
Sheet = Workbook.getSheet(sheetname);
number = getRowCount(sheetname);
return number-1;
} catch (Exception e) {
// TODO Auto-generated catch block
System.out.println("Unable to get step count for testcase "+ testcasename);
//Executor.currentTestResult = false;
e.printStackTrace();
System.exit(1);
}
return number-1;
}

public static synchronized LinkedHashMap<Integer, String> getRunableTests(String sheetname) throws Exception {
setExcelFile(Constants.Path_TestData);
LinkedHashMap<Integer, String> testsToRun = new LinkedHashMap<Integer, String>();
int testCounter = 1;
for (int i = 1; i < getRowCount(sheetname); i++) {
if (getCellData(i, Constants.Col_RunAble, sheetname).trim().toLowerCase().equals("yes")) {
String TestCaseName = getCellData(i, Constants.Col_TestCaseID, sheetname);
String PNR = getCellData(i, Constants.Col_PNR, sheetname);
String LastName = getCellData(i, Constants.Col_LastName, sheetname);
if (!testsToRun.isEmpty()) {
Boolean testExists=false;
Iterator<String> iter = testsToRun.values().iterator();
while (iter.hasNext()) {
String str = iter.next();
if (str.equalsIgnoreCase(TestCaseName.trim() + "," + PNR.trim() + "," + LastName.trim())) {
testExists = true;
break;
} 
}
if (!testExists) {
testsToRun.put(testCounter++, TestCaseName.trim() + "," + PNR.trim() + "," + LastName.trim());
}
} else {
testsToRun.put(testCounter++, TestCaseName.trim() + "," + PNR.trim() + "," + LastName.trim());
}
}
}
return testsToRun;
}

public static synchronized void setCellData(String Result,  int RowNum, int ColNum, String SheetName) throws Exception    {
try{
Sheet = Workbook.getSheet(SheetName);
Map<String, CellStyle> styles = styles(Workbook);
ERow  = Sheet.getRow(RowNum);
ECell = ERow.getCell(ColNum, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
if (ECell == null) {
ECell = ERow.createCell(ColNum);
ECell.setCellValue(Result);
} else {
ECell.setCellValue(Result);
}
ECell.setCellStyle(createBorderedStyle(Workbook));
if(Result.trim().equalsIgnoreCase("Pass")){
ECell.setCellStyle(styles.get("Pass"));
} else if(Result.trim().equalsIgnoreCase("Fail")){
ECell.setCellStyle(styles.get("Fail"));
} else if(Result.trim().equalsIgnoreCase("Not Run")) {
ECell.setCellStyle(styles.get("NotRun"));
}
// Constant variables Test Data path and Test Data file name
}catch(Exception e){
//Executor.currentTestResult = false;
System.out.println("Unable to write to Excel at row: " + RowNum + " column: "+ColNum+ " in sheet "+SheetName+"");
e.printStackTrace();
System.exit(1);
}
}

public static void closeExcel() throws FileNotFoundException, IOException {
FileOutputStream fileOut = new FileOutputStream(Constants.Path_TestData);
Workbook.write(fileOut);
//fileOut.flush();
fileOut.close();
Workbook = new XSSFWorkbook(new FileInputStream(Constants.Path_TestData));
}
protected static synchronized Map<String, CellStyle> styles(final XSSFWorkbook wb) {
Map<String, CellStyle> styles = new HashMap<String, CellStyle>();
    CellStyle styleFail;
    DataFormat df = wb.createDataFormat();
    styleFail = createBorderedStyle(wb);
    styleFail.setAlignment(HorizontalAlignment.CENTER);
    styleFail.setWrapText(true);
    styleFail.setDataFormat(df.getFormat("text"));
    styleFail.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    styleFail.setFillForegroundColor(IndexedColors.RED.index);
    styles.put("Fail", styleFail);
    //CellStyle stylePass = 
    CellStyle stylePass;
    stylePass = createBorderedStyle(wb);
    stylePass.setAlignment(HorizontalAlignment.CENTER);
    stylePass.setWrapText(true);
    stylePass.setDataFormat(df.getFormat("text"));
    stylePass.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    stylePass.setFillForegroundColor(IndexedColors.GREEN.index);
    styles.put("Pass", stylePass);
    
    CellStyle styleNotRun;
    styleNotRun = createBorderedStyle(wb);
    styleNotRun.setAlignment(HorizontalAlignment.CENTER);
    styleNotRun.setWrapText(true);
    styleNotRun.setDataFormat(df.getFormat("text"));
    styleNotRun.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    styleNotRun.setFillForegroundColor(IndexedColors.ORANGE.index);
    styles.put("NotRun", styleNotRun);
    
    return styles;
}
private static CellStyle createBorderedStyle(XSSFWorkbook wb) {
        CellStyle style = wb.createCellStyle();
        style.setBorderRight(BorderStyle.THIN);
    style.setRightBorderColor(IndexedColors.BLACK.getIndex());
    style.setBorderLeft(BorderStyle.THIN);
    style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
    style.setBorderTop(BorderStyle.THIN);
    style.setTopBorderColor(IndexedColors.BLACK.getIndex());
    style.setBorderBottom(BorderStyle.THIN);
    style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        return style;
    }
public static synchronized void prepareSuite(){
try {
for (int i = 1; i < getRowCount(Constants.Sheet_TestCases); i++) {
setCellData("", i, Constants.Col_TestCaseResult,
Constants.Sheet_TestCases);
}
closeExcel();
System.out.println("Suite Ready for Execution");
} catch (Exception e) {
// TODO Auto-generated catch block
System.out.println("Unable to run PrepareSuite");
e.printStackTrace();
System.exit(1);
}
}
public static synchronized void prepareTest(String test){
try {
int stepStart=ExcelUtils.getRowContains(test, Constants.Sheet_TestSteps);
int stepEnd=ExcelUtils.getTestStepsCount(Constants.Sheet_TestSteps, test);
for (int i = stepStart; i <= stepEnd; i++) {
//setCellData("", i, Constants.Col_TestStepResult,
//Constants.Sheet_TestSteps);
Sheet = Workbook.getSheet(Constants.Sheet_TestSteps);
ERow  = Sheet.getRow(i);
ECell = ERow.getCell(Constants.Col_TestStepResult, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
if (ECell == null) {
ECell = ERow.createCell(Constants.Col_TestStepResult);
ECell.setCellValue("");
} else {
ECell.setCellValue("");
}
ECell.setCellStyle(createBorderedStyle(Workbook));
}
closeExcel();
System.out.println("Test Ready for Execution");
} catch (Exception e) {
// TODO Auto-generated catch block
System.out.println("Unable to run prepareTest for "+ test);
e.printStackTrace();
System.exit(1);
}
}
public LinkedHashMap<Integer, String> sortHashMapByValues(
        HashMap<Integer, String> passedMap) {
    List<Integer> mapKeys = new ArrayList<>(passedMap.keySet());
    List<String> mapValues = new ArrayList<>(passedMap.values());
    Collections.sort(mapValues);
    Collections.sort(mapKeys);

    LinkedHashMap<Integer, String> sortedMap =
        new LinkedHashMap<>();

    Iterator<String> valueIt = mapValues.iterator();
    while (valueIt.hasNext()) {
        String val = valueIt.next();
        Iterator<Integer> keyIt = mapKeys.iterator();

        while (keyIt.hasNext()) {
            Integer key = keyIt.next();
            String comp1 = passedMap.get(key);
            String comp2 = val;

            if (comp1.equals(comp2)) {
                keyIt.remove();
                sortedMap.put(key, val);
                break;
            }
        }
    }
    return sortedMap;
}
}