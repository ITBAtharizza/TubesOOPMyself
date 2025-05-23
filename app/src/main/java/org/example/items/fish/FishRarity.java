package org.example.items.fish;

public enum FishRarity {
    COMMON(10),
    REGULAR(5),
    LEGENDARY(25);

    private final int priceConstantC;

    FishRarity(int constantC) {
        this.priceConstantC = constantC;
    }

    public int getPriceConstantC() {
        return priceConstantC;
    }
}