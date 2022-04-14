package com.poly.service.impl;

import java.sql.Timestamp;
import java.util.List;

import com.poly.dao.ShareDao;
import com.poly.dao.UserDao;
import com.poly.dao.VideoDao;
import com.poly.dao.impl.ShareDaoImpl;
import com.poly.dao.impl.UserDaoImpl;
import com.poly.dao.impl.VideoDaoImpl;
import com.poly.entity.Share;
import com.poly.entity.User;
import com.poly.entity.Video;
import com.poly.service.ShareService;

public class ShareServiceImpl implements ShareService {
	private ShareDao shareDao;
	private VideoDao videoDao;
	private UserDao userDao;
	
	public ShareServiceImpl(){
		shareDao = new ShareDaoImpl();
		videoDao = new VideoDaoImpl();
		userDao = new UserDaoImpl();
	}

	@Override
	public List<Share> findByUser(String userId) {
		if (userId.isEmpty()) {
			throw new RuntimeException("UserId is empty!");
		}
		return shareDao.findByUser(userId);
	}

	@Override
	public Share findByUserAndVideo(String userId, String videoId) {
		return shareDao.findByUserAndVideo(userId, videoId);
	}

	@Override
	public Share create(String userId, String videoId, String receiver) {
		if (userId.isEmpty()) {
			throw new RuntimeException("UserId is empty!");
		}
		if (videoId.isEmpty()) {
			throw new RuntimeException("VideoId is empty!");
		}
		if (receiver.isEmpty()) {
			throw new RuntimeException("Receiver is empty!");
		}
		
		Video video = videoDao.findById(videoId);
		User user = userDao.findById(userId);
		Share entity = new Share();
		entity.setVideo(video);
		entity.setUser(user);
		entity.setEmail(receiver);
		entity.setShareDate(new Timestamp(System.currentTimeMillis()));
		return shareDao.create(entity);
	}
	
	/*
	 * @Override public Share create(String userId, String videoId, String receiver)
	 * { Video video = videoDao.findById(videoId); User user =
	 * userDao.findById(userId); Share existShare = findByUserAndVideo(userId,
	 * videoId); if(existShare == null) { existShare = new Share();
	 * existShare.setVideo(video); existShare.setUser(user);
	 * existShare.setEmail(receiver); existShare.setShareDate(new
	 * Timestamp(System.currentTimeMillis())); return shareDao.create(existShare);
	 * }else { return existShare; } }
	 */
}
