package com.poly.service.impl;

import java.sql.Timestamp;
import java.util.List;

import com.poly.dao.HistoryDao;
import com.poly.dao.impl.HistoryDaoImpl;
import com.poly.entity.History;
import com.poly.entity.User;
import com.poly.entity.Video;
import com.poly.service.HistoryService;
import com.poly.service.VideoService;

public class HistoryServiceImpl implements HistoryService {
	private HistoryDao dao;
	private VideoService videoService;
	
	public HistoryServiceImpl() {
		dao = new HistoryDaoImpl();
		videoService = 	new VideoServiceImpl();			
	}
	
	@Override
	public List<History> findByUser(String userId) {
		if (userId.isEmpty()) {
			throw new RuntimeException("UserId is empty!");
		}
		return dao.findByUser(userId);
	}

	@Override
	public List<History> historiesNotInVideoId(String userId, String videoPlayingId) {
		return dao.findByUserIdNotInVideoId(userId, videoPlayingId);
	}

	@Override
	public List<History> findByUserAndIsLiked(String userId) {
		if (userId.isEmpty()) {
			throw new RuntimeException("UserId is empty!");
		}
		return dao.findByUserIdAndIsLiked(userId);
	}

	@Override
	public History findByUserIdAndVideoId(String userId, String videoId) {
		return dao.findByUserIdAndVideoId(userId, videoId);
	}

	@Override
	public History create(User user, Video video) {
		History existHistory = findByUserIdAndVideoId(user.getId(), video.getId());
		if (existHistory == null) {
			existHistory = new History();
			existHistory.setUser(user);
			existHistory.setVideo(video);
			existHistory.setViewedDate(new Timestamp(System.currentTimeMillis()));
			existHistory.setIsLiked(Boolean.FALSE);
			dao.create(existHistory);
		} else {
			existHistory.setViewedDate(new Timestamp(System.currentTimeMillis()));
			dao.update(existHistory);
		}
		return existHistory;
	}

	@Override
	public History delete(String userId, String videoId) {
		if (userId.isEmpty()) {
			throw new RuntimeException("UserId is empty!");
		}
		if (videoId.isEmpty()) {
			throw new RuntimeException("VideoId is empty!");
		}
		
		 History history = findByUserIdAndVideoId(userId, videoId);
		 
		 return dao.delete(history);
	}

	@Override
	public boolean updateLikeOrUnlike(User user, String videoId) {
		Video video = videoService.findById(videoId);
		History existHistory = findByUserIdAndVideoId(user.getId(), video.getId());
		if (existHistory.getIsLiked() == Boolean.FALSE) {
			existHistory.setIsLiked(Boolean.TRUE);
			existHistory.setLikeDate(new Timestamp(System.currentTimeMillis()));
		} else {
			existHistory.setIsLiked(Boolean.FALSE);
			existHistory.setLikeDate(null);
		}
		return dao.update(existHistory) != null ? true : false;
	}

}
