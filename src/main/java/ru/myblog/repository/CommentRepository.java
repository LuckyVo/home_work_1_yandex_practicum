package ru.myblog.repository;

import ru.myblog.model.entity.CommentEntity;
import ru.myblog.model.entity.PostEntity;

import java.util.List;
import java.util.Map;


public interface CommentRepository {

    void save(CommentEntity entity);
    void delete(CommentEntity entity);
    List<CommentEntity> findByPostId(PostEntity entity);
    List<CommentEntity> findById(Long id);
    Map<Long, List<CommentEntity>> findByPostIds(List<Long> postIds);
    int update(CommentEntity entity);
}
