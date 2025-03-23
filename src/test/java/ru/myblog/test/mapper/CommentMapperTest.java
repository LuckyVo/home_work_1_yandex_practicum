package ru.myblog.test.mapper;

import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.myblog.configuration.ContextTestConfig;
import ru.myblog.mapper.CommentMapper;
import ru.myblog.testData.TestDataUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringJUnitConfig(classes = {ContextTestConfig.class})
public class CommentMapperTest {

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
