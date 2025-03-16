package ru.myblog.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Entity
@Table(schema = "my_blog", name = "tag")
public class TagEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;


    @ToString.Exclude
    @ManyToMany(mappedBy = "tags")
    private Set<PostEntity> postEntities = new HashSet<>();
}
