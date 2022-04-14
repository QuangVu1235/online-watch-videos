package com.poly.service;

import java.util.List;

import com.poly.entity.Share;

public interface ShareService {
	List<Share> findByUser(String userId);
	Share findByUserAndVideo(String userId, String videoId);
	Share create(String userId, String videoId, String receiver);
}
