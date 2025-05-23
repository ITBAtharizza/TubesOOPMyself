package org.example;

import java.util.Scanner;

import org.example.map.FarmMapController;
import org.example.time.GameClock; // Assuming GameClock is here
import org.example.world.WorldMap;

public class App {
    public static void main(String[] args) {
        System.out.println("Welcome to This Small Project!!");
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter your name: ");
        String playerName = scanner.nextLine();
        System.out.print("Enter your gender: ");
        String playerGender = scanner.nextLine();

        GameClock gameClock = GameClock.getInstance();
        WorldMap worldMap = new WorldMap();
        Inventory inventory = new Inventory();
        Player player = new Player(playerName, playerGender, inventory, worldMap, scanner);
        Thread clockThread = new Thread(gameClock);
        clockThread.start();

        FarmMapController map = new FarmMapController(scanner);
        String input;
        
        do {
            System.out.println("Time: " + gameClock.getFormattedTime());
            System.out.println("Date: " + gameClock.getFormattedDate());
            System.out.println("Weather: " + gameClock.getTodayWeather());
            System.out.println("---------------");
            
            map.forceSleep(player);
            map.checkLand(player);
            map.displayFarmMap();
            boolean[] around = map.isAroundList(player);

            System.out.print("Enter a command: ");
            input = scanner.nextLine();
            System.out.println("You entered: " + input);

            if (input.equals("w") || input.equals("s") || input.equals("a") || input.equals("d")) {
                map.movePlayer(player, input);
            }

            if (input.equals("e")) {
                map.interact(player);
            }

            if (input.equals("f")) {
                map.tileInteract(player);
            }

            if (input.equals("i")) {
                player.openInventory();
            }

        } while (!input.equalsIgnoreCase("exit"));

        gameClock.stopClock();
        try {
            clockThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        scanner.close();
        System.out.println("Game exited.");
    }
}
