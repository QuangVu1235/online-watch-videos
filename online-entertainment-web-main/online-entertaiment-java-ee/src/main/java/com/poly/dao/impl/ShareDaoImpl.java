package com.poly.dao.impl;

import java.util.List;

import com.poly.dao.AbstractDao;
import com.poly.dao.ShareDao;
import com.poly.entity.Share;

public class ShareDaoImpl extends AbstractDao<Share> implements ShareDao {

	@Override
	public List<Share> findByUser(String userId) {
		String jpql = "SELECT o FROM Share o WHERE o.user.id = ?0 AND o.video.active = 1";
		return super.findMany(Share.class, jpql, userId);
	}

	@Override
	public Share findByUserAndVideo(String userId, String videoId) {
		String jqpl = "SELECT o FROM Share o WHERE o.user.id = ?0 AND o.video.id = ?1 AND o.video.active = 1";
		return super.findOne(Share.class, jqpl, userId, videoId);			
	}

}
