package net.minecraft.entity.effect;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

import java.util.List;

public class EntityLightningBolt extends EntityWeatherEffect {
    private static final String __OBFID = "CL_00001666";
    public long boltVertex;
    private int lightningState;
    private int boltLivingTime;

    public EntityLightningBolt(World p_i1703_1_, double p_i1703_2_, double p_i1703_4_, double p_i1703_6_) {
        super(p_i1703_1_);
        this.setLocationAndAngles(p_i1703_2_, p_i1703_4_, p_i1703_6_, 0.0F, 0.0F);
        this.lightningState = 2;
        this.boltVertex = this.rand.nextLong();
        this.boltLivingTime = this.rand.nextInt(3) + 1;
        if (!p_i1703_1_.isClient && p_i1703_1_.getGameRules().getGameRuleBooleanValue("doFireTick") && (p_i1703_1_.difficultySetting == EnumDifficulty.NORMAL || p_i1703_1_.difficultySetting == EnumDifficulty.HARD) && p_i1703_1_.doChunksNearChunkExist(MathHelper.floor_double(p_i1703_2_), MathHelper.floor_double(p_i1703_4_), MathHelper.floor_double(p_i1703_6_), 10)) {
            int var8 = MathHelper.floor_double(p_i1703_2_);
            int var9 = MathHelper.floor_double(p_i1703_4_);
            int var10 = MathHelper.floor_double(p_i1703_6_);
            if (p_i1703_1_.getBlock(var8, var9, var10).getMaterial() == Material.air && Blocks.fire.canPlaceBlockAt(p_i1703_1_, var8, var9, var10)) {
                p_i1703_1_.setBlock(var8, var9, var10, Blocks.fire);
            }

            for (var8 = 0; var8 < 4; ++var8) {
                var9 = MathHelper.floor_double(p_i1703_2_) + this.rand.nextInt(3) - 1;
                var10 = MathHelper.floor_double(p_i1703_4_) + this.rand.nextInt(3) - 1;
                int var11 = MathHelper.floor_double(p_i1703_6_) + this.rand.nextInt(3) - 1;
                if (p_i1703_1_.getBlock(var9, var10, var11).getMaterial() == Material.air && Blocks.fire.canPlaceBlockAt(p_i1703_1_, var9, var10, var11)) {
                    p_i1703_1_.setBlock(var9, var10, var11, Blocks.fire);
                }
            }
        }
    }

    public void onUpdate() {
        super.onUpdate();
        if (this.lightningState == 2) {
            this.worldObj.playSoundEffect(this.posX, this.posY, this.posZ, "ambient.weather.thunder", 10000.0F, 0.8F + this.rand.nextFloat() * 0.2F);
            this.worldObj.playSoundEffect(this.posX, this.posY, this.posZ, "random.explode", 2.0F, 0.5F + this.rand.nextFloat() * 0.2F);
        }

        --this.lightningState;
        if (this.lightningState < 0) {
            if (this.boltLivingTime == 0) {
                this.setDead();
            } else if (this.lightningState < -this.rand.nextInt(10)) {
                --this.boltLivingTime;
                this.lightningState = 1;
                this.boltVertex = this.rand.nextLong();
                if (!this.worldObj.isClient && this.worldObj.getGameRules().getGameRuleBooleanValue("doFireTick") && this.worldObj.doChunksNearChunkExist(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ), 10)) {
                    int var1 = MathHelper.floor_double(this.posX);
                    int var2 = MathHelper.floor_double(this.posY);
                    int var3 = MathHelper.floor_double(this.posZ);
                    if (this.worldObj.getBlock(var1, var2, var3).getMaterial() == Material.air && Blocks.fire.canPlaceBlockAt(this.worldObj, var1, var2, var3)) {
                        this.worldObj.setBlock(var1, var2, var3, Blocks.fire);
                    }
                }
            }
        }

        if (this.lightningState >= 0) {
            if (this.worldObj.isClient) {
                this.worldObj.lastLightningBolt = 2;
            } else {
                double var6 = 3.0D;
                List var7 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, AxisAlignedBB.getBoundingBox(this.posX - var6, this.posY - var6, this.posZ - var6, this.posX + var6, this.posY + 6.0D + var6, this.posZ + var6));

                for (int var4 = 0; var4 < var7.size(); ++var4) {
                    Entity var5 = (Entity) var7.get(var4);
                    var5.onStruckByLightning(this);
                }
            }
        }
    }

    protected void entityInit() {
    }

    protected void readEntityFromNBT(NBTTagCompound p_70037_1_) {
    }

    protected void writeEntityToNBT(NBTTagCompound p_70014_1_) {
    }
}
