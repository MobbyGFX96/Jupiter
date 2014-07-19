package net.minecraft.entity.passive;

import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import java.util.Calendar;

public class EntityBat extends EntityAmbientCreature {
    private static final String __OBFID = "CL_00001637";
    private ChunkCoordinates spawnPosition;

    public EntityBat(World p_i1680_1_) {
        super(p_i1680_1_);
        this.setSize(0.5F, 0.9F);
        this.setIsBatHanging(true);
    }

    protected void entityInit() {
        super.entityInit();
        this.dataWatcher.addObject(16, new Byte((byte) 0));
    }

    protected float getSoundVolume() {
        return 0.1F;
    }

    protected float getSoundPitch() {
        return super.getSoundPitch() * 0.95F;
    }

    protected String getLivingSound() {
        return this.getIsBatHanging() && this.rand.nextInt(4) != 0 ? null : "mob.bat.idle";
    }

    protected String getHurtSound() {
        return "mob.bat.hurt";
    }

    protected String getDeathSound() {
        return "mob.bat.death";
    }

    public boolean canBePushed() {
        return false;
    }

    protected void collideWithEntity(Entity p_82167_1_) {
    }

    protected void collideWithNearbyEntities() {
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(6.0D);
    }

    public boolean getIsBatHanging() {
        return (this.dataWatcher.getWatchableObjectByte(16) & 1) != 0;
    }

    public void setIsBatHanging(boolean p_82236_1_) {
        byte var2 = this.dataWatcher.getWatchableObjectByte(16);
        if (p_82236_1_) {
            this.dataWatcher.updateObject(16, Byte.valueOf((byte) (var2 | 1)));
        } else {
            this.dataWatcher.updateObject(16, Byte.valueOf((byte) (var2 & -2)));
        }
    }

    protected boolean isAIEnabled() {
        return true;
    }

    public void onUpdate() {
        super.onUpdate();
        if (this.getIsBatHanging()) {
            this.motionX = this.motionY = this.motionZ = 0.0D;
            this.posY = (double) MathHelper.floor_double(this.posY) + 1.0D - (double) this.height;
        } else {
            this.motionY *= 0.6000000238418579D;
        }
    }

    protected void updateAITasks() {
        super.updateAITasks();
        if (this.getIsBatHanging()) {
            if (!this.worldObj.getBlock(MathHelper.floor_double(this.posX), (int) this.posY + 1, MathHelper.floor_double(this.posZ)).isNormalCube()) {
                this.setIsBatHanging(false);
                this.worldObj.playAuxSFXAtEntity((EntityPlayer) null, 1015, (int) this.posX, (int) this.posY, (int) this.posZ, 0);
            } else {
                if (this.rand.nextInt(200) == 0) {
                    this.rotationYawHead = (float) this.rand.nextInt(360);
                }

                if (this.worldObj.getClosestPlayerToEntity(this, 4.0D) != null) {
                    this.setIsBatHanging(false);
                    this.worldObj.playAuxSFXAtEntity((EntityPlayer) null, 1015, (int) this.posX, (int) this.posY, (int) this.posZ, 0);
                }
            }
        } else {
            if (this.spawnPosition != null && (!this.worldObj.isAirBlock(this.spawnPosition.posX, this.spawnPosition.posY, this.spawnPosition.posZ) || this.spawnPosition.posY < 1)) {
                this.spawnPosition = null;
            }

            if (this.spawnPosition == null || this.rand.nextInt(30) == 0 || this.spawnPosition.getDistanceSquared((int) this.posX, (int) this.posY, (int) this.posZ) < 4.0F) {
                this.spawnPosition = new ChunkCoordinates((int) this.posX + this.rand.nextInt(7) - this.rand.nextInt(7), (int) this.posY + this.rand.nextInt(6) - 2, (int) this.posZ + this.rand.nextInt(7) - this.rand.nextInt(7));
            }

            double var1 = (double) this.spawnPosition.posX + 0.5D - this.posX;
            double var3 = (double) this.spawnPosition.posY + 0.1D - this.posY;
            double var5 = (double) this.spawnPosition.posZ + 0.5D - this.posZ;
            this.motionX += (Math.signum(var1) * 0.5D - this.motionX) * 0.10000000149011612D;
            this.motionY += (Math.signum(var3) * 0.699999988079071D - this.motionY) * 0.10000000149011612D;
            this.motionZ += (Math.signum(var5) * 0.5D - this.motionZ) * 0.10000000149011612D;
            float var7 = (float) (Math.atan2(this.motionZ, this.motionX) * 180.0D / Math.PI) - 90.0F;
            float var8 = MathHelper.wrapAngleTo180_float(var7 - this.rotationYaw);
            this.moveForward = 0.5F;
            this.rotationYaw += var8;
            if (this.rand.nextInt(100) == 0 && this.worldObj.getBlock(MathHelper.floor_double(this.posX), (int) this.posY + 1, MathHelper.floor_double(this.posZ)).isNormalCube()) {
                this.setIsBatHanging(true);
            }
        }
    }

    protected boolean canTriggerWalking() {
        return false;
    }

    protected void fall(float p_70069_1_) {
    }

    protected void updateFallState(double p_70064_1_, boolean p_70064_3_) {
    }

    public boolean doesEntityNotTriggerPressurePlate() {
        return true;
    }

    public boolean attackEntityFrom(DamageSource p_70097_1_, float p_70097_2_) {
        if (this.isEntityInvulnerable()) {
            return false;
        } else {
            if (!this.worldObj.isClient && this.getIsBatHanging()) {
                this.setIsBatHanging(false);
            }

            return super.attackEntityFrom(p_70097_1_, p_70097_2_);
        }
    }

    public void readEntityFromNBT(NBTTagCompound p_70037_1_) {
        super.readEntityFromNBT(p_70037_1_);
        this.dataWatcher.updateObject(16, Byte.valueOf(p_70037_1_.getByte("BatFlags")));
    }

    public void writeEntityToNBT(NBTTagCompound p_70014_1_) {
        super.writeEntityToNBT(p_70014_1_);
        p_70014_1_.setByte("BatFlags", this.dataWatcher.getWatchableObjectByte(16));
    }

    public boolean getCanSpawnHere() {
        int var1 = MathHelper.floor_double(this.boundingBox.minY);
        if (var1 >= 63) {
            return false;
        } else {
            int var2 = MathHelper.floor_double(this.posX);
            int var3 = MathHelper.floor_double(this.posZ);
            int var4 = this.worldObj.getBlockLightValue(var2, var1, var3);
            byte var5 = 4;
            Calendar var6 = this.worldObj.getCurrentDate();
            if ((var6.get(2) + 1 != 10 || var6.get(5) < 20) && (var6.get(2) + 1 != 11 || var6.get(5) > 3)) {
                if (this.rand.nextBoolean()) {
                    return false;
                }
            } else {
                var5 = 7;
            }

            return var4 > this.rand.nextInt(var5) ? false : super.getCanSpawnHere();
        }
    }
}
