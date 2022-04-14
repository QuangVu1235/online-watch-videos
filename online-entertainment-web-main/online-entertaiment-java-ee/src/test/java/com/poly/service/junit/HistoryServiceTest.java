package com.poly.service.junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.poly.entity.History;
import com.poly.entity.User;
import com.poly.entity.Video;
import com.poly.service.HistoryService;
import com.poly.service.impl.HistoryServiceImpl;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class HistoryServiceTest {
	private HistoryService service;

	@Before
	public void setUp() throws Exception {
		service = new HistoryServiceImpl();
	}

	@After
	public void tearDown() throws Exception {
		service = null;
	}

	@Test
	public void testHistoryList1() {
		String userId = "TeoNV";
		List<History> list = service.findByUser(userId);
		int actual = list.size();
		int expected = 2;
		assertEquals(expected, actual);
	}
	
	@Test(expected = Exception.class)
	public void testHistoryList2() {
		String userId = null;
		service.findByUser(userId);
	}

	@Test(expected = Exception.class)
	public void testHistoryList3() {
		String userId = "";
		service.findByUser(userId);
	}

	@Test
	public void testFavoriteList1() {
		String userId = "TeoNV";
		List<History> list = service.findByUserAndIsLiked(userId);
		int expected = 2;
		int actual = list.size();
		assertEquals(expected, actual);
	}

	@Test(expected = Exception.class)
	public void testFavoriteList2() {
		String userId = null;
		service.findByUserAndIsLiked(userId);
	}

	@Test(expected = Exception.class)
	public void testFavoriteList3() {
		String userId = "";
		service.findByUserAndIsLiked(userId);
	}

	@Test
	public void testHistoryAdd1() {
		User user = new User();
		user.setId("TeoNV");
		Video video = new Video();
		video.setId("IryGw25Kgi0");
		History result = service.create(user, video);
		assertNotNull(result);
	}

	@Test(expected = Exception.class)
	public void testHistoryAdd2() {
		User user = null;
		Video video = null;
		service.create(user, video);
	}

	@Test(expected = Exception.class)
	public void testHistoryAdd3() {
		User user = new User();
		Video video = new Video();
		service.create(user, video);
	}

	@Test
	public void testHistoryDelete1() {
		String userId = "TeoNV";
		String videoId = "IryGw25Kgi0";
		History result = service.delete(userId, videoId);
		assertNotNull(result);
	}

	@Test(expected = Exception.class)
	public void testHistoryDelete2() {
		String userId = "";
		String videoId = "";
		service.delete(userId, videoId);	
	}

	@Test(expected = Exception.class)
	public void testHistoryDelete3() {
		String userId = null;
		String videoId = null;
		service.delete(userId, videoId);
	}
	
	@Test(expected = Exception.class)
	public void testHistoryDelete4() {
		String userId = "Hehehe";
		String videoId = "Huhuhuhu";
		
		service.delete(userId, videoId);
	}
	
	@Test
	public void testLikeVideo1() {
		User user = new User();
		user.setId("VanNTT");
		String videoId = "Hi9eQnS7snc";
		
		boolean result = service.updateLikeOrUnlike(user, videoId);
		assertTrue(result);
	}
	
	@Test(expected = Exception.class)
	public void testLikeVideo2() {
		User user = new User();
		String videoId = "";
		service.updateLikeOrUnlike(user, videoId);
	}

}
