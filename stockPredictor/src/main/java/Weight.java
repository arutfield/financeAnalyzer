import org.apache.log4j.Logger;

public class Weight {
    private boolean isNegative;
    private byte[] digits = new byte[5];
    private double value = Double.POSITIVE_INFINITY;//default as uncalculated
    final static Logger logger = Logger.getLogger(Weight.class);

    enum digitEnum {
        TENS(0),
        ONES(1),
        TENTHS(2),
        HUNDREDTHS(3),
        THOUSANDTHS(4);

        private final int value;
        private digitEnum(int value) {
            this.value = value;
        }
    }

    public Weight() {
        if (Math.random() > 0.5) {
            this.isNegative = true;
        }
        for (int i=0; i<5; i++) {
            this.digits[i] = (byte) Math.floor(Math.random()*10);
        }

    }

    public Weight(Agent parent1, Agent parent2, int crossPoint) throws Exception {
        if (crossPoint < 0 || crossPoint > 6) {
            String message = "crosspoint of " + crossPoint + " is out of range";
            logger.error(message);
            throw new Exception(message);
        }

        if (crossPoint > 0) {
            //parent1
            this.isNegative = parent1.getLastDowClosingMultiplier().isNegative();
        } else {
            //parent2
            this.isNegative = parent2.getLastDowClosingMultiplier().isNegative();
        }

        for (int i=0; i<5; i++) {
            if (crossPoint > i+1) {
                //parent1
                digits[i] = parent1.getLastDowClosingMultiplier().getDigit(i);
            } else {
                //parent2
                digits[i] = parent2.getLastDowClosingMultiplier().getDigit(i);
            }
        }



    }


    public Weight(boolean isNegative, byte tens, byte ones, byte tenths, byte hundredths, byte thousandths) {
        this.isNegative = isNegative;
        this.digits[digitEnum.TENS.value] = tens;
        if (tens < 0 || tens > 9) {
            logger.warn("desired tens of " + tens + " is out of range");
            this.digits[digitEnum.TENS.value] = 0;
        }
        this.digits[digitEnum.ONES.value] = ones;
        if (ones < 0 || ones > 9) {
            logger.warn("desired ones of " + ones + " is out of range");
            this.digits[digitEnum.ONES.value] = 0;
        }
        this.digits[digitEnum.TENTHS.value] = tenths;
        if (tenths < 0 || tenths > 9) {
            logger.warn("desired tenths of " + tenths + " is out of range");
            this.digits[digitEnum.TENTHS.value] = 0;
        }
        this.digits[digitEnum.HUNDREDTHS.value] = hundredths;
        if (hundredths < 0 || hundredths > 9) {
            logger.warn("desired hundredths of " + hundredths + " is out of range");
            this.digits[digitEnum.HUNDREDTHS.value] = 0;
        }
        this.digits[digitEnum.THOUSANDTHS.value] = thousandths;
        if (thousandths < 0 || thousandths > 9) {
            logger.warn("desired thousandths of " + thousandths + " is out of range");
            this.digits[digitEnum.THOUSANDTHS.value] = 0;
        }
    }

    public double findValue() {
        if (value == Double.POSITIVE_INFINITY) {
            value = 0;
            double multiplier = 10.0;
            for (int i=0; i<5; i++) {
                value += digits[i] * multiplier;
                multiplier /= 10.0;
            }
            if (isNegative) {
                value = -value;
            }
        }
        return value;
    }

    public String printWeightValue() {
        return Double.toString(findValue());
    }

    public boolean isNegative() {
        return isNegative;
    }

    public byte getDigit(int digitNumber) {
        if (digitNumber > 4) {
            logger.warn("digit number is too high. Giving 4");
            digitNumber = 4;
        }
        return digits[digitNumber];
    }

}
