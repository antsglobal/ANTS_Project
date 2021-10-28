package utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.annotations.DataProvider;

public class excel_utils {
	
	public static Workbook workBook;
	public static Sheet excelSheet;
	public static Row row;
	public static Cell cell;
	
	public static Object[][] getExcelDataHM(String path, String sheet) {
		
		 
		Object[][] testData = null;
		
		try {
			FileInputStream ExcelFile = new FileInputStream(path);
			
			if (path.toLowerCase().endsWith("xlsx") == true) {
				workBook = new XSSFWorkbook(ExcelFile);
			} else if (path.toLowerCase().endsWith("xls") == true) {
				workBook = new HSSFWorkbook(ExcelFile);
			}
			
			excelSheet = workBook.getSheet(sheet);
			int totalRowCount = excelSheet.getLastRowNum();
			
			row = excelSheet.getRow(0);
			int totalColumns = row.getLastCellNum();
			
			testData = new Object[totalRowCount][1];
			
			//System.out.println(" no of rows " + totalRowCount);
			//System.out.println(" no of rocolsws " + totalColumns);
			
			
			for (int rowNo = 0; rowNo < totalRowCount; rowNo++) {
				
				Map<Object,Object> dataMap = new HashMap<Object,Object>();
				
				for (int columNo = 0; columNo < totalColumns; columNo++) {
					
					dataMap.put(excelSheet.getRow(0).getCell(columNo).toString(), excelSheet.getRow(rowNo+1).getCell(columNo));
					
					
				}
				
				testData[rowNo][0] = dataMap;
			}
			
		}catch(FileNotFoundException e) {
			System.out.println("Could not read the Excel sheet");
		}catch(IOException e) {
			
			System.out.println("Could not read the Excel sheet");
		}
		return testData;
	}
	
	@DataProvider(name = "assetlocationsadding")
	public static Object[][] assetlocationsadding() throws Exception {
		
		Object[][] testObjArray = getExcelDataHM("E:\\Automation\\eclipse-workspace\\ANTS_Project\\resource\\testdata.xlsx", "P_assetlocationsadding");
	
		return (testObjArray);

	}

	@DataProvider(name = "assetadding")
	public static Object[][] assetadding() throws Exception {
		
		Object[][] testObjArray = getExcelDataHM("E:\\Automation\\eclipse-workspace\\ANTS_Project\\resource\\testdata.xlsx", "p_assetadding");
	
		return (testObjArray);

	}
	
	@DataProvider(name = "getassets")
	public static Object[][] getassets() throws Exception {
		
		Object[][] testObjArray = getExcelDataHM("E:\\Automation\\eclipse-workspace\\ANTS_Project\\resource\\testdata.xlsx", "p_getassets");
	
		return (testObjArray);

	}
	
	@DataProvider(name = "assettracking")
	public static Object[][] assettracking() throws Exception {
		
		Object[][] testObjArray = getExcelDataHM("E:\\Automation\\eclipse-workspace\\ANTS_Project\\resource\\testdata.xlsx", "P_assettracking");
	
		return (testObjArray);

	}
	
	@DataProvider(name = "g_alldumperids")
	public static Object[][] g_alldumperids() throws Exception {
		
		Object[][] testObjArray = getExcelDataHM("E:\\Automation\\eclipse-workspace\\ANTS_Project\\resource\\get_data.xlsx", "g_alldumperids");
	
		return (testObjArray);

	}

	@DataProvider(name = "g_assetlocationsview")
	public static Object[][] g_assetlocationsview() throws Exception {
		
		Object[][] testObjArray = getExcelDataHM("E:\\Automation\\eclipse-workspace\\ANTS_Project\\resource\\get_data.xlsx", "g_assetlocationsview");
	
		return (testObjArray);

	}
	@DataProvider(name = "g_getlocations")
	public static Object[][] g_getlocations() throws Exception {
		
		Object[][] testObjArray = getExcelDataHM("E:\\Automation\\eclipse-workspace\\ANTS_Project\\resource\\get_data.xlsx", "g_getlocations");
	
		return (testObjArray);

	}
	
	@DataProvider(name = "g_assetview")
	public static Object[][] g_assetview() throws Exception {
		
		Object[][] testObjArray = getExcelDataHM("E:\\Automation\\eclipse-workspace\\ANTS_Project\\resource\\get_data.xlsx", "g_assetview");
	
		return (testObjArray);

	}
	
	@DataProvider(name = "g_assettrackingforui")
	public static Object[][] g_assettrackingforui() throws Exception {
		
		Object[][] testObjArray = getExcelDataHM("E:\\Automation\\eclipse-workspace\\ANTS_Project\\resource\\get_data.xlsx", "g_assettrackingforui");
	
		return (testObjArray);

	}

	@DataProvider(name = "p_dumperLiveLocation")
	public static Object[][] p_dumperLiveLocation() throws Exception {
		
		Object[][] testObjArray = getExcelDataHM("E:\\Automation\\eclipse-workspace\\ANTS_Project\\resource\\testdata.xlsx", "p_dumperLiveLocation");
	
		return (testObjArray);

	}
	
	@DataProvider(name = "p_dumperdetailscount")
	public static Object[][] p_dumperdetailscount() throws Exception {
		
		Object[][] testObjArray = getExcelDataHM("E:\\Automation\\eclipse-workspace\\ANTS_Project\\resource\\testdata.xlsx", "p_dumperdetailscount");
	
		return (testObjArray);

	}
	@DataProvider(name = "p_durationofthetrip")
	public static Object[][] p_durationofthetrip() throws Exception {
		
		Object[][] testObjArray = getExcelDataHM("E:\\Automation\\eclipse-workspace\\ANTS_Project\\resource\\testdata.xlsx", "p_durationofthetrip");
	
		return (testObjArray);

	}


}
