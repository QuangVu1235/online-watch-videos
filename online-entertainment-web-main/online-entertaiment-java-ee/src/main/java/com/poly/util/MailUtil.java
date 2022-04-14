package com.poly.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.servlet.ServletException;

public class MailUtil {
	public static final String TYPE_CONTENT_HTML = "text/html: charser:UTF-8";
	public static final String TYPE_CONTENT_PLAIN = "text/plain; charset=UTF-8";

	public static void SendMail(String email, String password, String typeContent, String recipient, String cc, String bcc, String subject,
			String body, List<File> files) throws MessagingException, IOException, ServletException {
		// Thông số kết nối Smtp Server
		Properties props = new Properties();
		props.setProperty("mail.smtp.auth", "true");
		props.setProperty("mail.smtp.starttls.enable", "true");
		props.setProperty("mail.smtp.host", "smtp.gmail.com");
		props.setProperty("mail.smtp.port", "587");
		// Kết nối Smtp Server
		Session session = Session.getInstance(props, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				// TODO Auto-generated method stub
				return new PasswordAuthentication(email, password);
			}
		});

		MimeMessage message = new MimeMessage(session);
		message.setFrom(new InternetAddress(email));
		message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
		if (!cc.isEmpty() && cc != null) {
			message.addRecipients(Message.RecipientType.CC, cc);
		}
		if (!bcc.isEmpty() && bcc != null) {
			message.addRecipients(Message.RecipientType.BCC, bcc);
		}
		message.setSubject(subject, "UTF-8");
		message.setText(body, "UTF-8", "html");
		message.setSentDate(new Date());

		MimeBodyPart textPart = new MimeBodyPart();
		
		/**
		 *  typeContent: "text/plain; charset=UTF-8", "text/html: charser:UTF-8"
		 */
		textPart.setContent(body, typeContent);
		Multipart mp = new MimeMultipart();
		mp.addBodyPart(textPart);

		// Đính kèm tệp
		for (File file : files) {
			MimeBodyPart attachFilePart = new MimeBodyPart();
			FileDataSource fds = new FileDataSource(file);

			attachFilePart.setDataHandler(new DataHandler(fds));
			attachFilePart.setFileName(fds.getName());

			mp.addBodyPart(attachFilePart);
		}

		message.setContent(mp);
		MailSender.queue(message);
	}

	public static void SendMail(String email, String password, String typeContent, String recipient, String subject, String body)
			throws MessagingException, IOException, ServletException {
		// Thông số kết nối Smtp Server
		Properties props = new Properties();
		props.setProperty("mail.smtp.auth", "true");
		props.setProperty("mail.smtp.starttls.enable", "true");
		props.setProperty("mail.smtp.host", "smtp.gmail.com");
		props.setProperty("mail.smtp.port", "587");
		// Kết nối Smtp Server
		Session session = Session.getInstance(props, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				// TODO Auto-generated method stub
				return new PasswordAuthentication(email, password);
			}
		});

		MimeMessage message = new MimeMessage(session);
		message.setFrom(new InternetAddress(email));
		message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
		message.setSubject(subject, "UTF-8");
		message.setText(body, "UTF-8", "html");
		message.setSentDate(new Date());

		MimeBodyPart textPart = new MimeBodyPart();
		textPart.setContent(body, typeContent);
		Multipart mp = new MimeMultipart();
		mp.addBodyPart(textPart);

		message.setContent(mp);
		MailSender.queue(message);
	}
}

class MailSender extends Thread {
	static {
		MailSender sender = new MailSender();
		sender.start();
	}
	private static final List<MimeMessage> queue = new ArrayList<>();

	public static void queue(MimeMessage mail) {
		synchronized (queue) {
			queue.add(mail);
			queue.notify();
		}
	}

	@Override
	public void run() {
		while (true) {
			try {
				synchronized (queue) {
					if (queue.size() > 0) {
						try {
							MimeMessage mail = queue.remove(0);
							Transport.send(mail);
							System.out.println("The mail was sent.");
						} catch (MessagingException e) {
							System.out.println("Unable to send mail.");
						}
					} else {
						queue.wait();
					}
				}
			} catch (InterruptedException e) {
				break;
			}
		}
	}
}
