package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Car;

import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
@Slf4j
public class CarDbRepository implements ICarRepository {

    private static final String FIND_BY_ID = """
            FROM Car c
            JOIN FETCH c.owners
            JOIN FETCH c.engine
            WHERE c.id = :tId
            """;

    private final CrudRepository crudRepository;

    /**
     * Сохранить в базе.
     * @param car автомобиль.
     * @return Optional с автомобилем с id, иначе Optional.empty().
     */
    @Override
    public Optional<Car> createCar(Car car) {
        Optional<Car> carOptional = Optional.empty();
        try {
            crudRepository.run(session -> session.persist(car));
            carOptional = Optional.of(car);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return carOptional;
    }

    /**
     * Найти автомобиль по id.
     *
     * @param id идентификатор.
     * @return Optional с автомобилем с id, иначе Optional.empty().
     */
    @Override
    public Optional<Car> findCarById(int id) {
        Optional<Car> carOptional = Optional.empty();
        try {
            carOptional = crudRepository.optional(FIND_BY_ID, Car.class, Map.of("tId", id));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return carOptional;
    }
}
