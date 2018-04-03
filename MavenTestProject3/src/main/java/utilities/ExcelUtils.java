package utilities;

import java.io.FileInputStream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtils {
	private static XSSFWorkbook Workbook;
	private static XSSFSheet Sheet;
	private static XSSFRow ERow;
	private static XSSFCell ECell;
	private static String CellData;
	
	public static void setExcelFile (String path, String sheet ) throws Exception {
		FileInputStream file = new FileInputStream(path);
		Workbook=new XSSFWorkbook(file);
		Sheet = Workbook.getSheet(sheet);
	}
	public static String getCellData (int rownum, int colnum) {
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
}
