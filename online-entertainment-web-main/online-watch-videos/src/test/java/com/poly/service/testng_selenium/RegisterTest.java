package com.poly.service.testng_selenium;

import static org.testng.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.JavascriptExecutor;
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

public class RegisterTest {
	private final String EXCEL_PATH = ExcelUtil.DATA_PATH + "REGISTER_TEST.xlsx";
	private final String SHEET_RESULT = "RESULT_TEST";
	private final String SHEET_DATA_TO_TEST = "REGISTER_DATA";

	public WebDriver driver;
	public UIMap uimap;
	public UIMap datafile;

	private Set<TestResult> testLogs;
	private TestResult testResult;
	private String workingDir;

	@BeforeClass
	public void init() {
		WebDriverManager.chromedriver().setup();
		testLogs = new LinkedHashSet<>();
		workingDir = System.getProperty("user.dir");
		datafile = new UIMap(workingDir + "/src/test/resources/properties/datafile.properties");
		uimap = new UIMap(workingDir + "/src/test/resources/properties/locator_register.properties");
	}

	@BeforeMethod
	public void setUp() throws Exception {
		driver = new ChromeDriver();
		driver.get(datafile.getData("url_register"));
		driver.manage().window().maximize();
		testResult = new TestResult();
	}

	@Test(dataProvider = "registerDP")
	public void processRegister(String testName, String username, String password, String cfmPassword, String fullname,
			String email, String expected) throws Exception {
		WebElement usernameReg = driver.findElement(uimap.getLocator("username_register_field"));
		usernameReg.sendKeys(username);
		WebElement passwordReg = driver.findElement(uimap.getLocator("password_register_field"));
		passwordReg.sendKeys(password);
		WebElement cfmPasswordReg = driver.findElement(uimap.getLocator("cfmpassword_register_field"));
		cfmPasswordReg.sendKeys(cfmPassword);
		WebElement fullnameReg = driver.findElement(uimap.getLocator("fullname_register_field"));
		fullnameReg.sendKeys(fullname);
		WebElement emailReg = driver.findElement(uimap.getLocator("email_register_field"));
		emailReg.sendKeys(email);
		WebElement registerButton = driver.findElement(uimap.getLocator("register_button"));
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView()", registerButton);
		Thread.sleep(1000);
		registerButton.click();

		Thread.sleep(1000);

		String actual = driver.getCurrentUrl();

		StringBuilder testData = new StringBuilder();
		testData.append("username = ");
		testData.append(username.isBlank() ? "''\n" : username + "\n");
		testData.append("password = ");
		testData.append(password.isBlank() ? "''\n" : password + "\n");
		testData.append("confirmPassword = ");
		testData.append(cfmPassword.isBlank() ? "''\n" : cfmPassword + "\n");
		testData.append("fullname = ");
		testData.append(fullname.isBlank() ? "''\n" : fullname + "\n");
		testData.append("email = ");
		testData.append(email.isBlank() ? "''" : email);

		testResult.setTestName(testName);
		testResult.setTestData(testData.toString());
		testResult.setTestTime(new Date());
		testResult.setExpected("url: " + expected);
		testResult.setActual("url: " + actual);

		assertEquals(expected, actual);
	}

	@AfterMethod
	public void tearDown(ITestResult result) throws Exception {
		testResult.setTestMethod(result.getName());

		File file = new File(ExcelUtil.IMAGES_SRC + "register-test");
		if (!file.exists()) {
			file.mkdirs();
		}

		switch (result.getStatus()) {
			case ITestResult.SUCCESS -> {
				testResult.setStatus("Success");
				testResult.setException("<none>");
			}
			case ITestResult.FAILURE -> {
				testResult.setStatus("Failure");
				testResult.setException(result.getThrowable().getMessage());
				String pathPhoto = ExcelUtil.IMAGES_SRC + "register-test/" + "failure-" + System.currentTimeMillis()
						+ ".jpg";
				ExcelUtil.takeScreenShot(driver, pathPhoto);
				testResult.setImage(pathPhoto);
			}
			case ITestResult.SKIP -> {
				testResult.setStatus("Skip");
				testResult.setException("<none>");
			}
			default -> {
	
			}
		}
		testLogs.add(testResult);
		
		driver.close();
		driver.quit();
	}

	@AfterClass
	public void destroy() throws IOException {
		testResult.writeExcel(EXCEL_PATH, SHEET_RESULT, testLogs);
	}
	
	@DataProvider(name = "registerDP")
	public Object[][] registerData() throws Exception{
		XSSFWorkbook workbook = ExcelUtil.getWorkbook(EXCEL_PATH);
		XSSFSheet sheet = workbook.getSheet(SHEET_DATA_TO_TEST);
		Object[][] data = ExcelUtil.readSheetData(sheet);
		return data;
	}
}
