package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.PriceHistory;

import java.util.Optional;

@Repository
@AllArgsConstructor
@Slf4j
public class PriceHistoryRepository {

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
}
