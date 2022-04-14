package com.poly.dao;

import java.util.List;
import java.util.Map;

import com.poly.dto.UserLikedInfo;

public interface UserLikedDao {
	List<UserLikedInfo> findUsersLikedByVideoId(Map<String, Object> params);
}
