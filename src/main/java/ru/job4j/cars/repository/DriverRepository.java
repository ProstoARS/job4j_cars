package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Driver;

import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
@Slf4j
public class DriverRepository {

    private static final String FIND_ALL = "FROM Driver";
    private final CrudRepository crudRepository;

    /**
     * Найти всех в базе
     *
     * @return список водителей
     */

    public List<Driver> findAll() {
     return crudRepository.query(FIND_ALL, Driver.class);
    }

    /**
     * Сохранить в базе.
     * @param driver водитель.
     * @return Optional с водителем с id, иначе Optional.empty().
     */

    public Optional<Driver> createDriver(Driver driver) {
        Optional<Driver> driverOptional = Optional.empty();
        try {
            crudRepository.run(session -> session.persist(driver));
            driverOptional = Optional.of(driver);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return driverOptional;
    }
}
