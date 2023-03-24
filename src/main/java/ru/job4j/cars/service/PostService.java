package ru.job4j.cars.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.cars.model.Post;
import ru.job4j.cars.repository.PostRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public Optional<Post> createPost(Post post) {
        return postRepository.createPost(post);
    }

    public Optional<Post> findPostById(int id) {
        return postRepository.findPostById(id);
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
}
