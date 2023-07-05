package ru.job4j.cars.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.job4j.cars.model.CarPhoto;
import ru.job4j.cars.model.Post;
import ru.job4j.cars.service.PostService;

import java.util.Optional;

@Controller
@AllArgsConstructor
@Slf4j
public class PhotoController {

    private final PostService postService;

    @GetMapping("/photoCar/{postId}")
    public ResponseEntity<Resource> downloadCarPhoto(@PathVariable("postId") int postId) {
        Optional<Post> postById = postService.findPostById(postId);
        Post post = postById.get();
        CarPhoto carPhoto = post.getCarPhoto();
        return ResponseEntity.ok()
                .headers(new HttpHeaders())
                .contentLength(carPhoto.getPhoto().length)
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(new ByteArrayResource(carPhoto.getPhoto()));
    }
}
