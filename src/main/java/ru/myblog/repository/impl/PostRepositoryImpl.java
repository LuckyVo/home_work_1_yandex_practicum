package ru.myblog.repository.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.myblog.model.entity.PostEntity;
import ru.myblog.repository.PostRepository;
import ru.myblog.utils.SqlUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepository {

    private final JdbcTemplate template;

    private static final String SAVE =
            """
                    INSERT INTO post (title, text, imagePath) VALUES (?, ?, ?)
                    """;
    private static final String COUNT_POSTS =
            "SELECT COUNT(DISTINCT p.id) FROM post p";
    private static final String FIND_ALL =
            """
                    SELECT DISTINCT p.*
                    FROM post p
                    LEFT JOIN posts_tags pt ON p.id = pt.post_id
                    LEFT JOIN tag t ON pt.tag_id = t.id
                    LIMIT ? OFFSET ?
                    """;

    private static final String UPDATE_QUERY =
            """ 
                    UPDATE post SET 
                      title = ?,
                      text = ?,
                      imagePath = ?,
                      likesCount = ?,
                      comment_unique_id = ?,
                      tag_unique_id = ?
                       WHERE id = ?
                    """;
    private static final String FIND_BY_ID =
            """
                    SELECT p.*
                    FROM post p
                    WHERE p.id = ?
                    """;

    private static final String DELETE =
            "DELETE FROM POST P WHERE P.id = ?";


    @Override
    public PostEntity findById(Long id) {
        return template.queryForObject(
                FIND_BY_ID,
                this::mapToPost,
                id
        );
    }

    @Override
    @Transactional
    public int save(PostEntity entity) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        template.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(SAVE, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, entity.getTitle());
            ps.setString(2, entity.getText());
            ps.setBytes(3, entity.getImagePath());
            return ps;
        }, keyHolder);
        return (int) keyHolder.getKey().intValue();
    }


    @Override
    public int update(PostEntity entity) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        template.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(UPDATE_QUERY, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, entity.getTitle());
            ps.setString(2, entity.getText());
            ps.setBytes(3, entity.getImagePath());
            ps.setInt(4, entity.getLikesCount());
            ps.setObject(5, entity.getComment());
            ps.setObject(6, entity.getTag());
            ps.setLong(7, entity.getId());
            return ps;
        }, keyHolder);
        return (int) keyHolder.getKey().intValue();
    }

    @Override
    @Transactional
    public void delete(PostEntity entity) {
        template.update(DELETE, entity.getId());
    }

    @Override
    public Page<PostEntity> findAll(Pageable pageable) {
        int limit = pageable.getPageSize();
        int offset = pageable.getPageNumber() * limit;

        List<PostEntity> posts = template.query(
                FIND_ALL,
                this::mapToPost,
                limit,
                offset
        );
        Integer size;
        try {
            size = template.queryForObject(COUNT_POSTS, Integer.class);
        } catch (EmptyResultDataAccessException exp) {
            size = 0;
        }
        return new PageImpl<>(posts, pageable, size);
    }


    PostEntity mapToPost(ResultSet rs, int rowNumber) throws SQLException {
        return new PostEntity(
                SqlUtils.getLong(rs, "id"),
                SqlUtils.getStringOrElseEmpty(rs, "title"),
                SqlUtils.getStringOrElseEmpty(rs, "text"),
                rs.getBytes("image"),
                SqlUtils.getInteger(rs, "likesCount")
        );
    }
}
