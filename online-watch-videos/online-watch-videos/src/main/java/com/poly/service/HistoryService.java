package com.poly.service;

import java.util.List;

import com.poly.entity.History;
import com.poly.entity.User;
import com.poly.entity.Video;

public interface HistoryService {
	List<History> findByUser(String userId);
	List<History> findByUserAndIsLiked(String userId);
	List<History> historiesNotInVideoId(String userId, String videoId);
	History findByUserIdAndVideoId(String userId, String videoId);
	History create(User user, Video video);
	History delete(String userId, String videoId);
	boolean updateLikeOrUnlike(User user, String videoId);
}
