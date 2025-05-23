package org.example.world;

import java.util.ArrayList;
import java.util.List;

import org.example.Player;
import org.example.npc.NPC;
import org.example.npc.NPCFactory;

public class WorldMap {
    private List<WorldLocation> locations = new ArrayList<>();

    public WorldMap() {
        addLocation(new FishingSpot("Mountain Lake"));
        addLocation(new FishingSpot("Forest River"));
        addLocation(new FishingSpot("Ocean"));
        
        NPCFactory npcFactory = new NPCFactory();
        npcFactory.loadAll();

        for (NPC npc : npcFactory.getAllNPC().values()) {
            if (!npc.getName().equals("Emily")) addLocation(new NPCHome(npc)); 
        }

        addLocation(new Store());
    }

    public void addLocation(WorldLocation location) {
        locations.add(location);
    }

    public void visitLocation(int index, Player player) {
        if (index < 1 || index > locations.size()) {
            System.out.println("No such location found at index: " + index);
            return;
        }
        WorldLocation loc = locations.get(index - 1);
        loc.interact(player);
    }

    public void showLocations() {
        int i = 1;
        for (WorldLocation loc : locations) {
            System.out.println(i + ". " + loc.getName());
            i++;
        }
    }
}

