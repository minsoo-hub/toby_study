package com.example.splearn.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class EmailTest {
	
	@DisplayName("값 객체의 동등성 확인")
	@Test
	void equality() {
	    var email1 = new Email("email@email.com");
		var email2 = new Email("email@email.com");
		
		assertThat(email1).isEqualTo(email2);
	    }
	
}