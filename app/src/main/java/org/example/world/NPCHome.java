package org.example.world;

import java.util.Scanner;

import org.example.Player;
import org.example.npc.NPC;

public class NPCHome extends WorldLocation {
    private NPC npc;

    public NPCHome(NPC npc) {
        super(npc.getName() + "'s Home");
        this.npc = npc;
    }

    @Override
    public void interact(Player player) {
        System.out.println("----" + npc.getName() + "'s Home" + "----");
        System.out.println("What do you want to do with " + npc.getName() + "?");
        System.out.println("1. Chat");
        System.out.println("2. Give Gift");
        System.out.println("3. Propose");
        System.out.println("4. Marry");

        System.out.print("Enter your choice : ");
        Scanner scanner = player.getScanner();
        int choice;
        try {
            choice = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid number. Leaving " + npc.getName() + "'s Home...");
            return;
        }

        if (choice == 1) player.chatting(npc);
        if (choice == 2) player.gifting(npc);
        if (choice == 3) player.proposing(npc);
        if (choice == 4) player.marry(npc);
        else System.out.println("Invalid number. Leaving " + npc.getName() + "'s Home...");
    }
}

