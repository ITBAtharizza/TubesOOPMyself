package org.example.map;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.example.map.objects.DeployedObject;
import org.example.map.objects.objects.House;
import org.example.map.objects.objects.Pond;
import org.example.map.objects.objects.ShippingBin;

public class FarmMap {
    private static final int MAP_SIZE = 32;
    private Tile[][] grid;
    private Random random;
    private House house;
    private Pond pond;
    private ShippingBin shippingBin;

    public FarmMap() {
        this.grid = new Tile[MAP_SIZE][MAP_SIZE];
        this.random = new Random();
        initializeGrid();
        placeObjects();
    }

    private void initializeGrid() {
        for (int i = 0; i < MAP_SIZE; i++) {
            for (int j = 0; j < MAP_SIZE; j++) {
                grid[i][j] = new Tile(); 
            }
        }
    }

    private void placeObjects() {
        house = new House();
        placeObjectRandomly(house);

        boolean housePlacedSuccessfully = house != null && 
                                          (house.getX() != 0 || house.getY() != 0 || 
                                          (house.getY() < MAP_SIZE && house.getX() < MAP_SIZE && 
                                           grid[house.getY()][house.getX()].getDeployedObjectChar() == 'h'));
        
        if (housePlacedSuccessfully) {
            shippingBin = new ShippingBin();
            placeShippingBinNearHouse(shippingBin, house);
        } else {
            System.err.println("Error: House was not placed successfully. Cannot place Shipping Bin.");
        }

        pond = new Pond();
        placeObjectRandomly(pond);
    }

    private void placeObjectRandomly(DeployedObject object) {
        if (object == null) {
            System.err.println("Error: Attempted to place a null object.");
            return;
        }
        boolean placed = false;
        int attempts = 0; 
        while (!placed && attempts < 1024) { 
            int x = random.nextInt(MAP_SIZE - object.getWidth() + 1);
            int y = random.nextInt(MAP_SIZE - object.getHeight() + 1);

            if (canPlaceObject(x, y, object.getWidth(), object.getHeight())) {
                deployObjectOnMap(object, x, y);
                placed = true;
            }
            attempts++;
        }
        if (!placed) {
            System.err.println("Warning: Could not place object '" + object.getNotation() + "' after " + attempts + " attempts. Map might be too full or object too large.");
        }
    }

    private void placeShippingBinNearHouse(ShippingBin bin, House houseObj) {
        if (bin == null || houseObj == null) {
             System.err.println("Error: ShippingBin or House object is null in placeShippingBinNearHouse.");
             return;
        }
        boolean houseActuallyPlaced = houseObj.getX() >= 0 && houseObj.getY() >= 0 &&
                                 houseObj.getY() < MAP_SIZE && houseObj.getX() < MAP_SIZE &&
                                 grid[houseObj.getY()][houseObj.getX()].getDeployedObjectChar() == 'h';
        if (!houseActuallyPlaced) {
            System.err.println("Error: House for shipping bin placement appears not to be properly placed on the grid. Fallback to random for bin.");
            placeObjectRandomly(bin);
            return;
        }

        List<Point> potentialTopLeftCorners = new ArrayList<>();

        int by_above = houseObj.getY() - bin.getHeight() - 1;
        for (int bx = houseObj.getX() - bin.getWidth() + 1; bx <= houseObj.getX() + houseObj.getWidth() -1; bx++) {
            potentialTopLeftCorners.add(new Point(bx, by_above));
        }

        int by_below = houseObj.getY() + houseObj.getHeight() + 1;
         for (int bx = houseObj.getX() - bin.getWidth() + 1; bx <= houseObj.getX() + houseObj.getWidth() -1; bx++) {
            potentialTopLeftCorners.add(new Point(bx, by_below));
        }

        int bx_left = houseObj.getX() - bin.getWidth() - 1;
        for (int by = houseObj.getY() - bin.getHeight() + 1; by <= houseObj.getY() + houseObj.getHeight() - 1; by++) {
            potentialTopLeftCorners.add(new Point(bx_left, by));
        }

        int bx_right = houseObj.getX() + houseObj.getWidth() + 1;
         for (int by = houseObj.getY() - bin.getHeight() + 1; by <= houseObj.getY() + houseObj.getHeight() - 1; by++) {
            potentialTopLeftCorners.add(new Point(bx_right, by));
        }
        
        Collections.shuffle(potentialTopLeftCorners, random);

        for (Point pos : potentialTopLeftCorners) {
            int x = pos.getX();
            int y = pos.getY();
            if (x >= 0 && y >= 0 &&
                x + bin.getWidth() <= MAP_SIZE &&
                y + bin.getHeight() <= MAP_SIZE &&
                canPlaceObject(x, y, bin.getWidth(), bin.getHeight())) {
                deployObjectOnMap(bin, x, y);
                return; 
            }
        }
        System.err.println("Warning: Could not place shipping bin ('" + bin.getNotation() + "') near the house. Placing randomly as fallback.");
        placeObjectRandomly(bin); 
    }

    private boolean canPlaceObject(int x, int y, int width, int height) {
        if (x < 0 || y < 0 || x + width > MAP_SIZE || y + height > MAP_SIZE) {
            return false;
        }

        for (int i = y; i < y + height; i++) {
            for (int j = x; j < x + width; j++) {
                if (i < 0 || i >= MAP_SIZE || j < 0 || j >= MAP_SIZE) { 
                    return false; 
                }
                if (grid[i][j].getDeployedObjectChar() != '.') { 
                    return false; 
                }
            }
        }
        return true;
    }

    private void deployObjectOnMap(DeployedObject object, int x, int y) {
        if (object == null) {
             System.err.println("Error: Attempted to deploy a null object on map.");
             return;
        }
        object.setX(x);
        object.setY(y);
        for (int i = y; i < y + object.getHeight(); i++) {
            for (int j = x; j < x + object.getWidth(); j++) {
                 if (i >= 0 && i < MAP_SIZE && j >= 0 && j < MAP_SIZE) { 
                    grid[i][j].setDeployedObjectChar(object.getNotation());
                }
            }
        }
        System.out.println(object.getClass().getSimpleName() + " ('" + object.getNotation() + "') placed at (" + x + ", " + y + ") with size " + object.getWidth() + "x" + object.getHeight());
    }

    public void displayMap() {
        for (int i = 0; i < MAP_SIZE; i++) {
            for (int j = 0; j < MAP_SIZE; j++) {
                System.out.print(grid[i][j].toString() + " ");
            }
            System.out.println();
        }
        System.out.println("\nLegend:");
        
        boolean houseIsPlaced = house != null && (house.getX() != 0 || house.getY() != 0 || (house.getY() < MAP_SIZE && house.getX() < MAP_SIZE && house.getY() >=0 && house.getX() >=0 && grid[house.getY()][house.getX()].getDeployedObjectChar() == 'h'));
        boolean pondIsPlaced = pond != null && (pond.getX() != 0 || pond.getY() != 0 || (pond.getY() < MAP_SIZE && pond.getX() < MAP_SIZE && pond.getY() >=0 && pond.getX() >=0 && grid[pond.getY()][pond.getX()].getDeployedObjectChar() == 'o'));
        boolean shippingBinIsPlaced = shippingBin != null && (shippingBin.getX() != 0 || shippingBin.getY() != 0 || (shippingBin.getY() < MAP_SIZE && shippingBin.getX() < MAP_SIZE && shippingBin.getY() >=0 && shippingBin.getX() >=0 && grid[shippingBin.getY()][shippingBin.getX()].getDeployedObjectChar() == 's'));

        if (houseIsPlaced) {
            System.out.println("h: House (" + house.getWidth() + "x" + house.getHeight() + ") at (" + house.getX() + "," + house.getY() + ")");
        } else {
            System.out.println("h: House (not placed)");
        }
        if (pondIsPlaced) {
            System.out.println("o: Pond (" + pond.getWidth() + "x" + pond.getHeight() + ") at (" + pond.getX() + "," + pond.getY() + ")");
        } else {
             System.out.println("o: Pond (not placed)");
        }
        if (shippingBinIsPlaced) {
            System.out.println("s: Shipping Bin (" + shippingBin.getWidth() + "x" + shippingBin.getHeight() + ") at (" + shippingBin.getX() + "," + shippingBin.getY() + ")");
        } else {
            System.out.println("s: Shipping Bin (not placed)");
        }
        System.out.println("'.': Empty usable tile\n"); 
    }

    public Tile getTile(int x, int y) {
        if (x >= 0 && x < MAP_SIZE && y >= 0 && y < MAP_SIZE) {
            return grid[y][x];
        } else {
            System.out.println("Invalid coordinates.");
            return null;
        }
    }

    public void setTile(int x, int y, Tile tile) {
        if (x >= 0 && x < MAP_SIZE && y >= 0 && y < MAP_SIZE) {
            grid[y][x] = tile;
        }
    }

    public House getHouse() {
        return house;
    }

    public void setHouse(House house) {
        this.house = house;
    }

    public Pond getPond() {
        return pond;
    }

    public void setPond(Pond pond) {
        this.pond = pond;
    }

    public ShippingBin getShippingBin() {
        return shippingBin;
    }

    public void setShippingBin(ShippingBin shippingBin) {
        this.shippingBin = shippingBin;
    }
}
