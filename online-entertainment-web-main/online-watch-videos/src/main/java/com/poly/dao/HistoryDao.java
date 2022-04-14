package com.poly.dao;

import java.util.List;

import com.poly.entity.History;

public interface HistoryDao {
	List<History> findByUser(String userId);
	List<History> findByUserIdNotInVideoId(String userId, String videoId);
	List<History> findByUserIdAndIsLiked(String userId);
	History findByUserIdAndVideoId(String userId, String videoId);
	History create(History entity);
	History update(History entity);
	History delete(History entity);
}
