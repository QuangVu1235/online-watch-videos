package com.poly.dto;

import java.sql.Timestamp;

public class VideoLikedInfo {
	private String id;
	private String title;
	private Integer totalLike;
	private Timestamp newest;
	private Timestamp oldest;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Integer getTotalLike() {
		return totalLike;
	}
	public void setTotalLike(Integer totalLike) {
		this.totalLike = totalLike;
	}
	public Timestamp getNewest() {
		return newest;
	}
	public void setNewest(Timestamp newest) {
		this.newest = newest;
	}
	public Timestamp getOldest() {
		return oldest;
	}
	public void setOldest(Timestamp oldest) {
		this.oldest = oldest;
	}	
}
