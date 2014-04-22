package com.gr.project.model;

import java.util.ArrayList;
import java.util.List;

public class Email {
	private String subject;
	private String body;
	private List<String> address;

	public Email(String subject, String body, String... addresses) {
		this.subject = subject;
		this.body = body;
		List<String> emails = new ArrayList<String>();
		for (String address : addresses) {
			emails.add(address);
		}
		setAddress(emails);
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public List<String> getAddress() {
		return address;
	}

	public void setAddress(List<String> address) {
		this.address = address;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}
}
