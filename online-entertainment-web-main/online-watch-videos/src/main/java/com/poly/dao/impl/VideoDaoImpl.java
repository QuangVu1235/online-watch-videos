package com.poly.dao.impl;

import java.util.List;
import java.util.Map;

import com.poly.constant.NamedStored;
import com.poly.dao.AbstractDao;
import com.poly.dao.VideoDao;
import com.poly.entity.Video;

public class VideoDaoImpl extends AbstractDao<Video> implements VideoDao {

	@Override
	public Video findById(String id) {
		return super.findById(Video.class, id);
	}

	@Override
	public List<Video> findAll() {
		return super.findAll(Video.class, true);
	}

	@Override
	public List<Video> findAll(int pageNumber, int pageSize) {
		return super.findAll(Video.class, true, pageNumber, pageSize);
	}

	@Override
	public List<Video> topViewNotInHistory(Map<String, Object> params) {
		return super.callStored(NamedStored.TOP_VIEW_NOT_IN_HISTORY, params);
	}
	
	@Override
	public List<Video> topViewNotInVideoId(Map<String, Object> params) {
		return super.callStored(NamedStored.TOP_VIEW_NOT_IN_VIDEOID, params);
	}

	@Override
	public List<Video> findAllSorted(String nameAttrSorted, String sortedType) {
		return super.findAllSorted(Video.class, true, nameAttrSorted, sortedType);
	}
	
	@Override
	public List<Video> findAllSorted(String nameAttrSorted, String sortedType, int pageNumber, int pageSize) {
		return super.findAllSorted(Video.class, true, nameAttrSorted, sortedType, pageNumber, pageSize);
	}

	@Override
	public List<Video> topRandomNotInHistory(Map<String, Object> params) {
		return super.callStored(NamedStored.TOP_RANDOM_NOT_IN_HISTORY, params);
	}

	@Override
	public List<Video> topRandomNotInVideoId(Map<String, Object> params) {
		return super.callStored(NamedStored.TOP_RANDOM_NOT_IN_VIDEOID, params);
	}

	@Override
	public List<Video> findByTitle(String keyword, int pageNumber, int pageSize) {
		String jpql = "SELECT v FROM Video v WHERE v.title LIKE ?0";
		return super.findManyWithLike(Video.class, pageNumber, pageSize, jpql, keyword);
	}
	
}
