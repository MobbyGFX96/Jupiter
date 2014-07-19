package com.jupiter.mods.impl;

import com.jupiter.Jupiter;
import com.jupiter.mods.Module;
import com.jupiter.mods.ModuleType;
import org.darkstorm.minecraft.gui.util.GuiManagerDisplayScreen;
import org.lwjgl.input.Keyboard;

/**
 * Created by corey on 16/07/14.
 */
public class JupiterGui extends Module {

    public JupiterGui() {
        super("GUI", Keyboard.KEY_GRAVE, ModuleType.NONE, false);
    }

    @Override
    public void onToggled() {
        getMinecraft().displayGuiScreen(new GuiManagerDisplayScreen(Jupiter.getInstance().getGuiManager()));
    }

    @Override
    public int getColour() {
        return 0;
    }
}
