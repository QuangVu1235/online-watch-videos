package com.poly.service.testng_selenium;

import static org.testng.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.LinkedHashSet;
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

public class LoginTest {
	private final String EXCEL_PATH = ExcelUtil.DATA_PATH + "LOGIN_TEST.xlsx";
	private final String SHEET_RESULT = "RESULT_TEST";
	private final String SHEET_DATA_TO_TEST = "LOGIN_DATA";

	public WebDriver driver;
	public UIMap uimap;
	public UIMap datafile;

	private Set<TestResult> testLogs; // Chứa toàn bộ resultTest
	private TestResult resultTest; // Chứa kết quả mỗi test case
	private String workingDir;

	@BeforeClass
	public void init() {
		WebDriverManager.chromedriver().setup();
		testLogs = new LinkedHashSet<>();

		workingDir = System.getProperty("user.dir");
		datafile = new UIMap(workingDir + "/src/test/resources/properties/datafile.properties");
		uimap = new UIMap(workingDir + "/src/test/resources/properties/locator_login.properties");
	}

	@BeforeMethod
	public void setUp() throws Exception {
		driver = new ChromeDriver();
		driver.get(datafile.getData("url_login"));

		resultTest = new TestResult();
	}

	@Test(dataProvider = "loginDP")
	public void processLogin(String testName, String username, String password, String expected) throws Exception {
		WebElement usernameInput = driver.findElement(uimap.getLocator("username_login_field"));
		usernameInput.sendKeys(username);
		WebElement passwordInput = driver.findElement(uimap.getLocator("password_login_field"));
		passwordInput.sendKeys(password);
		WebElement loginButton = driver.findElement(uimap.getLocator("login_button"));
		Thread.sleep(1000);
		loginButton.click();
		Thread.sleep(1000);
		
		String actual = driver.getCurrentUrl();
		
		StringBuilder testData = new StringBuilder();
		testData.append("username=");
		testData.append(username.isBlank() ? "''\n" : username + "\n");
		testData.append("password=");
		testData.append(password.isBlank() ? "''" : password);
		
		resultTest.setTestName(testName);
		resultTest.setTestData(testData.toString());
		resultTest.setTestTime(new Date()); 
		resultTest.setExpected("url: " +  expected);
		resultTest.setActual("url: " +  actual);
		
		assertEquals(expected, actual);
	}

	@AfterMethod
	public void tearDown(ITestResult result) throws IOException {
		resultTest.setTestMethod(result.getName());
		
		File file = new File(ExcelUtil.IMAGES_SRC + "login-test/");
		if (!file.exists()) {
			file.mkdirs();
		}
		
		switch (result.getStatus()) {
			case ITestResult.SUCCESS -> {
				resultTest.setStatus("SUCCESS");
				resultTest.setException("<none>");
			}
			case ITestResult.FAILURE -> {
				resultTest.setStatus("FAILURE");
				resultTest.setException(result.getThrowable().getMessage());
				String pathPhoto = ExcelUtil.IMAGES_SRC + "login-test/" + "failure-" + System.currentTimeMillis() + ".png";
				ExcelUtil.takeScreenShot(driver, pathPhoto);
				resultTest.setImage(pathPhoto);
			}
			case ITestResult.SKIP -> {
				resultTest.setStatus("SKIP");
				resultTest.setException("<none>");
			}
			default -> {}
		}
		testLogs.add(resultTest);
		
		driver.close();
		driver.quit();
	}

	@AfterClass
	public void destroy() throws IOException {
		resultTest.writeExcel(EXCEL_PATH, SHEET_RESULT, testLogs);
	}

	@DataProvider(name = "loginDP")
	public Object[][] data() throws IOException {
		XSSFWorkbook workbook = ExcelUtil.getWorkbook(EXCEL_PATH);
		XSSFSheet sheet = workbook.getSheet(SHEET_DATA_TO_TEST);
		Object[][] data = ExcelUtil.readSheetData(sheet);
		return data;
	}

}
