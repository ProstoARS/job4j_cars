package ru.job4j.cars.repository;

import ru.job4j.cars.model.Driver;

import java.util.List;
import java.util.Optional;

public interface IDriverRepository {

    List<Driver> findAll();

    Optional<Driver> createDriver(Driver driver);
}
