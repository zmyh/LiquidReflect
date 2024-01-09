package net.ccbluex.liquidbounce.value;

public class FloatValue extends Value<Float> {
    private float minimum;
    private float maximum;

    public FloatValue(String name, float value, float minimum, float maximum) {
        super(name, value);
        this.minimum = minimum;
        this.maximum = maximum;
    }

    public float getMaximum() {
        return maximum;
    }

    public float getMinimum() {
        return minimum;
    }
}