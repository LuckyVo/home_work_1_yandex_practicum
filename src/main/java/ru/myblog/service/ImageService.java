package ru.myblog.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final PostService postService;

    public byte[] getImage(Long id) {
        return postService.findById(id).getImagePath();
    }
}
