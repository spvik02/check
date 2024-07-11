package ru.clevertec.check.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ReceiptPosition {
    private Product product;
    private long quantity;
    private BigDecimal price;
    private BigDecimal total;
    private BigDecimal discount;
}
