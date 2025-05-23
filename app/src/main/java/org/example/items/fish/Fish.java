package org.example.items.fish;

import org.example.Player;
import org.example.items.Edible;
import org.example.items.Items;
import org.example.time.TimeOfDayRange;

public class Fish extends Items implements Edible {
    private FishRarity rarity;
    private String[] seasons;
    private String[] weathers;
    private String[] locations;
    private TimeOfDayRange[] timeOfDayRanges;

    private static final int ENERGY_GAINED = 1;

    public Fish(String name, FishRarity rarity, String[] seasons, String[] weathers, String[] locations, TimeOfDayRange[] timeOfDayRanges) {
        super(name, 0, null, true);
        this.rarity = rarity;
        this.seasons = seasons;
        this.weathers = weathers;
        this.locations = locations;
        this.timeOfDayRanges = timeOfDayRanges;
        this.setSellPrice(calculatePriceFromSpec());
    }

    public FishRarity getRarity() { return rarity; }
    public String[] getSeasons() { return seasons; }
    public String[] getWeathers() { return weathers; }
    public String[] getLocations() { return locations; }
    public TimeOfDayRange[] getTimeOfDayRanges() { return timeOfDayRanges; }
    public int getEnergyGained() { return ENERGY_GAINED; }

    private int calculatePriceFromSpec() {
        int seasonCount = containsAny(seasons) ? 4 : seasons.length;
        int weatherCount = containsAny(weathers) ? 2 : weathers.length;
        int locationCount = containsAny(locations) ? 4 : locations.length;
        int timeRangeCount = containsAnyTimeRange() ? 1 : timeOfDayRanges.length;

        if (seasonCount == 0 || weatherCount == 0 || locationCount == 0 || timeRangeCount == 0) return 0;

        double price = (4.0 / seasonCount) * (24.0 / timeRangeCount) * (2.0 / weatherCount) * (4.0 / locationCount) * rarity.getPriceConstantC();
        return (int) Math.round(price);
    }

    private boolean containsAny(String[] array) {
        return containsIgnoreCase(array, "any");
    }

    private boolean containsIgnoreCase(String[] array, String value) {
        for (String s : array) {
            if (s.equalsIgnoreCase(value)) return true;
        }
        return false;
    }

    private boolean containsAnyTimeRange() {
        for (TimeOfDayRange range : timeOfDayRanges) {
            if (range.isAny()) return true;
        }
        return false;
    }

    public boolean isAvailable(String season, String weather, String location, int hour) {
        return matches(seasons, season) && matches(weathers, weather) && matches(locations, location) && isInTimeOfDay(hour);
    }

    private boolean matches(String[] array, String target) {
        return containsIgnoreCase(array, "any") || containsIgnoreCase(array, target);
    }

    private boolean isInTimeOfDay(int hour) {
        for (TimeOfDayRange range : timeOfDayRanges) {
            if (range.isAny() || range.isInRange(hour)) return true;
        }
        return false;
    }

    @Override
    public void onEat(Player player) {
        player.addEnergy(ENERGY_GAINED);
        System.out.println("You ate " + getName() + " and gained " + ENERGY_GAINED + " energy.");
    }
}
