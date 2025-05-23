package org.example.time;

public class GameTime {
    public static final GameTime MIN = new GameTime(0, 0);
    public static final GameTime MAX = new GameTime(23, 59);

    public static GameTime parse(String timeStr) {
        String[] parts = timeStr.split(":");
        int h = Integer.parseInt(parts[0]);
        int m = parts.length > 1 ? Integer.parseInt(parts[1]) : 0;
        return new GameTime(h, m);
    }

    private int hour;
    private int minute;

    public GameTime(int hour, int minute) {
        if (hour < 0 || hour > 23 || minute < 0 || minute > 59)
            throw new IllegalArgumentException("Invalid time: " + hour + ":" + minute);
        this.hour = hour;
        this.minute = minute;
    }

    public GameTime(GameTime other) {
        this.hour = other.getHour();
        this.minute = other.getMinute();
    }

    public void advanceMinutes(int minutes) {
        minute += minutes;
        if (minute >= 60) {
            hour += minute / 60;
            minute %= 60;
        }
        if (hour >= 24) {
            hour %= 24;
        }
    }

    public boolean advanceMinutesWithMidnightCheck(int minutes) {
        boolean passedMidnight = false;
        for (int i = 0; i < minutes; i++) {
            advanceMinutes(1);
            if (hour == 0 && minute == 0) {
                passedMidnight = true;
            }
        }
        return passedMidnight;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public boolean isBefore(GameTime other) {
        return hour < other.hour || (hour == other.hour && minute < other.minute);
    }

    public boolean isAfter(GameTime other) {
        return hour > other.hour || (hour == other.hour && minute > other.minute);
    }

    public GameTime minusHours(int hours) {
        int newHour = this.hour - hours;
        while (newHour < 0) {
            newHour += 24;
        }
        return new GameTime(newHour, this.minute);
    }

    public void setHour(int hour) {
        if (hour < 0 || hour > 23)
            throw new IllegalArgumentException("Invalid hour: " + hour);
        this.hour = hour;
    }

    public void setMinute(int minute) {
        if (minute < 0 || minute > 59)
            throw new IllegalArgumentException("Invalid minute: " + minute);
        this.minute = minute;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof GameTime gt) {
            return hour == gt.hour && minute == gt.minute;
        }
        return false;
    }

    @Override
    public String toString() {
        return String.format("%02d:%02d", hour, minute);
    }
}
