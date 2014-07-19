package org.darkstorm.minecraft.gui.component;

public interface Label extends TextComponent {
    public TextAlignment getHorizontalAlignment();

    public void setHorizontalAlignment(TextAlignment alignment);

    public TextAlignment getVerticalAlignment();

    public void setVerticalAlignment(TextAlignment alignment);

    public enum TextAlignment {
        CENTER,
        LEFT,
        RIGHT,
        TOP,
        BOTTOM
    }
}
