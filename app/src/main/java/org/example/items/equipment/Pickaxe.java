package org.example.items.equipment;

import org.example.Player;

public class Pickaxe extends Equipments {
    public Pickaxe() {
        super("Pickaxe");
    }

    @Override
    public void onUse(Player player) {
        System.out.println("You mine rocks using the pickaxe.");
    }
    
}
