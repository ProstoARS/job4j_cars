package ru.job4j.cars.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.cars.model.Post;
import ru.job4j.cars.model.PriceHistory;
import ru.job4j.cars.repository.PriceHistoryRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PriceHistoryService {

    private PriceHistoryRepository priceHistoryRepository;

    public Optional<PriceHistory> createPriceHistory(PriceHistory priceHistory) {
        return priceHistoryRepository.createPriceHistory(priceHistory);
    }

    public List<PriceHistory> findPhByPost(Post post) {
        return priceHistoryRepository.findPhByPost(post);
    }
}
