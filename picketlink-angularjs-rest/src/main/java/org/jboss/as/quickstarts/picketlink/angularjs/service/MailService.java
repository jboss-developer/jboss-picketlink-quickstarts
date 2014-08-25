package org.jboss.as.quickstarts.picketlink.angularjs.service;

import org.jboss.as.quickstarts.picketlink.angularjs.model.Email;

import javax.annotation.Resource;
import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.enterprise.event.Observes;
import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import javax.ws.rs.core.MediaType;

/**
 * Session Bean implementation class MailService
 */
@Stateless
public class MailService {
	
	@Resource(mappedName = "java:/mail/gmail")
	private Session session;
	
	@Asynchronous
	public void send(@Observes Email email) {
		try {
			MimeMessage msg = new MimeMessage(session);

			msg.setHeader("Content-Type", "text/html;charset=UTF-8");
			msg.setSubject(email.getSubject(), "UTF-8");
			msg.setContent(email.getBody(), MediaType.TEXT_HTML);

			if (email.getAddress() != null) {
                Address[] address = new Address[email.getAddress().size()];

				for (int i = 0; i < email.getAddress().size(); i++) {
					address[i] = new InternetAddress(email.getAddress().get(i));
				}

				msg.setRecipients(RecipientType.TO, address);
			}

			Transport.send(msg);
		} catch (MessagingException e) {
			System.out.println(e);
		}
	}

}
