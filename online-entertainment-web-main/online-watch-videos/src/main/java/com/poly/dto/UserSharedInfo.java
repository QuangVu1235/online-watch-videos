package com.poly.dto;

import java.sql.Timestamp;

public class UserSharedInfo {
	private String senderEmail;
	private String senderName;
	private String receiverEmail;
	private Timestamp sendDate;
	
	public UserSharedInfo() {
		super();
	}
	
	public UserSharedInfo(String senderName, String senderEmail, String receiverEmail, Timestamp sendDate) {
		super();
		this.senderName = senderName;
		this.senderEmail = senderEmail;
		this.receiverEmail = receiverEmail;
		this.sendDate = sendDate;
	}

	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	public String getSenderEmail() {
		return senderEmail;
	}

	public void setSenderEmail(String senderEmail) {
		this.senderEmail = senderEmail;
	}

	public String getReceiverEmail() {
		return receiverEmail;
	}

	public void setReceiverEmail(String receiverEmail) {
		this.receiverEmail = receiverEmail;
	}

	public Timestamp getSendDate() {
		return sendDate;
	}

	public void setSendDate(Timestamp sendDate) {
		this.sendDate = sendDate;
	}
	
}
