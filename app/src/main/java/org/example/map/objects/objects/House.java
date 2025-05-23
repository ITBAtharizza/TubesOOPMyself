package org.example.map.objects.objects;

import org.example.map.objects.DeployedObject;
import org.example.map.objects.HouseInteract;

public class House extends DeployedObject {
    public House() {
        super(6, 6, 'h', new HouseInteract());
    }
}
