package com.poly.dao.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.poly.dao.AbstractDao;
import com.poly.dao.StatsDao;
import com.poly.dto.UserSharedInfo;
import com.poly.dto.VideoLikedInfo;

public class StatsDaoImpl extends AbstractDao<Object[]> implements StatsDao {
	@Override
	public List<VideoLikedInfo> findVideoLikedInfo() {
		String sql = "SELECT v.Id, v.Title, SUM(CAST(h.IsLiked AS INT)) AS TotalLike, MAX(h.LikeDate) AS Newest, MIN(h.LikeDate) AS Oldest"
				+ " FROM Video v LEFT JOIN History h ON v.Id = h.VideoId"
				+ " WHERE v.Active = 1"
				+ " GROUP BY v.Id, v.Title"
				+ " ORDER BY SUM(CAST(h.isLiked AS INT)) DESC";
		List<Object[]> objects = super.findManyByNativeQuery(sql);
		List<VideoLikedInfo> result = new ArrayList<>();
		objects.forEach(obj -> {
			VideoLikedInfo entity = new VideoLikedInfo();
			entity.setId((String) obj[0]);
			entity.setTitle((String) obj[1]);
			entity.setTotalLike(obj[2] == null ? 0 : (Integer) obj[2]);
			entity.setNewest((Timestamp) obj[3]);
			entity.setOldest((Timestamp) obj[4]);
			result.add(entity);
		});
		return result;
	}
	
	@Override
	public List<UserSharedInfo> findVideoSharedInfo(String videoId) {
		String sql = "SELECT u.Fullname AS senderName, u.Email AS senderEmail, s.Email AS receiverEmail, s.ShareDate AS sendDate"
				+ " FROM Share s INNER JOIN [User] u ON s.UserID = u.Id"
				+ " INNER JOIN Video v ON s.VideoId = v.Id"
				+ " WHERE s.VideoId = ?"
				+ " ORDER BY u.Email";
		List<Object[]> objects = super.findManyByNativeQuery(sql, videoId);
		List<UserSharedInfo> result = new ArrayList<>();
		objects.forEach(obj -> {
			UserSharedInfo entity = new UserSharedInfo();
			entity.setSenderName((String) obj[0]);
			entity.setSenderEmail((String) obj[1]);
			entity.setReceiverEmail((String) obj[2]);
			entity.setSendDate((Timestamp) obj[3]);
			result.add(entity);
		});
		return result;
	}
	
}
