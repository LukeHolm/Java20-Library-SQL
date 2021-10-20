package com.library;

import java.sql.*;

public class ResultClass {

    private static java.sql.ResultSet result;
    private static final InputHandler userInput = new InputHandler();

    public static void setResult(Statement sqlStatement, String tableName, String columnName, String inputId) throws SQLException {
        result = sqlStatement.executeQuery("SELECT * FROM " + tableName + " WHERE " + columnName + " = " + inputId + ";");
    }

    public static void setAll(Statement sqlStatement, String tableName) throws SQLException {
        result = sqlStatement.executeQuery("SELECT * FROM " + tableName + ";");
    }

    public static boolean checkAvailability(Statement sqlStatement,String columnName, String title, int id) throws SQLException {
        result = sqlStatement.executeQuery("SELECT " + columnName + " FROM books WHERE title = '" + title + "' AND BookID = " + id + ";");

        int ID = 0;
        while (result.next()) {
            ID = result.getInt(columnName);
        }
        return ID != 0;
    }

    public static boolean checkReturned(Statement sqlStatement,String columnName, int bookId) throws SQLException {
        result = sqlStatement.executeQuery("SELECT * FROM borrowedBooks WHERE bookId = '" + bookId + "';");

        int ID = 0;
        try {
            while (result.next()) {
                ID = result.getInt(columnName);
            }
        } catch (SQLDataException ignored) {
        }
        return ID != 0;
    }

    public static int getAuthor(Statement sqlStatement, String authorName) throws SQLException {
        String query = "SELECT * FROM authors WHERE author LIKE '" + authorName + "';";
        result = sqlStatement.executeQuery(query);

        int ID = 0;
        while (result.next()) {
            ID = result.getInt("authorId");
        }
        return ID;
    }

    public static int idMatches(String tableName, int inputValue, String columnName) throws SQLException {
        ResultSet result;
        Statement sqlStatement = Run.getSqlStatement();

        while (true) {
            String query = "SELECT * FROM " + tableName + " WHERE " + columnName + " = " + inputValue + ";";
            result = sqlStatement.executeQuery(query);
            int ID = 0;
            while (result.next()) {
                ID = result.getInt(columnName);
            }
            if (!(ID == inputValue)) {
                System.out.println("No customer with id " + inputValue + " was found, please try again");
                inputValue = userInput.getIntFromUser(1, 100);
            } else {
                return ID;
            }
        }
    }

    public static boolean ifInputMatches(Statement sqlStatement, String query, String columnName) throws SQLException {
        result = sqlStatement.executeQuery(query);

        int ID = 0;
        while (result.next()) {
            ID = result.getInt(columnName);
        }
        return ID != 0;

    }

    public static java.sql.ResultSet getResult() {
        return result;
    }

    public static void setupResult(Statement sqlStatement, String query) throws SQLException {
        int setup = sqlStatement.executeUpdate(query);
    }

}