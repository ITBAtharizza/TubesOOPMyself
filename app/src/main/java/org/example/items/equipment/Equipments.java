package org.example.items.equipment;

import org.example.Player;
import org.example.items.Items;

public class Equipments extends Items {
    public Equipments(String name) {
        super(name, null, null, false);
    }

    public void onUse(Player player) {
        System.out.println("You use the " + getName() + ".");
    }
}
