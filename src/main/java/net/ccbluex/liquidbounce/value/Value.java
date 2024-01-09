package net.ccbluex.liquidbounce.value;


public class Value<T> {

    private String valueName;
    protected T value;

    public Value(String valueName, T value) {
        this.valueName = valueName;
        this.value = value;
    }

    public String getName() {
        return valueName;
    }

    public T get() {
        return value;
    }

    public void set(T valueObject) {

        T oldValue = get();
        onChange(oldValue, valueObject);
        this.value = valueObject;
        onChanged(oldValue, valueObject);
    }


    protected void onChange(T oldValue, T newValue) {
    }

    protected void onChanged(T oldValue, T newValue) {
    }


    public void changeValue(T value) {
        this.value = value;
    }
}
