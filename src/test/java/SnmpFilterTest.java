import io.github.sskorol.core.DataSupplier;
import io.github.sskorol.data.CsvReader;
import one.util.streamex.StreamEx;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;

import static io.github.sskorol.data.TestDataReader.use;

public class SnmpFilterTest {

    private static String SUCCESSFULLY_STARTED = "Enter an OID to process.Enter 'quit' to exit at any time.";
    private static String APP_ERROR = "Error: Loader: cannot process file";
    private static String HELP_TEXT = "A program to determine whether an OID is a descendant of an OID in a YAML document.";


    @Test(dataProvider = "datas-provider" )
    public void testIfSnmpIsFiltered(String snmp, String expectedResult) throws IOException, InterruptedException {
        String result = CliOperations.getResponseAfterValidationAgainstDefaultYamlFile(snmp);
        Assert.assertTrue(isApplicationStarted(result), "Application didnt start");
        Assert.assertTrue(result.contains(expectedResult), " Trap is not validated.");
    }

    @Test(dataProvider = "datas-provider" )
    public void testIfSnmpIsFilteredBasedOnCustomYaml(String snmp, String expectedResult) throws IOException, InterruptedException {
        String result = CliOperations.getResponseAfterValidationAgainstGivenYamlFile(snmp);
        Assert.assertTrue(isApplicationStarted(result), "Application didnt start");
        Assert.assertTrue(result.contains(expectedResult), " Trap is not validated.");
    }

    @Test
    public void testIfTheApplicationStartsIfInvalidYamlIsProvided() throws IOException, InterruptedException {
        String result = CliOperations.tryStartingAppWithInvalidYamlFile(".1.3.6.1.4.1.9.9.117.2.0.1");
        Assert.assertFalse(isApplicationStarted(result));
        Assert.assertTrue(isApplicationStartThrowsError(result));
    }

    @Test
    public void testIfAppPrintsHelpDetails() throws IOException, InterruptedException {
        String result = CliOperations.getResponseAfterHelp(".1.3.6.1.4.1.9.9.117.2.0.1");
        Assert.assertTrue(isHelpDetailsDisplayed(result));
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
    @DataProvider(name = "datas-provider")
    public Object[][] dpMethod(){
        return new Object[][] { {".1.3.6.1.4.1.9.9.117.2.0.1", "true"},
                                {".1.3.6.1.2.1.10.55", "false"},
                                {".1.3.6.10", "false"},
                                {"asadasa", "invalid"},
                                {"quit", "Exiting"},
                                {".1.3.6.1.4.1.9.10.137.0.1.2147483647", "true"},
                                {".1.3.6.1.4.1.9.10.137.0.1.23.23.23.2.32", "true"},
                                {".1.3.6.1.4.1.9.10.137.0.1.23.23.23.45", "true"},
                                {".2.4.6.1.4.1.9.10.137.0.1.23.23.23.4.5", "true"}
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
}
