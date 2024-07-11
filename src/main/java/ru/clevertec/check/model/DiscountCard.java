package ru.clevertec.check.model;

import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class DiscountCard {
    @CsvBindByName(column = "id")
    private Long id;
    @CsvBindByName(column = "number")
    private int number;
    @CsvBindByName(column = "discount amount")
    private int discountAmount;
}
