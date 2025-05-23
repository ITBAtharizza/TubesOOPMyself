package org.example.items.seeds;

import org.example.items.Items;

public class Seeds extends Items {
    private String season;
    private int harvestDays;

    public Seeds(String name, String season, int harvestDays, int buyPrice) {
        super(name, buyPrice / 2, buyPrice, false);
        this.season = season;
        this.harvestDays = harvestDays;
    }

    public String getSeason() {
        return season;
    }

    public int getharvestDays() {
        return harvestDays;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public void setharvestDays(int harvestDays) {
        this.harvestDays = harvestDays;
    }
}