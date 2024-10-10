package edu.qut.cab302.wehab.pdf_report;
import edu.qut.cab302.wehab.database.Session;
import edu.qut.cab302.wehab.user_account.UserAccount;
import edu.qut.cab302.wehab.mood_ratings.moodRating;
import edu.qut.cab302.wehab.workout.Workout;
import edu.qut.cab302.wehab.medication.PrescribedMedicationDose;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class pdfReportTest
{
    private UserAccount testUser;
    private List<moodRating> testMoodRatings;
    private List<Workout> testWorkouts;
    private List<PrescribedMedicationDose> testMedications;
    private TreeMap<LocalDate, Integer> testMonthlyMinutes;

    @BeforeEach
    public void SetUp()
    {
        testUser = new UserAccount("testUserr", "TestName","TestLastName","Test@gmail.com","TestPassword123");
        testMoodRatings = new ArrayList<>();
        testMoodRatings.add(new moodRating(7, LocalDate.now()));
        testWorkouts = new ArrayList<>();
        testWorkouts.add(new Workout("Running", LocalDate.now(), 30, 4));
        testMedications = new ArrayList<>();
        testMedications.add(new PrescribedMedicationDose(testUser.toString(), "1", "Paracetamol",2, "tablets",LocalDate.now(), LocalTime.now()));

        testMonthlyMinutes = new TreeMap<>();
        testMonthlyMinutes.put(LocalDate.now(), 90);
    }

    @Test
    public void testGenerateReport_ShouldCreatePDFFile() throws IOException {
        Session session = Session.getInstance();
        session.setLoggedInUser(testUser);

        File tempFile = Files.createTempFile("test_report", ".pdf").toFile();
        PDFReportGenerator.generateReport(testMoodRatings, testWorkouts, testMedications, testMonthlyMinutes, tempFile.getAbsolutePath());
        assertTrue(tempFile.exists(), "PDF file should be created");
        tempFile.deleteOnExit();

    }

    @Test
    public void testGenerateReport_ShouldContainMoodRatings() throws IOException {
        Session session = Session.getInstance();
        session.setLoggedInUser(testUser);

        File tempFile = Files.createTempFile("test_report_mood", ".pdf").toFile();
        PDFReportGenerator.generateReport(testMoodRatings, testWorkouts, testMedications, testMonthlyMinutes, tempFile.getAbsolutePath());
        assertTrue(tempFile.exists(), "PDF file should be created");
        tempFile.deleteOnExit();
    }

    @Test
    public void testGenerateReport_ShouldHandleEmptyData() throws IOException {
        Session session = Session.getInstance();
        session.setLoggedInUser(testUser);

        File tempFile = Files.createTempFile("test_report_empty", ".pdf").toFile();
        PDFReportGenerator.generateReport(new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new TreeMap<>(), tempFile.getAbsolutePath());
        assertTrue(tempFile.exists(), "PDF file should be created even if no data is provided");
        tempFile.deleteOnExit();
    }


}
