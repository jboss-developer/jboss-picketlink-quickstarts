package com.gr.project.service;

import javax.annotation.Resource;
import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Named;
import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import javax.ws.rs.core.MediaType;

import com.gr.project.model.Email;

/**
 * Session Bean implementation class MailService
 */
@Stateless
public class MailService {
	
	@Resource(mappedName = "java:/mail/gmail")
	private Session session;
	
	@Inject
	@Named("default.encoding")
	private String defaultEncoding;

	@Asynchronous
	public void send(@Observes Email email) {
		try {
			MimeMessage msg = new MimeMessage(session);
			msg.setHeader("Content-Type", "text/html;charset=UTF-8");
			msg.setSubject(email.getSubject(), defaultEncoding);
			msg.setContent(email.getBody(), MediaType.TEXT_HTML);

			Address[] internetAdress;
			if (email.getAddress() != null) {
				internetAdress = new Address[email.getAddress().size()];
				for (int i = 0; i < email.getAddress().size(); i++) {
					internetAdress[i] = new InternetAddress(email.getAddress()
							.get(i));
				}
				msg.setRecipients(RecipientType.TO, internetAdress);
			}

			Transport.send(msg);
		} catch (MessagingException e) {
			System.out.println(e);
		}
	}

}
