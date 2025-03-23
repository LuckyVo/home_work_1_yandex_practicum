package ru.myblog.model.entity;

import lombok.*;

import java.util.List;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class PostEntity {

    private Long id;
    private String title;
    private String text;
    private byte[] imagePath;
    private Integer likesCount = 0;
    private List<CommentEntity> comment;
    private List<TagEntity> tag;


    public PostEntity(Long id, String title, String text, byte[] imagePath, Integer likesCount) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.imagePath = imagePath;
        this.likesCount = likesCount;
    }
}
