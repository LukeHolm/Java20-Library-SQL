package com.library;

import java.util.Scanner;

public class InputHandler {
    private final Scanner scanner = new Scanner(System.in);

    public int getIntFromUser(int a, int b) {
        while (true) {
            int userInput;
            try {
                userInput = Integer.parseInt(scanner.nextLine());
                if (userInput < a || userInput > b) {
                    System.out.println("Please enter a number between " + a + " and " + b);
                } else {
                    return userInput;
                }
            } catch (Exception e) {
                System.out.println("Only numbers allowed.");
                System.out.println("Please enter a number between " + a + " and " + b);
            }
        }
    }

    public String getStringFromUser() {
        return scanner.nextLine();
    }

}