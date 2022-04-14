package com.poly.dao.impl;

import java.util.List;

import com.poly.dao.AbstractDao;
import com.poly.dao.UserDao;
import com.poly.entity.User;

public class UserDaoImpl extends AbstractDao<User> implements UserDao {

	@Override
	public User findById(String id) {
		return super.findById(User.class, id);
	}

	@Override
	public User findByEmail(String email) {
		String jqpl = "SELECT o FROM User o WHERE o.email = ?0";
		return super.findOne(User.class, jqpl, email);
	}

	@Override
	public User findByIdAndPassword(String id, String password) {
		String jqpl = "SELECT o FROM User o WHERE o.id = ?0 AND o.password = ?1";
		return super.findOne(User.class, jqpl, id, password);
	}

	@Override
	public List<User> findAll() {
		return super.findAll(User.class, true);
	}

	@Override
	public List<User> findAll(int pageNumber, int pageSize) {
		return super.findAll(User.class, true, pageNumber, pageSize);
	}
	
	@Override
	public User create(User entity) {
		return super.create(entity);
	}
	
}
