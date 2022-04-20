import io.github.sskorol.core.DataSupplier;
import io.github.sskorol.data.CsvReader;
import one.util.streamex.StreamEx;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.io.IOException;

import static io.github.sskorol.data.TestDataReader.use;

@Listeners(ListenerTest.class)
public class SnmpFilterTest {

    private static String SUCCESSFULLY_STARTED = "Enter an OID to process.Enter 'quit' to exit at any time.";
    private static String APP_ERROR = "Error: Loader: cannot process file";
    private static String EMPTY_DOC_ERROR = "Error: Loader: empty document";
    private static String HELP_TEXT = "A program to determine whether an OID is a descendant of an OID in a YAML document.";


    /**
     * Validation against Default Yaml
     */

    @Test(dataProvider = "data-provider" )
    public void testIfSnmpIsFiltered(String snmp, String expectedResult) throws IOException, InterruptedException {
        String result = CliOperations.getResponseAfterValidationAgainstDefaultYamlFile(snmp);
        Assert.assertTrue(isApplicationStarted(result), "Application didnt start");
        Assert.assertTrue(result.contains(expectedResult), " Trap is not validated.");
    }

    /**
     * Validation against Custom YAML
     */
    @Test(dataProvider = "data-provider" )
    public void testIfSnmpIsFilteredBasedOnCustomYaml(String snmp, String expectedResult) throws IOException, InterruptedException {
        String result = CliOperations.getResponseAfterValidationAgainstGivenYamlFile(snmp);
        Assert.assertTrue(isApplicationStarted(result), "Application didnt start");
        Assert.assertTrue(result.contains(expectedResult), " Trap is not validated.");
    }

    /**
     * Invalid path for YAML
     */
    @Test
    public void testIfTheApplicationStartFailsWhenInvalidYamlIsProvided() throws IOException, InterruptedException {
        String result = CliOperations.tryStartingAppWithInvalidYamlFile(".1.3.6.1.4.1.9.9.117.2.0.1");
        Assert.assertFalse(isApplicationStarted(result));
        Assert.assertTrue(isApplicationStartThrowsError(result));
    }

    /**
     * Help Feature
     */
    @Test
    public void testIfAppPrintsHelpDetails() throws IOException, InterruptedException {
        String result = CliOperations.getResponseAfterHelp(".1.3.6.1.4.1.9.9.117.2.0.1");
        Assert.assertTrue(isHelpDetailsDisplayed(result));
    }

    /**
     * Empty YAML
     */
    @Test
    public void testIfTheApplicationStartFailsWhenEmptyYamlIsProvided() throws IOException, InterruptedException {
        String result = CliOperations.getResponseAfterValidationAgainstGivenEmptyYamlFile(".1.3.6.1.4.1.9.9.117.2.0.1");
        Assert.assertFalse(isApplicationStarted(result));
        Assert.assertTrue(isEmptyDocErrorDisplayed(result));
    }

    /**
     * This is under construction to Read the Test Data from YAML File .
     */

    /*@DataSupplier(name = "data-provider")
    public StreamEx<TestData> testDataWithExpectedResults() {
        return use(CsvReader.class)
                .withTarget(TestData.class)
                .withSource("/Users/alfinsamuel/Documents/openNMS/src/test/resources/snmpTest.csv")
                .read();
    }*/


    /**
     * Each set of Data contains SNMP trap as well as Expected Result
     */
    @DataProvider(name = "data-provider")
    public Object[][] dpMethod(){
        return new Object[][] { {".1.3.6.1.4.1.9.9.117.2.0.1",                      "true"   }, // Positive Scenario
                                {".1.3.6.1.2.1.10.55",                              "false"  },
                                {".1.3.6.10",                                       "false"  },
                                {"asadasa",                                         "invalid"}, // Invalid Integer
                                {"quit",                                            "Exiting"}, // Quiting after starting
                                {".1.3.6.1.4.1.9.10.137.0.1.2147483647",            "true"   }, // Maximum Integer
                                {".1.3.6.1.4.1.9.10.137.0.1.23.23.23.2.32",         "true"   },
                                {".1.3.6.1.4.1.9.10.137.0.1.23.23.23.45",           "true"   },
                                {".2.4.6.1.4.1.9.10.137.0.1.23.23.23.4.5",          "true"   },
                                {".9943.234",                                       "true"   },
                                {".1.234.10.20",                                    "true"   },//Oid .1.234. is present (Additional dot)
                                // Crazy Test to check a long input
                                {".2.4.6.1.4.1.9.10.137.0.1.23.23.23.4.2.4.6.1.4.1.9.10.137.0.1.23.23.23.4.2.4.6.1.4.1.9.10.137.0.1.23.23.23.4.2.4.6.1.4.1.9.10.137.0.1.23.23.23.4.34",          "true"   }
        };
    }

    private Boolean isApplicationStarted(String responseText) {
        return responseText.contains(SUCCESSFULLY_STARTED);
    }

    private Boolean isApplicationStartThrowsError(String responseText) {
        return responseText.contains(APP_ERROR);
    }

    private Boolean isHelpDetailsDisplayed(String responseText) {
        return responseText.contains(HELP_TEXT);
    }

    private Boolean isEmptyDocErrorDisplayed(String responseText) {
        return responseText.contains(EMPTY_DOC_ERROR);
    }
}
