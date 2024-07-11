package ru.clevertec.check.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@Getter
@Setter
public class Receipt {
    private int id;
    private List<ReceiptPosition> positions;
    private DiscountCard discountCard;
    private BigDecimal totalPrice;
    private BigDecimal totalPriceWithDiscount;
    private final LocalDate date;
    private final LocalTime time;

    @Override
    public String toString() {
        return "Receipt{" +
                "id=" + id +
                ", \npositions=" + positions +
                ", \ndiscountCard=" + discountCard +
                ", \ntotalPrice=" + totalPrice +
                ", \ntotalPriceWithDiscount=" + totalPriceWithDiscount +
                ", \ndate=" + date +
                ", time=" + time +
                '}';
    }
}
