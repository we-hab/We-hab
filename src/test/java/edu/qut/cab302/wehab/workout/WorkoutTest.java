package edu.qut.cab302.wehab.workout;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.*;


/**
 * This tests the result of creating a new workout object and comparing the results.
 */
public class WorkoutTest {

    private Workout workout;

    @BeforeEach
    public void setUp() {
        workout = new Workout("Jogging", LocalDate.of(2024, 10, 5), 30, 3);
    }

    @Test
    public void testWorkoutCreation() {
        assertEquals("Jogging", workout.getWorkoutType());
        assertEquals(LocalDate.of(2024, 10, 5), workout.getDate());
        assertEquals(30, workout.getDuration());
        assertEquals(3, workout.getEffort());
    }

    @Test
    public void testSetWorkoutType() {
        workout.setWorkoutType("Running");
        assertEquals("Running", workout.getWorkoutType());
    }

    @Test
    public void testSetDate() {
        workout.setDate(LocalDate.of(2024, 12, 25));
        assertEquals(LocalDate.of(2024, 12, 25), workout.getDate());
    }

    @Test
    public void testSetDuration() {
        workout.setDuration(60);
        assertEquals(60, workout.getDuration());
    }

    @Test
    public void testSetEffort() {
        workout.setEffort(5);
        assertEquals(5, workout.getEffort());
    }
}
