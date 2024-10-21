package edu.qut.cab302.wehab.models.pdf_report;

import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import edu.qut.cab302.wehab.database.Session;
import edu.qut.cab302.wehab.models.user_account.UserAccount;
import edu.qut.cab302.wehab.models.medication.MedicationReminder;
import edu.qut.cab302.wehab.models.mood_ratings.moodRating;
import edu.qut.cab302.wehab.models.workout.Workout;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.TreeMap;

public class PDFReportGenerator
{
    /**
     * Generates a PDF Report that includes mood ratings, completed workouts, medication medicationReminders and monthly workout minutes.
     * @param moodRatings The list of mood ratings within the last 7 days in the report.
     * @param workoutsCompleted The list of completed workouts in the month.
     * @param medicationReminders The list of prescribed medicationReminders.
     * @param monthlyMinutes The total of workout minutes by date for the entire month.
     * @param filePath The file path where the PDF will be saved.
     */
    public static void generateReport(List<moodRating> moodRatings, List<Workout> workoutsCompleted, List<MedicationReminder> medicationReminders, TreeMap<LocalDate, Integer> monthlyMinutes, String filePath)
    {
        try
        {
            // Retrieve the currently logged in user from the session
            UserAccount loggedInUser = Session.getInstance().getLoggedInUser();

            // Initialize the PDF writer and document
            PdfWriter writer = new PdfWriter(filePath);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // Get today's date and format it into Australian time format
            LocalDate today = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            String formattedDate = today.format(formatter);

            // Add a title to the pdf report
            Paragraph title = new Paragraph("Comprehensive Report for")
                    .setBold()
                    .setFontSize(16)
                    .setTextAlignment(TextAlignment.CENTER);
            document.add(title);

            // Add the user's first and last name
            Paragraph user = new Paragraph(loggedInUser.getFirstName() + " " + loggedInUser.getLastName())
                    .setBold()
                    .setFontSize(14)
                    .setTextAlignment(TextAlignment.CENTER);
            document.add(user);

            // Add the generation creation date
            Paragraph date = new Paragraph("Generated on: " + formattedDate)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(13);
            document.add(date);

            //*** Daily Mood Section ***\\
            Paragraph dailyMoodTitle = new Paragraph("Daily Mood Ratings")
                    .setBold()
                    .setFontSize(14);
            document.add(dailyMoodTitle);

            // Create a table with 2 columns
            Table dailyMoodTable = new Table(2); // Adds two columns to the table
            dailyMoodTable.setWidth(UnitValue.createPercentValue(100)); // Sets the table width to maximum (left side to right side of the entire page)
            dailyMoodTable.addCell("Date").setBold(); // Column header: Date
            dailyMoodTable.addCell("Mood Rating").setBold(); // Column header: Mood Rating

            // Populates the table with mood ratings
            for (moodRating rate : moodRatings)
            {
                dailyMoodTable.addCell(rate.getRatingDate().toString()); // Adds information into the 'Date' cell
                dailyMoodTable.addCell(String.valueOf(rate.getMoodRating())); // Adds information into the 'Mood Rating' cell
            }
            document.add(dailyMoodTable); // Adds the table into the pdf document.
            document.add(new Paragraph("\n")); // Adds a new line in between the tables, so it looks nice.


            //*** Completed Workout Section ***\\
            Paragraph completedWorkoutsTitle = new Paragraph("Completed Workouts")
                    .setBold()
                    .setFontSize(14);
            document.add(completedWorkoutsTitle);

            // Create a table with 4 columns
            Table completedWorkoutsTable = new Table(4); // Adds four columns to the table
            completedWorkoutsTable.setWidth(UnitValue.createPercentValue(100)); // Sets the table width to maximum (left side to right side of the entire page)
            completedWorkoutsTable.addCell("Date").setBold(); // Column header: Date
            completedWorkoutsTable.addCell("Type of Workout Completed").setBold(); // Column header: Type of Workout Completed
            completedWorkoutsTable.addCell("Minutes").setBold(); // Column header: Minutes
            completedWorkoutsTable.addCell("Effort (1-5)").setBold(); // Column header: Effort (1-5)

            // Populates the table
            for (Workout workout : workoutsCompleted)
            {
                completedWorkoutsTable.addCell(workout.getDate().toString()); // Adds information into the 'Date' cell
                completedWorkoutsTable.addCell(workout.getWorkoutType()); // Adds information into the Type of 'WOrkout Completed' cell
                completedWorkoutsTable.addCell(String.valueOf(workout.getDuration())); // Adds information into the 'Minutes' cell
                completedWorkoutsTable.addCell(String.valueOf(workout.getEffort())); // Adds information into the 'Effort' cell
            }
            document.add(completedWorkoutsTable); // Add the table into the pdf document
            document.add(new Paragraph("\n")); // Adds a new line in between the tables, so it looks nice.

            //*** Medication Reminders Section ***\\
            Paragraph remindersTitle = new Paragraph("Medication Log")
                    .setBold()
                    .setFontSize(14);
            document.add(remindersTitle);

            float[] columnWidths = {14f, 12f, 50f, 12f, 12f};

            // Create a table with 5 columns
            Table remindersTable = new Table(UnitValue.createPercentArray(columnWidths)); // Adds five columns to the table
            remindersTable.setWidth(UnitValue.createPercentValue(100)); // Sets the table width to maximum (left side to right side of the entire page)
            remindersTable.addCell("Date").setBold(); // Column header: Date
            remindersTable.addCell("Time").setBold(); // Column header: Time
            remindersTable.addCell("Medication").setBold(); // Column header: Medication
            remindersTable.addCell("Dosage").setBold(); // Column header: Dosage
            remindersTable.addCell("Status").setBold(); // Column header: Status


            // Populate the table
            for (MedicationReminder reminder : medicationReminders)
            {
                remindersTable.addCell(reminder.getDosageDate().toString());
                remindersTable.addCell(reminder.getDosageTime().toString());
                remindersTable.addCell(reminder.getDisplayName());
                remindersTable.addCell(reminder.getDosageString());
                remindersTable.addCell(reminder.getStatus());
            }

            document.add(remindersTable); // Add the table to the pdf document
            document.add(new Paragraph("\n")); // Adds a new line in between the tables, so it looks nice.

            //*** Monthly Minutes Section ***\\
            Paragraph monthlyTitle = new Paragraph("Monthly Minutes")
                    .setBold()
                    .setFontSize(14);
            document.add(monthlyTitle);

            // Create a table with 2 columns
            Table monthlyMinutesTable = new Table(2); // Adds two columns to the table
            monthlyMinutesTable.setWidth(UnitValue.createPercentValue(100)); // Sets the table width to maximum (left side to right side of the entire page)
            monthlyMinutesTable.addCell("Date").setBold(); // Column header: Date
            monthlyMinutesTable.addCell("Minutes").setBold(); // Column header: Minutes

            // Populate the table with monthly minutes, based on workout time and with coloured cells
            for (LocalDate eachDate : monthlyMinutes.keySet())
            {
                Cell dateCell = new Cell();
                dateCell.add(new Paragraph(eachDate.toString())); // Add the date as a paragraph in the cell

                Cell minutesCell = new Cell();
                int totalMinutes = monthlyMinutes.get(eachDate);
                minutesCell.add(new Paragraph(String.valueOf(totalMinutes))); // Add total minutes as a paragraph in the cell

                // Determine the cell background colour based on how many minutes per date.
                String labelColour = switch (totalMinutes / 30)
                {
                    case 0 -> "#96FF96"; // Very light lime green colour | Less time
                    case 1 -> "#FFE6C8"; // Very light orange colour
                    case 2 -> "#FFB496"; // Light red colour
                    case 3 -> "#FF8264"; // Darker red than above
                    case 4 -> "#FF5032"; // even darker red
                    default -> "#C800C8"; // Purple | Higher time
                };

                // Concert the colour from Hex to RGB and apply it to each cell
                DeviceRgb backgroundColour = new DeviceRgb(
                        Integer.parseInt(labelColour.substring(1, 3), 16),
                        Integer.parseInt(labelColour.substring(3, 5), 16),
                        Integer.parseInt(labelColour.substring(5, 7), 16)
                );
                minutesCell.setBackgroundColor(backgroundColour); // Sets the background colour for the minutes
                monthlyMinutesTable.addCell(dateCell); // Add the 'Date' cell to the table
                monthlyMinutesTable.addCell(minutesCell); // Add the 'Minutes' cell to the table
            }
            document.add(monthlyMinutesTable); // Add the monthly minutes table to the document

            //*** Close the document to finalize the PDF report ***\\
            document.close();

            System.out.println("PDF Created");
        } catch (Exception error) { error.printStackTrace(); } // Handles any errors that can occur during PDF generation
    }

}
