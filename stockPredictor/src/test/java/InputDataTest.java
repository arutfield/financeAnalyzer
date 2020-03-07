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
        assertEquals(inputData.getDowJonesClosingMap().size(), 0);
    }

    @Test
    public void readFile()
    {
        System.out.println("current directory: " + System.getProperty("user.dir"));
        InputData inputData = new InputData("target\\classes\\DJI.csv");
        assertTrue(inputData.getDowJonesClosingMap().size()> 8838);
    }

    @Test
    public void readDayZero()
    {
        System.out.println("current directory: " + System.getProperty("user.dir"));
        InputData inputData = new InputData("target\\classes\\DJI.csv");
        assertEquals(1292.619995, inputData.getDowJonesClosingMap().get(0), 1e-6);
    }

    @Test
    public void readDayOneHundred()
    {
        System.out.println("current directory: " + System.getProperty("user.dir"));
        InputData inputData = new InputData("target\\classes\\DJI.csv");
        assertEquals(1260.27002, inputData.getDowJonesClosingMap().get(100), 1e-6);
    }


    @Test
    public void predictDayFour()
    {
        System.out.println("current directory: " + System.getProperty("user.dir"));
        InputData inputData = new InputData("target\\classes\\DJI.csv");
        assertEquals(1281.839966, inputData.getDowJonesClosingMap().get(4), 1e-6);
    }

}