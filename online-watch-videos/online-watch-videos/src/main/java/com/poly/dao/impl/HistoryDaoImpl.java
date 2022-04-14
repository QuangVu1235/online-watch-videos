package com.poly.dao.impl;

import java.util.List;

import com.poly.dao.AbstractDao;
import com.poly.dao.HistoryDao;
import com.poly.entity.History;

public class HistoryDaoImpl extends AbstractDao<History> implements HistoryDao {

	@Override
	public List<History> findByUser(String userId) {
		String jpql = "SELECT h FROM History h WHERE h.user.id = ?0"
				+ " AND h.video.active = 1"
				+ " ORDER BY h.viewedDate DESC";
		return super.findMany(History.class, jpql, userId);
	}
	
	@Override
	public List<History> findByUserIdNotInVideoId(String userId, String videoId) {
		String jpql = "SELECT h FROM History h WHERE h.user.id = ?0"
				+ " AND h.video.active = 1"
				+ " AND h.video.id NOT IN (?1)"
				+ " ORDER BY h.viewedDate DESC";
		return super.findMany(History.class, jpql, userId, videoId);
	}

	@Override
	public List<History> findByUserIdAndIsLiked(String userId) {
		String jpql = "SELECT h FROM History h WHERE h.user.id = ?0"
				+ " AND h.video.active = 1"
				+ " AND h.isLiked = 1"
				+ " ORDER BY h.viewedDate DESC";
		return super.findMany(History.class, jpql, userId);
	}

	@Override
	public History findByUserIdAndVideoId(String userId, String videoId) {
		String jpql = "SELECT h FROM History h WHERE h.user.id = ?0"
				+ " AND h.video.id = ?1";
		return super.findOne(History.class, jpql, userId, videoId);
	}

}
