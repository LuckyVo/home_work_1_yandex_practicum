package ru.myblog.service;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.myblog.dto.PostDto;
import ru.myblog.mapper.CommentMapper;
import ru.myblog.mapper.PostMapper;
import ru.myblog.mapper.TagMapper;
import ru.myblog.model.entity.CommentEntity;
import ru.myblog.model.entity.PostEntity;
import ru.myblog.model.entity.TagEntity;
import ru.myblog.model.request.CommentRequest;
import ru.myblog.model.request.PostRequest;
import ru.myblog.repository.CommentRepository;
import ru.myblog.repository.PostRepository;
import ru.myblog.repository.TagRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final TagRepository tagRepository;
    private final PostMapper postMapper;
    private final CommentMapper commentMapper;
    private final TagMapper tagMapper;

    public PostDto findById(Long id) {
        var entity = postRepository.findById(id);
        entity.setComment(commentRepository.findByPostId(entity));
        entity.setTag(tagRepository.findByPostId(entity));
        return postMapper.fromEntityToDto(entity);
    }


    public PostDto save(PostRequest request) throws Exception {
        val entity = postMapper.fromRequestToEntity(request);
        val tagsFromRequest = Arrays.stream(request.tags().split(","))
                .map(String::trim)
                .map(String::toLowerCase)
                .collect(Collectors.toSet());
        val tagEntity = tagRepository.getOrCreate(tagsFromRequest);
        entity.setTag(tagEntity);
        val id = Long.valueOf(postRepository.save(entity));
        tagRepository.batchUpdateByPostId(id, tagEntity);
        return findById(id);
    }


    public void deletePost(Long id) {
        val entity = postRepository.findById(id);
        postRepository.delete(entity);
    }


    public PostDto addLike(Long id, Boolean like) {
        var entity = postRepository.findById(id);
        var likesNow = entity.getLikesCount();
        if (like) {
            likesNow++;
        } else if (!likesNow.equals(0)) {
            likesNow--;
        }
        entity.setLikesCount(likesNow);
        val idAfterChange = Long.valueOf(postRepository.save(entity));
        return findById(idAfterChange);
    }


    public PostDto editPost(Long id, PostRequest request) throws Exception {
        val entity = postRepository.findById(id);
        val tagsFromRequest = Arrays.stream(request.tags().split(","))
                .map(String::trim)
                .map(String::toLowerCase)
                .collect(Collectors.toSet());
        val tagEntity = tagRepository.getOrCreate(tagsFromRequest);
        val newEntity = postMapper.fromRequestToEntity(entity, request);
        newEntity.setTag(tagEntity);
        val idAfterChange = Long.valueOf(postRepository.update(newEntity));
        return findById(idAfterChange);
    }


    public Page<PostDto> findAllPosts(Pageable pageable) {
        val posts = postRepository.findAll(pageable);
        val postIds = posts
                .stream()
                .map(PostEntity::getId)
                .toList();
        Map<Long, List<TagEntity>> tags = tagRepository.findByPostIds(postIds);
        Map<Long, List<CommentEntity>> comments = commentRepository.findByPostIds(postIds);
        posts
                .stream()
                .forEach(post -> {
                    post.getTag().addAll(tags.getOrDefault(post.getId(), Collections.emptyList()));
                    post.getComment().addAll(comments.getOrDefault(post.getId(), Collections.emptyList()));
                });
        return posts.map(postMapper::fromEntityToDto);
    }


    public PostDto addComment(Long id, CommentRequest request) {
        var entity = postRepository.findById(id);
        var comment = commentMapper.fromRequestToEntity(new CommentEntity(), request);
        comment.setPostId(id);
        commentRepository.save(comment);
        entity.getComment().add(comment);
        val idAfterChange = Long.valueOf(postRepository.save(entity));
        return findById(idAfterChange);
    }


    public PostDto editComment(Long id, Long commentId, CommentRequest request) {
        val comment = commentRepository.findById(commentId).stream().findFirst().orElseGet(CommentEntity::new);
        val commentAfterExchange = commentMapper.fromRequestToEntity(comment, request);
        commentRepository.update(commentAfterExchange);
        return findById(id);
    }

    public PostDto deleteComment(Long id, Long commentId) {
        val comment = commentRepository.findById(commentId).stream().findFirst().orElseThrow();
        commentRepository.delete(comment);
        return findById(id);
    }


}
