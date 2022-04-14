package com.poly.service.impl;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.poly.constant.NamedStoredParam;
import com.poly.constant.OrderBy;
import com.poly.dao.VideoDao;
import com.poly.dao.impl.VideoDaoImpl;
import com.poly.entity.Video;
import com.poly.service.VideoService;

public class VideoServiceImpl implements VideoService {

	private VideoDao dao;
	
	public VideoServiceImpl(){
		dao = new VideoDaoImpl();
	}
	
	@Override
	public Video findById(String id) {
		return dao.findById(id);
	}

	@Override
	public List<Video> findAll() {
		return dao.findAll();
	}

	public List<Video> findAll(int pageNumber, int pageSize) {
		return dao.findAll(pageNumber, pageSize);
	}

	@Override
	public Video create(Video entity) {
		if (findById(entity.getId()) != null) {
			throw new RuntimeException("Id already exists!");
		}
		entity.setActive(Boolean.TRUE);
		entity.setViews(0);
		entity.setUploadDate(new Timestamp(System.currentTimeMillis()));
		return dao.create(entity);
	}

	@Override
	public Video update(Video entity) {
		return dao.update(entity);
	}

	@Override
	public Video delete(String id) {
		Video entity = dao.findById(id);
		entity.setActive(Boolean.FALSE);
		return dao.update(entity);
	}

	@Override
	public List<Video> findAllOrderByViewDesc() {
		return dao.findAllSorted("views", OrderBy.DESC);
	}
	
	@Override
	public List<Video> findAllOrderByViewDesc(int pageNumber, int pageSize) {
		return dao.findAllSorted("views", OrderBy.DESC, pageNumber, pageSize);
	}


	@Override
	public List<Video> topViewNotInHistory(int size, String userId) {
		Map<String, Object> params = new HashMap<>();
		params.put(NamedStoredParam.SIZE, size);
		params.put(NamedStoredParam.USER_ID, userId);
		return dao.topViewNotInHistory(params);
	}

	@Override
	public List<Video> topViewNotInVideoId(int size, String videoId) {
		Map<String, Object> params = new HashMap<>();
		params.put(NamedStoredParam.SIZE, size);
		params.put(NamedStoredParam.VIDEO_ID, videoId);
		return dao.topViewNotInVideoId(params);
	}

	@Override
	public List<Video> topRandomNotInHistory(int size, String userId) {
		Map<String, Object> params = new HashMap<>();
		params.put(NamedStoredParam.SIZE, size);
		params.put(NamedStoredParam.USER_ID, userId);
		return dao.topRandomNotInHistory(params);
	}

	@Override
	public List<Video> topRandomNotInVideoId(int size, String videoId) {
		Map<String, Object> params = new HashMap<>();
		params.put(NamedStoredParam.SIZE, size);
		params.put(NamedStoredParam.VIDEO_ID, videoId);
		return dao.topRandomNotInVideoId(params);
	}

	@Override
	public List<Video> findByTitle(String keyword, int pageNumber, int pageSize) {
		return dao.findByTitle(keyword, pageNumber, pageSize);
	}
	
}
