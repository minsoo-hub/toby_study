package com.example.splearn;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import com.example.splearn.application.required.EmailSender;
import com.example.splearn.domain.MemberFixture;
import com.example.splearn.domain.PasswordEncoder;

@TestConfiguration
public class SplearnTestConfiguration {
	@Bean
	public EmailSender emailSender() {
		return (email, subject, body) -> System.out.println("Sending email = " + email);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return MemberFixture.createPasswordEncoder();
	}
}
