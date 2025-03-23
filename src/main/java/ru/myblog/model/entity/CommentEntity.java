package ru.myblog.model.entity;

import lombok.*;


@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class CommentEntity {

    private Long id;
    private Long postId;
    private String text;

}