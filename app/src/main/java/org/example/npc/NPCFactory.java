package org.example.npc;

import java.util.HashMap;
import java.util.Map;

public class NPCFactory {
    private static final Map<String, NPC> npcByName = new HashMap<>();

    public static void register(NPC npc) {
        npcByName.put(npc.getName().toLowerCase(), npc);
    }

    public static NPC get(String name) {
        return npcByName.get(name.toLowerCase());
    }

    public static Map<String, NPC> getAllNPC() {
        return npcByName;
    }

    private static void loadNPC() {
        register(new Abigail());
        register(new Caroline());
        register(new Dasco());
        register(new Emily());
        register(new MayorTadi());
        register(new Perry());
    }

    public static void loadAll() {
        loadNPC();
    }
}
