package ru.myblog.repository.impl;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.myblog.model.entity.CommentEntity;
import ru.myblog.model.entity.PostEntity;
import ru.myblog.repository.CommentRepository;
import ru.myblog.utils.SqlUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepository {

    private static final String SAVE =
            "INSERT INTO comment (id, text) VALUES (?, ?)";
    private static final String DELETE =
            "DELETE FROM comment WHERE id = ?";
    private static final String FIND_BY_POST_ID =
            """
                    SELECT c.*
                    FROM comment c
                    WHERE c.post_id IN (%s)
                    """;
    private static final String UPDATE_QUERY =
            """ 
                    UPDATE comment SET text = ? WHERE id = ?
                    """;


    private final JdbcTemplate template;

    @Override
    @Transactional
    public void save(CommentEntity entity) {
        template.update(SAVE, entity.getId(), entity.getText());
    }

    @Override
    @Transactional
    public void delete(CommentEntity entity) {
        template.update(DELETE, entity.getId());
    }


    @Override
    public List<CommentEntity> findByPostId(PostEntity entity) {
        return template.query(
                String.format(FIND_BY_POST_ID, entity.getId()),
                this::mapToComment
        );
    }

    @Override
    public List<CommentEntity> findById(Long id) {
        return template.query(
                String.format(FIND_BY_POST_ID, id),
                this::mapToComment
        );
    }


    @Override
    public Map<Long, List<CommentEntity>> findByPostIds(List<Long> postIds) {
        String inSql = String.join(",", Collections.nCopies(postIds.size(), "?"));
        val comments = template.query(
                String.format(FIND_BY_POST_ID, inSql),
                this::mapToComment,
                postIds.toArray());
        return comments
                .stream()
                .collect(Collectors.groupingBy(CommentEntity::getPostId));
    }

    @Override
    public int update(CommentEntity entity) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        template.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(UPDATE_QUERY, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, entity.getText());
            ps.setObject(2, entity.getId());
            return ps;
        }, keyHolder);
        return (int) keyHolder.getKey().intValue();
    }


    private CommentEntity mapToComment(ResultSet rs, int rowNumber) throws SQLException {
        return CommentEntity.builder()
                .id(SqlUtils.getLong(rs, "id"))
                .postId(SqlUtils.getLong(rs, "post_id"))
                .text(SqlUtils.getStringOrElseEmpty(rs, "text"))
                .build();
    }
}
