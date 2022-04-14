package com.poly.dao;

import java.util.List;

import com.poly.entity.Share;

public interface ShareDao {
	List<Share> findByUser(String userId);
	Share findByUserAndVideo(String userId, String videoId);
	Share create(Share entity);
	Share update(Share entity);
	Share delete(Share entity);
}
