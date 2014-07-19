package net.minecraft.entity.ai;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.util.MathHelper;

public class EntityMoveHelper {
    private static final String __OBFID = "CL_00001573";
    private EntityLiving entity;
    private double posX;
    private double posY;
    private double posZ;
    private double speed;
    private boolean update;

    public EntityMoveHelper(EntityLiving p_i1614_1_) {
        this.entity = p_i1614_1_;
        this.posX = p_i1614_1_.posX;
        this.posY = p_i1614_1_.posY;
        this.posZ = p_i1614_1_.posZ;
    }

    public boolean isUpdating() {
        return this.update;
    }

    public double getSpeed() {
        return this.speed;
    }

    public void setMoveTo(double p_75642_1_, double p_75642_3_, double p_75642_5_, double p_75642_7_) {
        this.posX = p_75642_1_;
        this.posY = p_75642_3_;
        this.posZ = p_75642_5_;
        this.speed = p_75642_7_;
        this.update = true;
    }

    public void onUpdateMoveHelper() {
        this.entity.setMoveForward(0.0F);
        if (this.update) {
            this.update = false;
            int var1 = MathHelper.floor_double(this.entity.boundingBox.minY + 0.5D);
            double var2 = this.posX - this.entity.posX;
            double var4 = this.posZ - this.entity.posZ;
            double var6 = this.posY - (double) var1;
            double var8 = var2 * var2 + var6 * var6 + var4 * var4;
            if (var8 >= 2.500000277905201E-7D) {
                float var10 = (float) (Math.atan2(var4, var2) * 180.0D / Math.PI) - 90.0F;
                this.entity.rotationYaw = this.limitAngle(this.entity.rotationYaw, var10, 30.0F);
                this.entity.setAIMoveSpeed((float) (this.speed * this.entity.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getAttributeValue()));
                if (var6 > 0.0D && var2 * var2 + var4 * var4 < 1.0D) {
                    this.entity.getJumpHelper().setJumping();
                }
            }
        }
    }

    private float limitAngle(float p_75639_1_, float p_75639_2_, float p_75639_3_) {
        float var4 = MathHelper.wrapAngleTo180_float(p_75639_2_ - p_75639_1_);
        if (var4 > p_75639_3_) {
            var4 = p_75639_3_;
        }

        if (var4 < -p_75639_3_) {
            var4 = -p_75639_3_;
        }

        return p_75639_1_ + var4;
    }
}
