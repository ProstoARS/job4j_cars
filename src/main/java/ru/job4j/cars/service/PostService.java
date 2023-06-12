package ru.job4j.cars.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
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

    public boolean deleteFromPost(Post post) {
        return postRepository.deleteFromPost(post);
    }

    public List<Post> findPostOfSpecificBrand(String brand) {
        return postRepository.findPostOfSpecificBrand(brand);
    }

    public Optional<Post> findPostWithPriceHistory(Post post) {
        Optional<Post> optionalPost;
        try {
            optionalPost = postRepository.findPostWithPriceHistory(post.getId());
        } catch (Exception e) {
            log.error("Цены раньше не было", e);
            optionalPost = Optional.of(post);
        }
        return optionalPost;
    }

    public Optional<Post> findPostWithParticipates(int id) {
        return postRepository.findPostWithParticipates(id);
    }

    public void changePostTimeZone(Post post, ZoneId zoneId) {
        LocalDateTime createdWithTimeZone = post.getCreatedPost().atZone(ZoneId.of(defaultTimeZone))
                .withZoneSameInstant(zoneId).toLocalDateTime();
        post.setCreatedPost(createdWithTimeZone);
    }
}
