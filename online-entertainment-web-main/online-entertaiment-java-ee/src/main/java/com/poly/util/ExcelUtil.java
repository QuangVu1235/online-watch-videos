package com.poly.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class ExcelUtil {
	public static final String DATA_PATH = System.getProperty("user.dir") + "/src/test/resources/data/";
	public static final String IMAGES_SRC = System.getProperty("user.dir") + "/src/test/resources/photo/";
	
	public static XSSFWorkbook getWorkbook(String filePath) throws IOException  {
		File src = new File(filePath);
		if (!src.exists()) {
			throw new IOException("Đường dẫn file không tồn tại: " + filePath);
		}
		FileInputStream fis = new FileInputStream(src); 
		XSSFWorkbook workbook = new XSSFWorkbook(fis); 
		fis.close();
		return workbook;
	}

	public static XSSFSheet getSheet(XSSFWorkbook workbook, String sheetName) { 
		XSSFSheet sheet = workbook.getSheet(sheetName); 
		if (sheet == null) {
			sheet = workbook.createSheet(sheetName);
			Row header = sheet.createRow(0);
			CellStyle rowStyle = ExcelUtil.getRowHeaderStyle(workbook);
			
			Cell cell;
			
			cell = header.createCell(0);
			cell.setCellValue("Test Name");
		    cell.setCellStyle(rowStyle);
		    
		    cell = header.createCell(1);
			cell.setCellValue("Test Data");
		    cell.setCellStyle(rowStyle);

		    cell = header.createCell(2);
			cell.setCellValue("Test Time");
		    cell.setCellStyle(rowStyle);
		    
		    cell = header.createCell(3);
			cell.setCellValue("Method Test");
		    cell.setCellStyle(rowStyle);
		    
		    cell = header.createCell(4);
			cell.setCellValue("Expected");
		    cell.setCellStyle(rowStyle);
		    
		    cell = header.createCell(5);
			cell.setCellValue("Actual");
		    cell.setCellStyle(rowStyle);
		    
		    cell = header.createCell(6);
			cell.setCellValue("Status");
		    cell.setCellStyle(rowStyle);
		    
		    cell = header.createCell(7);
			cell.setCellValue("Exception");
		    cell.setCellStyle(rowStyle);
		    
		    cell = header.createCell(8);
			cell.setCellValue("Photo");
		    cell.setCellStyle(rowStyle);
		    
		    header.setHeightInPoints(30);
		}
		return sheet;
	}

	public static CellStyle getRowStyleAlignCenter(XSSFWorkbook workbook) { 
		CellStyle rowStyle = workbook.createCellStyle();
		rowStyle.setAlignment(HorizontalAlignment.CENTER);
		rowStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		rowStyle.setWrapText(true);
		return rowStyle;
	}
	
	public static CellStyle getRowStyle(XSSFWorkbook workbook) { 
		CellStyle rowStyle = workbook.createCellStyle();
		rowStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		rowStyle.setWrapText(true);
		return rowStyle;
	}
	
	public static CellStyle getRowLinkStyle(XSSFWorkbook workbook) { 
		CellStyle cellStyle = getRowStyleAlignCenter(workbook);
		
		Font font = workbook.createFont();
		font.setUnderline(Font.U_SINGLE);
		font.setColor(IndexedColors.BLUE.getIndex());
		cellStyle.setFont(font);
		
		return cellStyle;
	}
	
	public static CellStyle getRowHeaderStyle(XSSFWorkbook workbook) { 
		CellStyle cellStyle = getRowStyleAlignCenter(workbook);
		cellStyle.setFillForegroundColor(IndexedColors.BLACK.getIndex());
		cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		
		Font font = workbook.createFont();
		font.setBold(true);
		font.setColor(IndexedColors.WHITE.getIndex());
		cellStyle.setFont(font);
		
		return cellStyle;
	}

	public static String getCellValue(XSSFSheet sheet, int row, int column) {
		String returnValue;
		XSSFCell cell = sheet.getRow(row).getCell(column); 
		try {
			if (cell.getCellType() == CellType.STRING) {
				returnValue = cell.getStringCellValue();
			} else if (cell.getCellType() == CellType.NUMERIC) {
				returnValue = String.format("%.0f", cell.getNumericCellValue());
			} else { 
				returnValue = "";
			}
		} catch (Exception e) {
			returnValue = "";
		}
		return returnValue;
	}

	public static void takeScreenShot(WebDriver driver, String outputSrc) throws IOException {
		File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(screenshot, new File(outputSrc));
	}

	public static Object[][] readSheetData(XSSFSheet sheet) {
		// Tổng số hàng
		int rows = sheet.getPhysicalNumberOfRows(); 
		// Tổng số cột
		int columns = sheet.getRow(0).getLastCellNum(); 
		
		// Tạo mảng 2 chiều, [rows - 1]: bỏ qua hàng tiêu đề, bắt đầu từ hàng thứ 2
		Object[][] data = new Object[rows - 1][columns]; 
		for (int row = 1; row < rows; row++) { 
			for (int col = 0; col < columns; col++) {
				data[row - 1][col] = ExcelUtil.getCellValue(sheet, row, col);
			}
		}
		
		return data;
	}

	public static void writeImage(String image, Row row, Cell cell, XSSFSheet sheet) throws IOException {
		InputStream is = new FileInputStream(image); 
		byte[] bytes = IOUtils.toByteArray(is);
		int pictureId = sheet.getWorkbook().addPicture(bytes, XSSFWorkbook.PICTURE_TYPE_PNG); 
		
		is.close();

		XSSFDrawing drawing = sheet.createDrawingPatriarch();
		ClientAnchor anchor = new XSSFClientAnchor(); 
		anchor.setCol1(cell.getColumnIndex());
		anchor.setRow1(row.getRowNum());
		anchor.setCol2(cell.getColumnIndex() + 1);
		anchor.setRow2(row.getRowNum() + 1);
		drawing.createPicture(anchor, pictureId);
	}

	public static void export(String outputSrc, XSSFWorkbook workbook) throws IOException {
		FileOutputStream out = new FileOutputStream(outputSrc);
		workbook.write(out);
		out.close();
	}

}
