package com.poly.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.poly.constant.NamedStoredParam;
import com.poly.dao.UserLikedDao;
import com.poly.dao.impl.UserLikedDaoImpl;
import com.poly.dto.UserLikedInfo;
import com.poly.service.UserLikedService;

public class UserLikedServiceImpl implements UserLikedService {
	private UserLikedDao dao = new UserLikedDaoImpl();
	
	@Override
	public List<UserLikedInfo> findUsersLikedByVideoId(String videoId) {
		Map<String, Object> params = new HashMap<>();
		params.put(NamedStoredParam.VIDEO_ID, videoId);
		return dao.findUsersLikedByVideoId(params);
	}
	
}
