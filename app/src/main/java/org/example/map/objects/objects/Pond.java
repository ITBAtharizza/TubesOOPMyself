package org.example.map.objects.objects;

import org.example.map.objects.DeployedObject;
import org.example.map.objects.PondInteract;

public class Pond extends DeployedObject {
    public Pond() {
        super(4, 3, 'o', new PondInteract());
    }
}
