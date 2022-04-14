package com.poly.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.NamedStoredProcedureQueries;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.OneToMany;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;

import com.poly.constant.NamedStored;
import com.poly.constant.NamedStoredParam;


/**
 * The persistent class for the Video database table.
 * 
 */
@Entity
@NamedQuery(name="Video.findAll", query="SELECT v FROM Video v")
@NamedStoredProcedureQueries({
	@NamedStoredProcedureQuery(
		name = NamedStored.TOP_VIEW_NOT_IN_HISTORY,
		procedureName = "sp_TopViewNotInHistory",
		resultClasses = {Video.class},
		parameters = {
			@StoredProcedureParameter(name=NamedStoredParam.SIZE, type = Integer.class, mode=ParameterMode.IN),
			@StoredProcedureParameter(name=NamedStoredParam.USER_ID, type = String.class, mode=ParameterMode.IN)
		}
	),
	@NamedStoredProcedureQuery(
		name = NamedStored.TOP_VIEW_NOT_IN_VIDEOID,
		procedureName = "sp_TopViewNotInVideoId",
		resultClasses = {Video.class},
		parameters = {
			@StoredProcedureParameter(name=NamedStoredParam.SIZE, type = Integer.class, mode=ParameterMode.IN),
			@StoredProcedureParameter(name=NamedStoredParam.VIDEO_ID, type = String.class, mode=ParameterMode.IN)
		}
	),
	@NamedStoredProcedureQuery(
			name = NamedStored.TOP_RANDOM_NOT_IN_HISTORY,
			procedureName = "sp_TopRandomNotInHistory",
			resultClasses = {Video.class},
			parameters = {
				@StoredProcedureParameter(name=NamedStoredParam.SIZE, type = Integer.class, mode=ParameterMode.IN),
				@StoredProcedureParameter(name=NamedStoredParam.USER_ID, type = String.class, mode=ParameterMode.IN)
			}
		),
	@NamedStoredProcedureQuery(
		name = NamedStored.TOP_RANDOM_NOT_IN_VIDEOID,
		procedureName = "sp_TopRandomNotInVideoId",
		resultClasses = {Video.class},
		parameters = {
			@StoredProcedureParameter(name=NamedStoredParam.SIZE, type = Integer.class, mode=ParameterMode.IN),
			@StoredProcedureParameter(name=NamedStoredParam.VIDEO_ID, type = String.class, mode=ParameterMode.IN)
		}
	)
})
public class Video implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="Id")
	private String id;

	@Column(name="Active")
	private boolean active;

	@Column(name="Description")
	private String description;

	@Column(name="Poster")
	private String poster;

	@Column(name="Title")
	private String title;

	@Column(name="UploadDate")
	private Timestamp uploadDate;

	@Column(name="Views")
	private int views;

	//bi-directional many-to-one association to History
	@OneToMany(mappedBy="video")
	private List<History> histories;

	//bi-directional many-to-one association to Share
	@OneToMany(mappedBy="video")
	private List<Share> shares;

	public Video() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean isActive() {
		return this.active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPoster() {
		return this.poster;
	}

	public void setPoster(String poster) {
		this.poster = poster;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Timestamp getUploadDate() {
		return this.uploadDate;
	}

	public void setUploadDate(Timestamp uploadDate) {
		this.uploadDate = uploadDate;
	}

	public int getViews() {
		return this.views;
	}

	public void setViews(int views) {
		this.views = views;
	}

	public List<History> getHistories() {
		return this.histories;
	}

	public void setHistories(List<History> histories) {
		this.histories = histories;
	}

	public History addHistory(History history) {
		getHistories().add(history);
		history.setVideo(this);

		return history;
	}

	public History removeHistory(History history) {
		getHistories().remove(history);
		history.setVideo(null);

		return history;
	}

	public List<Share> getShares() {
		return this.shares;
	}

	public void setShares(List<Share> shares) {
		this.shares = shares;
	}

	public Share addShare(Share share) {
		getShares().add(share);
		share.setVideo(this);

		return share;
	}

	public Share removeShare(Share share) {
		getShares().remove(share);
		share.setVideo(null);

		return share;
	}

}