package ru.job4j.cars.repository;

import ru.job4j.cars.model.Engine;

import java.util.List;
import java.util.Optional;

public interface IEngineRepository {

    Optional<Engine> createEngine(Engine engine);

    List<Engine> findAll();

    Optional<Engine> findById(int id);
}
