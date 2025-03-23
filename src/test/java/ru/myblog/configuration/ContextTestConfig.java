package ru.myblog.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.myblog.mapper.*;
import ru.myblog.repository.CommentRepository;
import ru.myblog.repository.PostRepository;
import ru.myblog.repository.TagRepository;
import ru.myblog.service.ImageService;
import ru.myblog.service.PostService;

import static org.mockito.Mockito.mock;

@Configuration
public class ContextTestConfig {

    @Bean
    public CommentMapper getCommentMapper() {
        return new CommentMapperImpl();
    }

    @Bean
    public PostMapper getPostMapper() {
        return new PostMapperImpl();
    }

    @Bean
    public TagMapper getTagmapper() {
        return new TagMapperImpl();
    }

    @Bean
    public ImageService getImageService(PostService postService) {
        return new ImageService(postService);
    }

    @Bean
    public PostService getPostService(PostRepository postRepository,
                                      CommentRepository commentRepository,
                                      TagRepository tagRepository,
                                      PostMapper postMapper,
                                      CommentMapper commentMapper,
                                      TagMapper tagMapper
    ) {
        return new PostService(
                postRepository,
                commentRepository,
                tagRepository,
                postMapper,
                commentMapper,
                tagMapper);
    }

    @Bean
    public CommentRepository getCommentRepository() {
        return mock(CommentRepository.class);
    }

    @Bean
    public TagRepository getTagRepository() {
        return mock(TagRepository.class);
    }

    @Bean
    public PostRepository getPostRepository() {
        return mock(PostRepository.class);
    }
}
