package ru.myblog.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.myblog.model.entity.PostEntity;


public interface PostRepository {

    PostEntity findById(Long id);

    int save(PostEntity entity);

    void delete(PostEntity entity);

    int update(PostEntity entity);

    Page<PostEntity> findAll(Pageable pageable);
}
