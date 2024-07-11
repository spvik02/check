package ru.clevertec.check.util;

import ru.clevertec.check.model.CustomException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParameterUtil {
    /**
     * Make Map from pairs product-qty and discountCard=val and balanceDebitCard=val
     *
     * @param args arguments
     */
    public static Map<String, Object> parseParameters(String[] args) throws Exception {
        Map<String, Object> params = new HashMap<>();
        List<String> positions = new ArrayList<>();
        for (String arg : args) {
            if (arg.contains("discountCard=")) {
                params.put("discountCard", arg.substring(arg.indexOf("=") + 1));
            } else if (arg.contains("balanceDebitCard=")) {
                params.put("balanceDebitCard", arg.substring(arg.indexOf("=") + 1));
            } else {
                positions.add(arg);
            }
        }

        if (!params.containsKey("balanceDebitCard") || positions.isEmpty()) {
            throw new CustomException("BAD REQUEST");
        }

        params.put("positions", positions);
        return params;
    }

    /**
     * Parse list of positions to map with key - id and value - qty. Values for repeated id are summed
     *
     * @param positionsStringList list of Strings of format "id-qty"
     * @return map of positions id-qty
     * @throws CustomException
     */
    public static Map<Long, Long> parsePositionsStringList(List<String> positionsStringList) throws CustomException {
        Map<Long, Long> positions = new HashMap<>();

        for (String positionString : positionsStringList) {
            try {
                long productId = Long.parseLong(positionString.substring(0, positionString.indexOf("-")));
                long quantity = Long.parseLong(positionString.substring(positionString.indexOf("-") + 1));
                positions.merge(productId, quantity, Long::sum);
            } catch (NumberFormatException e) {
                System.out.println("Wrong parameter format: " + positionString);
                throw new CustomException("BAD REQUEST");
            }
        }

        return positions;
    }
}
