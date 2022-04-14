package com.poly.service.testng;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.util.List;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.poly.entity.Share;
import com.poly.service.ShareService;
import com.poly.service.impl.ShareServiceImpl;

public class ShareServiceTest {
	private ShareService service;
	@BeforeMethod
	public void setUp() throws Exception {
		service = new  ShareServiceImpl();
	}

	@AfterMethod
	public void tearDown() throws Exception {
		service = null;
	}

	@Test
	public void testShareList1() {
		String userId = "VanNTT";
		List<Share> list = service.findByUser(userId);
		int expected = 1;
		int actual = list.size();
		assertEquals(expected,actual);
	}
	
	@Test(expectedExceptions = Exception.class)
	public void testShareList2() {
		String userId = "";
		service.findByUser(userId);
	}
	
	@Test(expectedExceptions = Exception.class)
	public void testShareList3() {
		String userId = null;
		service.findByUser(userId);
	}

	@Test
	public void testShareAdd1() {
		String userId = "VanNTT";
		String videoId = "IryGw25Kgi0";
		String receiver = "haipvps14680@fpt.edu.vn";
		Share share = service.create(userId, videoId, receiver);
		assertNotNull(share);
	}
	
	@Test(expectedExceptions = Exception.class)
	public void testShareAdd2() {
		String userId = null;
		String videoId = null;
		String receiver = null;
		service.create(userId, videoId, receiver);
	}
	
	@Test(expectedExceptions = Exception.class)
	public void testShareAdd3() {
		String userId = "";
		String videoId = "";
		String receiver = "";
		service.create(userId, videoId, receiver);
	}

}
