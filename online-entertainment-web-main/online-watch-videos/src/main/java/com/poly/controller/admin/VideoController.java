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
import com.poly.entity.Video;
import com.poly.service.VideoService;
import com.poly.service.impl.VideoServiceImpl;
import com.poly.util.ScopeUtil;

/**
 * Servlet implementation class VideoController
 */
@WebServlet(name = "VideoControllerOfAdmin", urlPatterns = { "/admin/video"})
public class VideoController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private VideoService videoService;

	public VideoController() {
		super();
		videoService = new VideoServiceImpl();
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
		List<Video> videos = videoService.findAll();
		req.setAttribute("videos", videos);
		req.getRequestDispatcher("/views/admin/video-manager.jsp").forward(req, resp);
	}

	private void doGetDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("application/json");
		String videoId = req.getParameter("id");
		Video videoDeleted = videoService.delete(videoId);

		if (videoDeleted != null) {
			resp.setStatus(204);
		} else {
			resp.setStatus(400);
		}
	}

	private void doGetAdd(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getRequestDispatcher("/views/admin/video-edit.jsp").forward(req, resp);
	}
	
	private void doGetEdit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String videoId = req.getParameter("id");
		Video video = videoService.findById(videoId);

		req.setAttribute("edit", video);
		req.getRequestDispatcher("/views/admin/video-edit.jsp").forward(req, resp);
	}
	
	private void doPostAdd(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("application/json");
		
		String id = req.getParameter("id");
		String title = req.getParameter("title");
		String description = req.getParameter("description");
		String poster = req.getParameter("poster");
		
		if (videoService.findById(id) != null) {
			Video video = videoService.findById(id);
			video.setTitle(title);
			video.setDescription(description);
			video.setPoster(poster);
			
			Video result = videoService.update(video);
			
			if (result != null) {
				resp.setStatus(204);
			} else {
				resp.setStatus(400);
			}
		} else {
			Video video = new Video();
			video.setId(id);
			video.setTitle(title);
			video.setDescription(description);
			video.setPoster(poster);
			
			Video result = videoService.create(video);
			
			if (result != null) {
				resp.setStatus(204);
			} else {
				resp.setStatus(400);
			}
		}
	}
}
