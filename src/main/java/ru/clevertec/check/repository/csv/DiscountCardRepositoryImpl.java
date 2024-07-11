package ru.clevertec.check.repository.csv;

import com.opencsv.bean.CsvToBeanBuilder;
import lombok.Builder;
import lombok.Getter;
import ru.clevertec.check.model.DiscountCard;
import ru.clevertec.check.repository.DiscountCardRepository;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;
import java.util.Optional;

@Getter
@Builder
public class DiscountCardRepositoryImpl implements DiscountCardRepository {
    private static List<DiscountCard> cards;
    private final String fileName;

    public DiscountCardRepositoryImpl(String fileName) {
        this.fileName = fileName;
        cards = readFromCsv();
    }

    @Override
    public List<DiscountCard> findsAll() {
        return cards;
    }

    private List<DiscountCard> readFromCsv() {
        List<DiscountCard> cards;
        try {
            cards = new CsvToBeanBuilder<DiscountCard>(new FileReader(fileName))
                    .withSeparator(';')
                    .withType(DiscountCard.class)
                    .build()
                    .parse();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return cards;
    }

    public Optional<DiscountCard> findById(long id) {
        return cards.stream()
                .filter(card -> card.getId().equals(id))
                .findFirst();
    }

    @Override
    public Optional<DiscountCard> findByNumber(int number) {
        return cards.stream()
                .filter(card -> card.getNumber() == number)
                .findFirst();
    }
}
