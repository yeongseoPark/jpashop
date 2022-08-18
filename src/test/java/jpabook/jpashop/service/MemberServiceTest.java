package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class) // 스프링과 테스트 통합
@SpringBootTest // 스프링 부트에 실제 올려서 컨테이너 안에서 테스트(없으면 @Autowired 다 실패)
@Transactional // 트랜잭션 커밋x, 테스트 끝나면 롤백을 한다 : 테스트 케이스에서 사용될 때만
public class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;
    @Autowired
    EntityManager em;

    @Test
//    @Rollback(value = false)
    public void 회원가입() throws Exception {
        // given
        Member member = new Member();
        member.setName("kim");

        // when
        Long savedId = memberService.join(member);

        // then
        em.flush(); // flush : 영속성 컨텍스트에 있는 변경이나 등록 내용을 데이터베이스에 반영
        assertEquals(member, memberRepository.findOne(savedId));
        // 테스트 끝날때 Transactional이 롤백시킴
    }

    @Test(expected = IllegalStateException.class)
    public void 중복_회원_예외() throws Exception {
        // given
        Member member1 = new Member();
        member1.setName("kim");

        Member member2 = new Member();
        member2.setName("kim");
        // when
        memberService.join(member1);
//        try {
//            memberService.join(member2); // 예외발생
//        } catch (IllegalStateException e) {
//            return; // 정상적인 리턴이기에 테스트 성공
//        }
        /* 이 코드를 expected = IllegalStateException.class 가 대신함 */
        memberService.join(member2);

        // then
        Assert.fail("예외가 발생해야 한다"); // 코드가 여기까지 오면 테스트 실패한 것
    }


}