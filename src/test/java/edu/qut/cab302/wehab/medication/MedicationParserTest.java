package edu.qut.cab302.wehab.medication;

import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.SocketTimeoutException;

import static org.junit.jupiter.api.Assertions.*;

public class MedicationParserTest {

    private MedicationParser medicationParser;

    @BeforeEach
    public void setUp() {
        medicationParser = new MedicationParser();
    }

    @Test
    public void testParseMedications_ValidResults() {

        Medication[] medications = medicationParser.parseMedications(validJsonBlueprint);

        assertNotNull(medications);
        assertEquals(1, medications.length);
        assertEquals("Results: 1", medicationParser.getResultsMessage());
    }

    @Test
    public void testParseMedications_InvalidResults() {

        assertThrows(JSONException.class, () -> medicationParser.parseMedications("non_json_response"));
    }

    @Test
    public void testParseMedications_NoResultsField() {

        Medication[] medications = medicationParser.parseMedications("{}");
        assertNull(medications);
        assertEquals("No results found.", medicationParser.getResultsMessage());
    }

    @Test
    public void testParseMedications_NullResults() {
        assertNull(medicationParser.parseMedications(null));
        assertEquals("No results found.", medicationParser.getResultsMessage());
    }

    private String validJsonBlueprint = """
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
