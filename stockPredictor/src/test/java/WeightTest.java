import org.junit.Test;

import static org.junit.Assert.*;

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

    @Test
    public void weightsOutOfBounds()
    {
        Byte tens = 12;
        Byte ones = 17;
        Byte tenths = -4;
        Byte hundredths = 13;
        Byte thousandths = -6;
        Weight weight = new Weight(false, tens, ones, tenths, hundredths, thousandths);
        assertEquals(0, weight.findValue(), 1e-12);
    }

    @Test
    public void parentMerge0() throws Exception {
        Byte tens = 1;
        Byte ones = 1;
        Byte tenths = 4;
        Byte hundredths = 3;
        Byte thousandths = 6;
        Weight weight = new Weight(false, tens, ones, tenths, hundredths, thousandths);
        assertEquals(11.436, weight.findValue(), 1e-12);
        Agent agent = new Agent(weight);


        Byte tens2 = 2;
        Byte ones2 = 7;
        Byte tenths2 = 3;
        Byte hundredths2 = 1;
        Byte thousandths2 = 0;
        Weight weight2 = new Weight(true, tens2, ones2, tenths2, hundredths2, thousandths2);
        assertEquals(-27.31, weight2.findValue(), 1e-12);
        Agent agent1 = new Agent(weight2);
        Weight childWeight = new Weight(agent, agent1,0);

        assertEquals(weight2.isNegative(), childWeight.isNegative());
        assertEquals(tens2, childWeight.getDigit(0), 1e-6);
        assertEquals(ones2, childWeight.getDigit(1), 1e-6);
        assertEquals(tenths2, childWeight.getDigit(2), 1e-6);
        assertEquals(hundredths2, childWeight.getDigit(3), 1e-6);
        assertEquals(thousandths2, childWeight.getDigit(4), 1e-6);
    }

    @Test
    public void parentMerge1() throws Exception {
        Byte tens = 1;
        Byte ones = 1;
        Byte tenths = 4;
        Byte hundredths = 3;
        Byte thousandths = 6;
        Weight weight = new Weight(false, tens, ones, tenths, hundredths, thousandths);
        assertEquals(11.436, weight.findValue(), 1e-12);
        Agent agent = new Agent(weight);


        Byte tens2 = 2;
        Byte ones2 = 7;
        Byte tenths2 = 3;
        Byte hundredths2 = 1;
        Byte thousandths2 = 0;
        Weight weight2 = new Weight(true, tens2, ones2, tenths2, hundredths2, thousandths2);
        assertEquals(-27.31, weight2.findValue(), 1e-12);
        Agent agent1 = new Agent(weight2);
        Weight childWeight = new Weight(agent, agent1,1);

        assertEquals(weight.isNegative(), childWeight.isNegative());
        assertEquals(tens2, childWeight.getDigit(0), 1e-6);
        assertEquals(ones2, childWeight.getDigit(1), 1e-6);
        assertEquals(tenths2, childWeight.getDigit(2), 1e-6);
        assertEquals(hundredths2, childWeight.getDigit(3), 1e-6);
        assertEquals(thousandths2, childWeight.getDigit(4), 1e-6);
    }
    @Test
    public void parentMerge2() throws Exception {
        Byte tens = 1;
        Byte ones = 1;
        Byte tenths = 4;
        Byte hundredths = 3;
        Byte thousandths = 6;
        Weight weight = new Weight(false, tens, ones, tenths, hundredths, thousandths);
        assertEquals(11.436, weight.findValue(), 1e-12);
        Agent agent = new Agent(weight);


        Byte tens2 = 2;
        Byte ones2 = 7;
        Byte tenths2 = 3;
        Byte hundredths2 = 1;
        Byte thousandths2 = 0;
        Weight weight2 = new Weight(true, tens2, ones2, tenths2, hundredths2, thousandths2);
        assertEquals(-27.31, weight2.findValue(), 1e-12);
        Agent agent1 = new Agent(weight2);
        Weight childWeight = new Weight(agent, agent1,2);

        assertEquals(weight.isNegative(), childWeight.isNegative());
        assertEquals(tens, childWeight.getDigit(0), 1e-6);
        assertEquals(ones2, childWeight.getDigit(1), 1e-6);
        assertEquals(tenths2, childWeight.getDigit(2), 1e-6);
        assertEquals(hundredths2, childWeight.getDigit(3), 1e-6);
        assertEquals(thousandths2, childWeight.getDigit(4), 1e-6);
    }


    @Test
    public void parentMerge3() throws Exception {
        Byte tens = 1;
        Byte ones = 1;
        Byte tenths = 4;
        Byte hundredths = 3;
        Byte thousandths = 6;
        Weight weight = new Weight(false, tens, ones, tenths, hundredths, thousandths);
        assertEquals(11.436, weight.findValue(), 1e-12);
        Agent agent = new Agent(weight);


        Byte tens2 = 2;
        Byte ones2 = 7;
        Byte tenths2 = 3;
        Byte hundredths2 = 1;
        Byte thousandths2 = 0;
        Weight weight2 = new Weight(true, tens2, ones2, tenths2, hundredths2, thousandths2);
        assertEquals(-27.31, weight2.findValue(), 1e-12);
        Agent agent1 = new Agent(weight2);
        Weight childWeight = new Weight(agent, agent1,3);

        assertEquals(weight.isNegative(), childWeight.isNegative());
        assertEquals(tens, childWeight.getDigit(0), 1e-6);
        assertEquals(ones, childWeight.getDigit(1), 1e-6);
        assertEquals(tenths2, childWeight.getDigit(2), 1e-6);
        assertEquals(hundredths2, childWeight.getDigit(3), 1e-6);
        assertEquals(thousandths2, childWeight.getDigit(4), 1e-6);
    }

    @Test
    public void parentMerge4() throws Exception {
        Byte tens = 1;
        Byte ones = 1;
        Byte tenths = 4;
        Byte hundredths = 3;
        Byte thousandths = 6;
        Weight weight = new Weight(false, tens, ones, tenths, hundredths, thousandths);
        assertEquals(11.436, weight.findValue(), 1e-12);
        Agent agent = new Agent(weight);


        Byte tens2 = 2;
        Byte ones2 = 7;
        Byte tenths2 = 3;
        Byte hundredths2 = 1;
        Byte thousandths2 = 0;
        Weight weight2 = new Weight(true, tens2, ones2, tenths2, hundredths2, thousandths2);
        assertEquals(-27.31, weight2.findValue(), 1e-12);
        Agent agent1 = new Agent(weight2);
        Weight childWeight = new Weight(agent, agent1,4);

        assertEquals(weight.isNegative(), childWeight.isNegative());
        assertEquals(tens, childWeight.getDigit(0), 1e-6);
        assertEquals(ones, childWeight.getDigit(1), 1e-6);
        assertEquals(tenths, childWeight.getDigit(2), 1e-6);
        assertEquals(hundredths2, childWeight.getDigit(3), 1e-6);
        assertEquals(thousandths2, childWeight.getDigit(4), 1e-6);
    }

    @Test
    public void parentMerge5() throws Exception {
        Byte tens = 1;
        Byte ones = 1;
        Byte tenths = 4;
        Byte hundredths = 3;
        Byte thousandths = 6;
        Weight weight = new Weight(false, tens, ones, tenths, hundredths, thousandths);
        assertEquals(11.436, weight.findValue(), 1e-12);
        Agent agent = new Agent(weight);


        Byte tens2 = 2;
        Byte ones2 = 7;
        Byte tenths2 = 3;
        Byte hundredths2 = 1;
        Byte thousandths2 = 0;
        Weight weight2 = new Weight(true, tens2, ones2, tenths2, hundredths2, thousandths2);
        assertEquals(-27.31, weight2.findValue(), 1e-12);
        Agent agent1 = new Agent(weight2);
        Weight childWeight = new Weight(agent, agent1,5);

        assertEquals(weight.isNegative(), childWeight.isNegative());
        assertEquals(tens, childWeight.getDigit(0), 1e-6);
        assertEquals(ones, childWeight.getDigit(1), 1e-6);
        assertEquals(tenths, childWeight.getDigit(2), 1e-6);
        assertEquals(hundredths, childWeight.getDigit(3), 1e-6);
        assertEquals(thousandths2, childWeight.getDigit(4), 1e-6);
    }

    @Test
    public void parentMerge6() throws Exception {
        Byte tens = 1;
        Byte ones = 1;
        Byte tenths = 4;
        Byte hundredths = 3;
        Byte thousandths = 6;
        Weight weight = new Weight(false, tens, ones, tenths, hundredths, thousandths);
        assertEquals(11.436, weight.findValue(), 1e-12);
        Agent agent = new Agent(weight);


        Byte tens2 = 2;
        Byte ones2 = 7;
        Byte tenths2 = 3;
        Byte hundredths2 = 1;
        Byte thousandths2 = 0;
        Weight weight2 = new Weight(true, tens2, ones2, tenths2, hundredths2, thousandths2);
        assertEquals(-27.31, weight2.findValue(), 1e-12);
        Agent agent1 = new Agent(weight2);
        Weight childWeight = new Weight(agent, agent1,6);

        assertEquals(weight.isNegative(), childWeight.isNegative());
        assertEquals(tens, childWeight.getDigit(0), 1e-6);
        assertEquals(ones, childWeight.getDigit(1), 1e-6);
        assertEquals(tenths, childWeight.getDigit(2), 1e-6);
        assertEquals(hundredths, childWeight.getDigit(3), 1e-6);
        assertEquals(thousandths, childWeight.getDigit(4), 1e-6);
    }

    @Test
    public void parentMerge7() throws Exception {
        Byte tens = 1;
        Byte ones = 1;
        Byte tenths = 4;
        Byte hundredths = 3;
        Byte thousandths = 6;
        Weight weight = new Weight(false, tens, ones, tenths, hundredths, thousandths);
        assertEquals(11.436, weight.findValue(), 1e-12);
        Agent agent = new Agent(weight);


        Byte tens2 = 2;
        Byte ones2 = 7;
        Byte tenths2 = 3;
        Byte hundredths2 = 1;
        Byte thousandths2 = 0;
        Weight weight2 = new Weight(true, tens2, ones2, tenths2, hundredths2, thousandths2);
        assertEquals(-27.31, weight2.findValue(), 1e-12);
        Agent agent1 = new Agent(weight2);
        try {
            Weight childWeight = new Weight(agent, agent1, 7);
            assertFalse(true);
        }
        catch(Exception ex){
            assertTrue(true);
        }
    }

}
