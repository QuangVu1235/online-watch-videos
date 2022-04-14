package com.poly.service.testng;

import static org.testng.Assert.assertNotNull;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.poly.entity.User;
import com.poly.service.UserService;
import com.poly.service.impl.UserServiceImpl;

public class LoginServiceTest {
	private UserService userService;

	@BeforeMethod
	public void setUp() throws Exception {
		userService = new UserServiceImpl();
	}

	@AfterMethod
	public void tearDown() throws Exception {
		userService = null;
	}

	@Test
	public final void testLogin1() {
		String id = "MinhNH";
		String password = "123456";
		
		User result = userService.login(id, password);
		assertNotNull(result);
	}
	
	@Test(expectedExceptions = Exception.class)
	public final void testLogin2() {
		String id = "MinhNH";
		String password = "123123";
		
		userService.login(id, password);
	}
	
	@Test(expectedExceptions = Exception.class)
	public final void testLogin3() {
		String id = "NoPT";
		String password = "123456";
		
		userService.login(id, password);
	}

}
