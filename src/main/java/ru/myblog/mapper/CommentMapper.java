package ru.myblog.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.myblog.model.entity.CommentEntity;
import ru.myblog.model.request.CommentRequest;

@Mapper(
        componentModel = "spring",
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public abstract class CommentMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "text", source = "text")
    @Mapping(target = "postEntity", ignore = true)
    public abstract CommentEntity fromRequestToEntity (CommentRequest request);
}
