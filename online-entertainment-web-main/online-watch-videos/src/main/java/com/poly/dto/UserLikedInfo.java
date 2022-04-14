package com.poly.dto;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedStoredProcedureQueries;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.StoredProcedureParameter;

import com.poly.constant.NamedStored;
import com.poly.constant.NamedStoredParam;

@Entity
@NamedStoredProcedureQueries({
	@NamedStoredProcedureQuery(
		name = NamedStored.FIND_USERS_LIKED_BY_VIDEOID,
		procedureName = "sp_selectUsersLikedVideoByVideoId",
		resultClasses = {UserLikedInfo.class},
		parameters = @StoredProcedureParameter(name = NamedStoredParam.VIDEO_ID, type = String.class)
	)
})
public class UserLikedInfo {
	private String id;
	private String fullname;
	private String email;
	private Timestamp likeDate;
	
	public UserLikedInfo() {
		super();
	}
	
	public UserLikedInfo(String id, String fullname, String email, Timestamp likeDate) {
		super();
		this.id = id;
		this.fullname = fullname;
		this.email = email;
		this.likeDate = likeDate;
	}

	@Id
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFullname() {
		return fullname;
	}
	public void setFullname(String fullname) {
		this.fullname = fullname;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Timestamp getLikeDate() {
		return likeDate;
	}
	public void setLikeDate(Timestamp likeDate) {
		this.likeDate = likeDate;
	}
}
