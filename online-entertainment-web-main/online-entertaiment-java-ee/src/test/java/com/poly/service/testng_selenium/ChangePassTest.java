package com.poly.service.testng_selenium;

import static org.testng.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
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

public class ChangePassTest {
	private final String EXCEL_PATH = ExcelUtil.DATA_PATH + "CHANGE_PASS_TEST.xlsx";
	private final String SHEET_RESULT = "RESULT_TEST";
	private final String SHEET_INSERT_DATA = "TEST_DATA";

	public WebDriver driver;
	public UIMap uimap;
	public UIMap datafile;

	private Set<TestResult> testLogs;
	private TestResult testResult; // Chứa toàn bộ resultTest
	private String workingDir; // Chứa kết quả mỗi test case

	@BeforeClass
	public void init() {
		WebDriverManager.chromedriver().setup();
		testLogs = new LinkedHashSet<>();

		workingDir = System.getProperty("user.dir");
		datafile = new UIMap(workingDir + "/src/test/resources/properties/datafile.properties");
		uimap = new UIMap(workingDir + "/src/test/resources/properties/locator_change_pass.properties");
	}

	@BeforeMethod
	public void setUp(Method method) throws Exception {
		driver = new ChromeDriver();
		testResult = new TestResult();
	}
	
	public String getTestData(String username, String currentPass, String newPass) {
		StringBuilder testData = new StringBuilder();
		testData.append("username=");
		testData.append(username.isBlank() ? "''\n" : username + "\n");
		testData.append("password=");
		testData.append(currentPass.isBlank() ? "''\n" : currentPass + "\n");
		testData.append("newPassword=");
		testData.append(newPass.isBlank() ? "''" : newPass);
		
		return testData.toString();
	}

	public void processLogin(String username, String password) throws Exception {
		driver.get(datafile.getData("url_login"));
		driver.manage().window().maximize();

		WebElement usernameInput = driver.findElement(uimap.getLocator("username_login_field"));
		usernameInput.sendKeys(username);
		WebElement passwordInput = driver.findElement(uimap.getLocator("password_login_field"));
		passwordInput.sendKeys(password);
		WebElement loginButton = driver.findElement(uimap.getLocator("login_button"));
		Thread.sleep(1000);
		loginButton.click();
		Thread.sleep(1000);
	}
	
	public void processLogout() throws Exception {
		WebElement navbarDropdown = driver.findElement(uimap.getLocator("navbar_dropdown"));
		navbarDropdown.click();
		Thread.sleep(1000);
		WebElement logoutDropdownItem = driver.findElement(uimap.getLocator("logout_dropdown_item"));
		logoutDropdownItem.click();
		Thread.sleep(1000);
	}
	
	public void openChangePassModal() throws Exception {
		WebElement navbarDropdown = driver.findElement(uimap.getLocator("navbar_dropdown"));
		navbarDropdown.click();
		Thread.sleep(1000);
		WebElement changePassDropdownItem = driver.findElement(uimap.getLocator("change_pass_dropdown_item"));
		changePassDropdownItem.click();
		Thread.sleep(1000);
	}
	
	public void fillAndSubmitChangePassForm(String username, String currentPass, String newPass) throws Exception {
		WebElement currentPassField = driver.findElement(uimap.getLocator("current_pass_field"));
		currentPassField.sendKeys(currentPass);
		WebElement newPassField = driver.findElement(uimap.getLocator("new_pass_field"));
		newPassField.sendKeys(newPass);
		WebElement confirmNewPassField = driver.findElement(uimap.getLocator("confirm_new_pass_field"));
		confirmNewPassField.sendKeys(newPass);
		Thread.sleep(1000);
		WebElement submitChangePassBtn = driver.findElement(uimap.getLocator("change_pass_btn"));
		submitChangePassBtn.click();
		Thread.sleep(1000);
	}
	
	@Test(dataProvider = "testData")
	public void changePassword(String testName, String username, String currentPass, String newPass, String expected) 
			throws Exception {
		String urlHome = datafile.getData("url_home");
		String actual = null;
		
		processLogin(username, currentPass);
		
		if (!driver.getCurrentUrl().equals(urlHome)) {
			WebElement loginMessage = driver.findElement(uimap.getLocator("login_message"));
			actual = loginMessage.getText();
		} else {
			openChangePassModal();
			fillAndSubmitChangePassForm(username, currentPass, newPass);
			
			WebElement messageStatus = driver.findElement(uimap.getLocator("change_pass_message"));
			Thread.sleep(1000);
			actual = messageStatus.getText();
			
			if (actual.equals("Đổi mật khẩu thành công!")) {
				processLogout();
				processLogin(username, newPass);
				if (!driver.getCurrentUrl().equals(urlHome)) {
					actual = "Đổi mật khẩu thất bại!";
				}
			} 
		}
		
		testResult.setTestData(getTestData(username, currentPass, newPass));
		testResult.setTestName(testName);
		testResult.setTestTime(new Date());
		testResult.setExpected(expected);
		testResult.setActual(actual != null ? actual : "Đổi mật khẩu thất bại");

		assertEquals(testResult.getActual(), testResult.getExpected());
	}
	
	@AfterMethod
	public void tearDown(ITestResult result, Method method) throws IOException {
		testResult.setTestMethod(result.getName());
		
		File file = new File(ExcelUtil.IMAGES_SRC + "change-pass-test");
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
				String pathPhoto = ExcelUtil.IMAGES_SRC + "change-pass-test/" + "failure-" + System.currentTimeMillis() + ".png";
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
		driver.close();
		driver.quit();
	}

	@AfterClass
	public void destroy() throws IOException {
		testResult.writeExcel(EXCEL_PATH, SHEET_RESULT, testLogs);
	}

	@DataProvider(name = "testData")
	public Object[][] testData() throws IOException {
		XSSFWorkbook workbook = ExcelUtil.getWorkbook(EXCEL_PATH);
		XSSFSheet sheet = workbook.getSheet(SHEET_INSERT_DATA);
		Object[][] data = ExcelUtil.readSheetData(sheet);
		return data;
	}

}
