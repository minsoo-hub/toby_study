package com.example.splearn.domain;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MemberTest {
	Member member;
	PasswordEncoder passwordEncoder;

	@BeforeEach
	void setUp() {
		this.passwordEncoder = new PasswordEncoder() {
			@Override
			public String encode(String password) {
				return password.toUpperCase();
			}

			@Override
			public boolean matches(String password, String passwordHash) {
				return encode(password).equals(passwordHash);
			}
		};
		member = Member.create(new MemberCreateRequest("naminsoo1020@naver.com", "Minsoo", "secret"), passwordEncoder);
	}

	@DisplayName("회원 생성 : 생성시, status = PENDING")
	@Test
	void createMember() {
		assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
	}

	@DisplayName("가입승인 : PENDING -> ACTIVE")
	@Test
	void activate() {
		member.activate();

		assertThat(member.getStatus()).isEqualTo(MemberStatus.ACTIVE);
	}

	@DisplayName("가입승인 | 예외발생 : ACTIVE -> ACTIVE")
	@Test
	void activateFail() {
		member.activate();

		assertThatThrownBy(() -> {
			member.activate();
		}).isInstanceOf(IllegalStateException.class);
	}

	@DisplayName("회원 탈퇴 : ACTIVE -> DEACTIVATED")
	@Test
	void deactivate() {
		member.activate();

		member.deactivate();

		assertThat(member.getStatus()).isEqualTo(MemberStatus.DEACTIVATED);
	}

	@DisplayName("회원 탈퇴 | 예외발생 : PENDING, DEACTIVATED -> DEACTIVATED")
	@Test
	void deactivateFail() {
		assertThatThrownBy(() -> {
			member.deactivate();
		}).isInstanceOf(IllegalStateException.class);

		member.activate();
		member.deactivate();

		assertThatThrownBy(() -> {
			member.deactivate();
		}).isInstanceOf(IllegalStateException.class);
	}

	@DisplayName("비밀번호 검증")
	@Test
	void verifyPassword() {
		assertThat(member.verifyPassword("secret", passwordEncoder)).isTrue();
		assertThat(member.verifyPassword("open", passwordEncoder)).isFalse();
	}

	@DisplayName("닉네임 변경")
	@Test
	void changeNickname() {
		assertThat(member.getNickname()).isEqualTo("Minsoo");

		member.changeNickname("Change");

		assertThat(member.getNickname()).isEqualTo("Change");
	}

	@DisplayName("비밀번호 변경")
	@Test
	void changePassword() {
		member.changePassword("verysecret", passwordEncoder);

		assertThat(member.verifyPassword("verysecret", passwordEncoder)).isTrue();
	}

	@DisplayName("활성상태인 회원 검증")
	@Test
	void shouldBeActive() {
		assertThat(member.isActive()).isFalse();

		member.activate();

		assertThat(member.isActive()).isTrue();

		member.deactivate();

		assertThat(member.isActive()).isFalse();
	}

	@DisplayName("이메일 검증 : 회원 생성 시, 이메일 형식이 올바른지 검증")
	@Test
	void invalidEmail() {
		assertThatThrownBy(() ->
				Member.create(new MemberCreateRequest("kk", "Minsoo", "secret"), passwordEncoder)
		).isInstanceOf(IllegalArgumentException.class);

		Member.create(new MemberCreateRequest("naminsoo1020@naver.com", "Minsoo", "secret"), passwordEncoder);
	}

}
