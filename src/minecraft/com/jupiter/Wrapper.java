package com.jupiter;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.multiplayer.WorldClient;

/**
 * Created by corey on 13/07/14.
 */
public abstract class Wrapper {

    private Minecraft minecraft = Minecraft.getMinecraft();

    public Minecraft getMinecraft() {
        return minecraft;
    }

    public EntityClientPlayerMP getPlayer() {
        return minecraft.thePlayer;
    }

    public WorldClient getWorld() {
        return minecraft.theWorld;
    }

}
