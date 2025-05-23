package org.example;

import java.util.HashMap;
import java.util.Map;

import org.example.items.Items;
import org.example.items.equipment.FishingRod;
import org.example.items.equipment.Hoe;
import org.example.items.equipment.Pickaxe;
import org.example.items.equipment.WateringCan;
import org.example.items.seeds.Seeds;

public class Inventory {
    private Map<Items, Integer> items;

    public Inventory() {
        items = new HashMap<>();
        items.put(new Hoe(), 1);
        items.put(new FishingRod(), 1);
        items.put(new Pickaxe(), 1);
        items.put(new WateringCan(), 1);
        items.put(new Seeds("Parsnip Seeds", "Spring", 1, 20), 15);
    }

    public Inventory(Map<Items, Integer> items) {
        this.items = items;
    }

    public Map<Items, Integer> getItems() {
        return items;
    }

    public void addItem(Items item, int quantity) {
        if (items.containsKey(item)) {
            items.put(item, items.get(item) + quantity);
        } else {
            items.put(item, quantity);
        }
    }

    public void removeItem(Items item, int quantity) {
        if (items.containsKey(item)) {
            int currentQuantity = items.get(item);
            if (currentQuantity > quantity) {
                items.put(item, currentQuantity - quantity);
            } else {
                items.remove(item);
            }
        }
    }

    public int getItemQuantity(Items item) {
        return items.getOrDefault(item, 0);
    }

    public void clear() {
        items.clear();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }
    
    public Items get(String itemName) {
        for (Items item : items.keySet()) {
            if (item.getName().equalsIgnoreCase(itemName)) return item;
        }
        return null;
    }
}