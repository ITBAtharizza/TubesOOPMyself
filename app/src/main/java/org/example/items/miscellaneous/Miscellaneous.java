package org.example.items.miscellaneous;

import org.example.items.Items;

public class Miscellaneous extends Items {
    public Miscellaneous(String name, Integer sellPrice, Integer buyPrice) {
        super(name);
        if (sellPrice != null && buyPrice != null && sellPrice < buyPrice){
            super.setBuyPrice(buyPrice);
            super.setSellPrice(sellPrice);
        } else {
            throw new IllegalArgumentException("Sell price cannot be less than buy price");
        }
    }
}
