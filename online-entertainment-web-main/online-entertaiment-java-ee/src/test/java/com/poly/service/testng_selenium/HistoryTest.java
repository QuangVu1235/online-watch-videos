package com.poly.service.testng_selenium;

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
import org.testng.Assert;
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

public class HistoryTest {
	private final String EXCEL_PATH = ExcelUtil.DATA_PATH + "HISTORY_TEST.xlsx";
	private final String SHEET_RESULT = "RESULT_TEST";
	private final String SHEET_INSERT_TEST = "INSERT_DATA";
	private final String SHEET_DELETE_TEST = "DELETE_DATA";

	public WebDriver driver;
	public UIMap uimap;
	public UIMap datafile;

	private Set<TestResult> testLogs;
	private TestResult testResult;
	private String workingDir;

	@BeforeClass
	public void init() {
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();

		testLogs = new LinkedHashSet<>();

		workingDir = System.getProperty("user.dir");
		datafile = new UIMap(workingDir + "/src/test/resources/properties/datafile.properties");
		uimap = new UIMap(workingDir + "/src/test/resources/properties/locator_history.properties");
	}

	@BeforeMethod
	public void setUp(Method method) {
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

	public WebElement findVideoElementByVideoId(List<WebElement> videos, String videoId) {
		for (WebElement video : videos) {
			WebElement videoLink;
			try {
				videoLink = video.findElement(uimap.getLocator("video_link"));
				if (videoLink.getAttribute("href").contains(videoId)) {
					return video;
				}
			} catch (Exception e) {return null;}
		}
		return null;
	}

	@Test(dataProvider = "insertDP", dependsOnMethods = "processLogin", priority = 2)
	public void insertHistory(String testName, String videoId, String expected) throws Exception {
		String actual = null;
		try {
			driver.get(datafile.getData("url_home")); // Mở trang chủ
			List<WebElement> videoList = driver.findElements(uimap.getLocator("video_item"));
			WebElement video = findVideoElementByVideoId(videoList, videoId);
			video.click();
			Thread.sleep(1000);

			driver.get(datafile.getData("url_history_video")); // Mở trang lịch sử xem
			List<WebElement> historyList = driver.findElements(uimap.getLocator("history_item"));
			WebElement history = findVideoElementByVideoId(historyList, videoId);
			Thread.sleep(1000);
			
			actual = (history != null) ? "Thêm lịch sử xem thành công" : "Thêm lịch sử xem thất bại";
		} catch (NullPointerException e) {
			actual = "Video không tồn tại";
		} finally {
			StringBuilder testData = new StringBuilder();
			testData.append("videoId=");
			testData.append(videoId.isBlank() ? "''\n" : videoId);
			testResult.setTestData(testData.toString());
			testResult.setActual(actual);
			testResult.setTestName(testName);
			testResult.setTestTime(new Date());
			testResult.setExpected(expected);

			Assert.assertEquals(actual, expected);
		}
	}

	@Test(dataProvider = "deleteDP", dependsOnMethods = "processLogin", priority = 3)
	public void deleteHistoty(String testName, String videoId, String expected) throws Exception {
		String actual = null;
		try {
			driver.get(datafile.getData("url_history_video")); // Mở trang lịch sử xem
			List<WebElement> historyList = driver.findElements(uimap.getLocator("history_item"));
			WebElement history = findVideoElementByVideoId(historyList, videoId);
			WebElement historyDeleteBtn = history.findElement(uimap.getLocator("history_remove_btn"));
			System.out.println(historyDeleteBtn.getAttribute("data-user-id"));
			historyDeleteBtn.click();
			Thread.sleep(1000);
			
			WebElement result = findVideoElementByVideoId(historyList, videoId);
			
			actual = (result == null) ? "Xoá lịch sử xem thành công" : "Xoá lịch sử xem thất bại";
		} catch (NullPointerException e) {
			actual = "Video không tồn tại";
		} finally {
			StringBuilder testData = new StringBuilder();
			testData.append("videoId=");
			testData.append(videoId.isBlank() ? "''\n" : videoId);
			testResult.setActual(actual);
			testResult.setTestData(testData.toString());
			testResult.setTestName(testName);
			testResult.setTestTime(new Date());
			testResult.setExpected(expected);
			
			Assert.assertEquals(actual, expected);
		}
	}

	@AfterMethod()
	public void tearDown(ITestResult result, Method method) throws IOException {
		if (method.getName().equals("processLogin")) {
			return;
		}
		
		testResult.setTestMethod(result.getName());

		File file = new File(ExcelUtil.IMAGES_SRC + "history-test");

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
	
				String pathPhoto = ExcelUtil.IMAGES_SRC + "history-test/" + "failure-" + System.currentTimeMillis()
						+ ".png";
				ExcelUtil.takeScreenShot(driver, pathPhoto);
				testResult.setImage(pathPhoto);
			}
			case ITestResult.SKIP -> {
				testResult.setStatus("SKIP");
				testResult.setException("<none>");
			}
			default -> {}
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

}
