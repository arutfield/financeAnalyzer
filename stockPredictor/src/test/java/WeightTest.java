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
        Byte ones = 0;
        Byte tenths = 0;
        Byte hundredths = 0;
        Byte thousandths = 0;
        Byte tenThousandths = 0;
        Byte hundredThousandths = 0;
        Weight weight = new Weight(false, ones, tenths, hundredths, thousandths, tenThousandths, hundredThousandths);
        assertEquals( weight.findValue(), 0, 1e-6 );
    }

    @Test
    public void weightOfOne()
    {
        Byte ones = 1;
        Byte tenths = 0;
        Byte hundredths = 0;
        Byte thousandths = 0;
        Byte tenThousandths = 0;
        Byte hundredThousandths = 0;
        Weight weight = new Weight(false,  ones, tenths, hundredths, thousandths, tenThousandths, hundredThousandths);
        assertEquals( weight.findValue(), 1, 1e-6 );
    }

    @Test
    public void weightOfNegativeOne()
    {
        Byte ones = 1;
        Byte tenths = 0;
        Byte hundredths = 0;
        Byte thousandths = 0;
        Byte tenThousandths = 0;
        Byte hundredThousandths = 0;
        Weight weight = new Weight(true, ones, tenths, hundredths, thousandths, tenThousandths, hundredThousandths);
        assertEquals( weight.findValue(), -1, 1e-6 );
    }

    @Test
    public void weightOfNegativeDecimal()
    {
        Byte ones = 1;
        Byte tenths = 2;
        Byte hundredths = 1;
        Byte thousandths = 5;
        Byte tenThousandths = 6;
        Byte hundredThousandths = 0;
        Weight weight = new Weight(true, ones, tenths, hundredths, thousandths, tenThousandths, hundredThousandths);
        assertEquals( weight.findValue(), -1.2156, 1e-6 );
    }

    @Test
    public void weightOfPositiveDecimal()
    {
        Byte ones = 1;
        Byte tenths = 2;
        Byte hundredths = 1;
        Byte thousandths = 5;
        Byte tenThousandths = 6;
        Byte hundredThousandths = 0;
        Weight weight = new Weight(false, ones, tenths, hundredths, thousandths, tenThousandths, hundredThousandths);
        assertEquals( weight.findValue(), 1.2156, 1e-6 );
    }

    @Test
    public void weightCalculatingTwice()
    {
        Byte ones = 1;
        Byte tenths = 2;
        Byte hundredths = 1;
        Byte thousandths = 5;
        Byte tenThousandths = 4;
        Byte hundredThousandths = 0;
        Weight weight = new Weight(true, ones, tenths, hundredths, thousandths, tenThousandths, hundredThousandths);
        double value1 = weight.findValue();
        assertEquals( weight.findValue(), value1, 1e-6 );
    }


    /*@Test
    public void weightStringCheck()
    {
        Byte ones = 1;
        Byte tenths = 2;
        Byte hundredths = 1;
        Byte thousandths = 5;
        Byte tenThousandths = 6;
        Byte hundredThousandths = 0;
        Weight weight = new Weight(true, ones, tenths, hundredths, thousandths, tenThousandths, hundredThousandths);
        String valueString = weight.printWeightValue();
        assertTrue( valueString.equals("-1.2156"));
    }*/

   /* @Test
    public void weightStringPositiveCheck()
    {
        Byte ones = 7;
        Byte tenths = 4;
        Byte hundredths = 3;
        Byte thousandths = 0;
        Byte tenThousandths = 0;
        Byte hundredThousandths = 0;
        Weight weight = new Weight(false,ones, tenths, hundredths, thousandths, tenThousandths, hundredThousandths);
        String valueString = weight.printWeightValue();
        System.out.println("value string: " + valueString);
        assertTrue( valueString.equals("7.43"));
    }*/

    @Test
    public void weightsOutOfBounds()
    {
        Byte ones = 17;
        Byte tenths = -4;
        Byte hundredths = 13;
        Byte thousandths = -6;
        Byte tenThousandths = 0;
        Byte hundredThousandths = 0;
        Weight weight = new Weight(false, ones, tenths, hundredths, thousandths, tenThousandths, hundredThousandths);
        assertEquals(0, weight.findValue(), 1e-12);
    }

    @Test
    public void parentMerge0() throws Exception {
        Byte ones = 1;
        Byte tenths = 4;
        Byte hundredths = 3;
        Byte thousandths = 6;
        Byte tenThousandths = 2;
        Byte hundredThousandths = 0;
        Weight weight = new Weight(false, ones, tenths, hundredths, thousandths, tenThousandths, hundredThousandths);
        assertEquals(1.4362, weight.findValue(), 1e-12);
        Agent agent = new Agent(new Weight[] {weight, getZeroWeight(), getZeroWeight(), getZeroWeight(), getZeroWeight(), getZeroWeight(), getZeroWeight()});


        Agent agent1 = getNeg27Point31054Agent();
        Weight childWeight = new Weight(agent.getLastDowClosingMultiplier(), agent1.getLastDowClosingMultiplier(),0, 0);

        assertEquals(agent1.getLastDowClosingMultiplier().isNegative(), childWeight.isNegative());
        assertEquals(agent1.getLastDowClosingMultiplier().getDigit(0), childWeight.getDigit(0), 1e-6);
        assertEquals(agent1.getLastDowClosingMultiplier().getDigit(1), childWeight.getDigit(1), 1e-6);
        assertEquals(agent1.getLastDowClosingMultiplier().getDigit(2), childWeight.getDigit(2), 1e-6);
        assertEquals(agent1.getLastDowClosingMultiplier().getDigit(3), childWeight.getDigit(3), 1e-6);
        assertEquals(agent1.getLastDowClosingMultiplier().getDigit(4), childWeight.getDigit(4), 1e-6);
    }

    @Test
    public void parentMerge1() throws Exception {
        Agent agent = get11Point4363Agent();

        Agent agent1 = getNeg27Point31054Agent();
        Weight childWeight = new Weight(agent.getLastDowClosingMultiplier(), agent1.getLastDowClosingMultiplier(),1, 0);

        assertEquals(agent.getLastDowClosingMultiplier().isNegative(), childWeight.isNegative());
        assertEquals(agent1.getLastDowClosingMultiplier().getDigit(0), childWeight.getDigit(0), 1e-6);
        assertEquals(agent1.getLastDowClosingMultiplier().getDigit(1), childWeight.getDigit(1), 1e-6);
        assertEquals(agent1.getLastDowClosingMultiplier().getDigit(2), childWeight.getDigit(2), 1e-6);
        assertEquals(agent1.getLastDowClosingMultiplier().getDigit(3), childWeight.getDigit(3), 1e-6);
        assertEquals(agent1.getLastDowClosingMultiplier().getDigit(4), childWeight.getDigit(4), 1e-6);
    }
    @Test
    public void parentMerge2() throws Exception {
        Agent agent = get11Point4363Agent();

        Agent agent1 = getNeg27Point31054Agent();
        Weight childWeight = new Weight(agent.getLastDowClosingMultiplier(), agent1.getLastDowClosingMultiplier(),2, 0);

        assertEquals(getWeight11Point4363().isNegative(), childWeight.isNegative());
        assertEquals(getWeight11Point4363().getDigit(0), childWeight.getDigit(0), 1e-6);
        assertEquals(getWeightNeg27Point31054().getDigit(1), childWeight.getDigit(1), 1e-6);
        assertEquals(getWeightNeg27Point31054().getDigit(2), childWeight.getDigit(2), 1e-6);
        assertEquals(getWeightNeg27Point31054().getDigit(3), childWeight.getDigit(3), 1e-6);
        assertEquals(getWeightNeg27Point31054().getDigit(4), childWeight.getDigit(4), 1e-6);
    }


    @Test
    public void parentMerge3() throws Exception {
        Agent agent = get11Point4363Agent();
        Agent agent1 = getNeg27Point31054Agent();
        Weight childWeight = new Weight(agent.getLastDowClosingMultiplier(), agent1.getLastDowClosingMultiplier(),3, 0);

        assertEquals(getWeight11Point4363().isNegative(), childWeight.isNegative());
        assertEquals(getWeight11Point4363().getDigit(0), childWeight.getDigit(0), 1e-6);
        assertEquals(getWeight11Point4363().getDigit(1), childWeight.getDigit(1), 1e-6);
        assertEquals(getWeightNeg27Point31054().getDigit(2), childWeight.getDigit(2), 1e-6);
        assertEquals(getWeightNeg27Point31054().getDigit(3), childWeight.getDigit(3), 1e-6);
        assertEquals(getWeightNeg27Point31054().getDigit(4), childWeight.getDigit(4), 1e-6);
    }

    @Test
    public void parentMerge4() throws Exception {
        Agent agent = get11Point4363Agent();

        Agent agent1 = getNeg27Point31054Agent();
        Weight childWeight = new Weight(agent.getLastDowClosingMultiplier(), agent1.getLastDowClosingMultiplier(),4, 0);

        assertEquals(getWeight11Point4363().isNegative(), childWeight.isNegative());
        assertEquals(getWeight11Point4363().getDigit(0), childWeight.getDigit(0), 1e-6);
        assertEquals(getWeight11Point4363().getDigit(1), childWeight.getDigit(1), 1e-6);
        assertEquals(getWeight11Point4363().getDigit(2), childWeight.getDigit(2), 1e-6);
        assertEquals(getWeightNeg27Point31054().getDigit(3), childWeight.getDigit(3), 1e-6);
        assertEquals(getWeightNeg27Point31054().getDigit(4), childWeight.getDigit(4), 1e-6);
    }

    @Test
    public void parentMerge5() throws Exception {
        Agent agent = get11Point4363Agent();
        Agent agent1 = getNeg27Point31054Agent();
        Weight childWeight = new Weight(agent.getLastDowClosingMultiplier(), agent1.getLastDowClosingMultiplier(),
                5, 0);

        assertEquals(getWeight11Point4363().isNegative(), childWeight.isNegative());
        assertEquals(getWeight11Point4363().getDigit(0), childWeight.getDigit(0), 1e-6);
        assertEquals(getWeight11Point4363().getDigit(1), childWeight.getDigit(1), 1e-6);
        assertEquals(getWeight11Point4363().getDigit(2), childWeight.getDigit(2), 1e-6);
        assertEquals(getWeight11Point4363().getDigit(3), childWeight.getDigit(3), 1e-6);
        assertEquals(getWeightNeg27Point31054().getDigit(4), childWeight.getDigit(4), 1e-6);
    }

    @Test
    public void parentMerge6() throws Exception {
        Agent agent = get11Point4363Agent();
        Agent agent1 = getNeg27Point31054Agent();
        Weight childWeight = new Weight(agent.getLastDowClosingMultiplier(), agent1.getLastDowClosingMultiplier(),6, 0);

        assertEquals(getWeight11Point4363().isNegative(), childWeight.isNegative());
        assertEquals(getWeight11Point4363().getDigit(0), childWeight.getDigit(0), 1e-6);
        assertEquals(getWeight11Point4363().getDigit(1), childWeight.getDigit(1), 1e-6);
        assertEquals(getWeight11Point4363().getDigit(2), childWeight.getDigit(2), 1e-6);
        assertEquals(getWeight11Point4363().getDigit(3), childWeight.getDigit(3), 1e-6);
        assertEquals(getWeight11Point4363().getDigit(4), childWeight.getDigit(4), 1e-6);
    }

    @Test
    public void parentMerge7() throws Exception {
        Agent agent = get11Point4363Agent();
        Agent agent1 = getNeg27Point31054Agent();
        try {
            Weight childWeight = new Weight(agent.getLastDowClosingMultiplier(), agent1.getLastDowClosingMultiplier(),
                    Weight.NUM_DIGITS + 2, 0);
            assertFalse(true);
        }
        catch(Exception ex){
            assertTrue(true);
        }
    }


    private Weight getZeroWeight(){
        Byte tens2 = 0;
        return new Weight(true, tens2, tens2, tens2, tens2, tens2, tens2);

    }

    private Weight getWeight11Point4363(){
        Byte ones = 1;
        Byte tenths = 4;
        Byte hundredths = 3;
        Byte thousandths = 6;
        Byte tenThousandths = 3;
        Byte hundredThousandths = 0;
        return new Weight(false, ones, tenths, hundredths, thousandths, tenThousandths, hundredThousandths);

    }

    private Weight getWeightNeg27Point31054() {
        Byte ones2 = 7;
        Byte tenths2 = 3;
        Byte hundredths2 = 1;
        Byte thousandths2 = 0;
        Byte tenThousandths2 = 5;
        Byte hundredThousandths = 4;
        return  new Weight(true, ones2, tenths2, hundredths2, thousandths2, tenThousandths2, hundredThousandths);

    }

    private Agent getNeg27Point31054Agent() throws Exception {
        Weight weight = getWeightNeg27Point31054();
        assertEquals(-7.31054, weight.findValue(), 1e-12);
        return new Agent(new Weight[] {weight, getZeroWeight(), getZeroWeight(), getZeroWeight(), getZeroWeight(), getZeroWeight(), getZeroWeight()});
    }


    private Agent get11Point4363Agent() throws Exception {
        Weight weight = getWeight11Point4363();
        assertEquals(1.4363, weight.findValue(), 1e-12);
        return new Agent(new Weight[] {weight, getZeroWeight(), getZeroWeight(), getZeroWeight(), getZeroWeight(), getZeroWeight(), getZeroWeight()});
    }

}
