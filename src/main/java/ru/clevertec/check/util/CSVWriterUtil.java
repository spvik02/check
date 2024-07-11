package ru.clevertec.check.util;

import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

import com.opencsv.CSVWriter;
import com.opencsv.ICSVWriter;
import ru.clevertec.check.model.Receipt;
import ru.clevertec.check.model.ReceiptPosition;

public class CSVWriterUtil {

    public static void writeReceipt(Receipt receipt, String fileName) throws IOException {
        CSVWriter writer = new CSVWriter(new FileWriter(fileName), ';', ICSVWriter.DEFAULT_QUOTE_CHARACTER,
                ICSVWriter.DEFAULT_ESCAPE_CHARACTER, ICSVWriter.DEFAULT_LINE_END);

        String[] record = new String[]{"Date", "Time"};
        writer.writeNext(record, false);
        record = new String[]{receipt.getDate().toString(), receipt.getTime().toString()};
        writer.writeNext(record, false);
        writeEmptyLine(writer);

        record = new String[]{"QTY", "DESCRIPTION", "PRICE", "DISCOUNT", "TOTAL"};
        writer.writeNext(record, false);
        for (ReceiptPosition position : receipt.getPositions()) {
            record = new String[]{
                    String.valueOf(position.getQuantity()),
                    position.getProduct().getDescription(),
                    getLocalizedBigDecimalValue(position.getPrice(), Locale.US) + "$",
                    getLocalizedBigDecimalValue(position.getDiscount(), Locale.US) + "$",
                    getLocalizedBigDecimalValue(position.getTotal(), Locale.US) + "$"};
            writer.writeNext(record, false);
        }
        writeEmptyLine(writer);

        if (receipt.getDiscountCard() != null) {
            record = new String[]{"DISCOUNT CARD", "DISCOUNT PERCENTAGE"};
            writer.writeNext(record, false);
            record = new String[]{String.valueOf(receipt.getDiscountCard().getNumber()),
                    receipt.getDiscountCard().getDiscountAmount() + "%"};
            writer.writeNext(record, false);
        }

        record = new String[]{"TOTAL PRICE", "TOTAL DISCOUNT", "TOTAL WITH DISCOUNT"};
        writer.writeNext(record, false);
        record = new String[]{
                getLocalizedBigDecimalValue(receipt.getTotalPrice(), Locale.US) + "$",
                getLocalizedBigDecimalValue(receipt.getTotalPrice().subtract(receipt.getTotalPriceWithDiscount()), Locale.US) + "$",
                getLocalizedBigDecimalValue(receipt.getTotalPriceWithDiscount(), Locale.US) + "$"};
        writer.writeNext(record, false);

        //close the writer
        writer.close();
    }

    public static void writeException(Exception e, String fileName) throws IOException {
        CSVWriter writer = new CSVWriter(new FileWriter(fileName), ';', ICSVWriter.DEFAULT_QUOTE_CHARACTER,
                ICSVWriter.DEFAULT_ESCAPE_CHARACTER, ICSVWriter.DEFAULT_LINE_END);

        String[] record = new String[]{"ERROR"};
        writer.writeNext(record, false);
        record = new String[]{e.getMessage()};
        writer.writeNext(record, false);

        //close the writer
        writer.close();
    }

    private static void writeEmptyLine(CSVWriter writer) {
        writer.writeNext(new String[]{""}, false);
    }

    /**
     * Format BigDecimal to #.## format. The decimal character depends on locale
     *
     * @param input
     * @param locale
     * @return
     */
    private static String getLocalizedBigDecimalValue(BigDecimal input, Locale locale) {
        final NumberFormat numberFormat = NumberFormat.getNumberInstance(locale);
        numberFormat.setGroupingUsed(true);
        numberFormat.setMaximumFractionDigits(2);
        numberFormat.setMinimumFractionDigits(2);
        return numberFormat.format(input);
    }
}
