package com.poly.service.impl;

import javax.servlet.ServletContext;

import com.poly.entity.User;
import com.poly.service.EmailService;
import com.poly.util.MailUtil;

public class EmailServiceImpl implements EmailService {
	@Override
	public void sendEmailWelcome(ServletContext context, User recipient) {
		String username = context.getInitParameter("username");
		String password = context.getInitParameter("password");
		
		try {
			String subject = "Chào mừng đến với Online Entertaiment";
			String content = "Chào " + recipient.getFullname() + ", cám ơn bạn đã đăng ký sử dụng trên hệ thống của chúng tôi";
			String typeContent = MailUtil.TYPE_CONTENT_PLAIN;
			
			MailUtil.SendMail(username, password, typeContent, recipient.getEmail(), subject, content);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Override
	public void sendEmailForgotPassword(ServletContext context, User recipient) {
		String username = context.getInitParameter("username");
		String password = context.getInitParameter("password");
		
		try {
			String subject = "Online Entertaiment - Đặt lại mật khẩu";
			String content = "Chào " + recipient.getFullname() + ", đây là mật khẩu mới của bạn: " + recipient.getPassword();
			String typeContent = MailUtil.TYPE_CONTENT_PLAIN;
			
			MailUtil.SendMail(username, password, typeContent, recipient.getEmail(), subject, content);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Override
	public void sendEmailShareVideo(ServletContext context, User sender, String videoId, String recipient) {
		String username = context.getInitParameter("username");
		String password = context.getInitParameter("password");
		
		try {
			String subject = "Chia sẻ video từ Online Entertaiment";
			String content = "Người dùng " + sender.getId() + " đã chia sẻ cho bạn một video:"
					+ " https://youtu.be/" + videoId;
			String typeContent = MailUtil.TYPE_CONTENT_PLAIN;
			
			MailUtil.SendMail(username, password, typeContent, recipient, subject, content);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
