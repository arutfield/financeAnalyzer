import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AgentTest {

    @Test
    public void findFitnessFunctionWeight0(){
        InputData inputData = new InputData("target\\classes\\DJI.csv");
        Byte tens = 0;
        Byte ones = 0;
        Byte tenths = 0;
        Byte hundredths = 0;
        Byte thousandths = 0;
        Weight weight = new Weight(false, tens, ones, tenths, hundredths, thousandths );
        Agent agent = new Agent(weight);
        assertEquals(126980104, agent.getFitnessValueDowPrediction(), 1);

    }

    @Test
    public void findFitnessFunctionWeight1Point1() {
        InputData inputData = new InputData("target\\classes\\DJI.csv");
        Byte tens = 0;
        Byte ones = 1;
        Byte tenths = 1;
        Byte hundredths = 0;
        Byte thousandths = 0;
        Weight weight = new Weight(false, tens, ones, tenths, hundredths, thousandths );
        Agent agent = new Agent(weight);
        assertEquals(12667439, agent.getFitnessValueDowPrediction(), 1);

    }
}
