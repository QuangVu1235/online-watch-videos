package com.poly.service;

import java.util.List;

import com.poly.entity.Video;

public interface VideoService {
	Video findById(String id);
	List<Video> findAll();
	List<Video> findAll(int pageNumber, int pageSize);
	List<Video> findByTitle(String keyword, int pageNumber, int pageSize);
	List<Video> findAllOrderByViewDesc();
	List<Video> findAllOrderByViewDesc(int pageNumber, int pageSize);
	List<Video> topViewNotInHistory(int size, String userId);
	List<Video> topViewNotInVideoId(int size, String videoId);
	List<Video> topRandomNotInHistory(int size, String userId);
	List<Video> topRandomNotInVideoId(int size, String videoId);
	Video create(Video entity);
	Video update(Video entity);
	Video delete(String id);
}
