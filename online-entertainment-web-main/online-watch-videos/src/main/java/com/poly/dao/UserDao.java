package com.poly.dao;

import java.util.List;

import com.poly.entity.User;

public interface UserDao {
	User findById(String id);
	User findByEmail(String email);
	User findByIdAndPassword(String id, String password);
	List<User> findAll();
	List<User> findAll(int pageNumber, int pageSize);
	User create(User entity);
	User update(User entity);
	User delete(User entity);
}
