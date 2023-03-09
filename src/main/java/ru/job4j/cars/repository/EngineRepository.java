package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Engine;

import java.util.Optional;

@Repository
@AllArgsConstructor
@Slf4j
public class EngineRepository {

    private final CrudRepository crudRepository;

    /**
     * Сохранить в базе.
     * @param engine двигатель.
     * @return Optional с двигателем с id.
     * @exception Optional пустой.
     */

    public Optional<Engine> createEngine(Engine engine) {
        Optional<Engine> engineOptional = Optional.empty();
        try {
            crudRepository.run(session -> session.persist(engine));
            engineOptional = Optional.of(engine);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return engineOptional;
    }
}
