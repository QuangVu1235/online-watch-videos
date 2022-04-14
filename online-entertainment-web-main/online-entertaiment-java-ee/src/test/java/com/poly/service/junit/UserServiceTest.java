package com.poly.service.junit;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.poly.entity.User;
import com.poly.service.UserService;
import com.poly.service.impl.UserServiceImpl;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserServiceTest {
	private UserService userService;
	
	@Before
	public void setUp() throws Exception {
		userService = new UserServiceImpl();
	}

	@After
	public void tearDown() throws Exception {
		userService = null;
	}
	
	/* Kiểm tra xuất danh sách user */
	@Test
	public final void testUserList1() {
		List<User> list = userService.findAll();
		int result = 6;
		int actual = list.size();
		
		assertEquals(result, actual);
	}
	
	@Test
	public final void testUserList2() {
		int pageNumber = 1; 
		int pageSize = 3;
		
		List<User> list = userService.findAll(pageNumber, pageSize);
		int result = 3;
		int actual = list.size();
		
		assertEquals(result, actual);
	}
	
	/* Kiểm tra tìm user theo id */
	@Test
	public final void testUserSearch1() {
		String userId = "MinhNH";
		User user = userService.findById(userId);
		
		assertEquals(userId, user.getId());
	}
	
	@Test
	public final void testUserSearch2() {
		String userId = "123456";
		User user = userService.findById(userId);
		
		assertNull(user);
	}
	
	/* Kiểm tra thêm user */
	@Test
	public final void testUserAdd1() {
		String id = "ChungKP";
		String password = "123456"; 
		String fullname = "Kim Phan Chung"; 
		String email = "chungkp@gmail.com";
		
		User result = userService.register(id, password, fullname, email);
		
		assertNotNull(result);
	}
	
	
	@Test(expected = Exception.class)
	public final void testUserAdd2() {
		String id = null;
		String password = null; 
		String fullname = null; 
		String email = null;
		
		userService.register(id, password, fullname, email);
	}
	
	
	@Test(expected = Exception.class)
	public final void testUserAdd3() {
		String id = "";
		String password = ""; 
		String fullname = ""; 
		String email = "";
		
		userService.register(id, password, fullname, email);
	}

	
	@Test(expected = Exception.class)
	public final void testUserAdd4() {
		String id = "KhoaDHD";
		String password = "123456"; 
		String fullname = "Phạm Văn Hải"; 
		String email = "haipv@gmail.com";
		
		userService.register(id, password, fullname, email);
	}
	
	
	@Test(expected = Exception.class)
	public final void testUserAdd5() {
		String id = "HaiPV";
		String password = "123456"; 
		String fullname = "Phạm Văn Hải"; 
		String email = "khoadhd@gmail.com";
		
		userService.register(id, password, fullname, email);
	}
	
	/* Test xoá user */
	@Test
	public final void testUserDelete1() {
		String id = "MaiNT";
		User result = userService.delete(id);
		
		assertNotNull(result);
	}
	
	
	@Test(expected = Exception.class)
	public final void testUserDelete2() {
		String id = "NoPT";
		userService.delete(id);
	}
	
	/* Test cập nhật user */
	@Test
	public final void testUserEdit1() {
		User user = new User();
		user.setId("HaiPV");
		user.setPassword("123123");
		user.setFullname("Phạm Văn Hải");
		user.setEmail("haipv123@gmail.com");
		user.setActive(true);
		
		User result = userService.update(user);
		assertNotNull(result);
	}
	
	
	@Test(expected = Exception.class)
	public final void testUserEdit2() {
		User user = null;
		userService.update(user);
	}
	
	@Test(expected = Exception.class)
	public final void testUserEdit3() {
		User user = new User();
		userService.update(user);
	}
}
