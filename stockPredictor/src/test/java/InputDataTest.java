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
        assertEquals(inputData.getDowJonesClosingMap().size(), 8838);
    }


}