package ru.job4j.cars.repository;

import config.TestHibernateConfig;
import org.hibernate.Session;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.cars.model.User;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

class UserRepositoryTest {

    private static UserRepository userRepository;

    @BeforeAll
    public static void initHibernateTest() {
        CrudRepository crudRepository = new CrudRepository(new TestHibernateConfig().sf());
        userRepository = new UserRepository(crudRepository);
    }

    @AfterEach
    public void wipeTable() {
        Session session = userRepository.getCrudRepository().getSf().openSession();
        try {
            session.beginTransaction();
            session.createQuery("DELETE FROM User")
                    .executeUpdate();
            session.getTransaction().commit();
            session.close();
        } catch (Exception e) {
            session.getTransaction().rollback();
        }
    }

    @Test
    public void whenCreateUserAndFindById() throws SQLException {
        User user = User.builder()
                .login("User1")
                .password("12")
                .build();
        Optional<User> optionalUser = userRepository.create(user);
        int userId;
        if (optionalUser.isPresent()) {
            userId = optionalUser.get().getId();
        } else {
            throw new SQLException();
        }
        assertThat(userRepository.findById(userId).get().getLogin()).isEqualTo(user.getLogin());
    }

    @Test
    void whenUpdateUser() throws SQLException {
        User user = User.builder()
                .login("User1")
                .password("12")
                .build();
        Optional<User> optionalUser = userRepository.create(user);
        user.setPassword("13");
        int userId;
        if (optionalUser.isPresent()) {
            userId = optionalUser.get().getId();
        } else {
            throw new SQLException();
        }
        userRepository.update(user);
        assertThat(userRepository.findById(userId).get().getPassword()).isEqualTo(user.getPassword());
    }

    @Test
    void whereDeleteUser() throws SQLException {
        User user = User.builder()
                .login("User1")
                .password("12")
                .build();
        Optional<User> optionalUser = userRepository.create(user);
        int userId;
        if (optionalUser.isPresent()) {
            userId = optionalUser.get().getId();
        } else {
            throw new SQLException();
        }
        userRepository.delete(userId);
        assertThat(userRepository.findById(userId).isEmpty()).isTrue();
    }

    @Test
    void whenFindAllUsersOrderById() {
        User user1 = User.builder()
                .login("User1")
                .password("12")
                .build();
        User user2 = User.builder()
                .login("User2")
                .password("13")
                .build();
        userRepository.create(user1);
        userRepository.create(user2);
        List<User> users = List.of(user1, user2);
        assertThat(userRepository.findAllUsersOrderById()).isEqualTo(users);
    }

    @Test
    void whenFindByLikeLogin() {
        User user1 = User.builder()
                .login("User1")
                .password("12")
                .build();
        User user2 = User.builder()
                .login("User2")
                .password("13")
                .build();
        User user3 = User.builder()
                .login("Peter1")
                .password("14")
                .build();
        userRepository.create(user1);
        userRepository.create(user2);
        userRepository.create(user3);
        List<User> users = List.of(user1, user2);
        assertThat(userRepository.findByLikeLogin("ser")).isEqualTo(users);
    }

    @Test
    void whenFindByLogin() {
        User user1 = User.builder()
                .login("User2")
                .password("13")
                .build();
        User user2 = User.builder()
                .login("Peter1")
                .password("14")
                .build();
        userRepository.create(user1);
        userRepository.create(user2);
        assertThat(userRepository.findByLogin("Peter1").get()).isEqualTo(user2);
    }
}