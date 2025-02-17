import org.apache.log4j.Logger;


public class Weight {
    private boolean isNegative;
    private byte[] digits = new byte[NUM_DIGITS];
    private double value = Double.POSITIVE_INFINITY;//default as uncalculated
    final static Logger logger = Logger.getLogger(Weight.class);
    static final int NUM_DIGITS=digitEnum.values().length;

    enum digitEnum {
       // TENS(0),
        ONES(0),
        TENTHS(1),
        HUNDREDTHS(2),
        THOUSANDTHS(3),
        TENTHOUSANDTHS(4),
        HUNDREDTHOUSANDTHS(5),
        MILLIONTHS(6),
        TENMILLIONTHS(7);

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


    public Weight(boolean isNegative, byte ones, byte tenths, byte hundredths, byte thousandths,
                  byte tenThousandths, byte hundredThousandths, byte millionths, byte tenMillionths) {
        this.isNegative = isNegative;
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
        this.digits[digitEnum.HUNDREDTHOUSANDTHS.value] = hundredThousandths;
        if (tenThousandths < 0 || tenThousandths > 9) {
            logger.warn("desired hundred thousandths of " + hundredThousandths + " is out of range");
            this.digits[digitEnum.HUNDREDTHOUSANDTHS.value] = 0;
        }
        this.digits[digitEnum.MILLIONTHS.value] = millionths;
        if (millionths < 0 || millionths > 9) {
            logger.warn("desired millionths of " + millionths + " is out of range");
            this.digits[digitEnum.MILLIONTHS.value] = 0;
        }
        this.digits[digitEnum.TENMILLIONTHS.value] = tenMillionths;
        if (tenMillionths < 0 || tenMillionths > 9) {
            logger.warn("desired ten millionths of " + tenMillionths + " is out of range");
            this.digits[digitEnum.TENMILLIONTHS.value] = 0;
        }
    }

    public double findValue() {
        if (value == Double.POSITIVE_INFINITY) {
            value = 0;
            double multiplier = 1.0;
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
