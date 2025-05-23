package org.example.map;

import org.example.items.seeds.Seeds;

public class PlantedTile extends Tile {
    private int lastWateredDay = -2;
    private int plantedDay;
    private Seeds seed;
    private boolean readyToHarvest = false;

    public PlantedTile(Seeds seed, int currentDay) {
        super();
        this.seed = seed;
        this.plantedDay = currentDay;
        setDeployedObjectChar('l');
    }

    public void water(int currentDay) {
        this.lastWateredDay = currentDay;
    }

    public boolean isWateredToday(int currentDay, boolean isRaining) {
        return isRaining || lastWateredDay == currentDay;
    }

    public boolean isNeglected(int currentDay, boolean isRaining) {
        if (isRaining) return false;
        return currentDay - plantedDay > 1 && currentDay - lastWateredDay > 1;
    }


    public void updateGrowth(int currentDay, boolean isRaining) {
        if (!isNeglected(currentDay, isRaining)) {
            int daysSincePlanted = currentDay - plantedDay;
            if (daysSincePlanted >= seed.getharvestDays()) {
                readyToHarvest = true;
                setDeployedObjectChar('r');
            } else {
                readyToHarvest = false;
                setDeployedObjectChar('l');
            }
        }
    }

    public boolean checkSeasonalDeath(String currentSeason) {
        if (!seed.getSeason().equalsIgnoreCase(currentSeason)) return true;
        return false;
    }

    public boolean isReadyToHarvest() {
        return readyToHarvest;
    }

    public Seeds getSeed() {
        return seed;
    }

    public int getLastWateredDay(){
        return lastWateredDay;
    }
}

