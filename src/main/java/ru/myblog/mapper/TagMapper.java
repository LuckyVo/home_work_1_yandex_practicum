package ru.myblog.mapper;

import org.mapstruct.*;
import ru.myblog.model.entity.TagEntity;
import ru.myblog.model.request.PostRequest;

@Mapper(
        componentModel = "spring",
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public abstract class TagMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "title", source = "tags")
    @Mapping(target = "postId", ignore = true)
    public abstract TagEntity fromRequestToEntity(@MappingTarget TagEntity entity, PostRequest request);
}
