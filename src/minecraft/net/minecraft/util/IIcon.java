package net.minecraft.util;

public interface IIcon {
    int getIconWidth();

    int getIconHeight();

    float getMinU();

    float getMaxU();

    float getInterpolatedU(double p_94214_1_);

    float getMinV();

    float getMaxV();

    float getInterpolatedV(double p_94207_1_);

    String getIconName();
}
