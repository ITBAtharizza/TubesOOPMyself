package org.example.map.objects;

import java.util.Scanner;

import org.example.Player;

public class HouseInteract implements Interact {
    public void interact(Player player) {
        Scanner scanner = player.getScanner();
        System.out.print("\n\n---- HOUSE ---- \n\n");

        System.out.println("\n\nWhat would you like to do?");
        System.out.println("1. Sleep");
        System.out.println("2. Watch TV");
        System.out.println("3. Cook");
        System.out.println("4. Leave the house");
        System.out.print("Enter your choice: ");
        int choice;

        try {
            choice = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid number. Leaving the house...");
            return;
        }

        if (choice == 1) player.sleeping();
        else if (choice == 2) player.watching();
        else if (choice == 3) player.cooking();
        else System.out.println("Invalid choice.");
    }
}
