package com.poly.controller.admin;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.poly.constant.SessionAttr;
import com.poly.entity.User;
import com.poly.service.UserService;
import com.poly.service.impl.UserServiceImpl;
import com.poly.util.ScopeUtil;

/**
 * Servlet implementation class UserController
 */
@WebServlet(name = "UserControllerForAdmin", urlPatterns = { "/admin/user" })
public class UserController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UserService userService;

	public UserController() {
		super();
		userService = new UserServiceImpl();
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		User currentUser = (User) ScopeUtil.getSession(SessionAttr.CURRENT_USER);
		if (currentUser != null && currentUser.isAdmin() == Boolean.TRUE) {
			String action = req.getParameter("action");
			switch (action) {
			case "view":
				doGetView(req, resp);
				break;
			case "delete":
				doGetDelete(req, resp);
				break;
			case "add":
				doGetAdd(req, resp);
				break;
			case "edit":
				doGetEdit(req, resp);
				break;
			default:
				break;
			}
		} else {
			resp.sendRedirect("index");
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		User currentUser = (User) ScopeUtil.getSession(SessionAttr.CURRENT_USER);
		if (currentUser != null && currentUser.isAdmin() == Boolean.TRUE) {
			String action = req.getParameter("action");
			switch (action) {
			case "add":
				doPostAdd(req, resp);
				break;
			default:
				break;
			}
		} else {
			resp.sendRedirect("index");
		}
	}
	
	private void doGetView(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		List<User> users = userService.findAll();
		req.setAttribute("users", users);
		req.getRequestDispatcher("/views/admin/user-manager.jsp").forward(req, resp);
	}
	
	private void doGetDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("application/json");
		String userId = req.getParameter("id");
		User userDeleted = userService.delete(userId);

		if (userDeleted != null) {
			resp.setStatus(204);
		} else {
			resp.setStatus(400);
		}
	}
	
	private void doGetAdd(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getRequestDispatcher("/views/admin/user-edit.jsp").forward(req, resp);
	}
	
	private void doGetEdit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String userId = req.getParameter("id");
		User user = userService.findById(userId);

		req.setAttribute("edit", user);
		
		req.getRequestDispatcher("/views/admin/user-edit.jsp").forward(req, resp);
	}
	
	private void doPostAdd(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("application/json");
		
		String id = req.getParameter("id");
		String fullname = req.getParameter("fullname");
		String password = req.getParameter("password");
		String email = req.getParameter("email");
		Boolean role = req.getParameter("role").contains("admin");
		
		User user = userService.findById(id);
		if (user == null) {
			User result = userService.register(id, password, fullname, email, role);
			if (result != null) {
				resp.setStatus(204);
			} else {
				resp.setStatus(400);
			}
		} else {
			user.setPassword(password);
			user.setFullname(fullname);
			user.setEmail(email);
			user.setAdmin(role);
			
			User result = userService.update(user);
			if (result != null) {
				resp.setStatus(204);
			} else {
				resp.setStatus(400);
			}
		}
		
	}

}
