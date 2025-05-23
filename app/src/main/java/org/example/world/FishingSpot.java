package org.example.world;

import org.example.Player;

public class FishingSpot extends WorldLocation {
    private String locationType;

    public FishingSpot(String locationType) {
        super(locationType);
        this.locationType = locationType;
    }

    @Override
    public void interact(Player player) {
        System.out.println("----" + locationType + "----");
        player.fishing(locationType);
    }
}
