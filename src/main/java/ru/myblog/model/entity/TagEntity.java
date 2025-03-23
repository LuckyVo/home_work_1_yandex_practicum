package ru.myblog.model.entity;

import lombok.*;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class TagEntity {

    public TagEntity(Long id, String title) {
        this.id = id;
        this.title = title;
    }

    private Long id;
    private Long postId;
    private String title;
}
