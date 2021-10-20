package com.library;

public class Menu {
    private final InputHandler userInput = new InputHandler();

    public int mainMenu() {
        System.out.println();
        System.out.println("What would you like to do?");
        System.out.println("1. Check out");
        System.out.println("2. Return");
        System.out.println("3. Search");
        System.out.println("4. Register");
        System.out.println("5. Remove");
        System.out.println("0. Exit the Cobalt Reserve");

        return userInput.getIntFromUser(0, 5);
    }

    public int createNew() {
        System.out.println();
        System.out.println("1. Register new Author");
        System.out.println("2. Register new Book");
        System.out.println("3. Register new Member");
        System.out.println("0. Go back");

        return userInput.getIntFromUser(0,3);
    }

    public int search() {
        System.out.println();
        System.out.println("1. Search by Author");
        System.out.println("2. Search by Title");
        System.out.println("3. Search by ISBN");
        System.out.println("4. List all Authors");
        System.out.println("5. List all Books");
        System.out.println("6. Search for Member");
        System.out.println("0. Go back");

        return userInput.getIntFromUser(0, 6);
    }

    public int remove(){
        System.out.println();
        System.out.println("1. Remove Author");
        System.out.println("2. Remove Book");
        System.out.println("3. Remove Member");
        System.out.println("0. Go Back");

        return userInput.getIntFromUser(0,3);
    }

    public boolean confirm(String question) {
        System.out.println(question + " " + "(y/n)");
        String ans = userInput.getStringFromUser();

        return ans.equalsIgnoreCase("y");
    }

    public String enterValue(String value) {
        System.out.println("Please enter " + value);
        return userInput.getStringFromUser();
    }

    public int enterInteger(String value, int lowestNumber, int highestNumber) {
        System.out.println("Please enter " + value);

        return userInput.getIntFromUser(lowestNumber,highestNumber);
    }

    public static String PadRight(String string) {
        int totalStringLength = 40;
        int charsToPad = totalStringLength - string.length();

        if (string.length() >= totalStringLength)
            return string;

        StringBuilder stringBuilder = new StringBuilder(string);
        for (int i = 0; i < charsToPad; i++) {
            stringBuilder.append(" ");
        }

        return stringBuilder.toString();
    }

}