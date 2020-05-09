import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit test Weight.
 */
public class InputDataTest {

    @Test
    public void failedToFindFile()
    {
        try {
            InputData.loadFiles("fakeFile.csv", "fakeFile.csv", "//target//classes//UNRATE.csv",
                    "//target//classes//CIVPART.csv", "target\\classes\\bankBorrowing.csv");
            assertFalse(true);
        } catch (Exception e) {
            assertTrue(true);
        }
        assertEquals(InputData.getAllDataList().size(), 0);
    }

    @Test
    public void readFile()
    {
        System.out.println("current directory: " + System.getProperty("user.dir"));
        setupInputTest();
        assertTrue(InputData.getAllDataList().size()> 3600);
    }

    @Test
    public void readDayZero()
    {
        System.out.println("current directory: " + System.getProperty("user.dir"));
        setupInputTest();
        assertEquals(10989.089844, InputData.getAllDataList().get(0).dowJonesClosing, 1e-5);
    }

    @Test
    public void readDayOneHundred()
    {
        System.out.println("current directory: " + System.getProperty("user.dir"));
        setupInputTest();
        assertEquals(11673.25651, InputData.getAllDataList().get(100).dowJonesClosing, 1e-5);
    }


    @Test
    public void predictDayTwo()
    {
        System.out.println("current directory: " + System.getProperty("user.dir"));
        setupInputTest();
        assertEquals(11026.55013, InputData.getAllDataList().get(2).dowJonesClosing, 1e-5);
    }


    @Test
    public void predictDay1277()
    {
        System.out.println("current directory: " + System.getProperty("user.dir"));
        setupInputTest();
        assertEquals(10414.139648, InputData.getAllDataList().get(1277).dowJonesClosing, 1e-5);
    }

    @Test
    public void findPercentChangeDay0(){
        setupInputTest();
        assertEquals(0, InputData.getAllDataList().get(0).dowJonesClosingPercent, 1e-6);

    }

    @Test
    public void findPercentChangeDay1(){
        setupInputTest();
        assertEquals(0.1704431, InputData.getAllDataList().get(1).dowJonesClosingPercent, 1e-6);

    }

    @Test
    public void findPercentChangeDay2(){
        setupInputTest();
        assertEquals(0.1701531, InputData.getAllDataList().get(2).dowJonesClosingPercent, 1e-6);

    }


    @Test
    public void findCivRateDay0(){
        setupInputTest();
        assertEquals(66.2, InputData.getAllDataList().get(0).civilianParticipationRate, 1e-6);

    }

    @Test
    public void findCivRateDay1(){
        setupInputTest();
        assertEquals(66.2, InputData.getAllDataList().get(1).civilianParticipationRate, 1e-6);

    }

    @Test
    public void findCivRateDay35(){
        setupInputTest();
        assertEquals(66.1, InputData.getAllDataList().get(35).civilianParticipationRate, 1e-6);

    }

    @Test
    public void dumpInfo() {
        setupInputTest();
        InputData.printFullInformation();
    }

    @Test
    public void findUnemploymentRateDay0(){
        setupInputTest();
        assertEquals(4.6, InputData.getAllDataList().get(0).unemploymentRate, 1e-6);

    }

    @Test
    public void findUnemploymentRateDay3(){
        setupInputTest();
        assertEquals(4.6, InputData.getAllDataList().get(3).unemploymentRate, 1e-6);

    }

    @Test
    public void findUnemploymentRateLastDay(){
        setupInputTest();
        assertEquals(3.6, InputData.getAllDataList().get(InputData.getAllDataList().size()-1).unemploymentRate, 1e-6);

    }

    @Test
    public void findUnemploymentRatePercentChangeDay3(){
        setupInputTest();
        assertEquals(0, InputData.getAllDataList().get(3).unemploymentRatePercentChange, 1e-6);

    }

    @Test
    public void findUnemploymentRatePercentChangeSecondDay(){
        setupInputTest();
        assertEquals(0, InputData.getAllDataList().get(2).unemploymentRatePercentChange, 1e-6);

    }


    @Test
    public void findParticipationRatePercentChangeDay3(){
        setupInputTest();
        assertEquals(0.0, InputData.getAllDataList().get(3).civilianParticipationRatePercentChange, 1e-6);

    }

    @Test
    public void findParticipationRatePercentChangeDay31(){
        setupInputTest();
        assertEquals(0.0, InputData.getAllDataList().get(31).civilianParticipationRatePercentChange, 1e-6);
    }

    @Test
    public void findParticipationRatePercentChangeSecondDay(){
        setupInputTest();
        assertEquals(0, InputData.getAllDataList().get(2).civilianParticipationRatePercentChange, 1e-6);
    }

    private void setupInputTest(){
        try {
            InputData.loadFiles("FDN.csv", "DJI.csv",
                    "UNRATE.csv",
                    "CIVPART.csv", "bankBorrowing.csv");
            String startDateString = InputData.getStartDate().toString();
            assertTrue(startDateString.contains( "Jun 22 "));
            assertTrue(startDateString.contains("2006"));
            assertTrue(InputData.getEndDate().toString().contains("Feb 21"));
            assertTrue(InputData.getEndDate().toString().contains("2020"));
        } catch(Exception ex) {
            assertFalse(true);
        }
    }


}