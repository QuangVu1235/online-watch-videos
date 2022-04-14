package com.poly.controller.admin;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.poly.constant.SessionAttr;
import com.poly.dto.UserLikedInfo;
import com.poly.dto.UserSharedInfo;
import com.poly.dto.VideoLikedInfo;
import com.poly.entity.User;
import com.poly.service.StatsService;
import com.poly.service.UserLikedService;
import com.poly.service.impl.ReportServiceImpl;
import com.poly.service.impl.UserLikedServiceImpl;

/**
 * Servlet implementation class HomeController
 */
@WebServlet(name = "HomeControllerOfAdmin", urlPatterns = { "/admin", "/admin/favorite", "/admin/share" })
public class HomeController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private StatsService statsService;
	private UserLikedService userLikedService;
       
    public HomeController() {
        super();
        statsService = new ReportServiceImpl();
        userLikedService = new UserLikedServiceImpl();
    }
    
    @Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession();
		User currentUser = (User) session.getAttribute(SessionAttr.CURRENT_USER);
		
		if (currentUser != null && currentUser.isAdmin() == Boolean.TRUE) {
			String path = req.getServletPath();
			switch (path) {
			case "/admin":
				doGetAdmin(session, req, resp);
				break;
			case "/admin/favorite":
				doGetFavorite(req, resp);
				break;
			case "/admin/share":
				doGetShare(req, resp);
				break;
			default:
				break;
			}
		} else {
			resp.sendRedirect("index");
		}
    	
	}

	private void doGetAdmin(HttpSession session, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		List<VideoLikedInfo> videos = statsService.findVideoLikedInfo();
		req.setAttribute("videos", videos);
		req.getRequestDispatcher("/views/admin/home.jsp").forward(req, resp);
	}

	private void doGetFavorite(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("application/json");
		PrintWriter out = resp.getWriter();
		String videoId = req.getParameter("id");
		List<UserLikedInfo> users = userLikedService.findUsersLikedByVideoId(videoId);
		if (users.isEmpty()) {
			resp.setStatus(400);
		} else {
			ObjectMapper mapper = new ObjectMapper();
			String dataResponse = mapper.writeValueAsString(users);
			resp.setStatus(200);
			out.print(dataResponse);
			out.flush();
		}	
	}
	
	private void doGetShare(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("application/json");
		PrintWriter out = resp.getWriter();
		String videoId = req.getParameter("id");
		List<UserSharedInfo> users = statsService.findUserSharedInfo(videoId);
		if (users.isEmpty()) {
			resp.setStatus(400);
		} else {
			ObjectMapper mapper = new ObjectMapper();
			String dataResponse = mapper.writeValueAsString(users);
			resp.setStatus(200);
			out.print(dataResponse);
			out.flush();
		}
		
	}

}
