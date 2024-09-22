package io.sesac.admin.rest;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import io.sesac.admin.model.MemberDTO;
import io.sesac.admin.model.SimpleValue;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.server.SimpleRepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.Objects;


@Component
public class MemberAssembler implements SimpleRepresentationModelAssembler<MemberDTO> {

    @Override
    public void addLinks(final EntityModel<MemberDTO> entityModel) {
        entityModel.add(linkTo(methodOn(MemberResource.class).getMember(Objects.requireNonNull(entityModel.getContent()).getId())).withSelfRel());
        entityModel.add(linkTo(methodOn(MemberResource.class).getAllMembers(null, null)).withRel(IanaLinkRelations.COLLECTION));
    }

    @Override
    public void addLinks(final CollectionModel<EntityModel<MemberDTO>> collectionModel) {
        collectionModel.add(linkTo(methodOn(MemberResource.class).getAllMembers(null, null)).withSelfRel());
    }

    public EntityModel<SimpleValue<Long>> toSimpleModel(final Long id) {
        final EntityModel<SimpleValue<Long>> simpleModel = SimpleValue.entityModelOf(id);
        simpleModel.add(linkTo(methodOn(MemberResource.class).getMember(id)).withSelfRel());
        return simpleModel;
    }

}
