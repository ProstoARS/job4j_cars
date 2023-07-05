package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Post;
import ru.job4j.cars.model.PriceHistory;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
@Slf4j
public class PriceHistoryDbRepository implements IPriceHistoryRepository {

    private static final String FIND_SORTED_PH_BY_POST = """
            FROM PriceHistory ph
            JOIN FETCH ph.post
            WHERE ph.post = :tPost
            ORDER BY ph.created DESC
            """;

    CrudRepository crudRepository;

    /**
     * Сохранить в базе
     * @param priceHistory история цены.
     * @return Oprional с Историей цены с id, иначе Optional.empty().
     */
    public Optional<PriceHistory> createPriceHistory(PriceHistory priceHistory) {
        Optional<PriceHistory> optionalPriceHistory = Optional.empty();
        try {
            crudRepository.run(session -> session.persist(priceHistory));
            optionalPriceHistory = Optional.of(priceHistory);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return optionalPriceHistory;
    }

    /**
     * Найти Историю цены по идентификатору поста.
     * @param post идентификатор поста.
     * @return List с историями цены.
     */
    public List<PriceHistory> findPhByPost(Post post) {
        return crudRepository.query(FIND_SORTED_PH_BY_POST, PriceHistory.class, Map.of("tPost", post));
    }

    /**
     * Найти последнее изменение цены.
     * @param post идентификатор поста.
     * @return Optional с ценой, либо пустой.
     */
    public Optional<PriceHistory> findLastPrice(Post post) {
        return crudRepository.optionalOneLimit(FIND_SORTED_PH_BY_POST, PriceHistory.class, Map.of("tPost", post));
    }
}
