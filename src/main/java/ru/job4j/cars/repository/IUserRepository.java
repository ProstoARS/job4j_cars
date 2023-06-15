package ru.job4j.cars.repository;

import ru.job4j.cars.model.User;

import java.util.List;
import java.util.Optional;

public interface IUserRepository {

    Optional<User> create(User user);

    void update(User user);

    void delete(int userId);

    List<User> findAllUsersOrderById();

    Optional<User> findByLoginAndPassword(String login, String password);

    Optional<User> findById(int id);

    List<User> findByLikeLogin(String key);

    Optional<User> findUserWithParticipates(int userId);
}
