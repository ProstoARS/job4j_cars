package ru.job4j.cars.repository;
import config.TestHibernateConfig;
import org.hibernate.Session;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.cars.model.*;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.*;

class PostRepositoryTest {

    private static CrudRepository crudRepository;
    private static User user;

    @BeforeAll
    public static void initHibernateTest() {
        crudRepository = new CrudRepository(new TestHibernateConfig().sf());
        user = User.builder()
                .login("User1")
                .password("12")
                .build();
        UserRepository userRepository = new UserRepository(crudRepository);
        userRepository.create(user);
    }

    @AfterEach
    public void wipeTablePost() {
        Session session = crudRepository.getSf().openSession();
        try {
            session.beginTransaction();
            session.createQuery("DELETE FROM Post")
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

    @AfterAll
    public static void wipeTableUsers() {
        Session session = crudRepository.getSf().openSession();
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
    void whenCreatePost() throws SQLException {
        Post post = Post.builder()
                .createdPost(LocalDateTime.now())
                .description("Test1")
                .user(user)
                .build();
        PostRepository postRepository = new PostRepository(crudRepository);
        Optional<Post> postOptional = postRepository.createPost(post);
        if (postOptional.isPresent()) {
            assertThat(postRepository.findPostById(postOptional.get().getId()).get().getDescription())
                    .isEqualTo(post.getDescription());
        } else {
            throw new SQLException();
        }
    }

    @Test
    void whenFindPostLastDay() {
        LocalDateTime now = LocalDateTime.now();
        Post post = Post.builder()
                .createdPost(now.minusDays(2))
                .description("Test1")
                .user(user)
                .build();
        Post post2 = Post.builder()
                .createdPost(now)
                .description("Test2")
                .user(user)
                .build();
        Post post3 = Post.builder()
                .createdPost(now)
                .description("Test2")
                .user(user)
                .build();
        PostRepository postRepository = new PostRepository(crudRepository);
        postRepository.createPost(post);
        postRepository.createPost(post2);
        postRepository.createPost(post3);
        List<Post> posts = List.of(post2, post3);
        assertThat(postRepository.findPostLastDay()).isEqualTo(posts);
    }

    @Test
    void whenFindPostWithPhoto() {
        byte[] photo = new byte[]{1, 2, 3};
        Post post = Post.builder()
                .description("Test1")
                .user(user)
                .build();
        Post post2 = Post.builder()
                .description("Test2")
                .user(user)
                .photo(photo)
                .build();
        Post post3 = Post.builder()
                .description("Test2")
                .user(user)
                .photo(photo)
                .build();
        PostRepository postRepository = new PostRepository(crudRepository);
        postRepository.createPost(post);
        postRepository.createPost(post2);
        postRepository.createPost(post3);
        List<Post> posts = List.of(post2, post3);
        assertThat(postRepository.findPostWithPhoto()).isEqualTo(posts);
    }

    @Test
    void whenFindPostOfSpecificBrand() {
        Engine engine = Engine.builder()
                .name("Uzbek")
                .build();
        Engine engine2 = Engine.builder()
                .name("Tajik")
                .build();
        EngineRepository engineRepository = new EngineRepository(crudRepository);
        engineRepository.createEngine(engine);
        engineRepository.createEngine(engine2);
        Car car = Car.builder()
                .name("Lada")
                .engine(engine)
                .build();
        Car car2 = Car.builder()
                .name("Zil")
                .engine(engine2)
                .build();
        CarRepository carRepository = new CarRepository(crudRepository);
        carRepository.createCar(car);
        carRepository.createCar(car2);
        Post post = Post.builder()
                .description("Test1")
                .user(user)
                .car(car)
                .build();
        Post post2 = Post.builder()
                .description("Test2")
                .user(user)
                .car(car2)
                .build();
        PostRepository postRepository = new PostRepository(crudRepository);
        postRepository.createPost(post);
        postRepository.createPost(post2);
        List<Post> posts = List.of(post);
        assertThat(postRepository.findPostOfSpecificBrand("Lada")).isEqualTo(posts);
    }
}