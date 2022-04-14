package com.poly.dao.impl;

import java.util.List;
import java.util.Map;

import com.poly.constant.NamedStored;
import com.poly.dao.AbstractDao;
import com.poly.dao.UserLikedDao;
import com.poly.dto.UserLikedInfo;

public class UserLikedDaoImpl extends AbstractDao<UserLikedInfo> implements UserLikedDao {

	@Override
	public List<UserLikedInfo> findUsersLikedByVideoId(Map<String, Object> params) {
		return callStored(NamedStored.FIND_USERS_LIKED_BY_VIDEOID, params);
	}

}
