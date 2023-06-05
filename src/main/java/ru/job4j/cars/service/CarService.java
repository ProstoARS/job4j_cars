package ru.job4j.cars.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.job4j.cars.model.Car;
import ru.job4j.cars.model.Driver;
import ru.job4j.cars.repository.CarRepository;

import java.util.HashSet;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class CarService {

    private final CarRepository carRepository;

    public Optional<Car> createCar(Car car) {
        return carRepository.createCar(car);
    }

    public Optional<Car> findById(int id) {
        return carRepository.findCarById(id);
    }

    public Optional<Car> addDriverToOwners(Car car, Driver driver) {
        Optional<Car> optionalCar = Optional.empty();
        try {
            if (car.getOwners().isEmpty()) {
                HashSet<Driver> drivers = new HashSet<>();
                drivers.add(driver);
                car.setOwners(drivers);
                optionalCar = Optional.of(car);
            } else {
                car.getOwners().add(driver);
                optionalCar = Optional.of(car);
            }
        } catch (Exception e) {
            log.error("не удалось добавить водителя {} в коллекцию", driver.getName());
        }
        return optionalCar;
    }
}
