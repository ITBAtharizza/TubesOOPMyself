package org.example.items;

import org.example.Player;

public interface Edible {
    void onEat(Player player);
    int getEnergyGained();
}