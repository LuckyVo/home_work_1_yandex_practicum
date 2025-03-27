package ru.myblog.test.mapper;

import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.myblog.AbstractTest;
import ru.myblog.mapper.CommentMapper;
import ru.myblog.testData.TestDataUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class CommentMapperTest extends AbstractTest {

    @Autowired
    private CommentMapper commentMapper;

    @Test
    void fromRequestToEntityTest() {
        val res = commentMapper.fromRequestToEntity(TestDataUtils.getCommentEntity(), TestDataUtils.getCommentRequest());
        Assertions.assertAll(
                () -> assertEquals(TestDataUtils.getCommentEntity().getId(), res.getId()),
                () -> assertEquals(TestDataUtils.getCommentRequest().text(), res.getText()),
                () -> assertEquals(TestDataUtils.getCommentEntity().getPostId(), res.getPostId())
        );
    }
}
