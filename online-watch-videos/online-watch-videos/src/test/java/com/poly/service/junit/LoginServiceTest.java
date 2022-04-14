package com.poly.service.junit;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.poly.entity.User;
import com.poly.service.UserService;
import com.poly.service.impl.UserServiceImpl;

public class LoginServiceTest {
	private UserService userService;

	@Before
	public void setUp() throws Exception {
		userService = new UserServiceImpl();
	}

	@After
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
	
	@SuppressWarnings("unused")
	@Test(expected = Exception.class)
	public final void testLogin2() {
		String id = "MinhNH";
		String password = "123123";
		
		User result = userService.login(id, password);
	}
	
	@SuppressWarnings("unused")
	@Test(expected = Exception.class)
	public final void testLogin3() {
		String id = "NoPT";
		String password = "123456";
		
		User result = userService.login(id, password);
	}

}
