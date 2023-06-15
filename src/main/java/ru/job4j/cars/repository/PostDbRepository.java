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
public class PostDbRepository implements IPostRepository {

    private static final String FIND_BY_ID = """
            FROM Post p
            JOIN FETCH p.user
            WHERE p.id = :tId
            """;

    private static final String FIND_POST_WITH_PARTICIPATES = """
            FROM Post p
            JOIN FETCH p.user
            JOIN FETCH p.car
            JOIN FETCH p.participate
            WHERE p.id = :tId
            """;

    private static final String FIND_POST_WITH_PRICE_HISTORY = """
            FROM Post p
            JOIN FETCH p.priceHistories
            WHERE p.id = :tId
            """;

    private static final String FIND_ALL = """
            SELECT DISTINCT p
            FROM Post p
            JOIN FETCH p.car
            JOIN FETCH p.user
            LEFT JOIN FETCH p.participates
            ORDER BY p.id
            """;

    private static final String LAST_DAY = """
            FROM Post
            WHERE created
            BETWEEN :fFrom AND :fTo
            """;

    private static final String WITH_PHOTO = """
            FROM Post
            WHERE photo IS NOT NULL
            """;

    private static final String SPECIFIC_BRAND = """
            FROM Post p
            JOIN fetch p.car c
            WHERE c.brand = :tBrand
            """;

    private final CrudRepository crudRepository;

    /**
     * Сохранить в базе.
     *
     * @param post объявление.
     * @return Optional с объявлением с id, иначе Optional.empty().
     */
    @Override
    public Optional<Post> createPost(Post post) {
        Optional<Post> postOptional = Optional.empty();
        try {
            crudRepository.run(session -> session.persist(post));
            postOptional = Optional.of(post);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return postOptional;
    }

    /**
     * Найти в базе по идентификатору.
     * @param id идентификатор объявления.
     * @return Optional с объявлением с id, иначе Optional.empty().
     */
    @Override
    public Optional<Post> findPostById(int id) {
        Optional<Post> postOptional = Optional.empty();
        try {
            postOptional = crudRepository.optional(FIND_BY_ID, Post.class, Map.of("tId", id));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return postOptional;
    }

    /**
     * Обновить в базе пользователя.
     * @param post пользователь.
     */
    @Override
    public void update(Post post) {
        crudRepository.run(session -> session.merge(post));
    }

    /**
     * Найти объявление по идентификатору со списком подписок.
     *
     * @param id идентификатор объявления.
     * @return Optional  с объявлением, иначе Optional.empty().
     */
    @Override
    public Optional<Post> findPostWithParticipates(int id) {
        Optional<Post> postOptional = Optional.empty();
        try {
            postOptional = crudRepository.optional(FIND_POST_WITH_PARTICIPATES, Post.class, Map.of("tId", id));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return postOptional;
    }

    /**
     * Найти в базе все объявления.
     * @return Список найденых объявлений.
     */
    @Override
    public List<Post> findAll() {
        return crudRepository.query(FIND_ALL, Post.class);
    }

    /**
     * Найти в базе объявления за последний день.
     *
     * @return Список найденых объявлений.
     */
    public List<Post> findPostLastDay() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime yesterday = now.minusDays(1);
        return crudRepository.query(LAST_DAY, Post.class, Map.of("fTo", now, "fFrom", yesterday));
    }

    /**
     * Найти в базе объявления с фотографией.
     *
     * @return Список найденых объявлений.
     */

    public List<Post> findPostWithPhoto() {
        return crudRepository.query(WITH_PHOTO, Post.class);
    }

    /**
     * Найти в базе объявления по бренду автомобиля.
     *
     * @param brand Строка с названием бренда.
     * @return Список найденых объявлений.
     */

    public List<Post> findPostOfSpecificBrand(String brand) {
        return crudRepository.query(SPECIFIC_BRAND, Post.class, Map.of("tBrand", brand));
    }

    /**
     * Найти в базе объявления с историей цен.
     *
     * @param id идентификатор объявления.
     * @return Optional  с объявлением либо Optional.empty().
     */
    @Override
    public Optional<Post> findPostWithPriceHistory(int id) {
        return crudRepository.optional(FIND_POST_WITH_PRICE_HISTORY, Post.class, Map.of("tId", id));
    }

    /**
     * Удалить объявление.
     *
     * @param post объявление к удалению.
     * @return boolean true если удаление прошло успешно.
     */
    @Override
    public boolean deleteFromPost(Post post) {
        boolean rsl = false;
        try {
            crudRepository.run(session -> session.delete(post));
            rsl = true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return rsl;
    }
}
