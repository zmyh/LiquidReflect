package net.ccbluex.liquidbounce.value;

public class IntegerValue extends Value<Integer> {
    private int minimum;
    private int maximum;

    public IntegerValue(String name, int value, int minimum, int maximum) {
        super(name, value);
        this.minimum = minimum;
        this.maximum = maximum;
    }

    public int getMinimum() {
        return minimum;
    }

    public int getMaximum() {
        return maximum;
    }
}