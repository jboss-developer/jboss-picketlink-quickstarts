package org.jboss.as.quickstarts.picketlink.angularjs.model;

import java.util.Arrays;
import java.util.List;

public class Email {

	private final String subject;
	private final String body;
	private final List<String> address;

	public Email(String subject, String body, String... addresses) {
		this.subject = subject;
		this.body = body;
		this.address = Arrays.asList(addresses);
	}

	public String getSubject() {
		return this.subject;
	}

	public List<String> getAddress() {
		return this.address;
	}

	public String getBody() {
		return this.body;
	}
}
