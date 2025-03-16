package ru.myblog.dto;

import lombok.Data;
import ru.myblog.model.entity.CommentEntity;

import java.util.List;

@Data
public class PostDto {

    private Long id;
    private String title;
    private String text;
    private byte[] imagePath;
    private Integer likesCount;
    private List<CommentEntity> commentEntities;
}
