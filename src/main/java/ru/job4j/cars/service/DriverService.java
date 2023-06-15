package ru.job4j.cars.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.cars.model.Driver;
import ru.job4j.cars.repository.DriverDbRepository;

import java.util.Optional;

@Service
@AllArgsConstructor
public class DriverService {

    private final DriverDbRepository driverRepository;

    public Optional<Driver> createDriver(Driver driver) {
        return driverRepository.createDriver(driver);
    }
}
