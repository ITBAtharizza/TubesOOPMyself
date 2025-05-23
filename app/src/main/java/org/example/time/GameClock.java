package org.example.time;

public class GameClock implements Runnable {
    private static GameClock instance; // Singleton instance

    private final GameTime time = new GameTime(6, 0);
    private final GameDate date = new GameDate();
    private final SeasonWeather weather = new SeasonWeather(date);

    private boolean running = true;
    private boolean paused = false;

    // Private constructor to prevent external instantiation
    private GameClock() {
    }

    // Public synchronized method to get the singleton instance
    public static synchronized GameClock getInstance() {
        if (instance == null) {
            instance = new GameClock();
        }
        return instance;
    }

    @Override
    public void run() {
        while (running) {
            try {
                Thread.sleep(1000); // 1 real sec = 5 game mins
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            synchronized (this) {
                if (!paused) {
                    advanceTime(5);
                }
            }
        }
    }

    private void advanceTime(int minutes) {
        int oldHour = time.getHour();
        int oldMinute = time.getMinute();

        time.advanceMinutes(minutes);

        // If time passed midnight (e.g. from 23:55 to 00:00)
        if (passedMidnight(oldHour, oldMinute, time.getHour(), time.getMinute())) {
            date.nextDay();
            weather.onSeasonChange();
        }
    }

    public synchronized void advanceTimeSkip(int minutes) {
        for (int i = 0; i < minutes; i++) {
            time.advanceMinutes(1);
            if (time.getHour() == 0 && time.getMinute() == 0) {
                date.nextDay();
                if (date.getDay() == 1) {
                    weather.onSeasonChange();
                }
            }
        }
    }

    private boolean passedMidnight(int oldH, int oldM, int newH, int newM) {
        return (oldH > newH) || (oldH == 23 && oldM > newM);
    }

    public GameTime getTime() {
        return time;
    }

    public GameDate getDate() {
        return date;
    }

    public SeasonWeather getSeasonWeather(){
        return weather;
    }

    public synchronized void setTime(int hour, int minute) {
        int oldHour = time.getHour();
        int oldMinute = time.getMinute();

        time.setHour(hour);
        time.setMinute(minute);

        if (passedMidnight(oldHour, oldMinute, hour, minute)) {
            date.nextDay();
            weather.onSeasonChange();
        }
    }

    public synchronized boolean isNight() {
        int hour = time.getHour();
        return (hour >= 18 || hour < 6);
    }

    public synchronized int getCurrentNightCycle() {
        int hour = time.getHour();
        int day = date.getDay();

        if (hour < 6) return day - 1;
        else return day;
    }

    public synchronized String getFormattedTime() { return time.toString(); }
    public synchronized String getFormattedDate() { return date.toString(); }
    public synchronized String getTodayWeather() { return weather.getTodayWeather(); }
    public synchronized void pause() { paused = true; }
    public synchronized void resume() { paused = false; }
    public void stopClock() { running = false; }
}
