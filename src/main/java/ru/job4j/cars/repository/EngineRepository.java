package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Engine;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
@Slf4j
public class EngineRepository {

    private static final String FIND_ALL = """
            FROM Engine e
            ORDER BY e.id
            """;

    private static final String FIND_BY_ID = """
            FROM Engine e
            WHERE e.id = :tId
            """;

    private final CrudRepository crudRepository;

    /**
     * Сохранить в базе.
     * @param engine двигатель.
     * @return Optional с двигателем с id, иначе Optional.empty().
     */

    public Optional<Engine> createEngine(Engine engine) {
        Optional<Engine> engineOptional = Optional.empty();
        try {
            crudRepository.run(session -> session.persist(engine));
            engineOptional = Optional.of(engine);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return engineOptional;
    }

    public List<Engine> findAll() {
        return crudRepository.query(FIND_ALL, Engine.class);
    }

    public Optional<Engine> findById(int id) {
        return crudRepository.optional(FIND_BY_ID, Engine.class, Map.of("tId", id));
    }
}
