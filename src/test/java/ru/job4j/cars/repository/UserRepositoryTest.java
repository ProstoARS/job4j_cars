package ru.job4j.cars.repository;

import config.TestHibernateConfig;
import org.hibernate.Session;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.cars.model.Post;
import ru.job4j.cars.model.User;
import ru.job4j.cars.service.UserService;

import java.sql.SQLException;
import java.util.*;

import static org.assertj.core.api.Assertions.*;

class UserRepositoryTest {

    private static CrudRepository crudRepository;

    @BeforeAll
    public static void initHibernateTest() {
        crudRepository = new CrudRepository(new TestHibernateConfig().sf());
    }

    @AfterEach
    public void wipeTable() {
        Session session = crudRepository.getSf().openSession();
        try {
            session.beginTransaction();
            session.createQuery("DELETE FROM Post")
                    .executeUpdate();
            session.createQuery("DELETE FROM User")
                    .executeUpdate();
            session.createQuery("DELETE FROM Car")
                    .executeUpdate();
            session.createQuery("DELETE FROM Engine")
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
                .login("User17")
                .password("12")
                .build();
        IUserRepository userRepository = new UserDbRepository(crudRepository);
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
        IUserRepository userRepository = new UserDbRepository(crudRepository);
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
        IUserRepository userRepository = new UserDbRepository(crudRepository);
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
        IUserRepository userRepository = new UserDbRepository(crudRepository);
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
        UserDbRepository userRepository = new UserDbRepository(crudRepository);
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
        IUserRepository userRepository = new UserDbRepository(crudRepository);
        userRepository.create(user1);
        userRepository.create(user2);
        assertThat(userRepository.findByLoginAndPassword("Peter1", "14").get()).isEqualTo(user2);
    }

    @Test
    void whenUpdateUserUseParticipate() {
        User user1 = User.builder()
                .login("User1")
                .password("13")
                .build();
        User user2 = User.builder()
                .login("User2")
                .password("14")
                .posts(new HashSet<>())
                .build();
        UserDbRepository userRepository = new UserDbRepository(crudRepository);
        userRepository.create(user1);
        userRepository.create(user2);
        Post post = Post.builder()
                .user(user1)
                .participates(new HashSet<>())
                .build();
        Post post2 = Post.builder()
                .description("второй пост")
                .user(user1)
                .participates(new HashSet<>())
                .build();
        PostDbRepository postRepository = new PostDbRepository(crudRepository);
        postRepository.createPost(post);
        postRepository.createPost(post2);

        UserService userService = new UserService(userRepository);
        userService.addParticipate(user2, post);
        userRepository.update(user2);
        Optional<User> participatesByUser1 = userRepository.findUserWithParticipates(user2.getId());
        User ars = participatesByUser1.get();

        userService.addParticipate(ars, post2);

        userRepository.update(ars);

        Optional<User> participatesByUser2 = userRepository.findUserWithParticipates(ars.getId());
        Iterator<Post> iterator = participatesByUser2.get().getPosts().stream().iterator();
        assertThat(iterator.next()).isEqualTo(post);
        assertThat(iterator.next()).isEqualTo(post2);
    }
}