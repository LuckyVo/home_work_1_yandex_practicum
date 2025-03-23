package ru.myblog.testData;

import lombok.experimental.UtilityClass;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import ru.myblog.model.entity.CommentEntity;
import ru.myblog.model.entity.PostEntity;
import ru.myblog.model.entity.TagEntity;
import ru.myblog.model.request.CommentRequest;
import ru.myblog.model.request.PostRequest;

import java.nio.charset.StandardCharsets;
import java.util.List;

@UtilityClass
public class TestDataUtils {


    public static CommentEntity getCommentEntity(){
        return CommentEntity.builder()
                .id(1L)
                .text("text")
                .postId(1L)
                .build();
    }

    public static CommentRequest getCommentRequest(){
        return new CommentRequest("text2");
    }

    public static TagEntity getTagEntity(){
        return TagEntity.builder()
                .id(1L)
                .title("title")
                .postId(1L)
                .build();
    }

    public static PostEntity getPostEntity(){
        return PostEntity.builder()
                .id(1L)
                .text("text")
                .title("title")
                .likesCount(1)
                .imagePath("string".getBytes(StandardCharsets.UTF_8))
                .comment(List.of(getCommentEntity()))
                .tag(List.of(getTagEntity()))
                .build();
    }

    public static PostRequest getPostRequest(){
        MultipartFile image = new MockMultipartFile("string", "string".getBytes());
        return new PostRequest(
                "title2",
                "text2",
                "tag",
                image
        );
    }
}
