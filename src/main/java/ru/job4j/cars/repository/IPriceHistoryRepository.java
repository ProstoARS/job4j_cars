package ru.job4j.cars.repository;

import ru.job4j.cars.model.Post;
import ru.job4j.cars.model.PriceHistory;

import java.util.List;
import java.util.Optional;

public interface IPriceHistoryRepository {

    Optional<PriceHistory> createPriceHistory(PriceHistory priceHistory);

    List<PriceHistory> findPhByPost(Post post);
}
