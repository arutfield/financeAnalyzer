import org.apache.log4j.Logger;

public class Weight {
    private boolean isNegative;
    private byte tens = 0;
    private byte ones = 0;
    private byte tenths = 0;
    private byte hundredths = 0;
    private byte thousandths = 0;
    private double value = Double.POSITIVE_INFINITY;//default as uncalculated
    final static Logger logger = Logger.getLogger(Weight.class);

    public Weight() {
        if (Math.random() > 0.5) {
            this.isNegative = true;
        }
        this.tens = (byte) Math.floor(Math.random()*10);
        this.ones = (byte) Math.floor(Math.random()*10);
        this.tenths = (byte) Math.floor(Math.random()*10);
        this.hundredths = (byte) Math.floor(Math.random()*10);
        this.thousandths = (byte) Math.floor(Math.random()*10);

    }

    public Weight(boolean isNegative, byte tens, byte ones, byte tenths, byte hundredths, byte thousandths) {
        this.isNegative = isNegative;
        this.tens = tens;
        if (tens < 0 || tens > 9) {
            logger.warn("desired tens of " + tens + " is out of range");
            this.tens = 0;
        }
        this.ones = ones;
        if (ones < 0 || ones > 9) {
            logger.warn("desired ones of " + ones + " is out of range");
            this.ones = 0;
        }
        this.tenths = tenths;
        if (tenths < 0 || tenths > 9) {
            logger.warn("desired tenths of " + tenths + " is out of range");
            this.tenths = 0;
        }
        this.hundredths = hundredths;
        if (hundredths < 0 || hundredths > 9) {
            logger.warn("desired hundredths of " + hundredths + " is out of range");
            this.hundredths = 0;
        }
        this.thousandths = thousandths;
        if (thousandths < 0 || thousandths > 9) {
            logger.warn("desired thousandths of " + thousandths + " is out of range");
            this.thousandths = 0;
        }
    }

    public double findValue() {
        if (value == Double.POSITIVE_INFINITY) {
            value = tens * 10.0 + ones + tenths * 0.1 + hundredths * 0.01 + thousandths * 0.001;
            if (isNegative) {
                value = -value;
            }
        }
        return value;
    }

    public String printWeightValue() {
        return Double.toString(findValue());
    }
}
