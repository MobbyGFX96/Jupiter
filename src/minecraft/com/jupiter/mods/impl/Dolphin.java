package com.jupiter.mods.impl;

import com.jupiter.mods.Module;
import com.jupiter.mods.ModuleType;
import net.minecraft.client.entity.EntityPlayerSP;
import org.lwjgl.input.Keyboard;

/**
 * Created by corey on 15/07/14.
 */
public class Dolphin extends Module {

    private int count = 0;

    public Dolphin() {
        super("Dolphin", "", Keyboard.KEY_P, ModuleType.MOVEMENT);
    }

    @Override
    public void onPlayerUpdate(EntityPlayerSP player) {
        if (isEnabled()) {
            count++;
            if (count >= 3) {
                if (getPlayer().handleWaterMovement()) {
                    getPlayer().motionY = 0.1D;
                }
                count = 0;
            }
        }
    }

    @Override
    public int getColour() {
        return 0x00FF00;
    }
}
