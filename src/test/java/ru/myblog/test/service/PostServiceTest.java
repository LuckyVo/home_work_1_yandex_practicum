package ru.myblog.test.service;

import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.myblog.configuration.ContextTestConfig;
import ru.myblog.repository.CommentRepository;
import ru.myblog.repository.PostRepository;
import ru.myblog.repository.TagRepository;
import ru.myblog.service.PostService;
import ru.myblog.testData.TestDataUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

@SpringJUnitConfig(classes = {ContextTestConfig.class})
public class PostServiceTest {

    @Autowired
    private PostService postService;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private CommentRepository commentRepository;

    @BeforeEach
    void setUp() {
        Mockito.reset(postRepository);
        Mockito.reset(tagRepository);
        Mockito.reset(commentRepository);
    }

    @Test
    void findByIdTest() {
        Mockito.when(postRepository.findById(1L)).thenReturn(TestDataUtils.getPostEntity());
        Mockito.when(tagRepository.findByPostId(any())).thenReturn(List.of(TestDataUtils.getTagEntity()));
        Mockito.when(commentRepository.findByPostId(any())).thenReturn(List.of(TestDataUtils.getCommentEntity()));
        val res = postService.findById(1L);
        Assertions.assertAll(
                () -> assertEquals(TestDataUtils.getPostEntity().getId(), res.getId()),
                () -> assertEquals(TestDataUtils.getPostEntity().getText(), res.getText()),
                () -> assertEquals(TestDataUtils.getPostEntity().getTitle(), res.getTitle()),
                () -> assertEquals(TestDataUtils.getPostEntity().getLikesCount(), res.getLikesCount()),
                () -> assertArrayEquals(TestDataUtils.getPostRequest().image().getBytes(), res.getImagePath()),
                () -> assertEquals(TestDataUtils.getPostEntity().getComment(), res.getComment()),
                () -> assertEquals(TestDataUtils.getPostEntity().getTag(), res.getTag())
        );
    }

    @SneakyThrows
    @Test
    void saveTest() {
        Mockito.when(postRepository.save(any())).thenReturn(1);
        Mockito.when(postRepository.findById(1L)).thenReturn(TestDataUtils.getPostEntity());
        Mockito.when(tagRepository.findByPostId(any())).thenReturn(List.of(TestDataUtils.getTagEntity()));
        Mockito.when(commentRepository.findByPostId(any())).thenReturn(List.of(TestDataUtils.getCommentEntity()));
        val res = postService.save(TestDataUtils.getPostRequest());
        Assertions.assertAll(
                () -> assertEquals(TestDataUtils.getPostEntity().getId(), res.getId()),
                () -> assertEquals(TestDataUtils.getPostEntity().getText(), res.getText()),
                () -> assertEquals(TestDataUtils.getPostEntity().getTitle(), res.getTitle()),
                () -> assertArrayEquals(TestDataUtils.getPostRequest().image().getBytes(), res.getImagePath())
        );
    }


}
