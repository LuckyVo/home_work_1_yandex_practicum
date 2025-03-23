package ru.myblog.mapper;

import org.mapstruct.*;
import ru.myblog.dto.PostDto;
import ru.myblog.model.entity.PostEntity;
import ru.myblog.model.request.PostRequest;

@Mapper(
        componentModel = "spring",
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public abstract class PostMapper {


    @Mapping(target = "id", source = "id")
    @Mapping(target = "title", source = "title")
    @Mapping(target = "text", source = "text")
    @Mapping(target = "imagePath", source = "imagePath")
    @Mapping(target = "likesCount", source = "likesCount")
    @Mapping(target = "comment", source = "comment")
    @Mapping(target = "tag", source = "tag")
    public abstract PostDto fromEntityToDto(PostEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "title", source = "title")
    @Mapping(target = "text", source = "text")
    @Mapping(target = "imagePath", expression = "java(request.image().getBytes())")
    @Mapping(target = "likesCount", ignore = true)
    @Mapping(target = "comment", ignore = true)
    @Mapping(target = "tag", ignore = true)
    public abstract PostEntity fromRequestToEntity(PostRequest request) throws Exception;

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "title", source = "request.title")
    @Mapping(target = "text", source = "request.text")
    @Mapping(target = "imagePath", expression = "java(request.image().getBytes())")
    @Mapping(target = "likesCount", ignore = true)
    @Mapping(target = "comment", ignore = true)
    @Mapping(target = "tag", ignore = true)
    public abstract PostEntity fromRequestToEntity(@MappingTarget PostEntity entity, PostRequest request) throws Exception;
}
