package com.poly.service;

import java.util.List;

import com.poly.dto.UserLikedInfo;

public interface UserLikedService {
	List<UserLikedInfo> findUsersLikedByVideoId(String videoId);
}
