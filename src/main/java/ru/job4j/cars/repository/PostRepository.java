package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Post;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
@Slf4j
public class PostRepository {

    private static final String LAST_DAY = """
            FROM Post
            WHERE created > :tCreated
            """;

    private static final String WITH_PHOTO = """
            FROM Post
            WHERE photo IS NOT NULL
            """;

    private static final String SPECIFIC_BRAND = """
            FROM Post AS p
            JOIN fetch p.car
            WHERE car.name LIKE :tBrand
            """;

    private final CrudRepository crudRepository;

    /**
     * Сохранить в базе.
     *
     * @param post объявление.
     * @return Optional с объявлением с id.
     * @throws Optional пустой.
     */

    public Optional<Post> createPost(Post post) {
        Optional<Post> postOptional = Optional.empty();
        try {
            crudRepository.run(session -> session.persist(post));
            postOptional = Optional.of(post);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return postOptional;
    }

    public List<Post> findPostLastDay() {
        return crudRepository.query(LAST_DAY, Post.class, Map.of("tCreated", LocalDateTime.now().minusDays(1)));
    }

    public List<Post> findPostWithPhoto() {
        return crudRepository.query(WITH_PHOTO, Post.class);
    }

    public List<Post> findPostOfSpecificBrand(String brand) {
        return crudRepository.query(SPECIFIC_BRAND, Post.class, Map.of("tBrand", "%" + brand + "%"));
    }
}
