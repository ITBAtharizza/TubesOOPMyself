package org.example.map.objects;

import org.example.Player;

public abstract class DeployedObject {
    protected int width;
    protected int height;
    protected char notation;
    protected int x;
    protected int y;
    protected Interact behavior;

    public DeployedObject(int width, int height, char notation, Interact behavior) {
        this.width = width;
        this.height = height;
        this.notation = notation;
        this.behavior = behavior;
    }

    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public char getNotation() { return notation; }

    public int getX() { return x; }
    public void setX(int x) { this.x = x; }
    public int getY() { return y; }
    public void setY(int y) { this.y = y; }

    public void interact(Player player) {
        if (behavior != null) behavior.interact(player);
    }
}
