package org.example.items.equipment;

import org.example.Player;

public class Hoe extends Equipments {
    public Hoe() {
        super("Hoe");
    }

    @Override
    public void onUse(Player player) {
        System.out.println("You till the soil using the hoe.");
    }
}
