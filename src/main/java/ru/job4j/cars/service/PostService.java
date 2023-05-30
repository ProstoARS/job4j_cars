package ru.job4j.cars.service;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import ru.job4j.cars.model.Post;
import ru.job4j.cars.repository.PostRepository;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@PropertySource("classpath:application.properties")
public class PostService {

    private final PostRepository postRepository;
    @Value("${defaultTimeZone}")
    private String defaultTimeZone;

    public Optional<Post> createPost(Post post) {
        return postRepository.createPost(post);
    }

    public Optional<Post> findPostById(int id) {
        return postRepository.findPostById(id);
    }

    public List<Post> findAll(ZoneId zoneId) {
        return postRepository.findAll()
                .stream()
                .peek(post -> changePostTimeZone(post, zoneId))
                .collect(Collectors.toList());
    }

    public List<Post> findPostLastDay() {
        return postRepository.findPostLastDay();
    }

    public List<Post> findPostWithPhoto() {
        return postRepository.findPostWithPhoto();
    }

    public List<Post> findPostOfSpecificBrand(String brand) {
        return postRepository.findPostOfSpecificBrand(brand);
    }

    public Post changePostTimeZone(Post post, ZoneId zoneId) {
        LocalDateTime createdWithTimeZone = post.getCreatedPost().atZone(ZoneId.of(defaultTimeZone))
                .withZoneSameInstant(zoneId).toLocalDateTime();
        post.setCreatedPost(createdWithTimeZone);
        return post;
    }
}
