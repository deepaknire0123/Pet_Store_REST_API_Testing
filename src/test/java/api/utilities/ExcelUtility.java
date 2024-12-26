package api.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtility {
	
	public FileInputStream fi;
	public FileOutputStream fo;
	public XSSFWorkbook workbook;
	public XSSFSheet sheet;
	public XSSFRow row;
	public XSSFCell cell;
	public CellStyle style;
	String path;
	
	public ExcelUtility(String path) // Whenever we create object of ExcelUtility class -
	{								//It will automatically pass the Location of Excel/path of excel
		this.path = path;
	}
	
	/**
	 * Return the total number of rows in the specified sheet
	 * @return 
	 * @throws IOException 
	 */
	public int getRowCount(String sheetName) throws IOException
	{
		fi = new FileInputStream(path); //Creates a FileInputStream by opening a connection to an actual file,
		workbook = new XSSFWorkbook(fi); //the file named by the path name in the file system
		sheet = workbook.getSheet(sheetName);
		int rowCount = sheet.getLastRowNum();
//		int colCount = sheet.getRow(0).getLastCellNum();
		workbook.close();
		fi.close();
		return rowCount;
	}
	
	/**
	 * Returns total number of cell specified in a row
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public int getCellCount(String sheetName, int rowNum) throws FileNotFoundException, IOException
	{
		try(FileInputStream fi = new FileInputStream(path);
				XSSFWorkbook workbook = new XSSFWorkbook(fi))
		{
			sheet = workbook.getSheet(sheetName);
			row = sheet.getRow(rowNum);
			return row.getLastCellNum();
		}
	}
	
	/**
	 * Returns the cell data as a String from the specified row and column.
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	
	public String getCellData(String sheetName, int rowNum, int column) throws FileNotFoundException, IOException
	{
		try(FileInputStream fi = new FileInputStream(path);
				XSSFWorkbook workbook = new XSSFWorkbook(fi))
		{
			sheet = workbook.getSheet(sheetName);
			row = sheet.getRow(rowNum);
			if(row == null || row.getCell(column) == null)
			{
				return ""; // Handle null row or cell
			}
			cell = row.getCell(column);
			DataFormatter formatter = new DataFormatter(); //Returns the formatted value of a cell as a String regardless of the cell type
			return formatter.formatCellValue(cell);
		}
	}
	
	/**
	 * Writes data into the specified cell and saves the Excel file.
	 * @throws IOException 
	 */
	
	public void setCellData(String sheetName, int rowNum, int column, String data) throws IOException
	{
		File xlfile = new File(path);
		if(!xlfile.exists()) //If file not exists then create a new file
		{
			workbook = new XSSFWorkbook();
			fo = new FileOutputStream(path);
			workbook.write(fo);
		}
		
		fi = new FileInputStream(path);
		workbook= new XSSFWorkbook(fi);
		
		if(workbook.getSheetIndex(sheetName) == -1) //If sheet not exists it returns index -1 "Create a new Sheet"
		{
			workbook.createSheet(sheetName);
		}
		sheet = workbook.getSheet(sheetName);
		
		if(sheet.getRow(rowNum) == null) //If row does not exists
		{
			sheet.createRow(rowNum);
		}
		row = sheet.getRow(rowNum);
		
		cell = row.createCell(column);
		cell.setCellValue(data);
		
		fo = new FileOutputStream(path);
		workbook.write(fo);
		
		workbook.close();
		fi.close();
		fo.close();
	}
	
	/**
	 * Fills a cell with the specified color (GREEN or RED).
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	
	public void fillCellColor(String sheetName, int rowNum, int column, IndexedColors color) throws FileNotFoundException, IOException
	{
		fi = new FileInputStream(path);
		workbook = new XSSFWorkbook(fi);
		sheet = workbook.getSheet(sheetName);
		
		row = sheet.getRow(rowNum);
		cell = row.getCell(column);

		style = workbook.createCellStyle();
		style.setFillForegroundColor(color.getIndex());
		style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		cell.setCellStyle(style);
		fo = new FileOutputStream(path);
		workbook.write(fo);
		
		workbook.close();
		fi.close();
		fo.close();
	}
	
	/**
	 * Fills the cell with green color to indicate success.
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	
	public void fillGreenColor(String sheetName, int rowNum, int column) throws FileNotFoundException, IOException
	{
		fillCellColor(sheetName, rowNum, column, IndexedColors.GREEN);
	}
	
	/**
	 * Fills the cell with red color to indicate success.
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public void fillRedColor(String sheetName, int rowNum, int column) throws FileNotFoundException, IOException
	{
		fillCellColor(sheetName, rowNum, column, IndexedColors.RED);
	}

}
