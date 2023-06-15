package ru.job4j.cars.repository;

import ru.job4j.cars.model.Car;

import java.util.Optional;

public interface ICarRepository {

    Optional<Car> createCar(Car car);

    Optional<Car> findCarById(int id);
}
