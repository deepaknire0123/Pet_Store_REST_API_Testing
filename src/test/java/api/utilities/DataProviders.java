package api.utilities;

import java.io.IOException;

import org.testng.annotations.DataProvider;

public class DataProviders {
	
	@DataProvider(name = "AllData")
	public Object[][] allData() throws IOException
	{
		String path = System.getProperty("user.dir") + "\\testData\\RestAPITestData.xlsx";
		
		ExcelUtility xlUtils = new ExcelUtility(path);
		
		int totalRows = xlUtils.getRowCount("Sheet1");
		int totalCols = xlUtils.getCellCount("Sheet1", 1);
		
		Object [][] Data = new Object[totalRows][totalCols];
		
		for(int r=1;r<=totalRows;r++)
		{
			for(int c=0;c<totalCols;c++)
			{
				Data[r-1][c] = xlUtils.getCellData("Sheet1", r, c);
			}
		}
		return Data;
	}
	
	
	@DataProvider(name = "UserNamesData")
	public Object[] UserNamesData() throws IOException
	{
		String path = System.getProperty("user.dir") + "\\testData\\RestAPITestData.xlsx";
		
		ExcelUtility xlUtils = new ExcelUtility(path);
		
		int totalRows = xlUtils.getRowCount("Sheet1");
		//int totalCols = xlUtils.getCellCount("Sheet1", 1);
		
		Object [] Data = new Object[totalRows];
		
		for(int r=1;r<=totalRows;r++)
		{
			Data[r-1] = xlUtils.getCellData("Sheet1", r, 1);

		}
		return Data;
	}
	
	

}
