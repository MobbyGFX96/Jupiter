package net.minecraft.client.gui;

import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.resources.I18n;
import net.minecraft.network.play.client.C00PacketKeepAlive;

public class GuiDownloadTerrain extends GuiScreen {
    private static final String __OBFID = "CL_00000708";
    private NetHandlerPlayClient field_146594_a;
    private int field_146593_f;

    public GuiDownloadTerrain(NetHandlerPlayClient p_i45023_1_) {
        this.field_146594_a = p_i45023_1_;
    }

    protected void keyTyped(char p_73869_1_, int p_73869_2_) {
    }

    public void initGui() {
        this.buttonList.clear();
    }

    public void updateScreen() {
        ++this.field_146593_f;
        if (this.field_146593_f % 20 == 0) {
            this.field_146594_a.addToSendQueue(new C00PacketKeepAlive());
        }

        if (this.field_146594_a != null) {
            this.field_146594_a.onNetworkTick();
        }
    }

    public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_) {
        this.func_146278_c(0);
        this.drawCenteredString(this.fontRendererObj, I18n.format("multiplayer.downloadingTerrain", new Object[0]), this.width / 2, this.height / 2 - 50, 16777215);
        super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
    }

    public boolean doesGuiPauseGame() {
        return false;
    }
}
