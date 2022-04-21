package com.example.javachallenge;

import com.example.javachallenge.database.Database;
import com.example.javachallenge.models.Transaction;

import java.math.BigDecimal;

public class Main {

    public static void main(String[] args) throws Exception {
        // Open a database connection
        Database database = new Database();

        // Calculate a random non-zero price less than or equal to R100
        BigDecimal price = randomCurrency(1, 100);

        // Calculate a random tender amount greater than the price
        BigDecimal tender = randomCurrency(price.intValue() + 1, 200);

        // Create the transaction object that represents the sale
        Transaction transaction = new Transaction(price, tender);

        // Display the transaction summary
        System.out.println("---------------------------");
        System.out.println(transaction.getTransactionSummary());
        System.out.println("---------------------------");

        // Display the change by denomination
        System.out.println("Notes and coins returned:");
        System.out.println(transaction.getChangeMoney());
        System.out.println("---------------------------");

        // Persist the transaction to the database
        database.addTransaction(transaction);
        System.out.println("Transaction added to the database");
        System.out.println("---------------------------");

    }

    /**
     * Return a random currency value
     * Assumptions:
     * 1) price is rounded down to the nearest 5c
     * 2) customer tenders cash with the smallest denomination being a 5c coin
     * @return random amount rounded down to the nearest 5c
     */
    private static BigDecimal randomCurrency(int min, int max) {
        // Ensure a 0 is not returned by making the minimum value 1 and the maximum value as passed in
        final double value = random(min, max);
        // round down to two decimal places
        double randomCurrency = floorCurrency(value, 2);

        // Calculate the rounded cent value
        final double truncated = floorCurrency(randomCurrency, 1);
        int centValue = (int) ((randomCurrency - truncated) * 100d);
        centValue = (centValue >= 5) ? 5 : 0;

        // Return the truncated value with corrected cents
        return BigDecimal.valueOf(truncated).add(BigDecimal.valueOf(centValue / 100d));
    }

    /**
     * Generates a random number within the min and max values
     */
    private static double random(int min, int max) {
        return Math.random() * (max - min) + min;
    }

    /**
     * Round the value to two decimal places
     */
    private static double floorCurrency(double value, int precision) {
        final double pow = Math.pow(10, precision);
        return Math.floor(value * pow)/pow;
    }
}
