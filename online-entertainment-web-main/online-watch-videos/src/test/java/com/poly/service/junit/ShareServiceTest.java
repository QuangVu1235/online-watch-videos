package com.poly.service.junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.poly.entity.Share;
import com.poly.service.ShareService;
import com.poly.service.impl.ShareServiceImpl;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ShareServiceTest {
	private ShareService service;
	@Before
	public void setUp() throws Exception {
		service = new  ShareServiceImpl();
	}

	@After
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
	
	@Test(expected = Exception.class)
	public void testShareList2() {
		String userId = "";
		service.findByUser(userId);
	}
	
	@Test(expected = Exception.class)
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
	
	@Test(expected = Exception.class)
	public void testShareAdd2() {
		String userId = null;
		String videoId = null;
		String receiver = null;
		service.create(userId, videoId, receiver);
	}
	
	@Test(expected = Exception.class)
	public void testShareAdd3() {
		String userId = "";
		String videoId = "";
		String receiver = "";
		service.create(userId, videoId, receiver);
	}

}
