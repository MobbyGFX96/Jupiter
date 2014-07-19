package org.darkstorm.minecraft.gui.component;

public interface BoundedRangeComponent extends Component {
    public double getValue();

    public void setValue(double value);

    public double getMinimumValue();

    public void setMinimumValue(double minimumValue);

    public double getMaximumValue();

    public void setMaximumValue(double maximumValue);

    public double getIncrement();

    public void setIncrement(double increment);

    public ValueDisplay getValueDisplay();

    public void setValueDisplay(ValueDisplay display);

    public enum ValueDisplay {
        DECIMAL,
        INTEGER,
        PERCENTAGE,
        NONE
    }
}
