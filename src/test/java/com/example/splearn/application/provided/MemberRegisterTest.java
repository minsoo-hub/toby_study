package com.example.splearn.application.provided;

import static com.example.splearn.domain.MemberFixture.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import com.example.splearn.SplearnTestConfiguration;
import com.example.splearn.domain.DuplicateEmailException;
import com.example.splearn.domain.Member;
import com.example.splearn.domain.MemberRegisterRequest;
import com.example.splearn.domain.MemberStatus;

import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;

@Transactional
@SpringBootTest
@Import(SplearnTestConfiguration.class)
public record MemberRegisterTest(MemberRegister memberRegister) {
	@Test
	void register() {
		Member member = memberRegister.register(createMemberRegisterRequest());

		assertThat(member.getId()).isEqualTo(1L);
		assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
	}

	@Test
	void duplicateEmailFail() {
		Member member = memberRegister.register(createMemberRegisterRequest());

		assertThatThrownBy(() -> memberRegister.register(createMemberRegisterRequest()))
			.isInstanceOf(DuplicateEmailException.class);
	}

	@Test
	void memberRegisterFail() {
		extracted(new MemberRegisterRequest("naminsoo1020@naver.com", "soo", "longlongsecret"));
		extracted(new MemberRegisterRequest("naminsoo1020@naver.com", "Minsoo____________________________", "longsecret"));
		extracted(new MemberRegisterRequest("naminsoo102.com", "Minsoo", "secret"));
	}

	private void extracted(MemberRegisterRequest invalid) {
		assertThatThrownBy(() -> memberRegister.register(invalid))
			.isInstanceOf(ConstraintViolationException.class);
	}

}
