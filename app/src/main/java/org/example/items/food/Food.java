package org.example.items.food;

import org.example.Player;
import org.example.items.Edible;
import org.example.items.Items;

public class Food extends Items implements Edible {
    private int energyGained;
    
    public Food(String name, int energyGained, int sellPrice, int buyPrice) {
        super(name, sellPrice, buyPrice, true);
        this.energyGained = energyGained;
    }

    public int getEnergyGained() {
        return energyGained;
    }

    @Override
    public void onEat(Player player) {
        player.addEnergy(energyGained);
        System.out.println("You ate " + getName() + " and gained " + energyGained + " energy.");
    }
}
