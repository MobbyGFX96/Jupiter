package com.jupiter.override;

import com.jupiter.Jupiter;
import com.jupiter.mods.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;

/**
 * Created by corey on 15/07/14.
 */
public class OverlayHook extends GuiIngame {

    private Minecraft mc = Jupiter.getInstance().getMinecraft();

    public OverlayHook(Minecraft minecraft) {
        super(minecraft);
    }

    @Override
    public void renderGameOverlay(float par1, boolean par2, int par3, int par4) {
        super.renderGameOverlay(par1, par2, par3, par4);
        FontRenderer fr = Jupiter.getInstance().getFontManager().getClientFont();
        ScaledResolution sr = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
        int width = sr.getScaledWidth();
        //int height = sr.getScaledHeight();

        int count = 0;
        for (Module module : Jupiter.getInstance().getModuleManager().getActiveModules()) {
            int x = width - fr.getStringWidth(module.getModuleName()) - 2;
            int y = (10 * count) + 2;
            fr.drawString(module.getModuleName(), x, y, module.getColour());
            count++;
        }

        Jupiter.getInstance().getGuiManager().renderPinned();

        fr.drawString(Jupiter.getInstance().getClientName() + " (1.7.10)", 2, 2, 0xffff00);
    }
}
