package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.User;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
@Slf4j
public class UserRepository {

    private static final String DELETE_USER_BY_ID = """
            DELETE FROM User
            WHERE id = :fId
            """;
    private static final String FIND_USER_BY_LIKE_LOGIN = """
            FROM User
            WHERE login LIKE :fKey
            """;
    private static final String FIND_USER_BY_LOGIN_AND_PASSWORD = """
            FROM User
            WHERE login = :fLogin
            AND password= :fPassword
            """;

    private static final String FIND_ALL_USERS = """
            FROM User
            ORDER BY id asc
            """;

    private static final String FIND_USER_BY_ID = """
            FROM User u
            WHERE u.id = :fId
            """;

    private static final String FIND_USER_WITH_PARTICIPATES = """
            FROM User u
            JOIN FETCH u.posts
            WHERE u.id = :fId
            """;

//    private static final String DELETE_PARTICIPATE_FROM_USER = """
//
//            """;
    private final CrudRepository crudRepository;


    /**
     * Сохранить в базе.
     * @param user пользователь.
     * @return Optional с пользователем с id или пустой.
     */
    public Optional<User> create(User user) {
        Optional<User> userOptional = Optional.empty();
        try {
            crudRepository.run(session -> session.persist(user));
           userOptional = Optional.of(user);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return userOptional;
    }

    /**
     * Обновить в базе пользователя.
     * @param user пользователь.
     */
    public void update(User user) {
        crudRepository.run(session -> session.merge(user));
    }

    /**
     * Удалить пользователя по id.
     * @param userId ID
     */
    public void delete(int userId) {
        crudRepository.run(
                DELETE_USER_BY_ID,
                Map.of("fId", userId)
        );
    }

    /**
     * Список пользователь отсортированных по id.
     * @return список пользователей.
     */
    public List<User> findAllUsersOrderById() {
        return crudRepository.query(FIND_ALL_USERS, User.class);
    }

    /**
     * Найти пользователя по ID
     * @return Optional c пользователем или пустой.
     */
    public Optional<User> findById(int id) {
        Optional<User> optionalUser = Optional.empty();
        try {
            optionalUser = crudRepository.optional(
                    FIND_USER_BY_ID, User.class,
                    Map.of("fId", id)
            );
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
         return optionalUser;
    }

    /**
     * Список пользователей по login LIKE %key%
     * @param key key
     * @return список пользователей.
     */
    public List<User> findByLikeLogin(String key) {
        return crudRepository.query(
                FIND_USER_BY_LIKE_LOGIN, User.class,
                Map.of("fKey", "%" + key + "%")
        );
    }

    /**
     * Найти пользователя по логину и паролю.
     * @param login login.
     * @param password password.
     * @return Optional с пользователем или пустой.
     */
    public Optional<User> findByLoginAndPassword(String login, String password) {
        Optional<User> optionalUser = Optional.empty();
        try {
           optionalUser = crudRepository.optional(
                   FIND_USER_BY_LOGIN_AND_PASSWORD, User.class,
                    Map.of("fLogin", login, "fPassword", password)
            );
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return optionalUser;
    }

    /**
     * Найти пользователя с подписками.
     * @param userId идентификатор пользователя.
     * @return Optional с пользователем или пустой.
     */
    public Optional<User> findUserWithParticipates(int userId) {
        return crudRepository.optional(
                FIND_USER_WITH_PARTICIPATES, User.class,
                Map.of("fId", userId));
    }

    public CrudRepository getCrudRepository() {
        return this.crudRepository;
    }
}
