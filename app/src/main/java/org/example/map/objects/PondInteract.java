package org.example.map.objects;

import org.example.Player;

public class PondInteract implements Interact {
    public void interact(Player player) {
        System.out.println("\n\n---- POND ---- \n\n");

        System.out.println("You can fish here.");
        System.out.println("These are the fish types you can catch:");
        System.out.println("1. Common");
        System.out.println("2. Regular");
        System.out.println("3. Legendary");

        System.out.println("Let's see what you catch!");
        
        player.fishing("Pond");
    }
}
