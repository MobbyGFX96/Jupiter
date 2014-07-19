package net.minecraft.client.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.MovingSoundMinecartRiding;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.*;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatFileWriter;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Session;
import net.minecraft.world.World;

public class EntityClientPlayerMP extends EntityPlayerSP {
    private static final String __OBFID = "CL_00000887";
    public final NetHandlerPlayClient sendQueue;
    private final StatFileWriter field_146108_bO;
    public double oldPosX;
    public double oldMinY;
    public double oldPosY;
    public double oldPosZ;
    public boolean wasOnGround;
    public int ticksSinceMovePacket;
    protected float oldRotationYaw;
    protected float oldRotationPitch;
    protected boolean shouldStopSneaking;
    protected boolean wasSneaking;
    private boolean hasSetHealth;
    private String field_142022_ce;

    public EntityClientPlayerMP(Minecraft p_i45064_1_, World p_i45064_2_, Session p_i45064_3_, NetHandlerPlayClient p_i45064_4_, StatFileWriter p_i45064_5_) {
        super(p_i45064_1_, p_i45064_2_, p_i45064_3_, 0);
        this.sendQueue = p_i45064_4_;
        this.field_146108_bO = p_i45064_5_;
    }

    public boolean attackEntityFrom(DamageSource p_70097_1_, float p_70097_2_) {
        return false;
    }

    public void heal(float p_70691_1_) {
    }

    public void mountEntity(Entity p_70078_1_) {
        super.mountEntity(p_70078_1_);
        if (p_70078_1_ instanceof EntityMinecart) {
            this.mc.getSoundHandler().playSound(new MovingSoundMinecartRiding(this, (EntityMinecart) p_70078_1_));
        }
    }

    public void onUpdate() {
        if (this.worldObj.blockExists(MathHelper.floor_double(this.posX), 0, MathHelper.floor_double(this.posZ))) {
            super.onUpdate();
            if (this.isRiding()) {
                this.sendQueue.addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(this.rotationYaw, this.rotationPitch, this.onGround));
                this.sendQueue.addToSendQueue(new C0CPacketInput(this.moveStrafing, this.moveForward, this.movementInput.jump, this.movementInput.sneak));
            } else {
                this.sendMotionUpdates();
            }
        }
    }

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

    public EntityItem dropOneItem(boolean p_71040_1_) {
        int var2 = p_71040_1_ ? 3 : 4;
        this.sendQueue.addToSendQueue(new C07PacketPlayerDigging(var2, 0, 0, 0, 0));
        return null;
    }

    protected void joinEntityItemWithWorld(EntityItem p_71012_1_) {
    }

    public void sendChatMessage(String p_71165_1_) {
        this.sendQueue.addToSendQueue(new C01PacketChatMessage(p_71165_1_));
    }

    public void swingItem() {
        super.swingItem();
        this.sendQueue.addToSendQueue(new C0APacketAnimation(this, 1));
    }

    public void respawnPlayer() {
        this.sendQueue.addToSendQueue(new C16PacketClientStatus(C16PacketClientStatus.EnumState.PERFORM_RESPAWN));
    }

    protected void damageEntity(DamageSource p_70665_1_, float p_70665_2_) {
        if (!this.isEntityInvulnerable()) {
            this.setHealth(this.getHealth() - p_70665_2_);
        }
    }

    public void closeScreen() {
        this.sendQueue.addToSendQueue(new C0DPacketCloseWindow(this.openContainer.windowId));
        this.closeScreenNoPacket();
    }

    public void closeScreenNoPacket() {
        this.inventory.setItemStack((ItemStack) null);
        super.closeScreen();
    }

    public void setPlayerSPHealth(float p_71150_1_) {
        if (this.hasSetHealth) {
            super.setPlayerSPHealth(p_71150_1_);
        } else {
            this.setHealth(p_71150_1_);
            this.hasSetHealth = true;
        }
    }

    public void addStat(StatBase p_71064_1_, int p_71064_2_) {
        if (p_71064_1_ != null) {
            if (p_71064_1_.isIndependent) {
                super.addStat(p_71064_1_, p_71064_2_);
            }
        }
    }

    public void sendPlayerAbilities() {
        this.sendQueue.addToSendQueue(new C13PacketPlayerAbilities(this.capabilities));
    }

    protected void func_110318_g() {
        this.sendQueue.addToSendQueue(new C0BPacketEntityAction(this, 6, (int) (this.getHorseJumpPower() * 100.0F)));
    }

    public void func_110322_i() {
        this.sendQueue.addToSendQueue(new C0BPacketEntityAction(this, 7));
    }

    public void func_142020_c(String p_142020_1_) {
        this.field_142022_ce = p_142020_1_;
    }

    public String func_142021_k() {
        return this.field_142022_ce;
    }

    public StatFileWriter func_146107_m() {
        return this.field_146108_bO;
    }
}
