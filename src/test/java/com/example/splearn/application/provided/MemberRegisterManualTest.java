package com.example.splearn.application.provided;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import com.example.splearn.application.MemberService;
import com.example.splearn.application.required.EmailSender;
import com.example.splearn.application.required.MemberRepository;
import com.example.splearn.domain.Email;
import com.example.splearn.domain.Member;
import com.example.splearn.domain.MemberFixture;
import com.example.splearn.domain.MemberStatus;

class MemberRegisterManualTest {

	@Test
	void registerTestStub() {
		MemberRegister register = new MemberService(
			new MemberRepositoryStub(),
			new EmailSenderStub(),
			MemberFixture.createPasswordEncoder()
		);

		Member member = register.register(MemberFixture.createMemberRegisterRequest());

		assertThat(member.getId()).isEqualTo(1L);
		assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
	}

	@Test
	void registerTestMock() {
		EmailSenderMock emailSenderMock = new EmailSenderMock();

		MemberRegister register = new MemberService(
			new MemberRepositoryStub(),
			emailSenderMock,
			MemberFixture.createPasswordEncoder()
		);

		Member member = register.register(MemberFixture.createMemberRegisterRequest());

		assertThat(member.getId()).isEqualTo(1L);
		assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);

		assertThat(emailSenderMock.getTos()).hasSize(1);
		assertThat(emailSenderMock.getTos().getFirst()).isEqualTo(member.getEmail());
	}

	@Test
	void registerTestMockito() {
		EmailSender emailSenderMock = mock(EmailSender.class);

		MemberRegister register = new MemberService(
			new MemberRepositoryStub(),
			emailSenderMock,
			MemberFixture.createPasswordEncoder()
		);

		Member member = register.register(MemberFixture.createMemberRegisterRequest());

		assertThat(member.getId()).isEqualTo(1L);
		assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);

		verify(emailSenderMock, times(1)).send(eq(member.getEmail()), any(), any());
	}

	static class MemberRepositoryStub implements MemberRepository {
		@Override
		public Member save(Member member) {
			ReflectionTestUtils.setField(member, "id", 1L);
			return member;
		}

		@Override
		public Optional<Member> findByEmail(Email email) {
			return Optional.empty();
		}
	}

	static class EmailSenderStub implements EmailSender {
		@Override
		public void send(Email email, String subject, String body) {
		}
	}

	static class EmailSenderMock implements EmailSender {
		List<Email> tos = new ArrayList<Email>();

		public List<Email> getTos() {
			return tos;
		}

		@Override
		public void send(Email email, String subject, String body) {
			tos.add(email);
		}
	}
}