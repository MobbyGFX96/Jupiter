package org.darkstorm.minecraft.gui.component;

import org.darkstorm.minecraft.gui.listener.ButtonListener;

public interface Button extends Component, TextComponent {

    boolean isPressed();

    public void press();

    public void addButtonListener(ButtonListener listener);

    public void removeButtonListener(ButtonListener listener);

    public ButtonGroup getGroup();

    public void setGroup(ButtonGroup group);

    void setPressedState(boolean state);
}
