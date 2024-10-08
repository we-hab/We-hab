package edu.qut.cab302.wehab;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.borders.SolidBorder;
import edu.qut.cab302.wehab.medication.PrescribedMedicationDose;
import edu.qut.cab302.wehab.workout.Workout;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PDFReportGenerator
{
    static UserAccount loggedInUser = Session.getInstance().getLoggedInUser();
    public static void generateReport(List<moodRating> moodRatings, List<Workout> workoutsCompleted, List<PrescribedMedicationDose> medications, String filePath)
    {
        try
        {
            PdfWriter writer = new PdfWriter(filePath);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            LocalDate today = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            String formattedDate = today.format(formatter);

            Paragraph title = new Paragraph("Comprehensive Report for")
                    .setBold()
                    .setFontSize(16)
                    .setTextAlignment(TextAlignment.CENTER);
            document.add(title);

            Paragraph user = new Paragraph(loggedInUser.getFirstName() + " " + loggedInUser.getLastName())
                    .setBold()
                    .setFontSize(14)
                    .setTextAlignment(TextAlignment.CENTER);
            document.add(user);

            // add in LoggedInUser.firstName + LoggedInUser.lastName;

            Paragraph date = new Paragraph("Generated on: " + formattedDate)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(13);
            document.add(date);

            // Daily Mood Table
            Table dailyMoodTable = new Table(2);
            dailyMoodTable.setWidth(UnitValue.createPercentValue(100));
            dailyMoodTable.addCell("Date").setBold();
            dailyMoodTable.addCell("Mood Rating").setBold();

            for (moodRating rate : moodRatings)
            {
                dailyMoodTable.addCell(rate.getRatingDate().toString());
                dailyMoodTable.addCell(String.valueOf(rate.getMoodRating()));
            }
            document.add(dailyMoodTable);

            document.add(new Paragraph("\n"));

            Paragraph scheduleTableTitle = new Paragraph("Schedule Overview")
                    .setBold()
                    .setFontSize(14);
            document.add(scheduleTableTitle);

            // Schedule Table
            Table scheduleTable = new Table(3);
            scheduleTable.setWidth(UnitValue.createPercentValue(100));
            scheduleTable.addCell("Date").setBold();
            scheduleTable.addCell("Type of Workout").setBold();
            scheduleTable.addCell("Minutes").setBold();
            document.add(scheduleTable);

            document.add(new Paragraph("\n"));

            Paragraph completedWorkoutsTitle = new Paragraph("Completed Workouts")
                    .setBold()
                    .setFontSize(14);
            document.add(completedWorkoutsTitle);

            // Schedule Table
            Table completedWorkoutsTable = new Table(4);
            completedWorkoutsTable.setWidth(UnitValue.createPercentValue(100));
            completedWorkoutsTable.addCell("Date").setBold();
            completedWorkoutsTable.addCell("Type of Workout Completed").setBold();
            completedWorkoutsTable.addCell("Minutes").setBold();
            completedWorkoutsTable.addCell("Effort (1-5)").setBold();

            for (Workout workout : workoutsCompleted)
            {
                completedWorkoutsTable.addCell(workout.getDate().toString());
                completedWorkoutsTable.addCell(workout.getWorkoutType());
                completedWorkoutsTable.addCell(String.valueOf(workout.getDuration()));
                completedWorkoutsTable.addCell(String.valueOf(workout.getEffort()));
            }

            document.add(completedWorkoutsTable);

            document.add(new Paragraph("\n"));

            Paragraph remindersTitle = new Paragraph("Medication Reminders")
                    .setBold()
                    .setFontSize(14);
            document.add(remindersTitle);

            // Reminders Table
            Table remindersTable = new Table(3);
            remindersTable.setWidth(UnitValue.createPercentValue(100));
            remindersTable.addCell("Medication").setBold();
            remindersTable.addCell("When to take").setBold();
            remindersTable.addCell("How many tablets").setBold();
            document.add(remindersTable);

            for (PrescribedMedicationDose medication : medications)
            {
                remindersTable.addCell(medication.getDisplayName());
                remindersTable.addCell(medication.getDosageTime().toString());
                remindersTable.addCell(medication.getDosageAmount() + " " + medication.getDosageUnit());
            }

            document.close();

            System.out.println("PDF Created mate");
        } catch (Exception error) { System.err.println("Error pdf mate" + error.getMessage()); }
    }

}
