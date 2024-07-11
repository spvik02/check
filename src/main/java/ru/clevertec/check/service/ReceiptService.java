package ru.clevertec.check.service;

import lombok.AllArgsConstructor;
import ru.clevertec.check.model.CustomException;
import ru.clevertec.check.model.DiscountCard;
import ru.clevertec.check.model.Product;
import ru.clevertec.check.model.Receipt;
import ru.clevertec.check.model.ReceiptPosition;
import ru.clevertec.check.repository.DiscountCardRepository;
import ru.clevertec.check.repository.ProductRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
public class ReceiptService {
    private ProductRepository productRepository;
    private DiscountCardRepository discountCardRepository;

    public Receipt calculateTotal(Map<String, Object> parameters, Map<Long, Long> positionsMap) throws Exception {
        BigDecimal totalReceiptPriceWithDiscount = new BigDecimal(0);
        BigDecimal totalReceiptPrice = new BigDecimal(0);

        Receipt receipt = new Receipt(LocalDate.now(), LocalTime.now());

        //find and set discount card
        if (parameters.containsKey("discountCard")) {
            Integer discountCardNumber = Integer.valueOf((String) parameters.get("discountCard"));
            DiscountCard discountCard = discountCardRepository.findByNumber(discountCardNumber)
                    .orElseThrow(() -> new RuntimeException("discountCard with id " + discountCardNumber + " wasn't found"));
            receipt.setDiscountCard(discountCard);
        }

        //add receipt positions
        List<ReceiptPosition> positions = new ArrayList<>();
        positionsMap.forEach((key, value) -> {
            Product product = productRepository.findById(key).orElseThrow();
            ReceiptPosition receiptPosition = ReceiptPosition.builder()
                    .product(product)
                    .price(product.getPrice())
                    .quantity(value)
                    .build();
            positions.add(receiptPosition);
        });
        receipt.setPositions(positions);

        //count everything
        for (ReceiptPosition position : positions) {
            BigDecimal positionTotal; //(QTY * PRICE)
            BigDecimal positionPriceWithDiscount;

            positionTotal = position.getPrice().multiply(BigDecimal.valueOf(position.getQuantity()));
            positionTotal = positionTotal.setScale(2, BigDecimal.ROUND_HALF_EVEN);
            position.setTotal(positionTotal);
            totalReceiptPrice = totalReceiptPrice.add(positionTotal);

            BigDecimal discount = new BigDecimal(0); //(TOTAL * DISCOUNT PERCENTAGE / 100)
            BigDecimal discountValue = new BigDecimal(0);
            //calculation of the discount by quantity
            if (position.getProduct().isWholesaleProduct() && position.getQuantity() >= 5) {
                discountValue = BigDecimal.valueOf(10).divide(BigDecimal.valueOf(100));
            }
            //calculation of the discount on the discount card
            //discounts are not cumulative
            //if the discount card has been presented
            else if (receipt.getDiscountCard() != null) {
                try {
                    discountValue = BigDecimal.valueOf(receipt.getDiscountCard().getDiscountAmount())
                            .divide(BigDecimal.valueOf(100));

                } catch (Exception e) {
                    throw new CustomException("BAD REQUEST");
                }
            }
            if (discountValue.compareTo(BigDecimal.ZERO) == 1) {
                discount = positionTotal.multiply(discountValue);
                discount = discount.setScale(2, BigDecimal.ROUND_HALF_EVEN);
                positionPriceWithDiscount = positionTotal.subtract(discount);
                positionPriceWithDiscount = positionPriceWithDiscount.setScale(2, BigDecimal.ROUND_HALF_EVEN);
            } else {
                positionPriceWithDiscount = positionTotal;
            }
            position.setDiscount(discount);

            //add the calculated cost to the cost of the entire receipt
            totalReceiptPriceWithDiscount = totalReceiptPriceWithDiscount.add(positionPriceWithDiscount);
        }
        receipt.setTotalPrice(totalReceiptPrice);
        receipt.setTotalPriceWithDiscount(totalReceiptPriceWithDiscount);

        //check if there is enough money
        if (receipt.getTotalPriceWithDiscount()
                .compareTo(BigDecimal.valueOf((Long.parseLong((String) parameters.get("balanceDebitCard"))))) > 0) {
            throw new CustomException("NOT ENOUGH MONEY");
        }

        //sout
        System.out.println(receipt);

        return receipt;
    }

}
