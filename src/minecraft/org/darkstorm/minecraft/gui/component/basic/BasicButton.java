package org.darkstorm.minecraft.gui.component.basic;

import org.darkstorm.minecraft.gui.component.AbstractComponent;
import org.darkstorm.minecraft.gui.component.Button;
import org.darkstorm.minecraft.gui.component.ButtonGroup;
import org.darkstorm.minecraft.gui.listener.ButtonListener;
import org.darkstorm.minecraft.gui.listener.ComponentListener;

public class BasicButton extends AbstractComponent implements Button {

    protected String text = "";
    protected ButtonGroup group;
    protected boolean state;

    public BasicButton() {
    }

    public BasicButton(String text) {
        this.text = text;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public void setText(String text) {
        this.text = text;
    }

    @Override
    public void press() {
        for (ComponentListener listener : getListeners())
            ((ButtonListener) listener).onButtonPress(this);
        this.state = !this.state;
    }

    @Override
    public boolean isPressed() {
        return state;
    }

    @Override
    public void addButtonListener(ButtonListener listener) {
        addListener(listener);
    }

    @Override
    public void removeButtonListener(ButtonListener listener) {
        removeListener(listener);
    }

    @Override
    public ButtonGroup getGroup() {
        return group;
    }

    @Override
    public void setGroup(ButtonGroup group) {
        this.group = group;
    }

    @Override
    public void setPressedState(boolean state) {
        this.state = state;
    }

    @Override
    public int getWidth() {
        return super.getWidth();
    }

    @Override
    public int getHeight() {
        return super.getHeight();
    }
}
