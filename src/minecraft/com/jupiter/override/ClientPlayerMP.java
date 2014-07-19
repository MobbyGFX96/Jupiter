package com.jupiter.override;

import com.jupiter.Jupiter;
import com.jupiter.mods.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C0CPacketInput;
import net.minecraft.stats.StatFileWriter;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Session;
import net.minecraft.world.World;

/**
 * Created by corey on 15/07/14.
 */
public class ClientPlayerMP extends EntityClientPlayerMP {

    public ClientPlayerMP(Minecraft minecraft, World world, Session session, NetHandlerPlayClient netHandlerPlayClient, StatFileWriter statFileWriter) {
        super(minecraft, world, session, netHandlerPlayClient, statFileWriter);
    }

    @Override
    public void sendMotionUpdates() {
        boolean var1 = this.isSprinting();
        if (var1 != this.wasSneaking) {
            if (var1) {
                this.sendQueue.addToSendQueue(new C0BPacketEntityAction(this, 4));
            } else {
                this.sendQueue.addToSendQueue(new C0BPacketEntityAction(this, 5));
            }
            this.wasSneaking = var1;
        }

        boolean var2 = this.isSneaking();
        if (var2 != this.shouldStopSneaking) {
            if (var2) {
                this.sendQueue.addToSendQueue(new C0BPacketEntityAction(this, 1));
            } else {
                this.sendQueue.addToSendQueue(new C0BPacketEntityAction(this, 2));
            }

            this.shouldStopSneaking = var2;
        }

        double var3 = this.posX - this.oldPosX;
        double var5 = this.boundingBox.minY - this.oldMinY;
        double var7 = this.posZ - this.oldPosZ;
        double var9 = (double) (this.rotationYaw - this.oldRotationYaw);
        double var11 = (double) (this.rotationPitch - this.oldRotationPitch);
        boolean var13 = var3 * var3 + var5 * var5 + var7 * var7 > 9.0E-4D || this.ticksSinceMovePacket >= 20;
        boolean var14 = var9 != 0.0D || var11 != 0.0D;
        if (this.ridingEntity != null) {
            this.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(this.motionX, -999.0D, -999.0D, this.motionZ, this.rotationYaw, this.rotationPitch, this.onGround));
            var13 = false;
        } else if (var13 && var14) {
            this.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(this.posX, this.boundingBox.minY, this.posY, this.posZ, this.rotationYaw, this.rotationPitch, this.onGround));
        } else if (var13) {
            this.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.posX, this.boundingBox.minY, this.posY, this.posZ, this.onGround));
        } else if (var14) {
            this.sendQueue.addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(this.rotationYaw, this.rotationPitch, this.onGround));
        } else {
            this.sendQueue.addToSendQueue(new C03PacketPlayer(this.onGround));
        }

        ++this.ticksSinceMovePacket;
        this.wasOnGround = this.onGround;
        if (var13) {
            this.oldPosX = this.posX;
            this.oldMinY = this.boundingBox.minY;
            this.oldPosY = this.posY;
            this.oldPosZ = this.posZ;
            this.ticksSinceMovePacket = 0;
        }

        if (var14) {
            this.oldRotationYaw = this.rotationYaw;
            this.oldRotationPitch = this.rotationPitch;
        }

    }

    @Override
    public void onUpdate() {
        if (this.worldObj.blockExists(MathHelper.floor_double(this.posX), 0, MathHelper.floor_double(this.posZ))) {
            for (Module module : Jupiter.getInstance().getModuleManager().getModules()) {
                module.onPlayerUpdate(this);
            }
            super.onUpdate();
            for (Module module : Jupiter.getInstance().getModuleManager().getModules()) {
                module.beforeUpdate(this);
            }
            for (Module module : Jupiter.getInstance().getModuleManager().getModules()) {
                module.onUpdate(this);
            }
            for (Module module : Jupiter.getInstance().getModuleManager().getModules()) {
                module.afterUpdate(this);
            }
            if (this.isRiding()) {
                this.sendQueue.addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(this.rotationYaw, this.rotationPitch, this.onGround));
                this.sendQueue.addToSendQueue(new C0CPacketInput(this.moveStrafing, this.moveForward, this.movementInput.jump, this.movementInput.sneak));
            } else {
                this.sendMotionUpdates();
            }
        }
    }
}
