package edu.qut.cab302.wehab.medication;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import org.skyscreamer.jsonassert.JSONAssert;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.HttpURLConnection;

public class FDAApiServiceTest {

    private FDAApiService fdaApiService;

    @BeforeEach
    public void setUp() {
        fdaApiService = new FDAApiService();
    }

    @Test
    public void testQueryAPI_Success() throws IOException {
        String results = fdaApiService.queryAPI("dayvigo");

        assertNotNull(results);
        assertTrue(results.contains("\"results\": {"));
        assertFalse(results.contains("\"error\": {"));
    }

    @Test
    public void testQueryAPI_NoResults() throws IOException {
        String results = fdaApiService.queryAPI("no_medication");

        assertNotNull(results);
        assertTrue(results.contains("\"code\": \"NOT_FOUND\""));
    }

    @Test
    public void testQueryAPI_InvalidMedicationName() throws IOException {
        String results = fdaApiService.queryAPI("###");

        assertNotNull(results);
        assertTrue(results.contains("\"code\": \"BAD_REQUEST\""));
    }

    @Test
    public void testCreateUrlObject_ValidUrl() throws MalformedURLException {

        fdaApiService = spy(new FDAApiService());

        String validUrlString = "https://api.fda.gov/drug/label.json";
        URL url = fdaApiService.createUrlObject(validUrlString);

        assertNotNull(url);
        assertEquals(validUrlString, url.toString());
    }

    @Test
    public void testCreateUrlObject_InvalidUrl() {
        assertThrows(MalformedURLException.class, () -> {
            fdaApiService.createUrlObject("invalid_url");
        });
    }

    @Test
    public void testQueryAPI_Timeout() throws IOException {
        HttpURLConnection mockConnection = mock(HttpURLConnection.class);
        when(mockConnection.getResponseCode()).thenThrow(new SocketTimeoutException("Connection timed out"));

        FDAApiService apiServiceSpy = spy(fdaApiService);
        doReturn(mockConnection).when(apiServiceSpy).openConnection(any(URL.class));

        String result = apiServiceSpy.queryAPI("medication");

        assertNull(result);
        assertNotNull(apiServiceSpy.getResultsMessage());
        assertEquals("Connection timed out", apiServiceSpy.getResultsMessage());
    }

    @Test
    public void testQueryAPI_OtherResponseCodes() throws IOException {
        HttpURLConnection mockConnection = mock(HttpURLConnection.class);
        when(mockConnection.getResponseCode()).thenReturn(500);

        FDAApiService apiServiceSpy = spy(fdaApiService);
        doReturn(mockConnection).when(apiServiceSpy).openConnection(any(URL.class));

        String result = apiServiceSpy.queryAPI("medication");

        assertNull(result);
        assertTrue(apiServiceSpy.getResultsMessage().contains("HTTP error response code"));
    }
}