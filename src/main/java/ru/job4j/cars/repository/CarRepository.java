package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Car;

import java.util.Optional;

@Repository
@AllArgsConstructor
@Slf4j
public class CarRepository {

    private final CrudRepository crudRepository;

    /**
     * Сохранить в базе.
     * @param car автомобиль.
     * @return Optional с автомобилем с id.
     * @exception Optional пустой.
     */

    public Optional<Car> createCar(Car car) {
        Optional<Car> carOptional = Optional.empty();
        try {
            crudRepository.run(session -> session.persist(car));
            carOptional = Optional.of(car);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return carOptional;
    }
}
