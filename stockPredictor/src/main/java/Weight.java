import org.apache.log4j.Logger;


public class Weight {
    private boolean isNegative;
    private byte[] digits = new byte[NUM_DIGITS];
    private double value = Double.POSITIVE_INFINITY;//default as uncalculated
    final static Logger logger = Logger.getLogger(Weight.class);
    private static final int NUM_DIGITS=6;

    enum digitEnum {
        TENS(0),
        ONES(1),
        TENTHS(2),
        HUNDREDTHS(3),
        THOUSANDTHS(4),
        TENTHOUSANDTHS(5);

        private final int value;
        private digitEnum(int value) {
            this.value = value;
        }
    }

    public Weight() {
        if (Math.random() > 0.5) {
            this.isNegative = true;
        }
        for (int i=0; i<NUM_DIGITS; i++) {
            this.digits[i] = (byte) Math.floor(Math.random()*10);
        }

    }

    public Weight(Weight parent1, Weight parent2, int crossPoint, double mutationProbability) throws Exception {
        if (crossPoint < 0 || crossPoint > NUM_DIGITS + 1) {
            String message = "crosspoint of " + crossPoint + " is out of range";
            logger.error(message);
            throw new Exception(message);
        }
        if (mutationProbability < 0 || mutationProbability > 1.0) {
            String message = "mutation probability of " + mutationProbability + " is out of range";
            logger.error(message);
            throw new Exception(message);
        }

        if (crossPoint > 0) {
            //parent1
            this.isNegative = parent1.isNegative();
        } else {
            //parent2
            this.isNegative = parent2.isNegative();
        }
        //allow mutations
        if (Math.random() < mutationProbability) {
            this.isNegative = (Math.random() < 0.5);
        }

        for (int i=0; i<NUM_DIGITS; i++) {
            if (crossPoint > i+1) {
                //parent1
                digits[i] = parent1.getDigit(i);
            } else {
                //parent2
                digits[i] = parent2.getDigit(i);
            }

            // allow mutations
            if (Math.random() < mutationProbability) {
                digits[i] = (byte) Math.floor(Math.random()*10);
            }
            if (digits[i] > 9 || digits[i] < 0) {
                String message = "digit of " + digits[i] + " is out of bounds. May be code error";
                logger.error(message);
                throw new Exception(message);
            }
        }



    }


    public Weight(boolean isNegative, byte tens, byte ones, byte tenths, byte hundredths, byte thousandths, byte tenThousandths) {
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
        this.digits[digitEnum.TENTHOUSANDTHS.value] = tenThousandths;
        if (tenThousandths < 0 || tenThousandths > 9) {
            logger.warn("desired ten thousandths of " + tenThousandths + " is out of range");
            this.digits[digitEnum.TENTHOUSANDTHS.value] = 0;
        }
    }

    public double findValue() {
        if (value == Double.POSITIVE_INFINITY) {
            value = 0;
            double multiplier = 10.0;
            for (int i=0; i<NUM_DIGITS; i++) {
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
        if (digitNumber > NUM_DIGITS - 1) {
            logger.warn("digit number is too high. Giving " +(NUM_DIGITS - 1));
            digitNumber = NUM_DIGITS - 1;
        }
        return digits[digitNumber];
    }

}
