package edu.qut.cab302.wehab.medication;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class FDAApiServiceTest {

    @Test
    public void testSearchForMedicationSuccess() {



    }

    private String mockApiResult = "{\n" +
            "    \"meta\": {\n" +
            "      \"disclaimer\": \"\",\n" +
            "      \"terms\": \"\",\n" +
            "      \"license\": \"\",\n" +
            "      \"last_updated\": \"\",\n" +
            "      \"results\": {\n" +
            "        \"skip\": 0,\n" +
            "        \"limit\": 30,\n" +
            "        \"total\": 1\n" +
            "      }\n" +
            "    },\n" +
            "    \"results\": [\n" +
            "      {\n" +
            "        \"spl_product_data_elements\": [\n" +
            "          \"\"\n" +
            "        ],\n" +
            "        \"recent_major_changes\": [\n" +
            "          \"\"\n" +
            "        ],\n" +
            "        \"indications_and_usage\": [\n" +
            "          \"\"\n" +
            "        ],\n" +
            "        \"dosage_and_administration\": [\n" +
            "          \"\"\n" +
            "        ],\n" +
            "        \"dosage_forms_and_strengths\": [\n" +
            "          \"\"\n" +
            "        ],\n" +
            "        \"contraindications\": [\n" +
            "          \"\"\n" +
            "        ],\n" +
            "        \"warnings_and_cautions\": [\n" +
            "          \"\"\n" +
            "        ],\n" +
            "        \"adverse_reactions\": [\n" +
            "          \"\"\n" +
            "        ],\n" +
            "        \"adverse_reactions_table\": [\n" +
            "          \"\"\n" +
            "        ],\n" +
            "        \"drug_interactions\": [\n" +
            "          \"\"\n" +
            "        ],\n" +
            "        \"drug_interactions_table\": [\n" +
            "          \"\"\n" +
            "        ],\n" +
            "        \"use_in_specific_populations\": [\n" +
            "          \"\"\n" +
            "        ],\n" +
            "        \"pregnancy\": [\n" +
            "          \"\"\n" +
            "        ],\n" +
            "        \"pediatric_use\": [\n" +
            "          \"\"\n" +
            "        ],\n" +
            "        \"geriatric_use\": [\n" +
            "          \"\"\n" +
            "        ],\n" +
            "        \"drug_abuse_and_dependence\": [\n" +
            "          \"\"\n" +
            "        ],\n" +
            "        \"controlled_substance\": [\n" +
            "          \"\"\n" +
            "        ],\n" +
            "        \"abuse\": [\n" +
            "          \"\"\n" +
            "        ],\n" +
            "        \"dependence\": [\n" +
            "          \"\"\n" +
            "        ],\n" +
            "        \"overdosage\": [\n" +
            "          \"\"\n" +
            "        ],\n" +
            "        \"description\": [\n" +
            "          \"\"\n" +
            "        ],\n" +
            "        \"description_table\": [\n" +
            "          \"\"\n" +
            "        ],\n" +
            "        \"clinical_pharmacology\": [\n" +
            "          \"\"\n" +
            "        ],\n" +
            "        \"mechanism_of_action\": [\n" +
            "          \"\"\n" +
            "        ],\n" +
            "        \"pharmacodynamics\": [\n" +
            "          \"\"\n" +
            "        ],\n" +
            "        \"pharmacokinetics\": [\n" +
            "          \"\"\n" +
            "        ],\n" +
            "        \"nonclinical_toxicology\": [\n" +
            "          \"\"\n" +
            "        ],\n" +
            "        \"carcinogenesis_and_mutagenesis_and_impairment_of_fertility\": [\n" +
            "          \"\"\n" +
            "        ],\n" +
            "        \"animal_pharmacology_and_or_toxicology\": [\n" +
            "          \"\"\n" +
            "        ],\n" +
            "        \"clinical_studies\": [\n" +
            "          \"\"\n" +
            "        ],\n" +
            "        \"clinical_studies_table\": [\n" +
            "          \"\"\n" +
            "        ],\n" +
            "        \"how_supplied\": [\n" +
            "          \"\"\n" +
            "        ],\n" +
            "        \"storage_and_handling\": [\n" +
            "          \"\"\n" +
            "        ],\n" +
            "        \"information_for_patients\": [\n" +
            "          \"\"\n" +
            "        ],\n" +
            "        \"information_for_patients_table\": [\n" +
            "          \"\"\n" +
            "        ],\n" +
            "        \"spl_medguide\": [\n" +
            "          \"\"\n" +
            "        ],\n" +
            "        \"spl_medguide_table\": [\n" +
            "          \"\"\n" +
            "        ],\n" +
            "        \"package_label_principal_display_panel\": [\n" +
            "          \"\"\n" +
            "        ],\n" +
            "        \"set_id\": \"7074cb65-77b3-45d2-8e8d-da8dc0f70bfd\",\n" +
            "        \"id\": \"fab80860-4be1-4a9b-a2f5-81a7d31f7d20\",\n" +
            "        \"effective_time\": \"20231218\",\n" +
            "        \"version\": \"38\",\n" +
            "        \"openfda\": {\n" +
            "          \"application_number\": [\n" +
            "            \"NDA212028\"\n" +
            "          ],\n" +
            "          \"brand_name\": [\n" +
            "            \"DAYVIGO\"\n" +
            "          ],\n" +
            "          \"generic_name\": [\n" +
            "            \"LEMBOREXANT\"\n" +
            "          ],\n" +
            "          \"manufacturer_name\": [\n" +
            "            \"Eisai Inc.\"\n" +
            "          ],\n" +
            "          \"product_ndc\": [\n" +
            "            \"62856-405\",\n" +
            "            \"62856-410\",\n" +
            "            \"62856-455\"\n" +
            "          ],\n" +
            "          \"product_type\": [\n" +
            "            \"HUMAN PRESCRIPTION DRUG\"\n" +
            "          ],\n" +
            "          \"route\": [\n" +
            "            \"ORAL\"\n" +
            "          ],\n" +
            "          \"substance_name\": [\n" +
            "            \"LEMBOREXANT\"\n" +
            "          ],\n" +
            "          \"rxcui\": [\n" +
            "            \"2272408\",\n" +
            "            \"2272414\",\n" +
            "            \"2288425\",\n" +
            "            \"2288427\"\n" +
            "          ],\n" +
            "          \"spl_id\": [\n" +
            "            \"fab80860-4be1-4a9b-a2f5-81a7d31f7d20\"\n" +
            "          ],\n" +
            "          \"spl_set_id\": [\n" +
            "            \"7074cb65-77b3-45d2-8e8d-da8dc0f70bfd\"\n" +
            "          ],\n" +
            "          \"package_ndc\": [\n" +
            "            \"62856-405-30\",\n" +
            "            \"62856-405-90\",\n" +
            "            \"62856-405-10\",\n" +
            "            \"62856-410-30\",\n" +
            "            \"62856-410-90\",\n" +
            "            \"62856-455-10\"\n" +
            "          ],\n" +
            "          \"is_original_packager\": [\n" +
            "            true\n" +
            "          ],\n" +
            "          \"upc\": [\n" +
            "            \"0362856410304\",\n" +
            "            \"0362856405300\",\n" +
            "            \"0362856410908\",\n" +
            "            \"0362856405904\"\n" +
            "          ],\n" +
            "          \"nui\": [\n" +
            "            \"N0000191000\",\n" +
            "            \"N0000190998\",\n" +
            "            \"N0000187064\"\n" +
            "          ],\n" +
            "          \"pharm_class_epc\": [\n" +
            "            \"Orexin Receptor Antagonist [EPC]\"\n" +
            "          ],\n" +
            "          \"pharm_class_moa\": [\n" +
            "            \"Orexin Receptor Antagonists [MoA]\",\n" +
            "            \"Cytochrome P450 2B6 Inducers [MoA]\"\n" +
            "          ],\n" +
            "          \"unii\": [\n" +
            "            \"0K5743G68X\"\n" +
            "          ]\n" +
            "        }\n" +
            "      }\n" +
            "    ]\n" +
            "  }";
    
}
