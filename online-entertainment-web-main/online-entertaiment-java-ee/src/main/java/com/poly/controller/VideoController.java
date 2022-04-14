package com.poly.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.poly.constant.SessionAttr;
import com.poly.entity.History;
import com.poly.entity.User;
import com.poly.entity.Video;
import com.poly.service.EmailService;
import com.poly.service.HistoryService;
import com.poly.service.ShareService;
import com.poly.service.VideoService;
import com.poly.service.impl.EmailServiceImpl;
import com.poly.service.impl.HistoryServiceImpl;
import com.poly.service.impl.ShareServiceImpl;
import com.poly.service.impl.VideoServiceImpl;

/**
 * Servlet implementation class VideoController
 */
@WebServlet("/video")
public class VideoController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private VideoService videoService;
	private HistoryService historyService;
	private EmailService emailService;
	private ShareService shareService;

	public VideoController() {
		super();
		videoService = new VideoServiceImpl();
		historyService = new HistoryServiceImpl();
		emailService = new EmailServiceImpl();
		shareService = new ShareServiceImpl();
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String actionParam = req.getParameter("action");
		String videoId = req.getParameter("id");
		
		HttpSession session = req.getSession();

		switch (actionParam) {
		case "watch":
			doGetWatch(session, videoId, req, resp);
			break;
		case "like":
			doGetLike(session, videoId, req, resp);
			break;
		case "history":
			doGetHistory(session, videoId, req, resp);
			break;
		case "share":
			doGetShare(session, videoId, req, resp);
			break;
		default:
			break;
		}
	}

	private void doGetWatch(HttpSession session, String videoId, HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// Hiển thị video chi tiết
		Video video = videoService.findById(videoId);
		req.setAttribute("video", video);
		
		video.setViews(video.getViews() + 1);
		videoService.update(video);
		
		List<Video> videos;

		User currentUser = (User) session.getAttribute(SessionAttr.CURRENT_USER);
		if (currentUser != null) {
			// Thêm lịch sử xem
			History history = historyService.create(currentUser, video);
			
			req.setAttribute("flagLiked", history.getIsLiked());
			
			// Hiển thị top ngẫu nhiên
			videos = videoService.topRandomNotInHistory(15, currentUser.getId());
			
			if (videos.size() == 0) {
				List<History> histories = historyService.historiesNotInVideoId(currentUser.getId(), videoId);
				histories.forEach(item -> videos.add(item.getVideo()));
			}
			
		} else {
			videos = videoService.topRandomNotInVideoId(15, videoId);
		}
			
		req.setAttribute("videos", videos);
		req.getRequestDispatcher("/views/user/video-detail.jsp").forward(req, resp);
	}

	private void doGetLike(HttpSession session, String videoId, HttpServletRequest req, HttpServletResponse resp) {
		resp.setContentType("application/json");

		User currentUser = (User) session.getAttribute(SessionAttr.CURRENT_USER);
		boolean result = historyService.updateLikeOrUnlike(currentUser, videoId);

		if (result == true) {
			resp.setStatus(204); // Success but no data
		} else {
			resp.setStatus(400);
		}
	}
	
	private void doGetHistory(HttpSession session, String videoId, HttpServletRequest req, HttpServletResponse resp) {
		resp.setContentType("application/json");

		User currentUser = (User) session.getAttribute(SessionAttr.CURRENT_USER);
		
		History history = historyService.delete(currentUser.getId(), videoId);
		if (history != null) {
			resp.setStatus(204); // Success but no data
		} else {
			resp.setStatus(400);
		}
	}
	
	private void doGetShare(HttpSession session, String videoId, HttpServletRequest req, HttpServletResponse resp) {
		resp.setContentType("application/json");

		User currentUser = (User) session.getAttribute(SessionAttr.CURRENT_USER);
		String email = req.getParameter("emailShare");
		
		try {
			emailService.sendEmailShareVideo(getServletContext(), currentUser, videoId, email);
			shareService.create(currentUser.getId(), videoId, email);
			resp.setStatus(204);
		} catch (Exception e) {
			e.printStackTrace();
			resp.setStatus(400);
		}
	}
}
