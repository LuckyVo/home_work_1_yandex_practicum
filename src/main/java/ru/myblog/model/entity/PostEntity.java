package ru.myblog.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Entity
@Table(schema = "my_blog", name = "post")
public class PostEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "text")
    private String text;

    @Column(name = "imagePath")
    private byte[] imagePath;

    @Column(name = "likesCount")
    private Integer likesCount = 0;

    @ToString.Exclude
    @OneToMany(
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            targetEntity = CommentEntity.class,
            mappedBy = "postEntity")
    private List<CommentEntity> commentEntities;


    @ToString.Exclude
    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "post_tag",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<TagEntity> tags = new HashSet<>();
}
