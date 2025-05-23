package org.example.npc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.example.items.Items;
import org.example.items.fish.Fish;

public class Perry extends NPC {

    public Perry() {
        super("Perry");
    }

    @Override
    protected Items[] mapLovedItems(Map<String, Items> itemsMap) {
        return new Items[] {
            itemsMap.get("Cranberry"),
            itemsMap.get("Blueberry")
        };
    }

    @Override
    protected Items[] mapLikedItems(Map<String, Items> itemsMap) {
        return new Items[] {
            itemsMap.get("Wine")
        };
    }

    @Override
    protected Items[] mapHatedItems(Map<String, Items> itemsMap) {
        List<Items> hated = new ArrayList<>();
        for (Items item : itemsMap.values()) {
            if (item instanceof Fish) {
                hated.add(item);
            }
        }

        return hated.toArray(Items[]::new);
    }
    
}
