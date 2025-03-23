package ru.myblog.test.mapper;

import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.myblog.configuration.ContextTestConfig;
import ru.myblog.mapper.PostMapper;
import ru.myblog.testData.TestDataUtils;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringJUnitConfig(classes = {ContextTestConfig.class})
public class PostMapperTest {


    @Autowired
    private PostMapper postMapper;

    @Test
    void fromEntityToDtoTest() {
        val res = postMapper.fromEntityToDto(TestDataUtils.getPostEntity());
        Assertions.assertAll(
                () -> assertEquals(TestDataUtils.getPostEntity().getId(), res.getId()),
                () -> assertEquals(TestDataUtils.getPostEntity().getText(), res.getText()),
                () -> assertEquals(TestDataUtils.getPostEntity().getTitle(), res.getTitle()),
                () -> assertEquals(TestDataUtils.getPostEntity().getLikesCount(), res.getLikesCount()),
                () -> assertArrayEquals(TestDataUtils.getPostEntity().getImagePath(), res.getImagePath())
        );
    }

    @SneakyThrows
    @Test
    void fromRequestToEntityTest() {
        val res = postMapper.fromRequestToEntity(TestDataUtils.getPostRequest());
        Assertions.assertAll(
                () -> assertEquals(TestDataUtils.getPostRequest().text(), res.getText()),
                () -> assertEquals(TestDataUtils.getPostRequest().title(), res.getTitle()),
                () -> assertArrayEquals(TestDataUtils.getPostRequest().image().getBytes(), res.getImagePath())
        );
    }

    @SneakyThrows
    @Test
    void fromRequestToEntityWithTwoParamsTest() {
        val res = postMapper.fromRequestToEntity(TestDataUtils.getPostEntity(), TestDataUtils.getPostRequest());
        Assertions.assertAll(
                () -> assertEquals(TestDataUtils.getPostEntity().getId(), res.getId()),
                () -> assertEquals(TestDataUtils.getPostRequest().text(), res.getText()),
                () -> assertEquals(TestDataUtils.getPostRequest().title(), res.getTitle()),
                () -> assertEquals(TestDataUtils.getPostEntity().getLikesCount(), res.getLikesCount()),
                () -> assertArrayEquals(TestDataUtils.getPostRequest().image().getBytes(), res.getImagePath()),
                () -> assertEquals(TestDataUtils.getPostEntity().getComment(), res.getComment()),
                () -> assertEquals(TestDataUtils.getPostEntity().getTag(), res.getTag())
        );
    }


}
