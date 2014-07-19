package net.minecraft.client.gui;

public class GuiPlayerInfo {
    private static final String __OBFID = "CL_00000888";
    public final String name;
    private final String nameinLowerCase;
    public int responseTime;

    public GuiPlayerInfo(String p_i1190_1_) {
        this.name = p_i1190_1_;
        this.nameinLowerCase = p_i1190_1_.toLowerCase();
    }
}
