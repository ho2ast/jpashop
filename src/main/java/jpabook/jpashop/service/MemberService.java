package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

  private final MemberRepository memberRepository;

  /**
   * 회원가입
   */
  @Transactional // 클래스 레벨에서는 리드온리하나 여기서는 일반 트랜잭셔널로 사용됨
  public Long join(Member member) {
    validateDuplicateMember(member); //중복회원검증
    memberRepository.save(member);
    return member.getId();
  }

  /**
   * 중복회원검증
   */
  private void validateDuplicateMember(Member member) {
    // EXCEPTION
    List<Member> findMembers = memberRepository.findByName(member.getName());
    if (!findMembers.isEmpty()) {
      throw new IllegalStateException("이미 존재하는 회원 입니다.");
    }
  }

  /**
   * 회원 전체 조회
   */
  // @Transactional(readOnly = true) readOnly = true하면 db에서 읽기전용 모드로 조회해서 성능에 좋아짐.
  public List<Member> findMembers() {
    return memberRepository.findAll();
  }

  /**
   * 회원 단건 조회
   */
  public Member findOne(Long memberId) {
    return memberRepository.findOne(memberId);
  }

  /**
   * 회원 수정
   * @param id
   * @param name
   */
  @Transactional
  public void update(Long id, String name) {
    Member member = memberRepository.findOne(id);
    member.setName(name);
  }
}
