public class Weight {
    private boolean isNegative;
    private byte tens = 0;
    private byte ones = 0;
    private byte tenths = 0;
    private byte hundredths = 0;
    private byte thousandths = 0;
    private double value = Double.POSITIVE_INFINITY;//default as uncalculated

    public Weight(boolean isNegative, byte tens, byte ones, byte tenths, byte hundredths, byte thousandths) {
        this.isNegative = isNegative;
        this.tens = tens;
        this.ones = ones;
        this.tenths = tenths;
        this.hundredths = hundredths;
        this.thousandths = thousandths;
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
