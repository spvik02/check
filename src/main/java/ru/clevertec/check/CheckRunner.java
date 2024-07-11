package ru.clevertec.check;

import lombok.Builder;
import lombok.Getter;
import ru.clevertec.check.model.CustomException;
import ru.clevertec.check.model.Receipt;
import ru.clevertec.check.repository.csv.DiscountCardRepositoryImpl;
import ru.clevertec.check.repository.csv.ProductRepositoryImpl;
import ru.clevertec.check.service.ReceiptService;
import ru.clevertec.check.util.CSVWriterUtil;
import ru.clevertec.check.util.ParameterUtil;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Getter
@Builder
public class CheckRunner {

    public static void main(String[] args) {

        Map<String, Object> parameters = null;
        Map<Long, Long> positionsMap = null;
        try {
            parameters = ParameterUtil.parseParameters(args);
            List<String> positionsStringList = (List<String>) parameters.get("positions");
            positionsMap = ParameterUtil.parsePositionsStringList(positionsStringList);
        } catch (CustomException e) {
            handleException(e);
            throw new RuntimeException(e);
        } catch (Exception e) {
            handleException(new Exception("INTERNAL SERVER ERROR"));
            throw new RuntimeException(e);
        }

        ReceiptService receiptService = new ReceiptService(
                new ProductRepositoryImpl("./src/main/resources/products.csv"),
                new DiscountCardRepositoryImpl("./src/main/resources/discountCards.csv"));

        Receipt receipt;
        try {
            receipt = receiptService.calculateTotal(parameters, positionsMap);
            CSVWriterUtil.writeReceipt(receipt, "./result.csv");
        } catch (CustomException e) {
            handleException(e);
            throw new RuntimeException(e);
        } catch (Exception e) {
            handleException(new Exception("INTERNAL SERVER ERROR"));
            throw new RuntimeException(e);
        }
    }

    private static void handleException(Exception e) {
        System.out.println(e.getMessage());
        try {
            CSVWriterUtil.writeException(e, "./result.csv");
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
