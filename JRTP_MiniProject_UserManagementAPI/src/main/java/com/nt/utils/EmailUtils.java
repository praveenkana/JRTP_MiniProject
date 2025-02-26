package com.nt.utils;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import jakarta.mail.internet.MimeMessage;

@Component
public class EmailUtils {

	@Autowired
	private JavaMailSender mailSender;

	public boolean sendEmailMessage(String toMailAdd, String subject, String body)throws Exception {

		boolean mailSendStatus = false;

		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setTo(toMailAdd);
			helper.setSentDate(new Date());
			helper.setText(body,true);
			helper.setSubject(subject);
			mailSender.send(message);
			mailSendStatus=true;

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

		return mailSendStatus;

	}
}
