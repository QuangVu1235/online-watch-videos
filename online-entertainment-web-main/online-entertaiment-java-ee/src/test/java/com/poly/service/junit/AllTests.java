package com.poly.service.junit;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ 
	HistoryServiceTest.class, 
	LoginServiceTest.class, 
	ShareServiceTest.class, 
	UserServiceTest.class,
	VideoServiceTest.class 
})
public class AllTests {}
