package ru.clevertec.check.repository;

import ru.clevertec.check.model.DiscountCard;

import java.util.List;
import java.util.Optional;

public interface DiscountCardRepository {
    List<DiscountCard> findsAll();

    Optional<DiscountCard> findById(long id);

    Optional<DiscountCard> findByNumber(int number);
}
