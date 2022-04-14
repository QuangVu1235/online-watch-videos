package com.poly.service.testng_selenium;

import static org.testng.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.poly.service.testng.log.TestResult;
import com.poly.util.ExcelUtil;
import com.poly.util.UIMap;

import io.github.bonigarcia.wdm.WebDriverManager;

public class UserTest {
	private final String EXCEL_PATH = ExcelUtil.DATA_PATH + "USER_TEST.xlsx";
	private final String SHEET_RESULT = "RESULT_TEST";
	private final String SHEET_INSERT_TEST = "INSERT_DATA";
	private final String SHEET_DELETE_TEST = "DELETE_DATA";
	private final String SHEET_UPDATE_TEST = "UPDATE_DATA";

	public WebDriver driver;
	public UIMap uimap;
	public UIMap datafile;

	private Set<TestResult> testLogs;
	private TestResult testResult; // Chứa toàn bộ resultTest
	private String workingDir; // Chứa kết quả mỗi test case

	@BeforeClass
	public void init() {
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
		testLogs = new LinkedHashSet<>();

		workingDir = System.getProperty("user.dir");
		datafile = new UIMap(workingDir + "/src/test/resources/properties/datafile.properties");
		uimap = new UIMap(workingDir + "/src/test/resources/properties/locator_user.properties");
	}

	@BeforeMethod
	public void setUp(Method method) throws Exception {
		if (method.getName().equals("processLogin")) {
			return;
		}
		testResult = new TestResult();
	}

	@Test(priority = 1)
	public void processLogin() throws Exception {
		String username = datafile.getData("username_login");
		String password = datafile.getData("password_login");

		driver.get(datafile.getData("url_login"));
		driver.manage().window().maximize();

		WebElement usernameInput = driver.findElement(uimap.getLocator("username_login_field"));
		usernameInput.sendKeys(username);
		WebElement passwordInput = driver.findElement(uimap.getLocator("password_login_field"));
		passwordInput.sendKeys(password);
		WebElement loginButton = driver.findElement(uimap.getLocator("login_button"));
		loginButton.click();
	}
	
	@Test(dataProvider = "insertDP", dependsOnMethods = "processLogin", priority = 2, enabled = true)
	public void insertUser(String testName, String id, String password, String fullname, String email, String role,
			String expected) throws Exception {
		driver.get(datafile.getData("url_add_user"));
		
		fillAndSubmitUserForm(id, password, fullname, email, role);
		
		WebElement messageStatus = driver.findElement(uimap.getLocator("user_message_status"));
		String actual = messageStatus.getText();

		testResult.setTestData(getTestData(id, password, fullname, email, role));
		testResult.setTestName(testName);
		testResult.setTestTime(new Date());
		testResult.setExpected(expected);
		testResult.setActual(actual);

		assertEquals(actual, expected);
	}
	
	@Test(dataProvider = "updateDP", dependsOnMethods = "processLogin", priority = 3, enabled = true)
	public void updateUser(String testName, String id, String password, String fullname, String email, String role,
			String expected) throws Exception {
		String actual = null;
		
		driver.get(datafile.getData("url_list_user"));
		driver.manage().window().maximize();
		
		WebElement userRow = searchUserTableByUserId(id);
		if (userRow != null) {
			WebElement editButton = userRow.findElement(uimap.getLocator("user_edit_button"));
			editButton.click();
			Thread.sleep(1000);
			
			fillAndSubmitUserForm(id, password, fullname, email, role);
			
			WebElement messageStatus = driver.findElement(uimap.getLocator("user_message_status"));
			actual = messageStatus.getText();
		} else {
			actual = "User không tồn tại";
		}
		
		testResult.setTestData(getTestData(id, password, fullname, email, role));
		testResult.setTestName(testName);
		testResult.setTestTime(new Date());
		testResult.setExpected(expected);
		testResult.setActual(actual);

		assertEquals(actual, expected);
	}
	
	@Test(dataProvider = "deleteDP", dependsOnMethods = "processLogin", priority = 4, enabled = true)
	public void deleteUser(String testName, String id, String expected) throws Exception {
		String actual = "Xoá không thành công!";
		
		driver.get(datafile.getData("url_list_user"));
		driver.manage().window().maximize();
		
		WebElement userRow = searchUserTableByUserId(id);
		
		if (userRow != null){
			WebElement deleteBtn = userRow.findElement(uimap.getLocator("user_delete_button"));
			deleteBtn.click();
			Thread.sleep(1000);
			
			if (searchUserTableByUserId(id) == null) {
				actual = "Xoá thành công!";
			} 
		} else {
			actual = "User không tồn tại!";
		}
		
		testResult.setTestData("id=" + id);
		testResult.setTestName(testName);
		testResult.setTestTime(new Date());
		testResult.setExpected(expected);
		testResult.setActual(actual);
		
		assertEquals(actual, expected);
	}

	@AfterMethod
	public void tearDown(ITestResult result, Method method) throws IOException {
		if (method.getName().equals("processLogin")) {
			return;
		}
		
		testResult.setTestMethod(result.getName());
		
		File file = new File(ExcelUtil.IMAGES_SRC + "user-test");
		if (!file.exists()) {
			file.mkdirs();
		}
		
		switch (result.getStatus()) {
			case ITestResult.SUCCESS -> {
				testResult.setStatus("SUCCESS");
				testResult.setException("<none>");
			}
			case ITestResult.FAILURE -> {
				testResult.setStatus("FAILURE");
				testResult.setException(result.getThrowable().getMessage());
				String pathPhoto = ExcelUtil.IMAGES_SRC + "user-test/" + "failure-" + System.currentTimeMillis() + ".png";
				ExcelUtil.takeScreenShot(driver, pathPhoto);
				testResult.setImage(pathPhoto);
			}
			case ITestResult.SKIP -> {
				testResult.setStatus("SKIP");
				testResult.setException("<none>");
			}
			default -> {
			}
		}
		testLogs.add(testResult);
	}

	@AfterClass
	public void destroy() throws IOException {
		driver.close();
		driver.quit();
		testResult.writeExcel(EXCEL_PATH, SHEET_RESULT, testLogs);
	}

	@DataProvider(name = "insertDP")
	public Object[][] insertData() throws IOException {
		XSSFWorkbook workbook = ExcelUtil.getWorkbook(EXCEL_PATH);
		XSSFSheet sheet = workbook.getSheet(SHEET_INSERT_TEST);
		Object[][] data = ExcelUtil.readSheetData(sheet);
		return data;
	}
	
	@DataProvider(name = "deleteDP")
	public Object[][] deleteData() throws IOException {
		XSSFWorkbook workbook = ExcelUtil.getWorkbook(EXCEL_PATH);
		XSSFSheet sheet = workbook.getSheet(SHEET_DELETE_TEST);
		Object[][] data = ExcelUtil.readSheetData(sheet);
		return data;
	}
	
	@DataProvider(name = "updateDP")
	public Object[][] updateData() throws IOException {
		XSSFWorkbook workbook = ExcelUtil.getWorkbook(EXCEL_PATH);
		XSSFSheet sheet = workbook.getSheet(SHEET_UPDATE_TEST);
		Object[][] data = ExcelUtil.readSheetData(sheet);
		return data;
	}
	
	public WebElement searchUserTableByUserId(String id) throws Exception {
		List<WebElement> rows = driver.findElements(uimap.getLocator("user_row"));
		for (WebElement row : rows) {
			if (row.findElement(uimap.getLocator("user_cell_id")).getText().equals(id)) {
				return row;
			}
		}
		return null;
	}
	
	public void fillAndSubmitUserForm(String id, String password, String fullname, String email, String role) throws Exception {
		WebElement idField = driver.findElement(uimap.getLocator("user_id_field"));
		if (idField.isEnabled()) {
			idField.clear();
			idField.sendKeys(id);
		}
		WebElement passField = driver.findElement(uimap.getLocator("user_password_field"));
		passField.clear();
		passField.sendKeys(password);
		WebElement fullnameField = driver.findElement(uimap.getLocator("user_fullname_field"));
		fullnameField.clear();
		fullnameField.sendKeys(fullname);
		WebElement emailField = driver.findElement(uimap.getLocator("user_email_field"));
		emailField.clear();
		emailField.sendKeys(email);
		WebElement roleRadio = driver.findElement(uimap.getLocator(role.equals("admin") ? "admin_radio" : "user_radio"));
		roleRadio.click();
		WebElement submitButton = driver.findElement(uimap.getLocator("user_submit_button"));
		submitButton.click();
		Thread.sleep(1000);
	}

	public String getTestData(String id, String password, String fullname, String email, String role) {
		StringBuilder testData = new StringBuilder();
		testData.append("id=");
		testData.append(id.isBlank() ? "''\n" : id + "\n");
		testData.append("password=");
		testData.append(password.isBlank() ? "''\n" : password + "\n");
		testData.append("fullname=");
		testData.append(fullname.isBlank() ? "''\n" : fullname + "\n");
		testData.append("email=");
		testData.append(email.isBlank() ? "''\n" : email + "\n");
		testData.append("role=");
		testData.append(role.isBlank() ? "''\n" : role);
		
		return testData.toString();
	}

}
