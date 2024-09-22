package io.sesac.admin.service;

import io.sesac.admin.domain.Member;
import io.sesac.admin.model.MemberDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;


@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface MemberMapper {

    MemberDTO updateMemberDTO(Member member, @MappingTarget MemberDTO memberDTO);

    @Mapping(target = "id", ignore = true)
    Member updateMember(MemberDTO memberDTO, @MappingTarget Member member);

}
