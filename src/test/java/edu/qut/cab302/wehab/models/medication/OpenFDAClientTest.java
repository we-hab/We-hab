package edu.qut.cab302.wehab.models.medication;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;
import static org.junit.jupiter.api.Assertions.*;

import java.net.SocketTimeoutException;


public class OpenFDAClientTest {

    OpenFDAClient openFDAClient;

    @BeforeEach
    public void setUp() {
        openFDAClient = new OpenFDAClient();
    }

    @Test
    public void testSearchForMedications_Success() {

        Medication[] medications = openFDAClient.searchForMedications("dayvigo");

        assertNotNull(medications);
        assertTrue(medications.length > 0);
    }

    @Test
    public void testSearchForMedications_Failure() {
        Medication[] medications = openFDAClient.searchForMedications("no_such_medication");
        assertNull(medications);
    }

    @Test
    public void testSearchForMedications_Timeout() throws Exception {
        FDAApiService apiServiceMock = mock(FDAApiService.class);
        when(apiServiceMock.queryAPI("timeout_medication")).thenThrow(new SocketTimeoutException("Connection timed out"));

        Medication[] medications = openFDAClient.searchForMedications("timeout_medication");

        assertNull(medications);
        assertEquals("No results found.", openFDAClient.getResultsMessageForView());
    }

}