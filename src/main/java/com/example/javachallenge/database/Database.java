package com.example.javachallenge.database;

import com.example.javachallenge.models.Transaction;

import java.sql.*;

public class Database {
    private final Connection con;

    // Constructor method
    public Database() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        // To create the schema, run this MySQL command:
        // CREATE SCHEMA `javachallenge`;

        // Connect to the database
        con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/javachallenge", "root", "");

        // Create the table if it doesn't exist
        con.prepareStatement("" +
                "CREATE TABLE IF NOT EXISTS `transactions` (\n" +
                "  `transactionid` INT NOT NULL AUTO_INCREMENT,\n" +
                "  `timestamp` TIMESTAMP NOT NULL DEFAULT NOW(),\n" +
                "  `tender` DECIMAL(10,2) NOT NULL,\n" +
                "  `price` DECIMAL(10,2) NOT NULL,\n" +
                "  `change` DECIMAL(10,2) NOT NULL,\n" +
                "  PRIMARY KEY (`transactionid`),\n" +
                "  UNIQUE INDEX `transactionid_UNIQUE` (`transactionid` ASC) VISIBLE);")
                .execute();
    }

    /**
     * Persist a new transaction to the database
     * @throws SQLException
     */
    public void addTransaction(Transaction transaction) throws SQLException {
        PreparedStatement st = con.prepareStatement(
                "INSERT INTO transactions (`timestamp`, `tender`, `price`, `change`) VALUES (NOW(), ?, ?, ?)");
        st.setBigDecimal(1, transaction.getTender());
        st.setBigDecimal(2, transaction.getPrice());
        st.setBigDecimal(3, transaction.getChange());
        st.execute();
    }
}
