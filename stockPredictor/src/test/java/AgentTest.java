import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class AgentTest {

    @Test
    public void findFitnessFunctionWeight0() throws Exception {
        setupInputTest();

        Agent agent = new Agent(new Weight[]{getEmptyWeight(), getEmptyWeight(), getEmptyWeight(), getEmptyWeight(), getEmptyWeight(), getEmptyWeight(),
                getEmptyWeight(), getEmptyWeight(), getEmptyWeight(), getEmptyWeight(), getEmptyWeight()});
        assertEquals(0.498675, agent.getFitnessValue(), 1);

    }

    @Test
    public void findFitnessFunctionWeight1Point1() throws Exception {
        setupInputTest();
        Byte ones = 0;
        Byte tenths = 1;
        Byte hundredths = 0;
        Byte thousandths = 0;
        Byte tenThousandths = 0;
        Byte hundredThousandths = 0;
        Byte millionths = 0;
        Byte tenMillionths = 0;
        Weight weight = new Weight(false, ones, tenths, hundredths, thousandths, tenThousandths, hundredThousandths, millionths, tenMillionths);


        Agent agent = new Agent(new Weight[]{weight, getEmptyWeight(), getEmptyWeight(), getEmptyWeight(), getEmptyWeight(), getEmptyWeight(),
                getEmptyWeight(), getEmptyWeight(), getEmptyWeight(), getEmptyWeight(), getEmptyWeight()});
        assertEquals(5.929342, agent.getFitnessValue(), 1e-5);

    }

    private void setupInputTest(){
        try {
            InputData.loadFiles("target\\classes\\FDN.csv","target\\classes\\DJI.csv",
                    "target\\classes\\UNRATE.csv",
                    "target\\classes\\CIVPART.csv",
                    "target\\classes\\bankBorrowing.csv");
        } catch (Exception e) {
            assertFalse(true);
        }

    }

    private Weight getEmptyWeight() {
        Byte zeroValue = 0;
        return new Weight(false, zeroValue, zeroValue, zeroValue, zeroValue, zeroValue, zeroValue, zeroValue, zeroValue );
    }
}
