package ru.job4j.cars.repository;

import config.TestHibernateConfig;
import org.hibernate.Session;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.cars.model.Car;
import ru.job4j.cars.model.Driver;
import ru.job4j.cars.model.Engine;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;

class CarRepositoryTest {

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
            session.createQuery("DELETE FROM Car")
                    .executeUpdate();
            session.createQuery("DELETE FROM Driver")
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
    void whenCreatedCarWithOwners() {
        IDriverRepository driverRepository = new DriverDbRepository(crudRepository);
        Driver driver = Driver.builder()
                .name("Ivan")
                .build();
        driverRepository.createDriver(driver);
        IEngineRepository engineRepository = new EngineDbRepository(crudRepository);
        Engine engine = Engine.builder()
                .name("Бензин")
                .build();
        engineRepository.createEngine(engine);
        ICarRepository carRepository = new CarDbRepository(crudRepository);
        Car car = Car.builder()
                .brand("Nissan")
                .model("Note")
                .engine(engine)
                .owners(Set.of(driver))
                .build();
        carRepository.createCar(car);
        assertThat(carRepository.findCarById(car.getId()).get().getOwners().stream().findFirst().get().getName())
                .isEqualTo("Ivan");
    }
}