import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Unit test Weight.
 */
public class WeightTest
{

    @Test
    public void weightOfZero()
    {
        Byte tens = 0;
        Byte ones = 0;
        Byte tenths = 0;
        Byte hundredths = 0;
        Byte thousandths = 0;
        Weight weight = new Weight(false, tens, ones, tenths, hundredths, thousandths);
        assertEquals( weight.findValue(), 0, 1e-6 );
    }

    @Test
    public void weightOfOne()
    {
        Byte tens = 0;
        Byte ones = 1;
        Byte tenths = 0;
        Byte hundredths = 0;
        Byte thousandths = 0;
        Weight weight = new Weight(false, tens, ones, tenths, hundredths, thousandths);
        assertEquals( weight.findValue(), 1, 1e-6 );
    }

    @Test
    public void weightOfNegativeOne()
    {
        Byte tens = 0;
        Byte ones = 1;
        Byte tenths = 0;
        Byte hundredths = 0;
        Byte thousandths = 0;
        Weight weight = new Weight(true, tens, ones, tenths, hundredths, thousandths);
        assertEquals( weight.findValue(), -1, 1e-6 );
    }

    @Test
    public void weightOfNegativeDecimal()
    {
        Byte tens = 3;
        Byte ones = 1;
        Byte tenths = 2;
        Byte hundredths = 1;
        Byte thousandths = 5;
        Weight weight = new Weight(true, tens, ones, tenths, hundredths, thousandths);
        assertEquals( weight.findValue(), -31.215, 1e-6 );
    }

    @Test
    public void weightOfPositiveDecimal()
    {
        Byte tens = 3;
        Byte ones = 1;
        Byte tenths = 2;
        Byte hundredths = 1;
        Byte thousandths = 5;
        Weight weight = new Weight(false, tens, ones, tenths, hundredths, thousandths);
        assertEquals( weight.findValue(), 31.215, 1e-6 );
    }

    @Test
    public void weightCalculatingTwice()
    {
        Byte tens = 3;
        Byte ones = 1;
        Byte tenths = 2;
        Byte hundredths = 1;
        Byte thousandths = 5;
        Weight weight = new Weight(true, tens, ones, tenths, hundredths, thousandths);
        double value1 = weight.findValue();
        assertEquals( weight.findValue(), value1, 1e-6 );
    }


    @Test
    public void weightStringCheck()
    {
        Byte tens = 3;
        Byte ones = 1;
        Byte tenths = 2;
        Byte hundredths = 1;
        Byte thousandths = 5;
        Weight weight = new Weight(true, tens, ones, tenths, hundredths, thousandths);
        String valueString = weight.printWeightValue();
        assertTrue( valueString.equals("-31.215"));
    }

    @Test
    public void weightStringPositiveCheck()
    {
        Byte tens = 2;
        Byte ones = 7;
        Byte tenths = 4;
        Byte hundredths = 3;
        Byte thousandths = 0;
        Weight weight = new Weight(false, tens, ones, tenths, hundredths, thousandths);
        String valueString = weight.printWeightValue();
        System.out.println("value string: " + valueString);
        assertTrue( valueString.equals("27.43"));
    }
}
