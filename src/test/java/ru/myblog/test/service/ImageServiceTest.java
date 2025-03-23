package ru.myblog.test.service;

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
import ru.myblog.service.ImageService;
import ru.myblog.testData.TestDataUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.mockito.ArgumentMatchers.any;

@SpringJUnitConfig(classes = {ContextTestConfig.class})
public class ImageServiceTest {

    @Autowired
    private ImageService imageService;
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
    void getImageTest() {
        Mockito.when(postRepository.findById(1L)).thenReturn(TestDataUtils.getPostEntity());
        Mockito.when(tagRepository.findByPostId(any())).thenReturn(List.of(TestDataUtils.getTagEntity()));
        Mockito.when(commentRepository.findByPostId(any())).thenReturn(List.of(TestDataUtils.getCommentEntity()));
        val res = imageService.getImage(1L);
        Assertions.assertAll(
                () -> assertArrayEquals(TestDataUtils.getPostEntity().getImagePath(), res)
        );
    }
}
