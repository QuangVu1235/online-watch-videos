package com.poly.service.testng.log;

import java.io.IOException;
import java.util.Date;
import java.util.Set;

import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFHyperlink;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.poly.util.ExcelUtil;

public class TestResult {
	private String testName;
	private String testData;
	private Date testTime;
	private String testMethod;
	private String expected;
	private String actual;
	private String status;
	private String exception = null;
	private String image = null;
	
	public String getTestName() {
		return testName;
	}

	public void setTestName(String action) {
		this.testName = action;
	}

	public String getTestData() {
		return testData;
	}

	public void setTestData(String testData) {
		this.testData = testData;
	}

	public Date getTestTime() {
		return testTime;
	}

	public void setTestTime(Date logTime) {
		this.testTime = logTime;
	}

	public String getTestMethod() {
		return testMethod;
	}

	public void setTestMethod(String testMethod) {
		this.testMethod = testMethod;
	}

	public String getExpected() {
		return expected;
	}

	public void setExpected(String expected) {
		this.expected = expected;
	}

	public String getActual() {
		return actual;
	}

	public void setActual(String actual) {
		this.actual = actual;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getException() {
		return exception;
	}

	public void setException(String exception) {
		this.exception = exception;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
	
	public void writeExcel(String src, String sheetName, Set<TestResult> results) throws IOException {
		XSSFWorkbook workbook = ExcelUtil.getWorkbook(src);
		XSSFSheet sheet = ExcelUtil.getSheet(workbook, sheetName);
		
		int startRow = 0, lastRow = sheet.getPhysicalNumberOfRows();
		if (lastRow < startRow)
			lastRow = startRow;

		CellStyle rowStyle = ExcelUtil.getRowStyle(workbook);
		
		for (TestResult result : results) {
			Row row = sheet.createRow(lastRow);
			
			// row.setHeightInPoints(80);
			row.setHeight((short) - 1);
			row.setRowStyle(rowStyle);
			
			result.setRowExcel(row, sheet); 
			lastRow++;
		}
		ExcelUtil.export(src, workbook); 
	}
	
	public void setRowExcel(Row row, XSSFSheet sheet) throws IOException {
		int startIndex = 0;
		
		CreationHelper creationHelper = sheet.getWorkbook().getCreationHelper();
		CellStyle globalStyleAlignCenter = ExcelUtil.getRowStyleAlignCenter(sheet.getWorkbook());
		CellStyle globalStyle = ExcelUtil.getRowStyle(sheet.getWorkbook());
		CellStyle linkStyle = ExcelUtil.getRowLinkStyle(sheet.getWorkbook());
		Cell cell;
		
		cell = row.createCell(startIndex++); 
		cell.setCellValue(getTestName());
		cell.setCellStyle(globalStyle);
		
		cell = row.createCell(startIndex++);
		cell.setCellValue(getTestData());
		cell.setCellStyle(globalStyle);
		
		cell = row.createCell(startIndex++);
		cell.setCellValue(getTestTime());
		CellStyle datetimeStyle = globalStyleAlignCenter;
		datetimeStyle.setDataFormat(creationHelper.createDataFormat().getFormat("hh:mm:ss dd-mm-yyyy"));
		cell.setCellStyle(datetimeStyle);
		
		cell = row.createCell(startIndex++);
		cell.setCellValue(getTestMethod());
		cell.setCellStyle(globalStyleAlignCenter);
		
		cell = row.createCell(startIndex++);
		cell.setCellValue(getExpected());
		cell.setCellStyle(globalStyle);
		
		cell = row.createCell(startIndex++);
		cell.setCellValue(getActual());
		cell.setCellStyle(globalStyle);
		
		cell = row.createCell(startIndex++);
		cell.setCellValue(getStatus());
		cell.setCellStyle(globalStyleAlignCenter);
		
		if (getException() != null) {
			cell = row.createCell(startIndex++);
			cell.setCellValue(getException());
			cell.setCellStyle(globalStyleAlignCenter);
		}
		
		if (getImage() != null) {
			cell = row.createCell(startIndex++);
			cell.setCellStyle(globalStyle);
			ExcelUtil.writeImage(getImage(), row, cell, sheet);
			
			cell = row.createCell(startIndex++);
			cell.setCellValue("Link Photo");		
			cell.setCellStyle(linkStyle);
			
			XSSFHyperlink hyperlink = (XSSFHyperlink) creationHelper.createHyperlink(HyperlinkType.URL);
			hyperlink.setAddress(getImage().replace("\\", "/"));
			cell.setHyperlink(hyperlink);
		} else {
			cell = row.createCell(startIndex++);
			cell.setCellValue("<none>");		
			cell.setCellStyle(globalStyleAlignCenter);
		}
		
		//cell.getRow().setHeight((short) -1);
		
		//for (int i = 1; i <= startIndex; i++) {
		//	sheet.autoSizeColumn(i);
		//}
	}
}
