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
        Byte tenThousandths = 0;
        Weight weight = new Weight(false, tens, ones, tenths, hundredths, thousandths, tenThousandths);
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
        Byte tenThousandths = 0;
        Weight weight = new Weight(false, tens, ones, tenths, hundredths, thousandths, tenThousandths);
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
        Byte tenThousandths = 0;

        Weight weight = new Weight(true, tens, ones, tenths, hundredths, thousandths, tenThousandths);
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
        Byte tenThousandths = 6;

        Weight weight = new Weight(true, tens, ones, tenths, hundredths, thousandths, tenThousandths);
        assertEquals( weight.findValue(), -31.2156, 1e-6 );
    }

    @Test
    public void weightOfPositiveDecimal()
    {
        Byte tens = 3;
        Byte ones = 1;
        Byte tenths = 2;
        Byte hundredths = 1;
        Byte thousandths = 5;
        Byte tenThousandths = 6;

        Weight weight = new Weight(false, tens, ones, tenths, hundredths, thousandths, tenThousandths);
        assertEquals( weight.findValue(), 31.2156, 1e-6 );
    }

    @Test
    public void weightCalculatingTwice()
    {
        Byte tens = 3;
        Byte ones = 1;
        Byte tenths = 2;
        Byte hundredths = 1;
        Byte thousandths = 5;
        Byte tenThousandths = 4;

        Weight weight = new Weight(true, tens, ones, tenths, hundredths, thousandths, tenThousandths);
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
        Byte tenThousandths = 6;

        Weight weight = new Weight(true, tens, ones, tenths, hundredths, thousandths, tenThousandths);
        String valueString = weight.printWeightValue();
        assertTrue( valueString.equals("-31.2156"));
    }

    @Test
    public void weightStringPositiveCheck()
    {
        Byte tens = 2;
        Byte ones = 7;
        Byte tenths = 4;
        Byte hundredths = 3;
        Byte thousandths = 0;
        Byte tenThousandths = 0;

        Weight weight = new Weight(false, tens, ones, tenths, hundredths, thousandths, tenThousandths);
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
        Byte tenThousandths = 0;

        Weight weight = new Weight(false, tens, ones, tenths, hundredths, thousandths, tenThousandths);
        assertEquals(0, weight.findValue(), 1e-12);
    }

    @Test
    public void parentMerge0() throws Exception {
        Byte tens = 1;
        Byte ones = 1;
        Byte tenths = 4;
        Byte hundredths = 3;
        Byte thousandths = 6;
        Byte tenThousandths = 2;

        Weight weight = new Weight(false, tens, ones, tenths, hundredths, thousandths, tenThousandths);
        assertEquals(11.4362, weight.findValue(), 1e-12);
        Agent agent = new Agent(new Weight[] {weight, getZeroWeight(), getZeroWeight(), getZeroWeight()});


        Agent agent1 = getNeg27Point3105Agent();
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

        Agent agent1 = getNeg27Point3105Agent();
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

        Agent agent1 = getNeg27Point3105Agent();
        Weight childWeight = new Weight(agent.getLastDowClosingMultiplier(), agent1.getLastDowClosingMultiplier(),2, 0);

        assertEquals(getWeight11Point4363().isNegative(), childWeight.isNegative());
        assertEquals(getWeight11Point4363().getDigit(0), childWeight.getDigit(0), 1e-6);
        assertEquals(getWeightNeg27Point3105().getDigit(1), childWeight.getDigit(1), 1e-6);
        assertEquals(getWeightNeg27Point3105().getDigit(2), childWeight.getDigit(2), 1e-6);
        assertEquals(getWeightNeg27Point3105().getDigit(3), childWeight.getDigit(3), 1e-6);
        assertEquals(getWeightNeg27Point3105().getDigit(4), childWeight.getDigit(4), 1e-6);
    }


    @Test
    public void parentMerge3() throws Exception {
        Agent agent = get11Point4363Agent();
        Agent agent1 = getNeg27Point3105Agent();
        Weight childWeight = new Weight(agent.getLastDowClosingMultiplier(), agent1.getLastDowClosingMultiplier(),3, 0);

        assertEquals(getWeight11Point4363().isNegative(), childWeight.isNegative());
        assertEquals(getWeight11Point4363().getDigit(0), childWeight.getDigit(0), 1e-6);
        assertEquals(getWeight11Point4363().getDigit(1), childWeight.getDigit(1), 1e-6);
        assertEquals(getWeightNeg27Point3105().getDigit(2), childWeight.getDigit(2), 1e-6);
        assertEquals(getWeightNeg27Point3105().getDigit(3), childWeight.getDigit(3), 1e-6);
        assertEquals(getWeightNeg27Point3105().getDigit(4), childWeight.getDigit(4), 1e-6);
    }

    @Test
    public void parentMerge4() throws Exception {
        Agent agent = get11Point4363Agent();

        Agent agent1 = getNeg27Point3105Agent();
        Weight childWeight = new Weight(agent.getLastDowClosingMultiplier(), agent1.getLastDowClosingMultiplier(),4, 0);

        assertEquals(getWeight11Point4363().isNegative(), childWeight.isNegative());
        assertEquals(getWeight11Point4363().getDigit(0), childWeight.getDigit(0), 1e-6);
        assertEquals(getWeight11Point4363().getDigit(1), childWeight.getDigit(1), 1e-6);
        assertEquals(getWeight11Point4363().getDigit(2), childWeight.getDigit(2), 1e-6);
        assertEquals(getWeightNeg27Point3105().getDigit(3), childWeight.getDigit(3), 1e-6);
        assertEquals(getWeightNeg27Point3105().getDigit(4), childWeight.getDigit(4), 1e-6);
    }

    @Test
    public void parentMerge5() throws Exception {
        Agent agent = get11Point4363Agent();
        Agent agent1 = getNeg27Point3105Agent();
        Weight childWeight = new Weight(agent.getLastDowClosingMultiplier(), agent1.getLastDowClosingMultiplier(),
                5, 0);

        assertEquals(getWeight11Point4363().isNegative(), childWeight.isNegative());
        assertEquals(getWeight11Point4363().getDigit(0), childWeight.getDigit(0), 1e-6);
        assertEquals(getWeight11Point4363().getDigit(1), childWeight.getDigit(1), 1e-6);
        assertEquals(getWeight11Point4363().getDigit(2), childWeight.getDigit(2), 1e-6);
        assertEquals(getWeight11Point4363().getDigit(3), childWeight.getDigit(3), 1e-6);
        assertEquals(getWeightNeg27Point3105().getDigit(4), childWeight.getDigit(4), 1e-6);
    }

    @Test
    public void parentMerge6() throws Exception {
        Agent agent = get11Point4363Agent();
        Agent agent1 = getNeg27Point3105Agent();
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
        Agent agent1 = getNeg27Point3105Agent();
        try {
            Weight childWeight = new Weight(agent.getLastDowClosingMultiplier(), agent1.getLastDowClosingMultiplier(),
                    8, 0);
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
        Byte tens = 1;
        Byte ones = 1;
        Byte tenths = 4;
        Byte hundredths = 3;
        Byte thousandths = 6;
        Byte tenThousandths = 3;

        return new Weight(false, tens, ones, tenths, hundredths, thousandths, tenThousandths);

    }

    private Weight getWeightNeg27Point3105() {
        Byte tens2 = 2;
        Byte ones2 = 7;
        Byte tenths2 = 3;
        Byte hundredths2 = 1;
        Byte thousandths2 = 0;
        Byte tenThousandths2 = 5;
        return  new Weight(true, tens2, ones2, tenths2, hundredths2, thousandths2, tenThousandths2);

    }

    private Agent getNeg27Point3105Agent() throws Exception {
        Weight weight = getWeightNeg27Point3105();
        assertEquals(-27.3105, weight.findValue(), 1e-12);
        return new Agent(new Weight[] {weight, getZeroWeight(), getZeroWeight(), getZeroWeight(), getZeroWeight()});
    }


    private Agent get11Point4363Agent() throws Exception {
        Weight weight = getWeight11Point4363();
        assertEquals(11.4363, weight.findValue(), 1e-12);
        return new Agent(new Weight[] {weight, getZeroWeight(), getZeroWeight(), getZeroWeight(), getZeroWeight()});
    }

}
