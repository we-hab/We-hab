package edu.qut.cab302.wehab;

import java.time.LocalDate;

public class Workout {
    private String workoutType;
    private LocalDate date;
    private int duration; // in minutes
    private int effort; // scale 1-5

    // Constructor
    public Workout(String workoutType, LocalDate date, int duration, int effort) {
        this.workoutType = workoutType;
        this.date = date;
        this.duration = duration;
        this.effort = effort;
    }

    // Getters and Setters
    public String getWorkoutType() {
        return workoutType;
    }

    public void setWorkoutType(String workoutType) {
        this.workoutType = workoutType;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getEffort() {
        return effort;
    }

    public void setEffort(int effort) {
        this.effort = effort;
    }
}

