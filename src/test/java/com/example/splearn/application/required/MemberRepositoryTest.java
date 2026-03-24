package com.example.splearn.application.required;

import static com.example.splearn.domain.MemberFixture.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import com.example.splearn.domain.Member;

import jakarta.persistence.EntityManager;

@DataJpaTest
class MemberRepositoryTest {
	@Autowired
	MemberRepository memberRepository;

	@Autowired
	EntityManager em;

	@Test
	void createMember() {
		Member member = Member.register(createMemberRegisterRequest(), createPasswordEncoder());

		assertThat(member.getId()).isNull();

		memberRepository.save(member);

		assertThat(member.getId()).isNotNull();

		em.flush();
	}

	@Test
	void duplicateEmailFail() {
		Member member1 = Member.register(createMemberRegisterRequest(), createPasswordEncoder());
		memberRepository.save(member1);

		Member member2 = Member.register(createMemberRegisterRequest(), createPasswordEncoder());
		assertThatThrownBy(() -> memberRepository.save(member2))
			.isInstanceOf(DataIntegrityViolationException.class);
	}
}