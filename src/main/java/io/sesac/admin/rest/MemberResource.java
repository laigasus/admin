package io.sesac.admin.rest;

import io.sesac.admin.model.MemberDTO;
import io.sesac.admin.model.SimpleValue;
import io.sesac.admin.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.data.web.SortDefault;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/members", produces = MediaType.APPLICATION_JSON_VALUE)
public class MemberResource {

    private final MemberService memberService;
    private final MemberAssembler memberAssembler;
    private final PagedResourcesAssembler<MemberDTO> pagedResourcesAssembler;

    public MemberResource(final MemberService memberService, final MemberAssembler memberAssembler,
            final PagedResourcesAssembler<MemberDTO> pagedResourcesAssembler) {
        this.memberService = memberService;
        this.memberAssembler = memberAssembler;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @Operation(
            parameters = {
                    @Parameter(
                            name = "page",
                            in = ParameterIn.QUERY,
                            schema = @Schema(implementation = Integer.class)
                    ),
                    @Parameter(
                            name = "size",
                            in = ParameterIn.QUERY,
                            schema = @Schema(implementation = Integer.class)
                    ),
                    @Parameter(
                            name = "sort",
                            in = ParameterIn.QUERY,
                            schema = @Schema(implementation = String.class)
                    )
            }
    )
    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<MemberDTO>>> getAllMembers(
            @RequestParam(name = "filter", required = false) final String filter,
            @Parameter(hidden = true) @SortDefault(sort = "id") @PageableDefault(size = 20) final Pageable pageable) {
        final Page<MemberDTO> memberDTOs = memberService.findAll(filter, pageable);
        return ResponseEntity.ok(pagedResourcesAssembler.toModel(memberDTOs, memberAssembler));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<MemberDTO>> getMember(
            @PathVariable(name = "id") final Long id) {
        final MemberDTO memberDTO = memberService.get(id);
        return ResponseEntity.ok(memberAssembler.toModel(memberDTO));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<EntityModel<SimpleValue<Long>>> createMember(
            @RequestBody @Valid final MemberDTO memberDTO) {
        final Long createdId = memberService.create(memberDTO);
        return new ResponseEntity<>(memberAssembler.toSimpleModel(createdId), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<SimpleValue<Long>>> updateMember(
            @PathVariable(name = "id") final Long id,
            @RequestBody @Valid final MemberDTO memberDTO) {
        memberService.update(id, memberDTO);
        return ResponseEntity.ok(memberAssembler.toSimpleModel(id));
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteMember(@PathVariable(name = "id") final Long id) {
        memberService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
