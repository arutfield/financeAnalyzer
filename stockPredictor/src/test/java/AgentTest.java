import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AgentTest {

    @Test
    public void findFitnessFunctionWeight0(){
        InputData.loadDowJonesClosing("target\\classes\\DJI.csv");
        Byte tens = 0;
        Byte ones = 0;
        Byte tenths = 0;
        Byte hundredths = 0;
        Byte thousandths = 0;
        Weight weight = new Weight(false, tens, ones, tenths, hundredths, thousandths );

        Byte tens1 = 0;
        Byte ones1 = 1;
        Byte tenths1 = 0;
        Byte hundredths1 = 0;
        Byte thousandths1 = 0;
        Weight weight2 = new Weight(false, tens1, ones1, tenths1, hundredths1, thousandths1 );

        Agent agent = new Agent(weight, weight2);
        assertEquals(126980104, agent.getFitnessValueDowPrediction(), 1);

    }

    @Test
    public void findFitnessFunctionWeight1Point1() {
        InputData.loadDowJonesClosing("target\\classes\\DJI.csv");
        Byte tens = 0;
        Byte ones = 1;
        Byte tenths = 1;
        Byte hundredths = 0;
        Byte thousandths = 0;
        Weight weight = new Weight(false, tens, ones, tenths, hundredths, thousandths );

        Byte tens1 = 0;
        Byte ones1 = 1;
        Byte tenths1 = 0;
        Byte hundredths1 = 0;
        Byte thousandths1 = 0;
        Weight weight2 = new Weight(false, tens1, ones1, tenths1, hundredths1, thousandths1 );


        Agent agent = new Agent(weight, weight2);
        assertEquals(12667439, agent.getFitnessValueDowPrediction(), 1);

    }
}
