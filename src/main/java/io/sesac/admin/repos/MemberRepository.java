package io.sesac.admin.repos;

import io.sesac.admin.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MemberRepository extends JpaRepository<Member, Long> {

    Page<Member> findAllById(Long id, Pageable pageable);

}
