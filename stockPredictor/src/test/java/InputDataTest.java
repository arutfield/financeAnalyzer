import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Unit test Weight.
 */
public class InputDataTest {

    @Test
    public void failedToFindFile()
    {
        InputData.loadFiles("fakeFile.csv", "//target//classes//UNRATE.csv",
                "//target//classes//CIVPART.csv");
        assertEquals(InputData.getAllDataList().size(), 0);
    }

    @Test
    public void readFile()
    {
        System.out.println("current directory: " + System.getProperty("user.dir"));
        setupInputTest();
        assertTrue(InputData.getAllDataList().size()> 8838);
    }

    @Test
    public void readDayZero()
    {
        System.out.println("current directory: " + System.getProperty("user.dir"));
        setupInputTest();
        assertEquals(1292.619995, InputData.getAllDataList().get(0).dowJonesClosing, 1e-6);
    }

    @Test
    public void readDayOneHundred()
    {
        System.out.println("current directory: " + System.getProperty("user.dir"));
        setupInputTest();
        assertEquals(1260.27002, InputData.getAllDataList().get(100).dowJonesClosing, 1e-6);
    }


    @Test
    public void predictDayFour()
    {
        System.out.println("current directory: " + System.getProperty("user.dir"));
        setupInputTest();
        assertEquals(1281.839966, InputData.getAllDataList().get(4).dowJonesClosing, 1e-6);
    }


    @Test
    public void predictDay12774()
    {
        System.out.println("current directory: " + System.getProperty("user.dir"));
        setupInputTest();
        assertEquals(29234.0541995, InputData.getAllDataList().get(12774).dowJonesClosing, 1e-6);
    }

    @Test
    public void findPercentChangeDay0(){
        setupInputTest();
        assertEquals(0, InputData.getAllDataList().get(0).dowJonesClosingPercent, 1e-6);

    }

    @Test
    public void findPercentChangeDay1(){
        setupInputTest();
        assertEquals(-0.3666963, InputData.getAllDataList().get(1).dowJonesClosingPercent, 1e-6);

    }

    @Test
    public void findPercentChangeDay2(){
        setupInputTest();
        assertEquals(-0.086187, InputData.getAllDataList().get(2).dowJonesClosingPercent, 1e-6);

    }


    @Test
    public void findCivRateDay0(){
        setupInputTest();
        assertEquals(64.7, InputData.getAllDataList().get(0).civilianParticipationRate, 1e-6);

    }

    @Test
    public void findCivRateDay1(){
        setupInputTest();
        assertEquals(64.7, InputData.getAllDataList().get(1).civilianParticipationRate, 1e-6);

    }

    @Test
    public void findCivRateDay35(){
        setupInputTest();
        assertEquals(64.9, InputData.getAllDataList().get(35).civilianParticipationRate, 1e-6);

    }




    @Test
    public void dumpInfo() {
        setupInputTest();
        InputData.printFullDowJonesClosing();
    }

    @Test
    public void findUnemploymentRateDay0(){
        setupInputTest();
        assertEquals(7.3, InputData.getAllDataList().get(0).unemploymentRate, 1e-6);

    }

    @Test
    public void findUnemploymentRateDay3(){
        setupInputTest();
        assertEquals(7.2, InputData.getAllDataList().get(3).unemploymentRate, 1e-6);

    }

    @Test
    public void findUnemploymentRateLastDay(){
        setupInputTest();
        assertEquals(3.6, InputData.getAllDataList().get(InputData.getAllDataList().size()-1).unemploymentRate, 1e-6);

    }

    @Test
    public void findUnemploymentRatePercentChangeDay3(){
        setupInputTest();
        assertEquals(-1.369863014, InputData.getAllDataList().get(3).unemploymentRatePercentChange, 1e-6);

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
        assertEquals(0.309119, InputData.getAllDataList().get(31).civilianParticipationRatePercentChange, 1e-6);
    }

    @Test
    public void findParticipationRatePercentChangeSecondDay(){
        setupInputTest();
        assertEquals(0, InputData.getAllDataList().get(2).civilianParticipationRatePercentChange, 1e-6);
    }

    private void setupInputTest(){
        InputData.loadFiles("target\\classes\\DJI.csv", "target\\classes\\UNRATE.csv", "target\\classes\\CIVPART.csv");

    }


}