package org.example.world;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.example.Player;
import org.example.items.ItemFactory;
import org.example.items.Items;
import org.example.npc.Emily;

public class Store extends WorldLocation {
    private Emily emily = new Emily();

    public Store(){
        super("STORE, also Emily's Home");
    }

    @Override
    public void interact(Player player) {
        System.out.println("----" + "STORE, also Emily's Home" + "----");
        System.out.println("What do you want to do with Emily?");
        System.out.println("1. Chat");
        System.out.println("2. Give Gift");
        System.out.println("3. Propose");
        System.out.println("4. Marry");
        System.out.println("5. Buy Things");

        System.out.print("Enter your choice : ");
        Scanner scanner = player.getScanner();
        int choice;
        try {
            choice = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid number. Leaving the store...");
            return;
        }

        if (choice == 1) player.chatting(emily);
        else if (choice == 2) player.gifting(emily);
        else if (choice == 3) player.proposing(emily);
        else if (choice == 4) player.marry(emily);
        else if (choice == 5){
            ItemFactory itemFactory = new ItemFactory();
            itemFactory.loadAll();
            System.out.println("Items available for purchase:");
            List<Items> items = new ArrayList<>();

            for (Items item : ItemFactory.getAllItems().values()) {
                if (item.getBuyPrice() != null) {
                    items.add(item);
                }
            }

            int i = 1;
            items.sort((a, b) -> a.getClass().getSimpleName().compareTo(b.getClass().getSimpleName()));

            String lastClass = "";
            for (Items item : items) {
                String currentClass = item.getClass().getSimpleName();
                if (!currentClass.equals(lastClass)) {
                    System.out.println("\n== " + currentClass + " ==");
                    lastClass = currentClass;
                }
                System.out.printf("[%d] %dg %s\n", i, item.getBuyPrice(), item.getName());
                i++;
            }

            System.out.println("\nWhich item do you want to buy?");
            System.out.print("Enter your choice : ");
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Leaving the store...");
                return;
            }

            if (choice > 0 && choice < i){
                Items selectedItem = items.get(choice - 1);
                String name = selectedItem.getName();
                int price = selectedItem.getBuyPrice();
                System.out.printf("Your choice is %s with the price of %d each\n", name, price);
                System.out.print("Enter the amount to purchase : ");

                int amount;
                try {
                    amount = Integer.parseInt(scanner.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("Invalid number. Leaving the store...");
                    return;
                }

                int total = amount * price;
                System.out.printf("You're about to buy %d %s for %dg\n", amount, name, total);
                if (player.getGold() < total) {
                    System.out.println("You don't have the funds to do this action!");
                    return;
                }
                else {
                    player.setGold(player.getGold() - total);
                    player.addItemToInventory(selectedItem, amount);
                    System.out.println("Items purchased. Leaving the store...");
                }
            }
            else System.out.println("Invalid number. Leaving the store...");
        }else System.out.println("Invalid number. Leaving the store...");
    }
}
