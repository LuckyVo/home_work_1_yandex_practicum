package ru.myblog.repository;

import ru.myblog.model.entity.PostEntity;
import ru.myblog.model.entity.TagEntity;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface TagRepository {

    List<TagEntity> save(List<TagEntity> tags);

    List<TagEntity> getOrCreate(Set<String> tagNames);

    List<TagEntity> findByPostId(PostEntity entity);

    Map<Long, List<TagEntity>> findByPostIds(List<Long> postIds);

    void batchUpdateByPostId(Long postId, List<TagEntity> tags);
}
