package com.poly.service.impl;

import java.util.List;

import com.poly.dao.UserDao;
import com.poly.dao.impl.UserDaoImpl;
import com.poly.entity.User;
import com.poly.service.UserService;
import com.poly.util.PasswordUtil;

public class UserServiceImpl implements UserService {

	private UserDao dao;

	public UserServiceImpl() {
		dao = new UserDaoImpl();
	}

	@Override
	public User findById(String id) {
		return dao.findById(id);
	}

	@Override
	public User findByEmail(String email) {
		return dao.findByEmail(email);
	}

	@Override
	public User login(String id, String password) {
		if (findById(id) == null) {
			throw new RuntimeException("Tên tài khoản không đúng!");
		}

		if (dao.findByIdAndPassword(id, password) == null) {
			throw new RuntimeException("Mật khẩu không đúng!");
		}

		return dao.findByIdAndPassword(id, password);
	}

	@Override
	public User resetPassword(String email) {
		User existUser = findByEmail(email);
		if (existUser != null) {
			String newPassword = PasswordUtil.generateRandomPassword(8);
			existUser.setPassword(newPassword);
			return dao.update(existUser);
		}
		return null;
	}

	@Override
	public List<User> findAll() {

		return dao.findAll();
	}

	@Override
	public List<User> findAll(int pageNumber, int pageSize) {
		return dao.findAll(pageNumber, pageSize);
	}

	@Override
	public User register(String id, String password, String fullname, String email) {
		if (id.isEmpty()) {
			throw new RuntimeException("Id is emplty!");
		}
		if (password.isEmpty()) {
			throw new RuntimeException("Password is emplty!");
		}
		if (fullname.isEmpty()) {
			throw new RuntimeException("Fullname is emplty!");
		}
		if (email.isEmpty()) {
			throw new RuntimeException("Email cannot be emplty!");
		}
		if (findById(id) != null) {
			throw new RuntimeException("Id already exists!");
		}
		if (findByEmail(id) != null) {
			throw new RuntimeException("Email already exists!");
		}

		User user = new User();
		user.setId(id);
		user.setPassword(password);
		user.setFullname(fullname);
		user.setEmail(email);
		user.setAdmin(Boolean.FALSE);
		user.setActive(Boolean.TRUE);
		return dao.create(user);
	}

	@Override
	public User register(String id, String password, String fullname, String email, Boolean role) {
		if (id.isEmpty()) {
			throw new RuntimeException("Id is emplty!");
		} else if (password.isEmpty()) {
			throw new RuntimeException("Password is emplty!");
		} else if (fullname.isEmpty()) {
			throw new RuntimeException("Fullname is emplty!");
		} else if (email.isEmpty()) {
			throw new RuntimeException("Email cannot be emplty!");
		}
		
		User user = new User();
		user.setId(id);
		user.setPassword(password);
		user.setFullname(fullname);
		user.setEmail(email);
		user.setAdmin(role);
		user.setActive(Boolean.TRUE);
		return dao.create(user);
	}

	@Override
	public User update(User entity) {
		return dao.update(entity);
	}

	@Override
	public User delete(String id) {
		User user = dao.findById(id);
		user.setActive(Boolean.FALSE);
		return dao.update(user);
	}

}
