package com.jupiter.mods.impl;

import com.jupiter.mods.Module;
import com.jupiter.mods.ModuleType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.play.client.C03PacketPlayer;
import org.lwjgl.input.Keyboard;

/**
 * Created by corey on 15/07/14.
 */
public class NoFall extends Module {

    public NoFall() {
        super("No Fall", "", Keyboard.KEY_O, ModuleType.WORLD);
    }

    @Override
    public void beforeUpdate(EntityPlayerSP player) {
        if (isState()) {
            if (getPlayer().fallDistance > 2.0F) {
                getPlayer().sendQueue.addToSendQueue(new C03PacketPlayer(true));
                if ((!getPlayer().onGround) && (blockBelowPlayer())) {
                    getPlayer().setVelocity(0.0D, 0.0D, 0.0D);
                    getPlayer().fallDistance = 0.0F;
                }
            }
        }
    }

    public boolean blockBelowPlayer() {
        int x = (int) Math.round(getPlayer().posX - 0.5D);
        int y = (int) Math.round(getPlayer().posY - 0.5D);
        int z = (int) Math.round(getPlayer().posZ - 0.5D);

        Block block = getWorld().getBlock(x, y - 3, z);
        return !(block instanceof BlockAir);
    }

    @Override
    public int getColour() {
        return 0xFF00FF;
    }
}
