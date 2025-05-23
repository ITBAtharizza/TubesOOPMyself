package org.example.items.crops;

import org.example.Player;
import org.example.items.Edible;
import org.example.items.Items;

public class Crops extends Items implements Edible {
    private int harvestedAmount;
    private boolean isHarvested = false;

    private static final int ENERGY_GAINED = 3;


    public Crops(String name, int harvestedAmount, int sellPrice, int buyPrice) {
        super(name, sellPrice, buyPrice, false);
        this.harvestedAmount = harvestedAmount;
    }

    public int getHarvestedAmount() { return harvestedAmount; }

    public void setHarvestedAmount(int harvestedAmount) {
        this.harvestedAmount = harvestedAmount;
    }

    public void setHarvested(boolean harvested) {
        this.isHarvested = harvested;
    }

    public void setEdible(){
        super.setEdible(isHarvested);
    }

    public int getEnergyGained() { return ENERGY_GAINED; }

    @Override
    public void onEat(Player player) {
        player.addEnergy(ENERGY_GAINED);
        System.out.println("You ate " + getName() + " and gained " + ENERGY_GAINED + " energy.");
    }
}
