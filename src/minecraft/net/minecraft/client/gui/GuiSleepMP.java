package net.minecraft.client.gui;

import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.resources.I18n;
import net.minecraft.network.play.client.C0BPacketEntityAction;

public class GuiSleepMP extends GuiChat {
    private static final String __OBFID = "CL_00000697";

    public void initGui() {
        super.initGui();
        this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height - 40, I18n.format("multiplayer.stopSleeping", new Object[0])));
    }

    protected void keyTyped(char character, int key) {
        if (key == 1) {
            this.func_146418_g();
        } else if (key != 28 && key != 156) {
            super.keyTyped(character, key);
        } else {
            String var3 = this.guiTextField.getText().trim();
            if (!var3.isEmpty()) {
                this.mc.thePlayer.sendChatMessage(var3);
            }

            this.guiTextField.setText("");
            this.mc.ingameGUI.getChatGUI().resetScroll();
        }
    }

    protected void actionPerformed(GuiButton p_146284_1_) {
        if (p_146284_1_.id == 1) {
            this.func_146418_g();
        } else {
            super.actionPerformed(p_146284_1_);
        }
    }

    private void func_146418_g() {
        NetHandlerPlayClient var1 = this.mc.thePlayer.sendQueue;
        var1.addToSendQueue(new C0BPacketEntityAction(this.mc.thePlayer, 3));
    }
}
