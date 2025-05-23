package org.example.map.objects.objects;

import org.example.map.objects.DeployedObject;
import org.example.map.objects.ShippingBinInteract;

public class ShippingBin extends DeployedObject {
    public ShippingBin() {
        super(3, 2, 's', new ShippingBinInteract());
    }
}

