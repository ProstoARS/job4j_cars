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
                "delete from User where id = :fId",
                Map.of("fId", userId)
        );
    }

    /**
     * Список пользователь отсортированных по id.
     * @return список пользователей.
     */
    public List<User> findAllUsersOrderById() {
        return crudRepository.query("from User order by id asc", User.class);
    }

    /**
     * Найти пользователя по ID
     * @return Optional c пользователем или пустой.
     */
    public Optional<User> findById(int id) {
        Optional<User> optionalUser = Optional.empty();
        try {
            optionalUser = crudRepository.optional(
                    "from User where id = :fId", User.class,
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
                "from User where login like :fKey", User.class,
                Map.of("fKey", "%" + key + "%")
        );
    }

    /**
     * Найти пользователя по login.
     * @param login login.
     * @return Optional с пользователем или пустой.
     */
    public Optional<User> findByLogin(String login) {
        Optional<User> optionalUser = Optional.empty();
        try {
           optionalUser = crudRepository.optional(
                    "from User where login = :fLogin", User.class,
                    Map.of("fLogin", login)
            );
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return optionalUser;
    }

    public CrudRepository getCrudRepository() {
        return this.crudRepository;
    }
}
