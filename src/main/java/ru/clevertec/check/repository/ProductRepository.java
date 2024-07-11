package ru.clevertec.check.repository;

import ru.clevertec.check.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    List<Product> findAll();

    Optional<Product> findById(long id);
}
