package com.poly.service;

import java.util.List;

import com.poly.dto.UserSharedInfo;
import com.poly.dto.VideoLikedInfo;

public interface StatsService {
	List<VideoLikedInfo> findVideoLikedInfo();
	List<UserSharedInfo> findUserSharedInfo(String videoId);
}
