package org.example.items.equipment;

import org.example.Player;

public class WateringCan extends Equipments {
    public WateringCan() {
        super("Watering Can");
    }

    @Override
    public void onUse(Player player) {
        System.out.println("You water the crops with the watering can.");
    }
}
