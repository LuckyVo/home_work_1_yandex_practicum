package ru.myblog.model.entity;

import jakarta.persistence.*;
import lombok.*;


@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Entity
@Table(schema = "my_blog", name = "сomment")
public class CommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "text")
    private String text;

    @ToString.Exclude
    @JoinColumn(nullable = false)
    @ManyToOne
    private PostEntity postEntity;

}
