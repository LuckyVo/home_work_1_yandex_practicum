package ru.myblog.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.myblog.dto.PostDto;
import ru.myblog.mapper.CommentMapper;
import ru.myblog.mapper.PostMapper;
import ru.myblog.model.entity.PostEntity;
import ru.myblog.model.request.CommentRequest;
import ru.myblog.model.request.PostRequest;
import ru.myblog.repository.CommentRepository;
import ru.myblog.repository.PostRepository;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final PostMapper postMapper;
    private final CommentMapper commentMapper;

    public PostDto findById(Long id) {
        return postMapper
                .fromEntityToDto(
                        postRepository.findById(id)
                                .orElseThrow(
                                        () -> new EntityNotFoundException("Пост не найден c id: %d".formatted(id))
                                )
                );
    }

    private PostEntity findEntityById(Long id) {
        return postRepository.findById(id)
                .orElseGet(PostEntity::new);
    }

    public PostDto save(PostRequest request) {
        return postMapper
                .fromEntityToDto(
                        postRepository.save(postMapper.fromRequestToEntity(request))
                );
    }


    public void deletePost(Long id) {
        postRepository.delete(findEntityById(id));
    }


    public PostDto addLike(Long id, Boolean like) {
        var entity = findEntityById(id);
        var likesNow = entity.getLikesCount();
        if (like) {
            likesNow++;
        } else if (!likesNow.equals(0)) {
            likesNow--;
        }
        entity.setLikesCount(likesNow);
        return postMapper.fromEntityToDto(postRepository.save(entity));
    }


    public PostDto editPost(Long id, PostRequest request) {
        return postMapper
                .fromEntityToDto(
                        postRepository.save(
                                postMapper.fromRequestToEntity(
                                        findEntityById(id),
                                        request
                                )
                        )
                );
    }


    public Page<PostDto> findAllPosts(String tag, int pageNumber, int pageSize) {
        return postRepository
                .findAllByTags(tag, PageRequest.of(pageNumber, pageSize))
                .map(postMapper::fromEntityToDto);
    }


    public PostDto addComment(Long id, CommentRequest request) {
        var entity = findEntityById(id);
        entity.getCommentEntities().add(commentMapper.fromRequestToEntity(request));
        return postMapper.fromEntityToDto(postRepository.save(entity));
    }


    public PostDto editComment(Long id, Long commentId, CommentRequest request) {
        findEntityById(id)
                .getCommentEntities()
                .stream()
                .filter(Objects::nonNull)
                .filter(commentEntity -> commentEntity.getId().equals(commentId))
                .map(comment -> {
                    val commentEntity = commentMapper.fromRequestToEntity(request);
                    return commentRepository.save(commentEntity);
                })
                .findFirst()
                .orElseThrow(
                        () -> new EntityNotFoundException("Комментарий не найден c id: %d".formatted(commentId))
                );
        return postMapper.fromEntityToDto(findEntityById(id));
    }

    public PostDto deleteComment(Long id, Long commentId) {
        findById(id)
                .getCommentEntities()
                .stream()
                .filter(Objects::nonNull)
                .filter(commentEntity -> commentEntity.getId().equals(commentId))
                .forEach(commentRepository::delete);
        return postMapper.fromEntityToDto(findEntityById(id));
    }


}
