package com.poly.dao;

import java.util.List;
import java.util.Map;

import com.poly.entity.Video;

public interface VideoDao {
	Video findById(String id);
	List<Video> findAll();
	List<Video> findAll(int pageNumber, int pageSize);
	List<Video> findAllSorted(String nameAttrSorted, String sortedType);
	List<Video> findAllSorted(String nameAttrSorted, String sortedType, int pageNumber, int pageSize);
	List<Video> findByTitle(String keyword, int pageNumber, int pageSize);
	List<Video> topViewNotInHistory(Map<String, Object> params);
	List<Video> topViewNotInVideoId(Map<String, Object> params);
	List<Video> topRandomNotInHistory(Map<String, Object> params);
	List<Video> topRandomNotInVideoId(Map<String, Object> params);
	Video create(Video entity);
	Video update(Video entity);
	Video delete(Video entity);
}
