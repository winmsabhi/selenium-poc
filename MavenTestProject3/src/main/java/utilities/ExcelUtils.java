package utilities;

import java.io.FileInputStream;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.collections4.map.HashedMap;
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

	public static void setExcelFile(String path) throws Exception {
		FileInputStream file = new FileInputStream(path);
		Workbook = new XSSFWorkbook(file);
	}

	public static String getCellData(int rownum, int colnum, String sheetname) {
		Sheet = Workbook.getSheet(sheetname);
		ERow = Sheet.getRow(rownum);
		ECell = ERow.getCell(colnum, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
		CellData = "";
		if (ECell != null) {
			switch (ECell.getCellTypeEnum()) {
			case BLANK:
				CellData = ECell.getStringCellValue().toString();
				break;
			case NUMERIC:
				CellData = ECell.getStringCellValue().toString();
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
		}
		return CellData;
	}

	public static int getRowContains(String sTestCaseName, String sheetname) {
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

	private static int getRowCount(String sheetname) {
		// TODO Auto-generated method stub
		Sheet = Workbook.getSheet(sheetname);
		int number = Sheet.getLastRowNum() + 1;
		return number;
	}

	public static int getTestStepsCount(String sheetname, String testcasename) {
		for (int i = getRowContains(testcasename, sheetname); i < getRowCount(sheetname); i++) {
			if (!testcasename.equals(getCellData(i, Constants.Col_TestCaseID, sheetname))) {
				int number = i;
				return number;
			}
		}
		Sheet = Workbook.getSheet(sheetname);
		int number = getRowCount(sheetname) + 1;
		return number;
	}

	public static Map<Integer, String> getRunableTests(String sheetname) throws Exception {
		setExcelFile(Constants.Path_TestData);
		Map<Integer, String> testsToRun = new HashedMap<Integer, String>();
		int testCounter = 1;
		for (int i = 1; i < getRowCount(sheetname); i++) {
			if (getCellData(i, Constants.Col_RunAble, sheetname).trim().toLowerCase().equals("yes")) {
				String TestCaseName = getCellData(i, Constants.Col_TestCaseID, sheetname);
				String Browser = getCellData(i, Constants.Col_Browser, sheetname);
				if (!testsToRun.isEmpty()) {
					Boolean testExists=false;
					Iterator<String> iter = testsToRun.values().iterator();
					while (iter.hasNext()) {
						String str = iter.next();
						if (str.equalsIgnoreCase(TestCaseName + "," + Browser)) {
							testExists = true;
							break;
						} 
					}
					if (!testExists) {
						testsToRun.put(testCounter++, TestCaseName + "," + Browser);
					}
				} else {
					testsToRun.put(testCounter++, TestCaseName + "," + Browser);
				}
			}
		}
		return testsToRun;
	}
}
