package ru.job4j.cars.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.cars.model.Engine;
import ru.job4j.cars.repository.EngineRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class EngineService {

    private final EngineRepository engineRepository;

    public Optional<Engine> createEngine(Engine engine) {
        return engineRepository.createEngine(engine);
    }

    public List<Engine> findAll() {
        return engineRepository.findAll();
    }

    public Optional<Engine> findById(int id) {
        return engineRepository.findById(id);
    }
}
