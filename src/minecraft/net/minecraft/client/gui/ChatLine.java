package net.minecraft.client.gui;

import net.minecraft.util.IChatComponent;

public class ChatLine {
    private static final String __OBFID = "CL_00000627";
    private final int updateCounterCreated;
    private final IChatComponent lineString;
    private final int chatLineID;

    public ChatLine(int p_i45000_1_, IChatComponent p_i45000_2_, int p_i45000_3_) {
        this.lineString = p_i45000_2_;
        this.updateCounterCreated = p_i45000_1_;
        this.chatLineID = p_i45000_3_;
    }

    public IChatComponent func_151461_a() {
        return this.lineString;
    }

    public int getUpdatedCounter() {
        return this.updateCounterCreated;
    }

    public int getChatLineID() {
        return this.chatLineID;
    }

    public String getChatLineString() {
        return lineString.getUnformattedTextForChat();
    }

}
