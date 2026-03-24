package com.example.splearn.adapter.integration;

import org.springframework.stereotype.Component;

import com.example.splearn.application.required.EmailSender;
import com.example.splearn.domain.Email;

@Component
public class DummyEmailSender implements EmailSender {
	@Override
	public void send(Email email, String subject, String body) {
		System.out.println("DummyEmailSender send email = " + email);
	}
}
