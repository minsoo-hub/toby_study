package com.example.splearn.domain;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MemberTest {
	@DisplayName("회원 생성 : 생성시, status = PENDING")
	@Test
	void createMember() {
		var member = new Member("naminsoo1020@naver.com", "Minsoo", "secret");

		assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
	}

	@DisplayName("회원 생성 | 예외 발생 : 생성자에 Null ")
	@Test
	void constructorEmailNullCheck() {
		assertThatThrownBy(() -> new Member(null, "Minsoo", "secret"))
			.isInstanceOf(NullPointerException.class);
	}

	@DisplayName("가입승인 : PENDING -> ACTIVE")
	@Test
	void activate() {
		var member = new Member("naminsoo1020@naver.com", "Minsoo", "secret");

		member.activate();

		assertThat(member.getStatus()).isEqualTo(MemberStatus.ACTIVE);
	}

	@DisplayName("가입승인 | 예외발생 : ACTIVE -> ACTIVE")
	@Test
	void activateFail() {
		var member = new Member("naminsoo1020@naver.com", "Minsoo", "secret");

		member.activate();

		assertThatThrownBy(() -> {
			member.activate();
		}).isInstanceOf(IllegalStateException.class);
	}

	@DisplayName("회원 탈퇴 : ACTIVE -> DEACTIVATED")
	@Test
	void deactivate() {
		var member = new Member("naminsoo1020@naver.com", "Minsoo", "secret");
		member.activate();

		member.deactivate();

		assertThat(member.getStatus()).isEqualTo(MemberStatus.DEACTIVATED);
	}

	@DisplayName("회원 탈퇴 | 예외발생 : PENDING, DEACTIVATED -> DEACTIVATED")
	@Test
	void deactivateFail() {
		var member = new Member("naminsoo1020@naver.com", "Minsoo", "secret");

		assertThatThrownBy(() -> {
			member.deactivate();
		}).isInstanceOf(IllegalStateException.class);

		member.activate();
		member.deactivate();

		assertThatThrownBy(() -> {
			member.deactivate();
		}).isInstanceOf(IllegalStateException.class);
	    }

}
