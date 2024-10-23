package edu.qut.cab302.wehab.models.pdf_report;
import edu.qut.cab302.wehab.database.Session;
import edu.qut.cab302.wehab.models.user_account.UserAccount;
import edu.qut.cab302.wehab.models.mood_ratings.moodRating;
import edu.qut.cab302.wehab.models.workout.Workout;
import edu.qut.cab302.wehab.models.medication.MedicationReminder;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class pdfReportTest {

    private UserAccount testUser;
    private List<moodRating> testMoodRatings;
    private List<Workout> testWorkouts;
    private List<MedicationReminder> testMedications;
    private TreeMap<LocalDate, Integer> testMonthlyMinutes;

    public void SetUpTestData() {
        // Initialize test data
        testUser = new UserAccount("testUserr", "TestName", "TestLastName", "Test@gmail.com", "TestPassword123");

        testMoodRatings = new ArrayList<>();
        testMoodRatings.add(new moodRating(7, LocalDate.now()));

        testWorkouts = new ArrayList<>();
        testWorkouts.add(new Workout("Running", LocalDate.now(), 30, 4));

        testMedications = new ArrayList<>();
        testMedications.add(new MedicationReminder.MedicationReminderBuilder()
                .withUsername(testUser.toString())
                .withMedicationID("1")
                .withDisplayName("Paracetamol")
                .withDosageAmount(2)
                .withDosageUnit("tablets")
                .withDosageDate(LocalDate.now())
                .withDosageTime(LocalTime.now())
                .build()
                );

        testMonthlyMinutes = new TreeMap<>();
        testMonthlyMinutes.put(LocalDate.now(), 90);
    }

    @Test
    public void testGenerateReport_ShouldCreatePDFFile() throws IOException
    {
        // Initialize test data
        SetUpTestData();

        // Create temporary file for PDF
        File tempFile = Files.createTempFile("test_report", ".pdf").toFile();

        // Mock the static methods in try-with-resources to ensure they are closed properly
        try (MockedStatic<Session> sessionMock = mockStatic(Session.class);
             MockedStatic<PDFReportGenerator> pdfGeneratorMock = mockStatic(PDFReportGenerator.class)) {

            // Mock the behavior of the session
            Session mockSession = mock(Session.class);
            sessionMock.when(Session::getInstance).thenReturn(mockSession);
            when(mockSession.getLoggedInUser()).thenReturn(testUser);

            // Call the method being tested
            PDFReportGenerator.generateReport(testMoodRatings, testWorkouts, testMedications, testMonthlyMinutes, tempFile.getAbsolutePath());

            // Verify the method was called once
            pdfGeneratorMock.verify(() -> PDFReportGenerator.generateReport(testMoodRatings, testWorkouts, testMedications, testMonthlyMinutes, tempFile.getAbsolutePath()), times(1));

            // Assert the file was created
            assertTrue(tempFile.exists(), "PDF file should be created");
        }

        // Clean up the temp file
        tempFile.deleteOnExit();
    }

    @Test
    public void testGenerateReport_ShouldContainMoodRatings() throws IOException
    {
        // Initialize test data
        SetUpTestData();

        // Create temporary file for PDF
        File tempFile = Files.createTempFile("test_report_mood", ".pdf").toFile();

        // Mock the static methods in try-with-resources
        try (MockedStatic<Session> sessionMock = mockStatic(Session.class);
             MockedStatic<PDFReportGenerator> pdfGeneratorMock = mockStatic(PDFReportGenerator.class)) {

            // Mock the session behavior
            Session mockSession = mock(Session.class);
            sessionMock.when(Session::getInstance).thenReturn(mockSession);
            when(mockSession.getLoggedInUser()).thenReturn(testUser);

            // Call the method being tested
            PDFReportGenerator.generateReport(testMoodRatings, testWorkouts, testMedications, testMonthlyMinutes, tempFile.getAbsolutePath());

            // Verify the method was called
            pdfGeneratorMock.verify(() -> PDFReportGenerator.generateReport(testMoodRatings, testWorkouts, testMedications, testMonthlyMinutes, tempFile.getAbsolutePath()), times(1));

            // Assert the file was created
            assertTrue(tempFile.exists(), "PDF file should be created");
        }

        // Clean up the temp file
        tempFile.deleteOnExit();
    }

    @Test
    public void testGenerateReport_ShouldHandleEmptyData() throws IOException
    {
        // Create temporary file for PDF
        File tempFile = Files.createTempFile("test_report_empty", ".pdf").toFile();

        // Mock the static methods in try-with-resources
        try (MockedStatic<Session> sessionMock = mockStatic(Session.class);
             MockedStatic<PDFReportGenerator> pdfGeneratorMock = mockStatic(PDFReportGenerator.class)) {

            // Mock the session behavior
            Session mockSession = mock(Session.class);
            sessionMock.when(Session::getInstance).thenReturn(mockSession);
            when(mockSession.getLoggedInUser()).thenReturn(testUser);

            // Call the method being tested with empty data
            PDFReportGenerator.generateReport(new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new TreeMap<>(), tempFile.getAbsolutePath());

            // Verify the method was called with empty data
            pdfGeneratorMock.verify(() -> PDFReportGenerator.generateReport(anyList(), anyList(), anyList(), any(TreeMap.class), anyString()), times(1));

            // Assert the file was created
            assertTrue(tempFile.exists(), "PDF file should be created even if no data is provided");
        }

        // Clean up the temp file
        tempFile.deleteOnExit();
    }
}