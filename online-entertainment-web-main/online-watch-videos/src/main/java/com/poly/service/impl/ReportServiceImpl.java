package com.poly.service.impl;

import java.util.List;

import com.poly.dao.StatsDao;
import com.poly.dao.impl.StatsDaoImpl;
import com.poly.dto.UserSharedInfo;
import com.poly.dto.VideoLikedInfo;
import com.poly.service.StatsService;

public class ReportServiceImpl implements StatsService {
	private StatsDao dao;
	
	public ReportServiceImpl() {
		dao = new StatsDaoImpl();
	}
	
	@Override
	public List<VideoLikedInfo> findVideoLikedInfo() {
		return dao.findVideoLikedInfo();
	}

	@Override
	public List<UserSharedInfo> findUserSharedInfo(String videoId) {
		return dao.findVideoSharedInfo(videoId);
	}

}
