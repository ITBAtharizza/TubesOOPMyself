package org.example.map;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.example.Player;
import org.example.map.objects.DeployedObject;
import org.example.time.GameClock;

public class FarmMapController {
    private FarmMap farmMap;
    private Tile hiddenTile;
    private Scanner scanner;
    private final GameClock gameClock = GameClock.getInstance();
    
    public FarmMapController(Scanner scanner) {
        this.farmMap = new FarmMap();
        this.farmMap.setTile(0, 0, new Tile() {{ setDeployedObjectChar('p'); }});
        this.hiddenTile = new Tile();
        this.scanner = scanner;
    }

    public void displayFarmMap() {
        farmMap.displayMap();
    }

    public void movePlayer(Player player, String direction) {
        int x = player.getPosition().getX();
        int y = player.getPosition().getY();

        switch (direction) {
            case "w" -> y--;
            case "s" -> y++;
            case "a" -> x--;
            case "d" -> x++;
        }

        if (x < 0 || y < 0 || x > 31 || y > 31) {
            player.visiting();
            return;
        }

        if (!placePlayerAt(player, x, y)) System.out.println("You can't move there, the tile is not usable.");
        else System.out.println("Player moved to: (" + x + ", " + y + ")");
    }

    public void interact(Player player) {
        int x = player.getPosition().getX();
        int y = player.getPosition().getY();

        boolean[] around = isAroundList(player);
        List<Runnable> actions = getActions(around, player);
        List<String> options = getOptions(around);

        if (options.isEmpty()) {
            System.out.println("There's nothing to interact with here.");
            return;
        }
        
        System.out.println("You're near something:");
        for (int i = 0; i < options.size(); i++) {
            System.out.printf("[%d] %s\n", i + 1, options.get(i));
        }

        System.out.print("Press number to choose: ");
        
        try {
            int choice = Integer.parseInt(scanner.nextLine()) - 1;
            if (choice >= 0 && choice < actions.size()) actions.get(choice).run();
            else System.out.println("Invalid choice.");
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number.");
        }
    }

    public void sell(Player player){
        player.selling(farmMap.getShippingBin(), isAround(farmMap.getShippingBin(), player));
    }
    
    public void checkLand(Player player) {        
        for (int x = 0; x < 32; x++) {
            for (int y = 0; y < 32; y++) {
                Tile tile = farmMap.getTile(x, y);
                if (tile instanceof PlantedTile plantedTile) {
                    if (plantedTile.checkSeasonalDeath(gameClock.getDate().getSeason())) {
                        System.out.println("Seed died due to season.");
                        farmMap.setTile(x, y, new Tile());
                        continue;
                    }

                    plantedTile.updateGrowth(gameClock.getDate().getDay(), gameClock.getTodayWeather().equals("Rainy"));

                    if (plantedTile.isReadyToHarvest()) {
                        System.out.println("Plant is ready to harvest!");
                    }
                }
            }
        }
    }

    public void forceSleep(Player player) {
        int hour = gameClock.getTime().getHour();

        boolean mustSleep = (player.getEnergy() == -20) || (hour == 2 && !player.hasSleptTonight());

        if (mustSleep) {
            System.out.println("You are too tired or it's 2 AM. Forced to sleep.");
            player.sleeping();

            putAround(farmMap.getHouse(), player);
        }
    }

    public void putAround(DeployedObject deployedObject, Player player) {
        int xLeft = Math.max(0, deployedObject.getX());
        int yTop = Math.max(0, deployedObject.getY());
        int xRight = Math.min(31, xLeft + deployedObject.getWidth() - 1);
        int yBottom = Math.min(31, yTop + deployedObject.getHeight() - 1);

        for (int x = xLeft; x <= xRight; x++) {
            int targetY = yTop - 1;
            if (targetY >= 0 && placePlayerAt(player, x, targetY)) return;
        }

        for (int x = xLeft; x <= xRight; x++) {
            int targetY = yBottom + 1;
            if (targetY <= 31 && placePlayerAt(player, x, targetY)) return;
        }

        for (int y = yTop; y <= yBottom; y++) {
            int targetX = xLeft - 1;
            if (targetX >= 0 && placePlayerAt(player, targetX, y)) return;
        }

        for (int y = yTop; y <= yBottom; y++) {
            int targetX = xRight + 1;
            if (targetX <= 31 && placePlayerAt(player, targetX, y)) return;
        }
    }

    public boolean isAround(DeployedObject deployedObject, Player player) {
        int x = player.getPosition().getX();
        int y = player.getPosition().getY();
        
        int xLeft = deployedObject.getX();
        int yTop = deployedObject.getY();
        int xRight = xLeft + deployedObject.getWidth() - 1;
        int yBottom = yTop + deployedObject.getHeight() - 1;

        boolean aroundTop = (y == yTop - 1 && x >= xLeft && x <= xRight);
        boolean aroundBottom = (y == yBottom + 1 && x >= xLeft && x <= xRight);
        boolean aroundLeft = (x == xLeft - 1 && y >= yTop && y <= yBottom);
        boolean aroundRight = (x == xRight + 1 && y >= yTop && y <= yBottom);

        return aroundTop || aroundBottom || aroundLeft || aroundRight;
    }

    public boolean[] isAroundList(Player player){
        int x = player.getPosition().getX();
        int y = player.getPosition().getY();
        boolean[] around = new boolean[3];
        around[0] = isAround(farmMap.getHouse(), player);
        around[1] = isAround(farmMap.getPond(), player);
        around[2] = isAround(farmMap.getShippingBin(), player);
        return around;
    }

    public List<String> getOptions(boolean[] around) {
        List<String> options = new ArrayList<>();
        if (around[0]) options.add("Enter House");
        if (around[1]) options.add("Fish in Pond");
        if (around[2]) options.add("Open Shipping Bin");
        return options;
    }

    public List<Runnable> getActions(boolean[] around, Player player) {
        List<Runnable> actions = new ArrayList<>();
        if (around[0]) actions.add(() -> farmMap.getHouse().interact(player));
        if (around[1]) actions.add(() -> farmMap.getPond().interact(player));
        if (around[2]) actions.add(() -> farmMap.getShippingBin().interact(player));
        return actions;
    }

    public void tileInteract(Player player) {
        int x = player.getPosition().getX();
        int y = player.getPosition().getY();
        Tile tile = hiddenTile;
    
        List<String> options = getTileOptions(player);
        List<Runnable> actions = getTileActions(player);
        
        for (int i = 0; i < options.size(); i++) System.out.printf("[%d] %s\n", i + 1, options.get(i));
        
        System.out.print("Press number to choose: ");
        
        try {
            int choice = Integer.parseInt(scanner.nextLine()) - 1;
            if (choice >= 0 && choice < actions.size()) actions.get(choice).run();
            else System.out.println("Invalid choice.");
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number.");
        }
    }

    public List<String> getTileOptions(Player player){
        List<String> options = new ArrayList<>();
        Tile tile = hiddenTile;
        if (tile.isTillable()) {
            options.add("Till the land");
        } else if (tile.isTilled()) {
            options.add("Recover the land");
            options.add("Plant seeds");
        } else if (tile.isPlanted() || tile.isHarvestable()) {
            options.add("Harvest crops");
        }
        return options;
    }

    public List<Runnable> getTileActions(Player player){
        List<Runnable> actions = new ArrayList<>();
        Tile tile = hiddenTile;
        if (tile.isTillable()) {
            actions.add(() -> player.tilling(tile));
        } else if (tile.isTilled()) {
            actions.add(() -> player.recoverLand(tile));
            actions.add(() -> { hiddenTile = player.planting(tile); });
        } else if (tile.isPlanted() || tile.isHarvestable()) {
            actions.add(() -> player.harvesting(tile));
        }
        return actions;
    }

    public boolean placePlayerAt(Player player, int x, int y) {
        if (!farmMap.getTile(x, y).isUsable()) return false;

        int oldX = player.getPosition().getX();
        int oldY = player.getPosition().getY();
        farmMap.setTile(oldX, oldY, hiddenTile);

        player.moving(x, y);

        hiddenTile = farmMap.getTile(x, y);

        Tile playerTile = new Tile();
        playerTile.setDeployedObjectChar('p');
        farmMap.setTile(x, y, playerTile);

        return true;
    }
}
