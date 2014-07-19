package net.minecraft.pathfinding;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class PathNavigate {
    private static final String __OBFID = "CL_00001627";
    private EntityLiving theEntity;
    private World worldObj;
    private PathEntity currentPath;
    private double speed;
    private IAttributeInstance pathSearchRange;
    private boolean noSunPathfind;
    private int totalTicks;
    private int ticksAtLastPos;
    private Vec3 lastPosCheck = Vec3.createVectorHelper(0.0D, 0.0D, 0.0D);
    private boolean canPassOpenWoodenDoors = true;
    private boolean canPassClosedWoodenDoors;
    private boolean avoidsWater;
    private boolean canSwim;

    public PathNavigate(EntityLiving p_i1671_1_, World p_i1671_2_) {
        this.theEntity = p_i1671_1_;
        this.worldObj = p_i1671_2_;
        this.pathSearchRange = p_i1671_1_.getEntityAttribute(SharedMonsterAttributes.followRange);
    }

    public boolean getAvoidsWater() {
        return this.avoidsWater;
    }

    public void setAvoidsWater(boolean p_75491_1_) {
        this.avoidsWater = p_75491_1_;
    }

    public void setBreakDoors(boolean p_75498_1_) {
        this.canPassClosedWoodenDoors = p_75498_1_;
    }

    public void setEnterDoors(boolean p_75490_1_) {
        this.canPassOpenWoodenDoors = p_75490_1_;
    }

    public boolean getCanBreakDoors() {
        return this.canPassClosedWoodenDoors;
    }

    public void setAvoidSun(boolean p_75504_1_) {
        this.noSunPathfind = p_75504_1_;
    }

    public void setSpeed(double p_75489_1_) {
        this.speed = p_75489_1_;
    }

    public void setCanSwim(boolean p_75495_1_) {
        this.canSwim = p_75495_1_;
    }

    public float func_111269_d() {
        return (float) this.pathSearchRange.getAttributeValue();
    }

    public PathEntity getPathToXYZ(double p_75488_1_, double p_75488_3_, double p_75488_5_) {
        return !this.canNavigate() ? null : this.worldObj.getEntityPathToXYZ(this.theEntity, MathHelper.floor_double(p_75488_1_), (int) p_75488_3_, MathHelper.floor_double(p_75488_5_), this.func_111269_d(), this.canPassOpenWoodenDoors, this.canPassClosedWoodenDoors, this.avoidsWater, this.canSwim);
    }

    public boolean tryMoveToXYZ(double p_75492_1_, double p_75492_3_, double p_75492_5_, double p_75492_7_) {
        PathEntity var9 = this.getPathToXYZ((double) MathHelper.floor_double(p_75492_1_), (double) ((int) p_75492_3_), (double) MathHelper.floor_double(p_75492_5_));
        return this.setPath(var9, p_75492_7_);
    }

    public PathEntity getPathToEntityLiving(Entity p_75494_1_) {
        return !this.canNavigate() ? null : this.worldObj.getPathEntityToEntity(this.theEntity, p_75494_1_, this.func_111269_d(), this.canPassOpenWoodenDoors, this.canPassClosedWoodenDoors, this.avoidsWater, this.canSwim);
    }

    public boolean tryMoveToEntityLiving(Entity p_75497_1_, double p_75497_2_) {
        PathEntity var4 = this.getPathToEntityLiving(p_75497_1_);
        return var4 != null ? this.setPath(var4, p_75497_2_) : false;
    }

    public boolean setPath(PathEntity p_75484_1_, double p_75484_2_) {
        if (p_75484_1_ == null) {
            this.currentPath = null;
            return false;
        } else {
            if (!p_75484_1_.isSamePath(this.currentPath)) {
                this.currentPath = p_75484_1_;
            }

            if (this.noSunPathfind) {
                this.removeSunnyPath();
            }

            if (this.currentPath.getCurrentPathLength() == 0) {
                return false;
            } else {
                this.speed = p_75484_2_;
                Vec3 var4 = this.getEntityPosition();
                this.ticksAtLastPos = this.totalTicks;
                this.lastPosCheck.xCoord = var4.xCoord;
                this.lastPosCheck.yCoord = var4.yCoord;
                this.lastPosCheck.zCoord = var4.zCoord;
                return true;
            }
        }
    }

    public PathEntity getPath() {
        return this.currentPath;
    }

    public void onUpdateNavigation() {
        ++this.totalTicks;
        if (!this.noPath()) {
            if (this.canNavigate()) {
                this.pathFollow();
            }

            if (!this.noPath()) {
                Vec3 var1 = this.currentPath.getPosition(this.theEntity);
                if (var1 != null) {
                    this.theEntity.getMoveHelper().setMoveTo(var1.xCoord, var1.yCoord, var1.zCoord, this.speed);
                }
            }
        }
    }

    private void pathFollow() {
        Vec3 var1 = this.getEntityPosition();
        int var2 = this.currentPath.getCurrentPathLength();

        for (int var3 = this.currentPath.getCurrentPathIndex(); var3 < this.currentPath.getCurrentPathLength(); ++var3) {
            if (this.currentPath.getPathPointFromIndex(var3).yCoord != (int) var1.yCoord) {
                var2 = var3;
                break;
            }
        }

        float var8 = this.theEntity.width * this.theEntity.width;

        int var4;
        for (var4 = this.currentPath.getCurrentPathIndex(); var4 < var2; ++var4) {
            if (var1.squareDistanceTo(this.currentPath.getVectorFromIndex(this.theEntity, var4)) < (double) var8) {
                this.currentPath.setCurrentPathIndex(var4 + 1);
            }
        }

        var4 = MathHelper.ceiling_float_int(this.theEntity.width);
        int var5 = (int) this.theEntity.height + 1;
        int var6 = var4;

        for (int var7 = var2 - 1; var7 >= this.currentPath.getCurrentPathIndex(); --var7) {
            if (this.isDirectPathBetweenPoints(var1, this.currentPath.getVectorFromIndex(this.theEntity, var7), var4, var5, var6)) {
                this.currentPath.setCurrentPathIndex(var7);
                break;
            }
        }

        if (this.totalTicks - this.ticksAtLastPos > 100) {
            if (var1.squareDistanceTo(this.lastPosCheck) < 2.25D) {
                this.clearPathEntity();
            }

            this.ticksAtLastPos = this.totalTicks;
            this.lastPosCheck.xCoord = var1.xCoord;
            this.lastPosCheck.yCoord = var1.yCoord;
            this.lastPosCheck.zCoord = var1.zCoord;
        }
    }

    public boolean noPath() {
        return this.currentPath == null || this.currentPath.isFinished();
    }

    public void clearPathEntity() {
        this.currentPath = null;
    }

    private Vec3 getEntityPosition() {
        return Vec3.createVectorHelper(this.theEntity.posX, (double) this.getPathableYPos(), this.theEntity.posZ);
    }

    private int getPathableYPos() {
        if (this.theEntity.isInWater() && this.canSwim) {
            int var1 = (int) this.theEntity.boundingBox.minY;
            Block var2 = this.worldObj.getBlock(MathHelper.floor_double(this.theEntity.posX), var1, MathHelper.floor_double(this.theEntity.posZ));
            int var3 = 0;

            do {
                if (var2 != Blocks.flowing_water && var2 != Blocks.water) {
                    return var1;
                }

                ++var1;
                var2 = this.worldObj.getBlock(MathHelper.floor_double(this.theEntity.posX), var1, MathHelper.floor_double(this.theEntity.posZ));
                ++var3;
            } while (var3 <= 16);

            return (int) this.theEntity.boundingBox.minY;
        } else {
            return (int) (this.theEntity.boundingBox.minY + 0.5D);
        }
    }

    private boolean canNavigate() {
        return this.theEntity.onGround || this.canSwim && this.isInFluid() || this.theEntity.isRiding() && this.theEntity instanceof EntityZombie && this.theEntity.ridingEntity instanceof EntityChicken;
    }

    private boolean isInFluid() {
        return this.theEntity.isInWater() || this.theEntity.handleLavaMovement();
    }

    private void removeSunnyPath() {
        if (!this.worldObj.canBlockSeeTheSky(MathHelper.floor_double(this.theEntity.posX), (int) (this.theEntity.boundingBox.minY + 0.5D), MathHelper.floor_double(this.theEntity.posZ))) {
            for (int var1 = 0; var1 < this.currentPath.getCurrentPathLength(); ++var1) {
                PathPoint var2 = this.currentPath.getPathPointFromIndex(var1);
                if (this.worldObj.canBlockSeeTheSky(var2.xCoord, var2.yCoord, var2.zCoord)) {
                    this.currentPath.setCurrentPathLength(var1 - 1);
                    return;
                }
            }
        }
    }

    private boolean isDirectPathBetweenPoints(Vec3 p_75493_1_, Vec3 p_75493_2_, int p_75493_3_, int p_75493_4_, int p_75493_5_) {
        int var6 = MathHelper.floor_double(p_75493_1_.xCoord);
        int var7 = MathHelper.floor_double(p_75493_1_.zCoord);
        double var8 = p_75493_2_.xCoord - p_75493_1_.xCoord;
        double var10 = p_75493_2_.zCoord - p_75493_1_.zCoord;
        double var12 = var8 * var8 + var10 * var10;
        if (var12 < 1.0E-8D) {
            return false;
        } else {
            double var14 = 1.0D / Math.sqrt(var12);
            var8 *= var14;
            var10 *= var14;
            p_75493_3_ += 2;
            p_75493_5_ += 2;
            if (!this.isSafeToStandAt(var6, (int) p_75493_1_.yCoord, var7, p_75493_3_, p_75493_4_, p_75493_5_, p_75493_1_, var8, var10)) {
                return false;
            } else {
                p_75493_3_ -= 2;
                p_75493_5_ -= 2;
                double var16 = 1.0D / Math.abs(var8);
                double var18 = 1.0D / Math.abs(var10);
                double var20 = (double) (var6 * 1) - p_75493_1_.xCoord;
                double var22 = (double) (var7 * 1) - p_75493_1_.zCoord;
                if (var8 >= 0.0D) {
                    ++var20;
                }

                if (var10 >= 0.0D) {
                    ++var22;
                }

                var20 /= var8;
                var22 /= var10;
                int var24 = var8 < 0.0D ? -1 : 1;
                int var25 = var10 < 0.0D ? -1 : 1;
                int var26 = MathHelper.floor_double(p_75493_2_.xCoord);
                int var27 = MathHelper.floor_double(p_75493_2_.zCoord);
                int var28 = var26 - var6;
                int var29 = var27 - var7;

                do {
                    if (var28 * var24 <= 0 && var29 * var25 <= 0) {
                        return true;
                    }

                    if (var20 < var22) {
                        var20 += var16;
                        var6 += var24;
                        var28 = var26 - var6;
                    } else {
                        var22 += var18;
                        var7 += var25;
                        var29 = var27 - var7;
                    }
                }
                while (this.isSafeToStandAt(var6, (int) p_75493_1_.yCoord, var7, p_75493_3_, p_75493_4_, p_75493_5_, p_75493_1_, var8, var10));

                return false;
            }
        }
    }

    private boolean isSafeToStandAt(int p_75483_1_, int p_75483_2_, int p_75483_3_, int p_75483_4_, int p_75483_5_, int p_75483_6_, Vec3 p_75483_7_, double p_75483_8_, double p_75483_10_) {
        int var12 = p_75483_1_ - p_75483_4_ / 2;
        int var13 = p_75483_3_ - p_75483_6_ / 2;
        if (!this.isPositionClear(var12, p_75483_2_, var13, p_75483_4_, p_75483_5_, p_75483_6_, p_75483_7_, p_75483_8_, p_75483_10_)) {
            return false;
        } else {
            for (int var14 = var12; var14 < var12 + p_75483_4_; ++var14) {
                for (int var15 = var13; var15 < var13 + p_75483_6_; ++var15) {
                    double var16 = (double) var14 + 0.5D - p_75483_7_.xCoord;
                    double var18 = (double) var15 + 0.5D - p_75483_7_.zCoord;
                    if (var16 * p_75483_8_ + var18 * p_75483_10_ >= 0.0D) {
                        Block var20 = this.worldObj.getBlock(var14, p_75483_2_ - 1, var15);
                        Material var21 = var20.getMaterial();
                        if (var21 == Material.air) {
                            return false;
                        }

                        if (var21 == Material.water && !this.theEntity.isInWater()) {
                            return false;
                        }

                        if (var21 == Material.lava) {
                            return false;
                        }
                    }
                }
            }

            return true;
        }
    }

    private boolean isPositionClear(int p_75496_1_, int p_75496_2_, int p_75496_3_, int p_75496_4_, int p_75496_5_, int p_75496_6_, Vec3 p_75496_7_, double p_75496_8_, double p_75496_10_) {
        for (int var12 = p_75496_1_; var12 < p_75496_1_ + p_75496_4_; ++var12) {
            for (int var13 = p_75496_2_; var13 < p_75496_2_ + p_75496_5_; ++var13) {
                for (int var14 = p_75496_3_; var14 < p_75496_3_ + p_75496_6_; ++var14) {
                    double var15 = (double) var12 + 0.5D - p_75496_7_.xCoord;
                    double var17 = (double) var14 + 0.5D - p_75496_7_.zCoord;
                    if (var15 * p_75496_8_ + var17 * p_75496_10_ >= 0.0D) {
                        Block var19 = this.worldObj.getBlock(var12, var13, var14);
                        if (!var19.getBlocksMovement(this.worldObj, var12, var13, var14)) {
                            return false;
                        }
                    }
                }
            }
        }

        return true;
    }
}
