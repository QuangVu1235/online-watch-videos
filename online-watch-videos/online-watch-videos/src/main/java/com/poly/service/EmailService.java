package com.poly.service;

import javax.servlet.ServletContext;

import com.poly.entity.User;

public interface EmailService {
	void sendEmailWelcome(ServletContext context, User recipient);
	void sendEmailForgotPassword(ServletContext context, User recipient);
	void sendEmailShareVideo(ServletContext context, User sender, String videoId, String recipient);
}
