package ru.myblog.repository.impl;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import ru.myblog.model.entity.PostEntity;
import ru.myblog.model.entity.TagEntity;
import ru.myblog.repository.TagRepository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class TagRepositoryImpl implements TagRepository {

    private final JdbcTemplate template;

    private static final String SAVE =
            """ 
                    INSERT INTO tag (title) VALUES (?)
                    """;


    private static final String FIND_BY_POST_IDS =
            """
                    SELECT t.*, pt.post_id as post_id
                    FROM tag t
                    JOIN posts_tags pt ON pt.tag_id = t.id
                    WHERE pt.post_id IN (%s)
                    """;

    private static final String SAVE_POST_TAGS = """
            INSERT INTO posts_tags (post_id, tag_id) VALUES (?, ?)
            """;

    private static final String FIND_BY_NAME_QUERY = """
            SELECT t.*
            FROM tag t
            WHERE LOWER(t.name) IN (%s)
            """;

    @Override
    public List<TagEntity> save(List<TagEntity> tags) {
        for (TagEntity tag : tags) {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            try {
                template.update(
                        connection -> {
                            PreparedStatement ps = connection.prepareStatement(SAVE,
                                    Statement.RETURN_GENERATED_KEYS);
                            ps.setString(1, tag.getTitle());
                            return ps;
                        },
                        keyHolder
                );
                Long tagId = (long) Objects.requireNonNull(keyHolder.getKeys()).get("id");
                tag.setId(tagId);
            } catch (EmptyResultDataAccessException exp) {
                return null;
            }
        }
        return tags;
    }


    @Override
    @Transactional
    public List<TagEntity> getOrCreate(Set<String> tagNames) {
        List<TagEntity> existedTags = this.findByNameInIgnoreCase(tagNames);
        Set<String> existedTagNames =
                existedTags
                        .stream()
                        .map(TagEntity::getTitle)
                        .collect(Collectors.toSet());
        List<TagEntity> newTags = new ArrayList<>();
        tagNames.forEach(t -> {
            if (!existedTagNames.contains(t)) {
                val tag = new TagEntity().toBuilder()
                        .title(t)
                        .build();
                newTags.add(tag);
            }
        });
        this.save(newTags);
        newTags.addAll(existedTags);
        return newTags;
    }


    @Override
    public List<TagEntity> findByPostId(PostEntity entity) {
        return template.query(
                String.format(FIND_BY_POST_IDS, entity.getId()),
                this::mapToTagWithPostId
        );
    }


    @Override
    public Map<Long, List<TagEntity>> findByPostIds(List<Long> postIds) {
        String inSql = String.join(",", Collections.nCopies(postIds.size(), "?"));
        val tags = template.query(
                String.format(FIND_BY_POST_IDS, inSql),
                this::mapToTagWithPostId,
                postIds.toArray());
        return tags
                .stream()
                .collect(Collectors.groupingBy(TagEntity::getPostId));
    }

    @Override
    public void batchUpdateByPostId(Long postId, List<TagEntity> tags) {
        if (!CollectionUtils.isEmpty(tags)) {
            val tagIds =
                    tags
                            .stream()
                            .map(TagEntity::getId)
                            .toList();
            var ids = new ArrayList<>(tagIds)
                    .stream()
                    .filter(Objects::nonNull)
                    .toList();
            int i = 1;
            while (i != tagIds.size() / 100 + 2) {
                List<Long> finalIds = ids;
                if (!CollectionUtils.isEmpty(finalIds)) {
                    template.batchUpdate(
                            SAVE_POST_TAGS,
                            new BatchPreparedStatementSetter() {
                                public void setValues(PreparedStatement ps, int i) throws SQLException {
                                    ps.setLong(1, postId);
                                    ps.setLong(2, finalIds.get(i));
                                }

                                public int getBatchSize() {
                                    return finalIds.size();
                                }
                            });
                }
                if (ids.size() > 100) {
                    ids = ids.subList(0, 100);
                }
                i++;
            }
        }
    }

    private List<TagEntity> findByNameInIgnoreCase(Set<String> tagNames) {
        String inSql = String.join(",", Collections.nCopies(tagNames.size(), "?"));
        List<TagEntity> tags = template.query(
                String.format(FIND_BY_NAME_QUERY, inSql),
                this::mapToTag,
                tagNames.toArray()
        );
        return tags.stream().filter(Objects::nonNull).collect(Collectors.toList());
    }


    private TagEntity mapToTagWithPostId(ResultSet rs, int rowNumber) throws SQLException {
        return TagEntity.builder()
                .id(rs.getLong("id"))
                .postId(rs.getLong("post_id"))
                .title(rs.getString("title"))
                .build();

    }

    private TagEntity mapToTag(ResultSet rs, int rowNumber) throws SQLException {
        if (rs.getString("title") == null) {
            return null;
        }
        return new TagEntity(
                rs.getLong("id"),
                rs.getString("title")
        );
    }
}
