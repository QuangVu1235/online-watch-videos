package com.poly.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.poly.constant.SessionAttr;
import com.poly.entity.User;
import com.poly.service.EmailService;
import com.poly.service.UserService;
import com.poly.service.impl.EmailServiceImpl;
import com.poly.service.impl.UserServiceImpl;
import com.poly.util.CookieUtil;

/**
 * Servlet implementation class UserController
 */
@WebServlet(urlPatterns = { "/login", "/logout", "/register", "/forgot-password", "/change-password", "/edit-profile"})
public class UserController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UserService userService;
	private EmailService emailService;

	public UserController() {
		super();
		userService = new UserServiceImpl();
		emailService = new EmailServiceImpl();
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession();
		String path = req.getServletPath();
		switch (path) {
		case "/login":
			doGetLogin(req, resp);
			break;
		case "/register":
			doGetRegister(req, resp);
			break;
		case "/logout":
			doGetLogout(session, req, resp);
			break;
		default:
			break;
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession();
		String path = req.getServletPath();
		switch (path) {
		case "/login":
			doPostLogin(session, req, resp);
			break;
		case "/register":
			doPostRegister(session, req, resp);
			break;
		case "/forgot-password":
			doPostForgotPassword(req, resp);
			break;
		case "/change-password":
			doPostChangePassword(session, req, resp);
			break;
		case "/edit-profile":
			doPostEditProfile(session, req, resp);
			break;
		default:
			break;
		}
	}

	private void doGetLogin(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String username = CookieUtil.get("username", "");
		String password = CookieUtil.get("password", "");
		String remember = CookieUtil.get("remember", "");

		req.setAttribute("username", username);
		req.setAttribute("password", password);
		req.setAttribute("rememberChecked", remember);

		req.getRequestDispatcher("/views/user/login.jsp").forward(req, resp);
	}

	private void doGetRegister(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getRequestDispatcher("/views/user/register.jsp").forward(req, resp);
	}

	private void doGetLogout(HttpSession session, HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		session.removeAttribute(SessionAttr.CURRENT_USER);
		resp.sendRedirect("index");
	}

	private void doPostLogin(HttpSession session, HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String username = req.getParameter("username");
		String password = req.getParameter("password");
		String remember = req.getParameter("remember");

		try {
			User user = userService.login(username, password);
			if (user != null) {
				boolean isRemember = (remember != null);
				int hours = !isRemember ? 0 : 30 * 24; // 0 = xoá cookie

				CookieUtil.add("username", username, hours);
				CookieUtil.add("password", password, hours);
				CookieUtil.add("remember", (isRemember ? "checked" : ""), hours);

				session.setAttribute(SessionAttr.CURRENT_USER, user);
				resp.sendRedirect("index");
			} else {
				req.setAttribute("errorMessage", "Đăng nhập không thành công!");
				req.getRequestDispatcher("/views/user/login.jsp").forward(req, resp);
			}
		} catch (Exception e) {
			req.setAttribute("errorMessage", e.getMessage());
			req.getRequestDispatcher("/views/user/login.jsp").forward(req, resp);
		}
	}

	private void doPostRegister(HttpSession session, HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String username = req.getParameter("username");
		String password = req.getParameter("password");
		String fullname = req.getParameter("fullname");
		String email = req.getParameter("email");

		User user = userService.register(username, password, fullname, email);
		if (user != null) {
			emailService.sendEmailWelcome(getServletContext(), user);
			session.setAttribute(SessionAttr.CURRENT_USER, user);
			resp.sendRedirect("index");
		} else {
			req.setAttribute("errorMessage", "Đăng ký không thành công!");
			req.getRequestDispatcher("/views/user/register.jsp").forward(req, resp);
		}
	}

	private void doPostForgotPassword(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setContentType("application/json");
		String email = req.getParameter("emailForgotPass");
		User userWithNewPass = userService.resetPassword(email);
		
		if (userWithNewPass != null) {
			emailService.sendEmailForgotPassword(getServletContext(), userWithNewPass);
			userService.update(userWithNewPass);
			resp.setStatus(204);
		} else {
			resp.setStatus(400);
		}
	}
	
	private void doPostChangePassword(HttpSession session, HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setContentType("application/json");
		String currentPass = req.getParameter("currentPass");
		String newPass = req.getParameter("newPass");
		User currentUser = (User) session.getAttribute(SessionAttr.CURRENT_USER);
		
		System.out.println(currentUser.getPassword());
		if (currentUser.getPassword().equals(currentPass)) {
			currentUser.setPassword(newPass);
			User updatedUser = userService.update(currentUser);
			if (updatedUser != null) {
				// Cập nhật user hiện tại có password mới vào session
				session.setAttribute(SessionAttr.CURRENT_USER, updatedUser);
				resp.setStatus(204);
			} else {
				resp.setStatus(400);
			}
		} else {
			resp.setStatus(400);
		}
	}
	
	private void doPostEditProfile(HttpSession session, HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException{
		resp.setContentType("application/json");
		User currentUser = (User) session.getAttribute(SessionAttr.CURRENT_USER);
		
		String fullname = req.getParameter("fullnameEdit");
		String email = req.getParameter("emailEdit");
		
		currentUser.setFullname(fullname);
		currentUser.setEmail(email);
		
		User updatedUser = userService.update(currentUser);
		
		if (updatedUser != null) {
			session.setAttribute(SessionAttr.CURRENT_USER, updatedUser);
			resp.setStatus(204);
		} else {
			resp.setStatus(204);
		}
	}
}
