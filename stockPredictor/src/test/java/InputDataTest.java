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
        InputData inputData = new InputData("fakeFile.csv");
        assertEquals(inputData.getDowJonesClosingList().size(), 0);
    }

    @Test
    public void readFile()
    {
        System.out.println("current directory: " + System.getProperty("user.dir"));
        InputData inputData = new InputData("target\\classes\\DJI.csv");
        assertTrue(inputData.getDowJonesClosingList().size()> 8838);
    }

    @Test
    public void readDayZero()
    {
        System.out.println("current directory: " + System.getProperty("user.dir"));
        InputData inputData = new InputData("target\\classes\\DJI.csv");
        assertEquals(1292.619995, inputData.getDowJonesClosingList().get(0), 1e-6);
    }

    @Test
    public void readDayOneHundred()
    {
        System.out.println("current directory: " + System.getProperty("user.dir"));
        InputData inputData = new InputData("target\\classes\\DJI.csv");
        assertEquals(1260.27002, inputData.getDowJonesClosingList().get(100), 1e-6);
    }


    @Test
    public void predictDayFour()
    {
        System.out.println("current directory: " + System.getProperty("user.dir"));
        InputData inputData = new InputData("target\\classes\\DJI.csv");
        assertEquals(1281.839966, inputData.getDowJonesClosingList().get(4), 1e-6);
    }


    @Test
    public void predictDay12774()
    {
        System.out.println("current directory: " + System.getProperty("user.dir"));
        InputData inputData = new InputData("target\\classes\\DJI.csv");
        assertEquals(29234.0541995, inputData.getDowJonesClosingList().get(12774), 1e-6);
    }

    @Test
    public void findPercentChangeDay0(){
        InputData inputData = new InputData("target\\classes\\DJI.csv");
        assertEquals(0, inputData.getDowJonesClosingMapChangePercent().get(0), 1e-6);

    }

    @Test
    public void findPercentChangeDay1(){
        InputData inputData = new InputData("target\\classes\\DJI.csv");
        assertEquals(-0.3666963, inputData.getDowJonesClosingMapChangePercent().get(1), 1e-6);

    }

    @Test
    public void findPercentChangeDay2(){
        InputData inputData = new InputData("target\\classes\\DJI.csv");
        assertEquals(-0.086187, inputData.getDowJonesClosingMapChangePercent().get(2), 1e-6);

    }

    @Test
    public void dumpInfo() {
        InputData inputData = new InputData("target\\classes\\DJI.csv");
        inputData.printFullDowJonesClosing();
    }


}