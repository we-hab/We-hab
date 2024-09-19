package edu.qut.cab302.wehab;

import java.time.LocalDate;

public class MoodRating
{
    private LocalDate date;
    private int rating;

    public MoodRating(LocalDate date, int rating)
    {
        this.date = date;
        this.rating = rating;
    }

    public LocalDate getDate()
    {
        return date;
    }

    public int getRating()
    {
        return rating;
    }
}
