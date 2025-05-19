package org.example;


public class Player {
    private String name;
    private String gender;
    private int energy = 100;
    private String farmName = null;
    private NPC partner = null;
    private Inventory inventory = new Inventory();
    private Point location = new Point();
}
