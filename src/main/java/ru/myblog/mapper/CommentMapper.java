package ru.myblog.mapper;

import org.mapstruct.*;
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
    @Mapping(target = "postId", ignore = true)
    public abstract CommentEntity fromRequestToEntity(@MappingTarget CommentEntity entity, CommentRequest request);
}
