package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Driver;

import java.util.Optional;

@Repository
@AllArgsConstructor
public class DriverRepository {

    private final CrudRepository crudRepository;

    /**
     * Сохранить в базе.
     * @param driver водитель.
     * @return Optional с водителем с id.
     * @exception Optional пустой.
     */

    public Optional<Driver> createDriver(Driver driver) {
        try {
            crudRepository.run(session -> session.persist(driver));
            return Optional.of(driver);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
