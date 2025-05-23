package org.example.items;

public class Items {
    private String name;
    private Integer sellPrice = null;
    private Integer buyPrice = null;
    private boolean isEdible = false;

    public Items(String name) {
        this.name = name;
    }
    
    public Items(String name, Integer sellPrice, Integer buyPrice, boolean isEdible) {
        this.name = name;
        this.sellPrice = sellPrice;
        this.buyPrice = buyPrice;
        this.isEdible = isEdible;

        if (sellPrice != null && sellPrice < 0) {
            this.sellPrice = null;
            throw new IllegalArgumentException("Sell price cannot be negative");
        }
        
        if (buyPrice != null && buyPrice < 0) {
            this.buyPrice = null;
            throw new IllegalArgumentException("Buy price cannot be negative");
        }
    }

    public String getName() {
        return name;
    }

    public Integer getSellPrice() {
        return sellPrice;
    }

    public Integer getBuyPrice() {
        return buyPrice;
    }
    
    public boolean isEdible() {
        return isEdible;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public void setSellPrice(Integer sellPrice) {
        this.sellPrice = sellPrice;
    }

    public void setBuyPrice(Integer buyPrice) {
        this.buyPrice = buyPrice;
    }

    public void setEdible(boolean edible) {
        this.isEdible = edible;
    }
}