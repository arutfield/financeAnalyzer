import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AgentTest {

    @Test
    public void findFitnessFunctionWeight0() throws Exception {
        setupInputTest();

        Agent agent = new Agent(new Weight[]{getEmptyWeight(), getEmptyWeight(), getEmptyWeight(), getEmptyWeight(),
                getEmptyWeight(), getEmptyWeight()});
        assertEquals(607821.785/InputData.getAllDataList().size(), agent.getFitnessValueDowPrediction(), 1);

    }

    @Test
    public void findFitnessFunctionWeight1Point1() throws Exception {
        setupInputTest();
        Byte tens = 0;
        Byte ones = 1;
        Byte tenths = 1;
        Byte hundredths = 0;
        Byte thousandths = 0;
        Byte tenThousandths = 0;
        Weight weight = new Weight(false, tens, ones, tenths, hundredths, thousandths, tenThousandths );


        Agent agent = new Agent(new Weight[]{weight, getEmptyWeight(), getEmptyWeight(), getEmptyWeight(), getEmptyWeight(), getEmptyWeight()});
        assertEquals(155739941.4, agent.getFitnessValueDowPrediction(), 1);

    }

    private void setupInputTest(){
        InputData.loadFiles("target\\classes\\DJI.csv", "target\\classes\\UNRATE.csv", "target\\classes\\CIVPART.csv");

    }

    private Weight getEmptyWeight() {
        Byte zeroValue = 0;
        return new Weight(false, zeroValue, zeroValue, zeroValue, zeroValue, zeroValue, zeroValue );
    }
}
