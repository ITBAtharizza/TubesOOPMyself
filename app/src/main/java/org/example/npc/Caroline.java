package org.example.npc;

import java.util.Map;

import org.example.items.Items;

public class Caroline extends NPC {

    public Caroline() {
        super("Caroline");
    }

    @Override
    protected Items[] mapLovedItems(Map<String, Items> itemsMap) {
        return new Items[] {
            itemsMap.get("Firewood"),
            itemsMap.get("Coal")
        };
    }

    @Override
    protected Items[] mapLikedItems(Map<String, Items> itemsMap) {
        return new Items[] {
            itemsMap.get("Potato"),
            itemsMap.get("Wheat")
        };
    }

    @Override
    protected Items[] mapHatedItems(Map<String, Items> itemsMap) {
        return new Items[] {
            itemsMap.get("Hot Pepper"),
        };
    }
    
}
