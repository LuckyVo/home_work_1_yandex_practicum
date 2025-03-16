package ru.myblog.model.request;

import org.springframework.web.multipart.MultipartFile;

public record PostRequest(String title, String text, String tags, MultipartFile image) {
}
