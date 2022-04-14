package com.poly.dao;

import java.util.List;

import com.poly.dto.UserSharedInfo;
import com.poly.dto.VideoLikedInfo;

public interface StatsDao {
	List<VideoLikedInfo> findVideoLikedInfo();
	List<UserSharedInfo> findVideoSharedInfo(String videoId);
}
