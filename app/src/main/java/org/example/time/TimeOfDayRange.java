package org.example.time;

import java.util.Objects;

public class TimeOfDayRange {
    private final GameTime startTime;
    private final GameTime endTime;

    public TimeOfDayRange(GameTime startTime, GameTime endTime) {
        Objects.requireNonNull(startTime, "Start time must not be null.");
        Objects.requireNonNull(endTime, "End time must not be null.");
        if (startTime.isAfter(endTime)) {
            throw new IllegalArgumentException("Start time must not be after end time.");
        }
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public static TimeOfDayRange ofInGameHours(int startHourInclusive, int endHourInclusive) {
        if (startHourInclusive < 0 || startHourInclusive > 23 || endHourInclusive < 0 || endHourInclusive > 23) {
            throw new IllegalArgumentException("Hours must be between 0 and 23.");
        }
        return new TimeOfDayRange(new GameTime(startHourInclusive, 0), new GameTime(endHourInclusive, 0));
    }

    public boolean contains(GameTime time) {
        Objects.requireNonNull(time, "Time must not be null.");
        return !time.isBefore(startTime) && !time.isAfter(endTime);
    }

    public boolean overlaps(TimeOfDayRange other) {
        Objects.requireNonNull(other, "Other TimeOfDayRange must not be null.");
        return !this.startTime.isAfter(other.endTime) && !this.endTime.isBefore(other.startTime);
    }

    public GameTime getStartTime() {
        return startTime;
    }

    public GameTime getEndTime() {
        return endTime;
    }

    @Override
    public String toString() {
        return "[In-Game " + startTime + " - " + endTime + "]";
    }

    // equals and hashCode can stay the same, adjusted for GameTime if needed


    @Override
    public int hashCode() {
        return Objects.hash(startTime, endTime);
    }

    public boolean isInRange(int hour) {
        if (hour < 0 || hour > 23) {
            throw new IllegalArgumentException("In-game hour must be between 0 and 23.");
        }
        return hour >= this.startTime.getHour() && hour <= this.endTime.getHour();
    }

    public boolean isAny() {
        return startTime.equals(0) && endTime.equals(23);
    }
}