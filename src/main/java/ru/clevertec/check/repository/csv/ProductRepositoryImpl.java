package ru.clevertec.check.repository.csv;

import com.opencsv.bean.CsvToBeanBuilder;
import lombok.Builder;
import lombok.Getter;
import ru.clevertec.check.model.Product;
import ru.clevertec.check.repository.ProductRepository;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;
import java.util.Optional;

@Getter
@Builder
public class ProductRepositoryImpl implements ProductRepository {
    private static List<Product> products;
    private final String fileName;

    public ProductRepositoryImpl(String fileName) {
        this.fileName = fileName;
        products = readFromCsv();
    }

    @Override
    public List<Product> findAll() {
        return products;
    }

    private List<Product> readFromCsv() {
        List<Product> products;
        try {
            products = new CsvToBeanBuilder<Product>(new FileReader(fileName))
                    .withSeparator(';')
                    .withType(Product.class)
                    .build()
                    .parse();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return products;
    }

    public Optional<Product> findById(long id) {
        return products.stream()
                .filter(product -> product.getId().equals(id))
                .findFirst();
    }
}
