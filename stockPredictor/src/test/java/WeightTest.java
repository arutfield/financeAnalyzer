import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit test Weight.
 */
public class WeightTest {

    @Test
    public void weightOfZero() {
        Byte ones = 0;
        Byte tenths = 0;
        Byte hundredths = 0;
        Byte thousandths = 0;
        Byte tenThousandths = 0;
        Byte hundredThousandths = 0;
        Byte millionths = 0;
        Byte tenMillionths = 0;
        Weight weight = new Weight(false, ones, tenths, hundredths, thousandths, tenThousandths, hundredThousandths, millionths, tenMillionths);
        assertEquals(weight.findValue(), 0, 1e-6);
    }

    @Test
    public void weightOfOne() {
        Byte ones = 1;
        Byte tenths = 0;
        Byte hundredths = 0;
        Byte thousandths = 0;
        Byte tenThousandths = 0;
        Byte hundredThousandths = 0;
        Byte millionths = 0;
        Byte tenMillionths = 0;

        Weight weight = new Weight(false, ones, tenths, hundredths, thousandths, tenThousandths, hundredThousandths, millionths, tenMillionths);
        assertEquals(weight.findValue(), 1, 1e-6);
    }

    @Test
    public void weightOfNegativeOne() {
        Byte ones = 1;
        Byte tenths = 0;
        Byte hundredths = 0;
        Byte thousandths = 0;
        Byte tenThousandths = 0;
        Byte hundredThousandths = 0;
        Byte millionths = 0;
        Byte tenMillionths = 0;

        Weight weight = new Weight(true, ones, tenths, hundredths, thousandths, tenThousandths, hundredThousandths, millionths, tenMillionths);
        assertEquals(weight.findValue(), -1, 1e-6);
    }

    @Test
    public void weightOfNegativeDecimal() {
        Byte ones = 1;
        Byte tenths = 2;
        Byte hundredths = 1;
        Byte thousandths = 5;
        Byte tenThousandths = 6;
        Byte hundredThousandths = 0;
        Byte millionths = 7;
        Byte tenMillionths = 0;
        Weight weight = new Weight(true, ones, tenths, hundredths, thousandths, tenThousandths, hundredThousandths, millionths, tenMillionths);
        assertEquals(weight.findValue(), -1.215607, 1e-6);
    }

    @Test
    public void weightOfPositiveDecimal() {
        Byte ones = 1;
        Byte tenths = 2;
        Byte hundredths = 1;
        Byte thousandths = 5;
        Byte tenThousandths = 6;
        Byte hundredThousandths = 0;
        Byte millionths = 7;
        Byte tenMillionths = 3;
        Weight weight = new Weight(false, ones, tenths, hundredths, thousandths, tenThousandths, hundredThousandths, millionths, tenMillionths);
        assertEquals(weight.findValue(), 1.2156073, 1e-6);
    }

    @Test
    public void weightCalculatingTwice() {
        Byte ones = 1;
        Byte tenths = 2;
        Byte hundredths = 1;
        Byte thousandths = 5;
        Byte tenThousandths = 4;
        Byte hundredThousandths = 0;
        Byte millionths = 6;
        Byte tenMillionths = 7;

        Weight weight = new Weight(true, ones, tenths, hundredths, thousandths, tenThousandths, hundredThousandths, millionths, tenMillionths);
        double value1 = weight.findValue();
        assertEquals(weight.findValue(), value1, 1e-6);
    }


    @Test
    public void weightsOutOfBounds() {
        Byte ones = 17;
        Byte tenths = -4;
        Byte hundredths = 13;
        Byte thousandths = -6;
        Byte tenThousandths = 0;
        Byte hundredThousandths = 0;
        Byte millionths = 18;
        Byte tenMillionths = 21;
        Weight weight = new Weight(false, ones, tenths, hundredths, thousandths, tenThousandths, hundredThousandths, millionths, tenMillionths);
        assertEquals(0, weight.findValue(), 1e-12);
    }

    @Test
    public void parentMergeTestDowClosingMultiplier() throws Exception {
        Byte ones = 1;
        Byte tenths = 4;
        Byte hundredths = 3;
        Byte thousandths = 6;
        Byte tenThousandths = 2;
        Byte hundredThousandths = 0;
        Byte millionths = 3;
        Byte tenMillionths = 8;
        Weight weight = new Weight(false, ones, tenths, hundredths, thousandths, tenThousandths, hundredThousandths, millionths, tenMillionths);
        assertEquals(1.4362038, weight.findValue(), 1e-12);
        for (int crossPoint = 0; crossPoint <= Weight.NUM_DIGITS + 1; crossPoint++) {
            Weight childWeight = new Weight(weight, getWeightNeg27Point3105486(), crossPoint, 0);
            System.out.println("crosspoint " + crossPoint + " leads to child weight " + childWeight.findValue());
            if (crossPoint < 1) {
                assertEquals(getWeightNeg27Point3105486().isNegative(), childWeight.isNegative());
            } else {
                assertEquals(weight.isNegative(), childWeight.isNegative());
            }

            for (int i = 0; i < Weight.NUM_DIGITS; i++) {
                if (i < crossPoint - 1) {
                    assertEquals(weight.getDigit(i), childWeight.getDigit(i), 1e-6);
                } else {
                    assertEquals(getWeightNeg27Point3105486().getDigit(i), childWeight.getDigit(i), 1e-6);
                }

            }
        }

    }



    private Weight getZeroWeight() {
        Byte tens2 = 0;
        return new Weight(true, tens2, tens2, tens2, tens2, tens2, tens2, tens2, tens2);

    }

    private Weight getWeight11Point4363075() {
        Byte ones = 1;
        Byte tenths = 4;
        Byte hundredths = 3;
        Byte thousandths = 6;
        Byte tenThousandths = 3;
        Byte hundredThousandths = 0;
        Byte millionths = 7;
        Byte tenMillionths = 5;
        return new Weight(false, ones, tenths, hundredths, thousandths, tenThousandths, hundredThousandths, millionths, tenMillionths);

    }

    private Weight getWeightNeg27Point3105486() {
        Byte ones2 = 7;
        Byte tenths2 = 3;
        Byte hundredths2 = 1;
        Byte thousandths2 = 0;
        Byte tenThousandths2 = 5;
        Byte hundredThousandths = 4;
        Byte millionths = 8;
        Byte tenMillionths = 6;

        return new Weight(true, ones2, tenths2, hundredths2, thousandths2, tenThousandths2, hundredThousandths, millionths, tenMillionths);

    }

    private Agent getNeg7Point3105486Agent() throws Exception {
        Weight weight = getWeightNeg27Point3105486();
        assertEquals(-7.3105486, weight.findValue(), 1e-12);
        return new Agent(new Weight[]{weight, getZeroWeight(), getZeroWeight(), getZeroWeight(), getZeroWeight(),
                getZeroWeight(), getZeroWeight(), getZeroWeight(), getZeroWeight()});
    }


    private Agent get11Point4363075Agent() throws Exception {
        Weight weight = getWeight11Point4363075();
        assertEquals(1.4363075, weight.findValue(), 1e-12);
        return new Agent(new Weight[]{weight, getZeroWeight(), getZeroWeight(), getZeroWeight(), getZeroWeight(),
                getZeroWeight(), getZeroWeight(), getZeroWeight(), getZeroWeight()});
    }

}
