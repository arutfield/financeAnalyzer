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
        InputData.loadDowJonesClosing("fakeFile.csv");
        assertEquals(InputData.getAllDataList().size(), 0);
    }

    @Test
    public void readFile()
    {
        System.out.println("current directory: " + System.getProperty("user.dir"));
        InputData.loadDowJonesClosing("target\\classes\\DJI.csv");
        assertTrue(InputData.getAllDataList().size()> 8838);
    }

    @Test
    public void readDayZero()
    {
        System.out.println("current directory: " + System.getProperty("user.dir"));
        InputData.loadDowJonesClosing("target\\classes\\DJI.csv");
        assertEquals(1292.619995, InputData.getAllDataList().get(0).dowJonesClosing, 1e-6);
    }

    @Test
    public void readDayOneHundred()
    {
        System.out.println("current directory: " + System.getProperty("user.dir"));
        InputData.loadDowJonesClosing("target\\classes\\DJI.csv");
        assertEquals(1260.27002, InputData.getAllDataList().get(100).dowJonesClosing, 1e-6);
    }


    @Test
    public void predictDayFour()
    {
        System.out.println("current directory: " + System.getProperty("user.dir"));
        InputData.loadDowJonesClosing("target\\classes\\DJI.csv");
        assertEquals(1281.839966, InputData.getAllDataList().get(4).dowJonesClosing, 1e-6);
    }


    @Test
    public void predictDay12774()
    {
        System.out.println("current directory: " + System.getProperty("user.dir"));
        InputData.loadDowJonesClosing("target\\classes\\DJI.csv");
        assertEquals(29234.0541995, InputData.getAllDataList().get(12774).dowJonesClosing, 1e-6);
    }

    @Test
    public void findPercentChangeDay0(){
        InputData.loadDowJonesClosing("target\\classes\\DJI.csv");
        assertEquals(0, InputData.getAllDataList().get(0).dowJonesClosingPercent, 1e-6);

    }

    @Test
    public void findPercentChangeDay1(){
        InputData.loadDowJonesClosing("target\\classes\\DJI.csv");
        assertEquals(-0.3666963, InputData.getAllDataList().get(1).dowJonesClosingPercent, 1e-6);

    }

    @Test
    public void findPercentChangeDay2(){
        InputData.loadDowJonesClosing("target\\classes\\DJI.csv");
        assertEquals(-0.086187, InputData.getAllDataList().get(2).dowJonesClosingPercent, 1e-6);

    }

    @Test
    public void dumpInfo() {
        InputData.loadDowJonesClosing("target\\classes\\DJI.csv");
        InputData.printFullDowJonesClosing();
    }


}