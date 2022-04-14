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

public class VideoTest {
	private final String EXCEL_PATH = ExcelUtil.DATA_PATH + "VIDEO_TEST.xlsx";
	private final String SHEET_RESULT = "RESULT_TEST";
	private final String SHEET_INSERT_TEST = "INSERT_DATA";
	private final String SHEET_DELETE_TEST = "DELETE_DATA";
	private final String SHEET_UPDATE_TEST = "UPDATE_DATA";

	public WebDriver driver;
	public UIMap uimap;
	public UIMap datafile;

	private Set<TestResult> testLogs; // Chứa tất cả các testResult
	private TestResult testResult; // Chứa kết quả cho mỗi test case
	private String workingDir;

	@BeforeClass
	public void init() {
		WebDriverManager.chromedriver().setup(); // Caì đặt chromeDriver
		driver = new ChromeDriver();
		testLogs = new LinkedHashSet<>(); // Tạo nhật ký chứa kết quả của tất cả các test case

		workingDir = System.getProperty("user.dir"); // Lấy đường dẫn hiện tại của project
		datafile = new UIMap(workingDir + "/src/test/resources/properties/datafile.properties"); // chứa các dữ liệu cần
																									// thiết để test như
																									// đường dẫn trang
																									// web...
		uimap = new UIMap(workingDir + "/src/test/resources/properties/locator_video.properties"); // chứa các giá trị
																									// định vị các phần
																									// tử của trang web
	}

	@BeforeMethod
	public void setUp(Method method) throws Exception {
		if (method.getName().equals("processLogin")) {
			return;
		}
		testResult = new TestResult(); // tạo mới một testResult để chứa kết quả cho test case tiếp theo
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
	public void insertVideo(String testName, String title, String id, String description, String expected)
			throws Exception {
		driver.get(datafile.getData("url_add_video"));
		driver.manage().window().maximize();
		Thread.sleep(1000);

		fillAndSubmitVideoForm(title, id, description);

		WebElement messageStatus = driver.findElement(uimap.getLocator("video_message_status"));
		Thread.sleep(1000);

		String actual = messageStatus.getText();

		if (actual.isBlank() || actual == null) {
			actual = "Thêm mới thất bại!";
		}

		testResult.setTestData(getTestData(title, id, description));
		testResult.setTestName(testName);
		testResult.setTestTime(new Date());
		testResult.setExpected(expected);
		testResult.setActual(actual);

		assertEquals(actual, expected);
	}

	@Test(dataProvider = "updateDP", dependsOnMethods = "processLogin", priority = 3, enabled = true)
	public void updateVideo(String testName, String title, String id, String description, String expected)
			throws Exception {
		driver.get(datafile.getData("url_list_video"));
		driver.manage().window().maximize();
		Thread.sleep(1000);

		String actual = null;
		WebElement videoRow = searchVideoElementByUserId(id);

		if (videoRow != null) {
			WebElement editButton = videoRow.findElement(uimap.getLocator("video_edit_button"));
			JavascriptExecutor jse = (JavascriptExecutor) driver;
			jse.executeScript("arguments[0].scrollIntoView()", editButton);
			Thread.sleep(500);
			editButton.click();
			Thread.sleep(500);
			fillAndSubmitVideoForm(title, id, description);
			WebElement messageStatus = driver.findElement(uimap.getLocator("video_message_status"));
			Thread.sleep(500);
			actual = messageStatus.getText();
		} else {
			actual = "Video ID không tồn tại!";
		}

		testResult.setTestData(getTestData(title, id, description));
		testResult.setTestName(testName);
		testResult.setTestTime(new Date());
		testResult.setExpected(expected);
		testResult.setActual(actual);

		assertEquals(actual, expected);
	}


	@Test(dataProvider = "deleteDP", dependsOnMethods = "processLogin", priority = 4, enabled = true)
	public void deleteVideo(String testName, String id, String expected) throws Exception { // kiểm thử chức năng xoá																		// video
		driver.get(datafile.getData("url_list_video"));
		driver.manage().window().maximize();

		WebElement videoRow = searchVideoElementByUserId(id);
		String actual = null;

		if (videoRow == null) {
			actual = "Video không tồn tại!";
		} else {
			WebElement deleteBtn = videoRow.findElement(uimap.getLocator("video_delete_button"));
			// Cuộn thanh kéo tới phần tử cần tìm
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView()", deleteBtn);
			Thread.sleep(1000);
			deleteBtn.click();
			Thread.sleep(1000);
			if (searchVideoElementByUserId(id) == null) {
				actual = "Xoá thành công!";
			} else {
				actual = "Xoá thất bại!";
			}
		}

		testResult.setTestData("id=" + id);
		testResult.setTestName(testName);
		testResult.setTestTime(new Date());
		testResult.setExpected(expected);
		testResult.setActual(actual);

		assertEquals(actual, expected);
	}

	public WebElement findVideoElementByVideoId(List<WebElement> videos, String videoId) {
		for (WebElement video : videos) {
			WebElement videoLink;
			try {
				videoLink = video.findElement(uimap.getLocator("video_link"));
				if (videoLink.getAttribute("href").contains(videoId)) {
					return video;
				}
			} catch (Exception e) {
				return null;
			}
		}
		return null;
	}

	@AfterMethod
	public void tearDown(ITestResult result, Method method) throws IOException {
		if (method.getName().equals("processLogin")) {
			return;
		}

		testResult.setTestMethod(result.getName());

		File file = new File(ExcelUtil.IMAGES_SRC + "video-test");
		if (!file.exists()) {
			file.mkdirs();
		}

		switch (result.getStatus()) {
		case ITestResult.SUCCESS -> {
			testResult.setStatus("SUCCESS");
		}
		case ITestResult.FAILURE -> {
			testResult.setStatus("FAILURE");
			testResult.setException(result.getThrowable().getMessage());
			String pathPhoto = ExcelUtil.IMAGES_SRC + "video-test/" + "failure-" + System.currentTimeMillis() + ".png";
			ExcelUtil.takeScreenShot(driver, pathPhoto);
			testResult.setImage(pathPhoto);
		}
		case ITestResult.SKIP -> {
			testResult.setStatus("SKIP");
		}
		default -> {
		}
		}
		testLogs.add(testResult); // ghi kq của test case vào nhật ký
	}

	@AfterClass
	public void destroy() throws IOException {
		driver.close(); // đóng trình duyệt
		driver.quit(); // ngắt kết nối driver
		testResult.writeExcel(EXCEL_PATH, SHEET_RESULT, testLogs); // ghi nhật ký kết quả vào file excel
	}

	@DataProvider(name = "insertDP")
	public Object[][] insertData() throws IOException {
		XSSFWorkbook workbook = ExcelUtil.getWorkbook(EXCEL_PATH); // lấy workbook file excel
		XSSFSheet sheet = workbook.getSheet(SHEET_INSERT_TEST); // lấy sheet cần tìm
		Object[][] data = ExcelUtil.readSheetData(sheet); // đọc dữ liệu từ sheet vừa lấy lưu vào đối tượng data
		return data;
	}

	@DataProvider(name = "updateDP")
	public Object[][] updateData() throws IOException {
		XSSFWorkbook workbook = ExcelUtil.getWorkbook(EXCEL_PATH);
		XSSFSheet sheet = workbook.getSheet(SHEET_UPDATE_TEST);
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
	
	public WebElement searchVideoElementByUserId(String id) throws Exception {
		List<WebElement> tableVideos = driver.findElements(uimap.getLocator("video_row"));
		for (WebElement row : tableVideos) {
			if (row.findElement(uimap.getLocator("video_cell_id")).getText().equals(id)) {
				return row;
			}
		}
		return null;
	}

	public void fillAndSubmitVideoForm(String title, String id, String description) throws Exception {
		WebElement titleInput = driver.findElement(uimap.getLocator("video_title_field"));
		titleInput.clear();
		titleInput.sendKeys(title);

		WebElement idInput = driver.findElement(uimap.getLocator("video_id_field"));
		if (idInput.isEnabled()) {
			idInput.clear();
			idInput.sendKeys(id);
		}
		WebElement descriptionInput = driver.findElement(uimap.getLocator("video_description_field"));
		descriptionInput.clear();
		descriptionInput.sendKeys(description);

		WebElement submitButton = driver.findElement(uimap.getLocator("video_submit_button"));
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView()", submitButton);
		submitButton.click();
	}

	public String getTestData(String title, String id, String description) { // ghi dữ liệu vào ô testData trong file
																				// excel
		StringBuilder testData = new StringBuilder();
		testData.append("title=");
		testData.append(title.isBlank() ? "''\n" : title + "\n");
		testData.append("id=");
		testData.append(id.isBlank() ? "''\n" : id + "\n");
		testData.append("description=");
		testData.append(description.isBlank() ? "''\n" : description);
		return testData.toString();
	}

}
