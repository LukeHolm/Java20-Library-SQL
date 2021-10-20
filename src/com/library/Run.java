package com.library;

import java.sql.*;

public class Run {
    private static final Menu MENU = new Menu();
    private final static Library LIBRARY = new Library();
    private static final String jdbcDriver = "com.mysql.cj.jdbc.Driver";
    private static final String dbAddress = "jdbc:mysql://localhost:3306/";
    private static final String dbName = "cobalt_reserve";
    private static final String timezone = "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    private static final String userName = "root";
    private static Statement sqlStatement;
    private static Connection connection;
    private static boolean exitProgram;
    private static boolean exitLoop;
    //#region password
    private static final String password = "";
    //endregion


    Run() {
        exitLoop = false;
        exitProgram = false;
    }

    public void Program() throws SQLException {
        try {
            Class.forName(jdbcDriver);
            connection = DriverManager.getConnection(dbAddress + dbName + timezone, userName, password);
        } catch (ClassNotFoundException e){
            e.printStackTrace();
        } catch (SQLException e) {
            createDatabase();
        }
        try {
            createSqlStatement();
            System.out.println("\n--- Welcome to the Cobalt Reserve ---");
            while (!exitProgram) {
                int selection = MENU.mainMenu();
                mainMenu(selection);
            }
            System.out.println("Thank you for using the Cobalt Reserve!");
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            connection.close();
        }
    }

    public void mainMenu(int selection) throws SQLException {
        while (!exitLoop) {
            switch (selection) {
                case 1 -> {LIBRARY.borrowBook(); exitLoop = true;}
                case 2 -> {LIBRARY.returnBook(); exitLoop = true;}
                case 3 -> search(MENU.search());
                case 4 -> createNew(MENU.createNew());
                case 5 -> remove(MENU.remove());
                case 0 -> {exitLoop = true; exitProgram = true;}
            }
        } exitLoop = false;
    }

    public void createNew(int selection)throws SQLException{
        switch (selection) {
            case 1 -> LIBRARY.newAuthor();
            case 2 -> LIBRARY.newBook();
            case 3 -> LIBRARY.newMember();
            case 0 -> exitLoop = true;
        }
    }

    public void search(int selection) throws SQLException{
        switch (selection){
            case 1 -> LIBRARY.searchAuthor();
            case 2 -> LIBRARY.searchBook();
            case 3 -> LIBRARY.searchISBN();
            case 4 -> LIBRARY.listAllAuthors();
            case 5 -> LIBRARY.listAllBooks();
            case 6 -> LIBRARY.searchMember();
            case 0 -> exitLoop = true;
            default -> exitLoop = false;
        }
    }

    public void remove(int selection) throws SQLException{
        switch (selection){
            case 1 -> LIBRARY.removeAuthor();
            case 2 -> LIBRARY.removeBook();
            case 3 -> LIBRARY.removeMember();
            case 0 -> exitLoop = true;
        }
    }

    public static Statement getSqlStatement() {
        return sqlStatement;
    }

    public static void createSqlStatement() throws SQLException {
        sqlStatement = connection.createStatement();
    }

    private void createDatabase()throws SQLException {
        try {
            Class.forName(jdbcDriver);
            connection = DriverManager.getConnection(dbAddress + timezone, userName, password);
            sqlStatement = connection.createStatement();
            int createDatabase = sqlStatement.executeUpdate("CREATE DATABASE " + dbName);
            LIBRARY.setup();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
}