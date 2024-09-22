package io.sesac.admin.model;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class MemberDTO {

    private Long id;

    @Size(max = 255)
    private String name;

    private Gender gender;

}
