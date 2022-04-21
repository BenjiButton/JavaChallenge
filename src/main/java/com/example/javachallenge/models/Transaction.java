package com.example.javachallenge.models;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * The transaction class defines the attributes and behaviour of a retail sale
 */
public class Transaction {
    /**
     * List of available currency denominations in descending order
     */
    public static final List<Double> NOTES_AND_COINS = List.of(200d, 100d, 50d, 20d, 10d, 5d, 2d, 1d, 0.5, 0.20, 0.10, 0.05);

    // Define formats for currency amounts and denominations
    private static final DecimalFormat currencyFormat = new DecimalFormat("0.00");
    private static final DecimalFormat randFormat = new DecimalFormat("R0");
    private static final DecimalFormat centsFormat = new DecimalFormat("#0c");

    // Define the transaction attributes
    private final BigDecimal price;
    private final BigDecimal tender;

    // Constructor method
    public Transaction(BigDecimal price, BigDecimal tender) {
        this.price = price;
        this.tender = tender;
    }

    // Getters (setters are not needed because the transaction attributes can't change
    public BigDecimal getPrice() {
        return price;
    }

    public BigDecimal getTender() {
        return tender;
    }

    /**
     * Calculate the change to be given (derived from tender - price)
     */
    public BigDecimal getChange() {
        return tender.subtract(price);
    }

    /**
     * Provide a displayable breakdown of change by denomination
     * Calculate the change to be given with the minimum number of notes and coins
     */
    public String getChangeMoney() {
        BigDecimal remaining = getChange();
        List<String> out = new ArrayList<>();

        // Loop through the denominations, from largest to smallest
        for (double i : NOTES_AND_COINS) {
            if (i <= remaining.doubleValue()) {

                final BigDecimal decimal = BigDecimal.valueOf(i);
                int count = remaining.divide(decimal, RoundingMode.FLOOR).intValue();
                remaining = remaining.subtract(decimal.multiply(BigDecimal.valueOf(count)));

                out.add(count + " x " + formatDenomination(i));
                out.add("Remaining: " + formatCurrency(remaining));
            }
        }

        return String.join("\n", out);
    }

    /**
     * Provide a displayable version of the transaction information
     */
    public String getTransactionSummary() {
        return "Price:  " + formatCurrency(getPrice()) +
                "\nTender: " + formatCurrency(getTender()) +
                "\n---------------------------" +
                "\nChange: " + formatCurrency(getChange());
    }

    /**
     * Change is given in denominated amounts
     * Format the denomination amount, Rands prefixed with R, cents converted to a whole number and suffixed with c
     */
    private static String formatDenomination(double amount) {
        DecimalFormat df = (amount >= 1) ? randFormat : centsFormat;
        return (amount >= 1) ? df.format(amount) : df.format(amount * 100);
    }

    /**
     * Format the currency value to a fixed length string, right justified
     */
    private static String formatCurrency(BigDecimal value) {
        return String.format("R %6s", currencyFormat.format(value));
    }
}
