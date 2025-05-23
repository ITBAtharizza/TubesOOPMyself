package org.example.time;

public class GameClockSnapshot {
    private final GameTime time;
    private final GameDate date;
    private final SeasonWeather weather;

    public GameClockSnapshot(GameClock clock) {
        this.time = new GameTime(clock.getTime());
        this.date = new GameDate(clock.getDate());
        this.weather = new SeasonWeather(clock.getSeasonWeather());
    }

    public GameTime getTime() {
        return time;
    }

    public GameDate getDate() {
        return date;
    }

    public SeasonWeather getWeather() {
        return weather;
    }
}
