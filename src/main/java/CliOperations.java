import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class CliOperations {

    private static String APPLICATION_PATH = System.getProperty("applicationPath", "/Users/alfinsamuel/Documents/oids-master/bin/oid.sh" );
    private static String TEST_YAML_FOR_APP_PATH =  System.getProperty("testYamlForAppStart",
            System.getProperty("user.dir")+"/snmpTest.yaml");

    /**
     * Command to Start the application with custom external YAML file
     */
    public static String tryStartingAppWithInvalidYamlFile(String snmp) throws IOException, InterruptedException {
        String command = "printf " +snmp +" | " + APPLICATION_PATH + " -f "+ "invalid.yaml";
        return runCommand(command);
    }
    /**
     * Command to Start the application with custom external YAML file
     */
    public static String getResponseAfterValidationAgainstGivenYamlFile(String snmp) throws IOException, InterruptedException {
        String command = "printf " +snmp +" | " + APPLICATION_PATH + " -f "+ TEST_YAML_FOR_APP_PATH;
        System.out.println("$$$$$$$File$$$$$ - "+ TEST_YAML_FOR_APP_PATH);
        return runCommand(command);
    }

    /**
     * Command to Start the application with default YAML file in the application repo
     */
    public static String getResponseAfterValidationAgainstDefaultYamlFile(String snmp) throws IOException, InterruptedException {
        String command = "printf " +snmp +" | " + APPLICATION_PATH;
        return runCommand(command);
    }

    /**
     * Get Help about the application
     */
    public static String getResponseAfterHelp(String snmp) throws IOException, InterruptedException {
        String command = "printf " +snmp +" | " + APPLICATION_PATH + " -h";
        return runCommand(command);
    }

    /**
     *
     * @param command
     * @return Response in Command Line after the execution of command
     * @throws IOException
     * @throws InterruptedException
     */
    private static String runCommand(String command) throws IOException, InterruptedException {
        StringBuilder stringBuilder = new StringBuilder();
        String result = null;
        ProcessBuilder builder = new ProcessBuilder();
        builder.command("bash","-c",command);
        builder.redirectErrorStream(true);
        Process process = builder.start();
        InputStream is = process.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));

        String line = null;
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
            System.out.println(line);

        }
        return stringBuilder.toString();
    }

}
