import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PopulationTest {
    @Test
    public void createPopulation() throws Exception {
        Population population = new Population("target\\classes\\DJI.csv", 10000);
        assertTrue(true);
    }


}
