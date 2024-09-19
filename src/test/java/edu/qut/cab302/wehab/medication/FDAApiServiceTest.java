package edu.qut.cab302.wehab.medication;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import org.skyscreamer.jsonassert.JSONAssert;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.net.URL;
import java.net.HttpURLConnection;

public class FDAApiServiceTest {

    @Spy
    @InjectMocks
    private FDAApiService apiService;

    @Mock
    private HttpURLConnection mockConnection;

    @Mock
    private URL mockURL;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testQueryAPI_BrandNameSuccess() throws IOException {

        doReturn(mockURL).when(apiService).createUrlObject(any(String.class));
        when(mockURL.openConnection()).thenReturn(mockConnection);  // Mock the connection
        when(mockConnection.getResponseCode()).thenReturn(200);  // Simulate a 200 response
        InputStream mockInputStream = new ByteArrayInputStream(mockApiResult.getBytes());
        when(mockConnection.getInputStream()).thenReturn(mockInputStream);  // Mock the input stream

        String response = apiService.queryAPI("dayvigo");

        verify(apiService, times(1)).createUrlObject(any(String.class));
        assertNotNull(response);
        JSONAssert.assertEquals(response, mockApiResult, false);
    }

    private String mockApiResult = """
    {
    "meta": {
      "disclaimer": "",
      "terms": "",
      "license": "",
      "last_updated": "",
      "results": {
        "skip": 0,
        "limit": 30,
        "total": 1
      }
    },
    "results": [
      {
        "spl_product_data_elements": [
          ""
        ],
        "recent_major_changes": [
          ""
        ],
        "indications_and_usage": [
          ""
        ],
        "dosage_and_administration": [
          ""
        ],
        "dosage_forms_and_strengths": [
          ""
        ],
        "contraindications": [
          ""
        ],
        "warnings_and_cautions": [
          ""
        ],
        "adverse_reactions": [
          ""
        ],
        "adverse_reactions_table": [
          ""
        ],
        "drug_interactions": [
          ""
        ],
        "drug_interactions_table": [
          ""
        ],
        "use_in_specific_populations": [
          ""
        ],
        "pregnancy": [
          ""
        ],
        "pediatric_use": [
          ""
        ],
        "geriatric_use": [
          ""
        ],
        "drug_abuse_and_dependence": [
          ""
        ],
        "controlled_substance": [
          ""
        ],
        "abuse": [
          ""
        ],
        "dependence": [
          ""
        ],
        "overdosage": [
          ""
        ],
        "description": [
          ""
        ],
        "description_table": [
          ""
        ],
        "clinical_pharmacology": [
          ""
        ],
        "mechanism_of_action": [
          ""
        ],
        "pharmacodynamics": [
          ""
        ],
        "pharmacokinetics": [
          ""
        ],
        "nonclinical_toxicology": [
          ""
        ],
        "carcinogenesis_and_mutagenesis_and_impairment_of_fertility": [
          ""
        ],
        "animal_pharmacology_and_or_toxicology": [
          ""
        ],
        "clinical_studies": [
          ""
        ],
        "clinical_studies_table": [
          ""
        ],
        "how_supplied": [
          ""
        ],
        "storage_and_handling": [
          ""
        ],
        "information_for_patients": [
          ""
        ],
        "information_for_patients_table": [
          ""
        ],
        "spl_medguide": [
          ""
        ],
        "spl_medguide_table": [
          ""
        ],
        "package_label_principal_display_panel": [
          ""
        ],
        "set_id": "7074cb65-77b3-45d2-8e8d-da8dc0f70bfd",
        "id": "fab80860-4be1-4a9b-a2f5-81a7d31f7d20",
        "effective_time": "20231218",
        "version": "38",
        "openfda": {
          "application_number": [
            "NDA212028"
          ],
          "brand_name": [
            "DAYVIGO"
          ],
          "generic_name": [
            "LEMBOREXANT"
          ],
          "manufacturer_name": [
            "Eisai Inc."
          ],
          "product_ndc": [
            "62856-405",
            "62856-410",
            "62856-455"
          ],
          "product_type": [
            "HUMAN PRESCRIPTION DRUG"
          ],
          "route": [
            "ORAL"
          ],
          "substance_name": [
            "LEMBOREXANT"
          ],
          "rxcui": [
            "",
          ],
          "spl_id": [
            "fab80860-4be1-4a9b-a2f5-81a7d31f7d20"
          ],
          "spl_set_id": [
            "7074cb65-77b3-45d2-8e8d-da8dc0f70bfd"
          ],
          "package_ndc": [
            "",
          ],
          "is_original_packager": [
            true
          ],
          "upc": [
            "",
          ],
          "nui": [
            ""
          ],
          "pharm_class_epc": [
            ""
          ],
          "pharm_class_moa": [
            "",
          ],
          "unii": [
            ""
          ]
        }
      }
    ]
  }
            """;
}
