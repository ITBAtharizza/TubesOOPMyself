package org.example.time;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class SeasonWeather {
    private final GameDate date;
    private Set<Integer> rainyDays;
    private final Random random = new Random();

    public SeasonWeather(GameDate date) {
        this.date = date;
        generateRainyDays();
    }

    public SeasonWeather(SeasonWeather other){
        this.date = other.date;
        this.rainyDays = other.getRainyDays();
    }

    private void generateRainyDays() {
        rainyDays = new HashSet<>();
        while (rainyDays.size() < 2) {
            int day = random.nextInt(10) + 1;
            rainyDays.add(day);
        }
    }

    public void onSeasonChange() {
        generateRainyDays();
    }

    public String getTodayWeather() {
        int currentDay = date.getDay();
        if (rainyDays.contains(currentDay)) {
            return "Rainy";
        }
        return "Sunny";
    }

    public Set<Integer> getRainyDays(){
        return rainyDays;
    }
}
