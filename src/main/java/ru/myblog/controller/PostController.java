package ru.myblog.controller;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.myblog.dto.PostDto;
import ru.myblog.model.request.CommentRequest;
import ru.myblog.model.request.PostRequest;
import ru.myblog.service.PostService;

@Controller
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;


    @GetMapping
    public String getPosts(@PageableDefault Pageable pageable, Model model) {
        Page<PostDto> posts = postService.findAllPosts(pageable);
        model.addAttribute("pageNumber", posts.getNumber());
        model.addAttribute("pageSize", posts.getSize());
        model.addAttribute("hasNext", posts.hasNext());
        model.addAttribute("hasPrevious", posts.hasPrevious());
        model.addAttribute("search", posts.getContent());
        model.addAttribute("posts", posts.getContent());

        return "posts";
    }

    @GetMapping("/{id}")
    public String getPost(@PathVariable("id") Long id, Model model) {

        val postDto = postService.findById(id);
        model.addAttribute("post", postDto);
        model.addAttribute("comment", postDto.getComment());

        return "post";
    }

    @GetMapping("/add")
    public String getPageAddPost() {
        return "add-post";
    }

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public String savePost(@ModelAttribute PostRequest request, Model model) throws Exception {
        val postDto = postService.save(request);
        model.addAttribute("post", postDto);
        model.addAttribute("comment", postDto.getComment());

        return "redirect:/posts/".concat(String.valueOf(postDto.getId()));
    }

    @PostMapping("/{id}/like")
    public String likePost(@PathVariable("id") Long id, @RequestParam("like") Boolean like, Model model) {
        val postDto = postService.addLike(id, like);
        model.addAttribute("post", postDto);
        model.addAttribute("comment", postDto.getComment());

        return "redirect:/posts/".concat(String.valueOf(postDto.getId()));
    }

    @PostMapping("/{id}/edit")
    public String getPageEditPost(@PathVariable("id") Long id, Model model) {
        val postDto = postService.findById(id);
        model.addAttribute("post", postDto);
        model.addAttribute("comment", postDto.getComment());

        return "redirect:/posts/".concat(String.valueOf(postDto.getId()));
    }

    @PostMapping(path = "/{id}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public String editPost(@PathVariable("id") Long id, @ModelAttribute PostRequest request, Model model) throws Exception {
        val postDto = postService.editPost(id, request);
        model.addAttribute("post", postDto);
        model.addAttribute("comment", postDto.getComment());

        return "redirect:/posts/".concat(String.valueOf(postDto.getId()));
    }

    @PostMapping("/{id}/comments")
    public String addComment(@PathVariable("id") Long id, @ModelAttribute CommentRequest request, Model model) {
        val postDto = postService.addComment(id, request);
        model.addAttribute("post", postDto);
        model.addAttribute("comment", postDto.getComment());

        return "redirect:/posts/".concat(String.valueOf(postDto.getId()));
    }

    @PostMapping("/{id}/comments/{commentId}")
    public String editComment(@PathVariable("id") Long id,
                              @PathVariable("commentId") Long commentId,
                              @ModelAttribute CommentRequest request,
                              Model model) {
        val postDto = postService.editComment(id, commentId, request);
        model.addAttribute("post", postDto);
        model.addAttribute("comment", postDto.getComment());

        return "redirect:/posts/".concat(String.valueOf(postDto.getId()));
    }

    @PostMapping("/{id}/comments/{commentId}/delete")
    public String deleteComment(@PathVariable("id") Long id,
                                @PathVariable("commentId") Long commentId,
                                Model model) {
        val postDto = postService.deleteComment(id, commentId);
        model.addAttribute("post", postDto);
        model.addAttribute("comment", postDto.getComment());

        return "redirect:/posts/".concat(String.valueOf(postDto.getId()));
    }

    @PostMapping("/{id}/delete")
    public String deleteComment(@PathVariable("id") Long id) {
        postService.deletePost(id);

        return "redirect:/posts";
    }

}
