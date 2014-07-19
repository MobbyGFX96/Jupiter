package net.minecraft.entity.item;

import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityEnderEye extends Entity {
    private static final String __OBFID = "CL_00001716";
    private double targetX;
    private double targetY;
    private double targetZ;
    private int despawnTimer;
    private boolean shatterOrDrop;

    public EntityEnderEye(World p_i1757_1_) {
        super(p_i1757_1_);
        this.setSize(0.25F, 0.25F);
    }

    public EntityEnderEye(World p_i1758_1_, double p_i1758_2_, double p_i1758_4_, double p_i1758_6_) {
        super(p_i1758_1_);
        this.despawnTimer = 0;
        this.setSize(0.25F, 0.25F);
        this.setPosition(p_i1758_2_, p_i1758_4_, p_i1758_6_);
        this.yOffset = 0.0F;
    }

    protected void entityInit() {
    }

    public boolean isInRangeToRenderDist(double p_70112_1_) {
        double var3 = this.boundingBox.getAverageEdgeLength() * 4.0D;
        var3 *= 64.0D;
        return p_70112_1_ < var3 * var3;
    }

    public void moveTowards(double p_70220_1_, int p_70220_3_, double p_70220_4_) {
        double var6 = p_70220_1_ - this.posX;
        double var8 = p_70220_4_ - this.posZ;
        float var10 = MathHelper.sqrt_double(var6 * var6 + var8 * var8);
        if (var10 > 12.0F) {
            this.targetX = this.posX + var6 / (double) var10 * 12.0D;
            this.targetZ = this.posZ + var8 / (double) var10 * 12.0D;
            this.targetY = this.posY + 8.0D;
        } else {
            this.targetX = p_70220_1_;
            this.targetY = (double) p_70220_3_;
            this.targetZ = p_70220_4_;
        }

        this.despawnTimer = 0;
        this.shatterOrDrop = this.rand.nextInt(5) > 0;
    }

    public void setVelocity(double p_70016_1_, double p_70016_3_, double p_70016_5_) {
        this.motionX = p_70016_1_;
        this.motionY = p_70016_3_;
        this.motionZ = p_70016_5_;
        if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {
            float var7 = MathHelper.sqrt_double(p_70016_1_ * p_70016_1_ + p_70016_5_ * p_70016_5_);
            this.prevRotationYaw = this.rotationYaw = (float) (Math.atan2(p_70016_1_, p_70016_5_) * 180.0D / Math.PI);
            this.prevRotationPitch = this.rotationPitch = (float) (Math.atan2(p_70016_3_, (double) var7) * 180.0D / Math.PI);
        }
    }

    public void onUpdate() {
        this.lastTickPosX = this.posX;
        this.lastTickPosY = this.posY;
        this.lastTickPosZ = this.posZ;
        super.onUpdate();
        this.posX += this.motionX;
        this.posY += this.motionY;
        this.posZ += this.motionZ;
        float var1 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
        this.rotationYaw = (float) (Math.atan2(this.motionX, this.motionZ) * 180.0D / Math.PI);

        for (this.rotationPitch = (float) (Math.atan2(this.motionY, (double) var1) * 180.0D / Math.PI); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F) {
            ;
        }

        while (this.rotationPitch - this.prevRotationPitch >= 180.0F) {
            this.prevRotationPitch += 360.0F;
        }

        while (this.rotationYaw - this.prevRotationYaw < -180.0F) {
            this.prevRotationYaw -= 360.0F;
        }

        while (this.rotationYaw - this.prevRotationYaw >= 180.0F) {
            this.prevRotationYaw += 360.0F;
        }

        this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
        this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;
        if (!this.worldObj.isClient) {
            double var2 = this.targetX - this.posX;
            double var4 = this.targetZ - this.posZ;
            float var6 = (float) Math.sqrt(var2 * var2 + var4 * var4);
            float var7 = (float) Math.atan2(var4, var2);
            double var8 = (double) var1 + (double) (var6 - var1) * 0.0025D;
            if (var6 < 1.0F) {
                var8 *= 0.8D;
                this.motionY *= 0.8D;
            }

            this.motionX = Math.cos((double) var7) * var8;
            this.motionZ = Math.sin((double) var7) * var8;
            if (this.posY < this.targetY) {
                this.motionY += (1.0D - this.motionY) * 0.014999999664723873D;
            } else {
                this.motionY += (-1.0D - this.motionY) * 0.014999999664723873D;
            }
        }

        float var10 = 0.25F;
        if (this.isInWater()) {
            for (int var3 = 0; var3 < 4; ++var3) {
                this.worldObj.spawnParticle("bubble", this.posX - this.motionX * (double) var10, this.posY - this.motionY * (double) var10, this.posZ - this.motionZ * (double) var10, this.motionX, this.motionY, this.motionZ);
            }
        } else {
            this.worldObj.spawnParticle("portal", this.posX - this.motionX * (double) var10 + this.rand.nextDouble() * 0.6D - 0.3D, this.posY - this.motionY * (double) var10 - 0.5D, this.posZ - this.motionZ * (double) var10 + this.rand.nextDouble() * 0.6D - 0.3D, this.motionX, this.motionY, this.motionZ);
        }

        if (!this.worldObj.isClient) {
            this.setPosition(this.posX, this.posY, this.posZ);
            ++this.despawnTimer;
            if (this.despawnTimer > 80 && !this.worldObj.isClient) {
                this.setDead();
                if (this.shatterOrDrop) {
                    this.worldObj.spawnEntityInWorld(new EntityItem(this.worldObj, this.posX, this.posY, this.posZ, new ItemStack(Items.ender_eye)));
                } else {
                    this.worldObj.playAuxSFX(2003, (int) Math.round(this.posX), (int) Math.round(this.posY), (int) Math.round(this.posZ), 0);
                }
            }
        }
    }

    public void writeEntityToNBT(NBTTagCompound p_70014_1_) {
    }

    public void readEntityFromNBT(NBTTagCompound p_70037_1_) {
    }

    public float getShadowSize() {
        return 0.0F;
    }

    public float getBrightness(float p_70013_1_) {
        return 1.0F;
    }

    public int getBrightnessForRender(float p_70070_1_) {
        return 15728880;
    }

    public boolean canAttackWithItem() {
        return false;
    }
}
