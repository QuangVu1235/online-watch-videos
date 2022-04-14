package com.poly.service;

import java.util.List;

import com.poly.entity.User;

public interface UserService {
	User findById(String id);
	User findByEmail(String email);
	User login(String id, String password);
	User resetPassword(String email);
	List<User> findAll();
	List<User> findAll(int pageNumber, int pageSize);
	User register(String id, String password, String fullname, String email);
	User register(String id, String password, String fullname, String email, Boolean role);
	User update(User entity);
	User delete(String id);
}
