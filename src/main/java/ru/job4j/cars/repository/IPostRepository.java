package ru.job4j.cars.repository;

import ru.job4j.cars.model.Post;

import java.util.List;
import java.util.Optional;

public interface IPostRepository {

    Optional<Post> createPost(Post post);

    Optional<Post> findPostById(int id);

    void update(Post post);

    List<Post> findAll();

    boolean deleteFromPost(Post post);

    Optional<Post> findPostWithParticipates(int id);

    Optional<Post> findPostWithPriceHistory(int id);
}
