package io.sesac.admin.service;

import io.sesac.admin.domain.Member;
import io.sesac.admin.model.MemberDTO;
import io.sesac.admin.repos.MemberRepository;
import io.sesac.admin.util.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;

    public MemberService(final MemberRepository memberRepository, final MemberMapper memberMapper) {
        this.memberRepository = memberRepository;
        this.memberMapper = memberMapper;
    }

    public Page<MemberDTO> findAll(final String filter, final Pageable pageable) {
        Page<Member> page;
        if (filter != null) {
            Long longFilter = null;
            try {
                longFilter = Long.parseLong(filter);
            } catch (final NumberFormatException numberFormatException) {
                // keep null - no parseable input
            }
            page = memberRepository.findAllById(longFilter, pageable);
        } else {
            page = memberRepository.findAll(pageable);
        }
        return new PageImpl<>(page.getContent()
                .stream()
                .map(member -> memberMapper.updateMemberDTO(member, new MemberDTO()))
                .toList(),
                pageable, page.getTotalElements());
    }

    public MemberDTO get(final Long id) {
        return memberRepository.findById(id)
                .map(member -> memberMapper.updateMemberDTO(member, new MemberDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final MemberDTO memberDTO) {
        final Member member = new Member();
        memberMapper.updateMember(memberDTO, member);
        return memberRepository.save(member).getId();
    }

    public void update(final Long id, final MemberDTO memberDTO) {
        final Member member = memberRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        memberMapper.updateMember(memberDTO, member);
        memberRepository.save(member);
    }

    public void delete(final Long id) {
        memberRepository.deleteById(id);
    }

}
