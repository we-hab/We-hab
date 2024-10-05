package edu.qut.cab302.wehab;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Workout {
    private String workoutType;
    private LocalDate date;
    private int duration;
    private int effort;

    /** @return The type of workout. */
    public String getWorkoutType() {
        return workoutType;
    }

    /** @return The date. */
    public LocalDate getDate() {
        return date;
    }

    /** @return The duration of the workout. */
    public int getDuration() {
        return duration;
    }

    /** @return The effort of the workout. */
    public int getEffort() {
        return effort;
    }

    /** @param workoutType Sets the type of workout*/
    public void setWorkoutType(String workoutType) {
        this.workoutType = workoutType;
    }

    /** @param date Sets the date of workout*/
    public void setDate(LocalDate date) {
        this.date = date;
    }
    /** @param duration Sets the duration of workout*/
    public void setDuration(int duration) {
        this.duration = duration;
    }

    /** @param effort Sets the effort of workout*/
    public void setEffort(int effort) {
        this.effort = effort;
    }

    /**
     * Constructs a Workout object by parsing user input from the GUI.
     */
    public Workout(String workoutType, LocalDate date, int duration, int effort) {
        this.workoutType = workoutType;
        this.date = date;
        this.duration = duration;
        this.effort = effort;
    }
}