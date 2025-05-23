package org.example.world;

import org.example.Player;

public abstract class WorldLocation {
    protected String name;

    public WorldLocation(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract void interact(Player player);
}
