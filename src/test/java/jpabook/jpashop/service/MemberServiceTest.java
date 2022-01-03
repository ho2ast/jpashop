package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberServiceTest {

  @Autowired MemberService memberService;
  @Autowired MemberRepository memberRepository;
//  @Autowired EntityManager em;

  @Test
  void 회원가입() throws Exception {
    //given
    Member member = new Member();
    member.setName("dongho");

    //when
    Long saveId = memberService.join(member); // persist한다고 바로 커밋 되는것이 아니다.

    //then
//    em.flush();
    assertEquals(member, memberRepository.findOne(saveId));
  }

  @Test
  void 중복_회원_예약() throws Exception {
    //given
    Member member1 = new Member();
    member1.setName("kim");

    Member member2 = new Member();
    member2.setName("kim");

    //when
    memberService.join(member1);

    //then
    assertThrows(IllegalStateException.class, () -> {
      memberService.join(member2); // 예외가 발생해야 한다
    });
//    fail("에러가 발생해야 한다.");
  }

}