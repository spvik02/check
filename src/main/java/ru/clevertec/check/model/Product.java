package ru.clevertec.check.model;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvNumber;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Product {
    @CsvBindByName(column = "id")
    private Long id;
    @CsvBindByName(column = "description")
    private String description;
    @CsvBindByName(column = "price")
    @CsvNumber("#,##")
    private BigDecimal price;
    @CsvBindByName(column = "quantity_in_stock")
    private int quantityInStock;
    @CsvBindByName(column = "wholesale_product")
    private boolean wholesaleProduct;
}
