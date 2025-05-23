package org.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import org.example.items.Edible;
import org.example.items.ItemFactory;
import org.example.items.Items;
import org.example.items.crops.Crops;
import org.example.items.equipment.Equipments;
import org.example.items.fish.Fish;
import org.example.items.fish.FishRarity;
import org.example.items.food.Food;
import org.example.items.miscellaneous.Miscellaneous;
import org.example.items.seeds.Seeds;
import org.example.map.PlantedTile;
import org.example.map.Point;
import org.example.map.Tile;
import org.example.map.objects.objects.ShippingBin;
import org.example.npc.NPC;
import org.example.time.GameClock;
import org.example.time.GameClockSnapshot;
import org.example.time.GameTime;
import org.example.world.WorldMap;

public class Player {
    private String name;
    private String gender;
    private int energy = 100;
    private String farmName = null;
    private NPC partner = null;
    private int gold = 0;
    private Inventory inventory;
    private Point position = new Point();

    private int hasSlept = -1;
    private int hasFished = 0;

    private static final int MAX_ENERGY = 100;
    private static final int MIN_ENERGY = -20;

    private final GameClock gameClock = GameClock.getInstance();
    private WorldMap worldMap;
    private Scanner scanner;

    public Player(String name, String gender, Inventory inventory, WorldMap worldMap, Scanner scanner) {
        this.name = name;
        this.gender = gender;
        this.inventory = inventory;
        this.worldMap = worldMap;
        this.scanner = scanner;
        this.gold = 1000;
    }

    public String getName() { return name; }
    public String getGender() { return gender; }
    public int getEnergy() { return energy; }
    public String getFarmName() { return farmName; }
    public Inventory getInventory() { return inventory; }
    public Point getPosition() { return position; }
    public int getGold() { return gold; }

    public void setName(String name) { this.name = name; }
    public void setGender(String gender) { this.gender = gender; }
    public void setGold(int gold) { this.gold = Math.max(0, gold); }

    public void setEnergy(int energy) {
        if (energy > MAX_ENERGY) this.energy = MAX_ENERGY;
        else if (energy < MIN_ENERGY) this.energy = MIN_ENERGY;
        else this.energy = energy;
    }

    public void addEnergy(int energy) { setEnergy(this.energy + energy); }
    public void subtractEnergy(int energy) { setEnergy(this.energy - energy); }
    public void setFarmName(String farmName) { this.farmName = farmName; }
    public void setPosition(Point position) { this.position = position; }

    public void addItemToInventory(Items item, int quantity) { inventory.addItem(item, quantity); }
    public void removeItemFromInventory(Items item, int quantity) { inventory.removeItem(item, quantity); }
    public int getItemQuantity(Items item) { return inventory.getItemQuantity(item); }
    public void clearInventory() { inventory.clear(); }
    public boolean isInventoryEmpty() { return inventory.isEmpty(); }
    public Items getItemFromInventory(String itemName) { return inventory.get(itemName); }
    
    public boolean hasSleptTonight() { return hasSlept == gameClock.getCurrentNightCycle(); }
    public int hasFished() { return hasFished; }

    public Scanner getScanner() { return scanner; }

    public void moveTo(Point newPosition) { this.position = newPosition; }

    public void tilling(Tile tile) {
        if (tile.isTillable()) {
            Equipments hoe = (Equipments) (inventory.get("Hoe"));
            hoe.onUse(this);
            tile.setDeployedObjectChar('t');
            System.out.println("You have tilled the land.");
            this.energy -= 5;
            gameClock.advanceTimeSkip(5);
            System.out.println("Time skips five minutes.");
        } else {
            System.out.println("This land is not tillable.");
        }
    }

    public void recoverLand(Tile tile) {
        if (tile.isTilled()) {
            Equipments pickaxe = (Equipments) (inventory.get("Hoe"));
            pickaxe.onUse(this);
            tile.setDeployedObjectChar('.');
            System.out.println("You have recovered the land.");
            this.energy -= 5;
            gameClock.advanceTimeSkip(5);
            System.out.println("Time skips five minutes.");
        } else {
            System.out.println("This land is not recoverable.");
        }
    }

    public Tile planting(Tile tile) {
        if (!tile.isTilled()) {
            System.out.println("This land is not tillable or the seed is invalid.");
            return tile;
        }

        List<Seeds> seedsList = new ArrayList<>();
        List<Integer> amounts = new ArrayList<>();
        for (Items item : inventory.getItems().keySet()) {
            if (item instanceof Seeds) {
                seedsList.add((Seeds) item);
                amounts.add(getItemQuantity(item));
            }
        }

        if (seedsList.isEmpty()) {
            System.out.println("You have no seeds to plant.");
            return tile;
        }

        System.out.println("Available seeds:");
        for (int i = 0; i < seedsList.size(); i++) {
            System.out.println((i + 1) + ". " + seedsList.get(i).getName() + " (x" + amounts.get(i) + ")");
        }

        System.out.print("Enter the number of the seed you want to plant: ");
        int choice;
        try {
            choice = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
            return tile;
        }

        if (choice < 1 || choice > seedsList.size()) {
            System.out.println("Invalid choice.");
            return tile;
        }

        Seeds selectedSeed = seedsList.get(choice - 1);

        tile = new PlantedTile(selectedSeed, gameClock.getDate().getDay());
        removeItemFromInventory(selectedSeed, 1);
        System.out.println("You have planted " + selectedSeed.getName() + ".");
        this.energy -= 5;
        gameClock.advanceTimeSkip(5);
        System.out.println("Time skips five minutes.");
        return tile;
    }

    public void watering(Tile tile) {
        if (tile.isPlanted()){
            PlantedTile plantedTile = (PlantedTile) tile;
            Equipments wateringCan = (Equipments) inventory.get("Watering Can");
            wateringCan.onUse(this);
            plantedTile.water(gameClock.getDate().getDay());
            this.energy -= 5;
            gameClock.advanceTimeSkip(5);
            System.out.println("Time skips five minutes.");
        } else System.out.println("You can't water this tile");
    }

    public void harvesting(Tile tile) {
        if (tile.isHarvestable()){
            PlantedTile plantedTile = (PlantedTile) tile;
            if (plantedTile.isReadyToHarvest()){
                String seedName = plantedTile.getSeed().getName();
                String cropName;

                if (seedName != null && seedName.endsWith(" Seeds")) cropName = seedName.substring(0, seedName.length() - " Seeds".length());
                else cropName = seedName;

                ItemFactory cropsFactory = new ItemFactory();
                cropsFactory.loadCrops();

                Items cropItem = cropsFactory.get(cropName);
                Crops crop = (Crops) cropItem;
                inventory.addItem(crop, crop.getHarvestedAmount());

                tile = new Tile();
                this.energy -= 5;
                gameClock.advanceTimeSkip(5);
                System.out.println("Time skips five minutes.");
            } else System.out.println("You can't harvest this tile yet");
        } else System.out.println("You can't harvest this tile");
    }

    public void eating(Items item) {
        if (item != null && getItemQuantity(item) > 0) {
            if (item instanceof Edible edibleItem) {
                edibleItem.onEat(this);
                gameClock.advanceTimeSkip(5);
                System.out.println("Time skips five minutes.");
                removeItemFromInventory(item, 1);
            } else System.out.println("This item is not edible.");
        } else System.out.println("You do not have this item in your inventory.");
    }

    public void sleeping() {
        synchronized (gameClock) {
            int currentHour = gameClock.getTime().getHour();
            int currentMinute = gameClock.getTime().getMinute();

            int minutesUntilMidnight = (24 - currentHour) * 60 - currentMinute;
            int minutesAfterMidnightToSix = 6 * 60;

            int totalMinutesToSkip = minutesUntilMidnight + minutesAfterMidnightToSix;

            gameClock.advanceTimeSkip(totalMinutesToSkip);
        }

        if (energy == 0) energy = 10;
        else if (energy < MAX_ENERGY / 10) energy = MAX_ENERGY / 2;
        else if (energy > MAX_ENERGY / 10) energy = MAX_ENERGY;
        System.out.println("You slept. Time skipped to 6:00 next day.");
    }

    public void cooking() {}

    public void fishing(String location) {
        gameClock.stopClock();
        ItemFactory itemFactory = new ItemFactory();
        itemFactory.loadFish();

        List<Fish> availableFish = new ArrayList<>();

        for (Items item : ItemFactory.getAllItems().values()) {
            if (item instanceof Fish fish) {
                if (fish.isAvailable(gameClock.getDate().getSeason(), gameClock.getTodayWeather(), location, gameClock.getTime().getHour())) {
                    availableFish.add(fish);
                }
            }
        }

        System.out.println("Available fish at this location:");
        for (Fish fish : availableFish) System.out.println("- " + fish.getName() + " (" + fish.getRarity() + ")");

        int i = availableFish.size();
        if (i == 0) {
            System.out.println("No fish available in this location right now.");
            return;
        }

        int randomIndex = (int) (Math.random() * i);
        Fish fish = availableFish.get(randomIndex);

        System.out.println("You caught a " + fish.getName() + "!");
        System.out.println("It is a " + fish.getRarity() + " fish.");
        System.out.println("To get it, you must play a little game.");

        int range;
        if (fish.getRarity() == FishRarity.COMMON) range = 10;
        else if (fish.getRarity() == FishRarity.REGULAR) range = 100;
        else range = 500;
        
        int randomNumber = (int) (Math.random() * range) + 1;

        System.out.println("You need to guess a number between 1 and " + range + " to catch the fish.");
        System.out.println("You only have 10 attempts.");

        int j = 0;
        boolean caught = false;

        while (j < 10) {
            System.out.print("Enter your guess: ");
            String input = scanner.nextLine();
            int guess;

            try {
                guess = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
                continue;
            }

            if (guess == randomNumber) {
                System.out.println("Congratulations! You caught the fish!");
                addItemToInventory(fish, 1);
                caught = true;
                hasFished++;
                break;
            }
            if (guess < randomNumber) System.out.println("Too low! Try again.");
            else System.out.println("Too high! Try again.");

            j++;
        }

        if (!caught){
            System.out.println("You failed to catch the fish. Better luck next time!");
            System.out.println("The correct number was: " + randomNumber);
        }

        System.out.println("You lost 5 energy.");
        this.energy -= 5;
        System.out.println("Time skips fifteen minutes.");
        gameClock.advanceTimeSkip(15);
    }

    public void proposing(NPC npc) {
        if (npc.getHeartPoints() < 150){
            System.out.println("You are rejected. You need to increase your heart points.");
            this.energy -= 20;
        } else {
            System.out.println("You are now engaged to " + npc.getName() + ".");
            this.partner = npc;
            this.partner.setRelationshipStatus("Fiance");
            this.energy -= 10;
        }
        gameClock.advanceTimeSkip(60);
        System.out.println("Time skips one hour.");
    }

    public void marry(NPC partner) {
        if (partner.getRelationshipStatus().equals("Married")) {
            System.out.println("You are already married to " + partner.getName() + ".");
        } else {
            GameClockSnapshot lastProposalTime = partner.getChangeLog().get("Fiance");
            if (lastProposalTime != null) {
                GameTime lastTime = lastProposalTime.getTime();
                GameTime currentTime = gameClock.getTime();

                GameTime thresholdTime = currentTime.minusHours(24);

                if (lastTime.isAfter(thresholdTime)) {
                    System.out.println("You cannot be married so soon.");
                } else {
                    partner.setRelationshipStatus("Spouse");
                    System.out.println("You are now married to " + partner.getName() + ".");
                    partner.setRelationshipStatus("Spouse");
                    this.energy -= 80;

                    synchronized (gameClock) {
                        int currentHour = gameClock.getTime().getHour();
                        int currentMinute = gameClock.getTime().getMinute();

                        int targetHour = 22;
                        int totalCurrentMinutes = currentHour * 60 + currentMinute;
                        int totalTargetMinutes = targetHour * 60;

                        int minutesToAdvance;
                        if (totalCurrentMinutes < totalTargetMinutes) minutesToAdvance = totalTargetMinutes - totalCurrentMinutes;
                        else minutesToAdvance = (24 * 60 - totalCurrentMinutes) + totalTargetMinutes;

                        gameClock.advanceTimeSkip(minutesToAdvance);
                    }

                    System.out.println("Time skips to 22:00.");
                }
            } else System.out.println("You must make this NPC a fiance first!");
        }
    }

    public void watching() {
        System.out.println("You are now watching television.");
        this.energy -= 5;
        gameClock.advanceTimeSkip(15);
        System.out.println("Time skips fifteen minutes.");
    }

    public void visiting() {
        System.out.println("----" + "VISITING" + "----");
        System.out.println("Where do you want to visit?");

        worldMap.showLocations();

        System.out.print("Enter your choice : ");

        int choice;
        try {
            choice = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid number. Going back to farm...");
            
            return;
        }

        if (choice >= 1 && choice <= 9) worldMap.visitLocation(choice, this);
        else {
            System.out.println("Invalid number. Going back to farm...");
            return;  
        }

        this.energy -= 10;
        gameClock.advanceTimeSkip(15);
        System.out.println("Time skips fifteen minutes.");
    }

    public void chatting(NPC npc) {
        npc.setHeartPoints(npc.getHeartPoints() + 10);
        this.energy -= 10;
        System.out.println("You chatted with " + npc.getName() + ". (+10 heart points)");
        gameClock.advanceTimeSkip(10);
        System.out.println("Time skips ten minutes.");
    }

    public void gifting(NPC npc) {
        List<Items> giftableItems = new ArrayList<>();
        for (Items item : inventory.getItems().keySet()) {
            if (!(item instanceof Equipments)) {
                giftableItems.add(item);
            }
        }

        if (giftableItems.isEmpty()) {
            System.out.println("You have no giftable items in your inventory.");
            return;
        }

        System.out.println("Choose an item to gift to " + npc.getName() + ":");
        for (int i = 0; i < giftableItems.size(); i++) {
            Items item = giftableItems.get(i);
            System.out.println((i + 1) + ". " + item.getName() + " (x" + getItemQuantity(item) + ")");
        }
        System.out.print("Enter the number of the item: ");
        int choice;
        try {
            choice = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
            return;
        }

        if (choice < 1 || choice > giftableItems.size()) {
            System.out.println("Invalid choice.");
            return;
        }

        Items item = giftableItems.get(choice - 1);
        int maxAmount = getItemQuantity(item);
        System.out.println("You have " + maxAmount + " of this item. Enter the amount you want to gift:");
        int amount;
        try {
            amount = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount.");
            return;
        }

        if (amount <= 0 || amount > maxAmount) {
            System.out.println("Invalid amount.");
            return;
        }

        removeItemFromInventory(item, amount);

        int heartPointsChange = 0;
        String message = npc.getName() + " felt neutral about your gift.";
        if (Arrays.asList(npc.getLovedItems()).contains(item)) {
            heartPointsChange = 25 * amount;
            message = npc.getName() + " loved your gift! (+" + (25 * amount) + " heart points)";
        } else if (Arrays.asList(npc.getLikedItems()).contains(item)) {
            heartPointsChange = 20 * amount;
            message = npc.getName() + " liked your gift! (+" + (20 * amount) + " heart points)";
        } else if (Arrays.asList(npc.getHatedItems()).contains(item)) {
            heartPointsChange = -25 * amount;
            message = npc.getName() + " hated your gift! (" + (-25 * amount) + " heart points)";
        }
        npc.addHeartPoints(heartPointsChange);
        System.out.println(message);

        this.energy -= 5 * amount;
        gameClock.advanceTimeSkip(10 * amount);
        System.out.println("Time skips " + (10 * amount) + " minutes.");
    }

    public void moving(int x, int y) {
        setPosition(new Point(x, y));
    }

    public void openInventory() {
        System.out.println("Your Inventory:");
        System.out.println("+----------------------+--------+");
        System.out.println("| Item                 | Amount |");
        System.out.println("+----------------------+--------+");

        List<Class<?>> subclasses = Arrays.asList(Equipments.class, Seeds.class, Crops.class, Fish.class, Food.class, Miscellaneous.class);

        for (Class<?> subclass : subclasses) {
            boolean hasSubclassItems = false;
            for (Items item : inventory.getItems().keySet()) {
                if (subclass.isInstance(item)) {
                    hasSubclassItems = true;
                    break;
                }
            }

            if (!hasSubclassItems) continue;
            String subclassName = subclass.getSimpleName();
            System.out.printf("| %-20s |   --   |\n", subclassName + ":");
            for (Items item : inventory.getItems().keySet()) {
                if (subclass.isInstance(item)) {
                    if (item instanceof Equipments) {
                        System.out.printf("|   %-18s |   --   |\n", item.getName());
                    } else {
                        System.out.printf("|   %-18s | %6d |\n", item.getName(), getItemQuantity(item));
                    }
                }
            }
        }
        System.out.println("+----------------------+--------+");
    }

    public void showTime(GameClock gameClock) {
        System.out.println("Current time: " + gameClock.getFormattedTime());
        System.out.println("Current date: " + gameClock.getFormattedDate());
        System.out.println("Current weather: " + gameClock.getTodayWeather());
    }

    public void showPosition() { 
        System.out.println("You are at " + position.getX() + ", " + position.getY()); 
    }

    public void selling(ShippingBin shippingBin, boolean isAround) {
        if (isAround){
            shippingBin.interact(this);    
        }
        System.out.println("You must be near Shipping Bin to perform this action!");
    }
}
