package net.ccbluex.liquidbounce.value;

import java.util.Arrays;

public class ListValue extends Value<String> {
    private String[] values;
    public boolean openList = false;

    public ListValue(String name, String[] values, String value) {
        super(name, value);
        this.values = values;
        this.value = value;
    }

    public String[] getValues() {
        return values;
    }

    public boolean contains(String string) {
        return Arrays.stream(values).anyMatch(s -> s.equalsIgnoreCase(string));
    }

    @Override
    public void changeValue(String value) {
        for (String element : values) {
            if (element.equalsIgnoreCase(value)) {
                this.value = element;
                break;
            }
        }
    }

    public boolean isOpenList() {
        return openList;
    }

    public void setOpenList(boolean openList) {
        this.openList = openList;
    }

}