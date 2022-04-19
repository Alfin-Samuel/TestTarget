import lombok.*;

@Data
@RequiredArgsConstructor

public class TestData {

    private String snmp;
    private String expectedResult;

    public String getSnmp() {
        return snmp;
    }

    public void setSnmp(String snmp) {
        this.snmp = snmp;
    }

    public String getExpectedResult() {
        return expectedResult;
    }

    public void setExpectedResult(String expectedResult) {
        this.expectedResult = expectedResult;
    }


}
