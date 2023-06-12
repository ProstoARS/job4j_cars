package ru.job4j.cars.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.job4j.cars.model.Post;
import ru.job4j.cars.model.User;
import ru.job4j.cars.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    public Optional<User> create(User user) {
        return userRepository.create(user);
    }

    public void update(User user) {
        userRepository.update(user);
    }

    public void delete(int userId) {
        userRepository.delete(userId);
    }

    public List<User> findAllUsersOrderById() {
        return userRepository.findAllUsersOrderById();
    }

    public Optional<User> findById(int id) {
        return userRepository.findById(id);
    }

    public List<User> findByLikeLogin(String key) {
        return userRepository.findByLikeLogin(key);
    }

    public Optional<User> findByLoginAndPassword(String login, String password) {
        return userRepository.findByLoginAndPassword(login, password);
    }

    public Optional<User> findUserWithParticipates(User user) {
        Optional<User> optionalUser;
        try {
            optionalUser = userRepository.findUserWithParticipates(user.getId());
        } catch (Exception e) {
            log.error("Подписок ещё нет", e);
            user.setPosts(new ArrayList<>());
            return Optional.of(user);
        }
        return optionalUser;
    }

    public boolean addParticipate(User user, Post post) {
        boolean rsl = false;
        try {
            user.getPosts().add(post);
            post.getParticipates().add(user);
            rsl = true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return !rsl;
    }

    public boolean delParticipate(User user, Post post) {
        boolean rsl = false;
        try {
            user.getPosts().remove(post);
            post.getParticipates().remove(user);
            rsl = true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return !rsl;
    }
}
