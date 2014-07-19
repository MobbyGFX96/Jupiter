package net.minecraft.entity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

import java.util.Iterator;
import java.util.List;

public class EntityLeashKnot extends EntityHanging {
    private static final String __OBFID = "CL_00001548";

    public EntityLeashKnot(World p_i1592_1_) {
        super(p_i1592_1_);
    }

    public EntityLeashKnot(World p_i1593_1_, int p_i1593_2_, int p_i1593_3_, int p_i1593_4_) {
        super(p_i1593_1_, p_i1593_2_, p_i1593_3_, p_i1593_4_, 0);
        this.setPosition((double) p_i1593_2_ + 0.5D, (double) p_i1593_3_ + 0.5D, (double) p_i1593_4_ + 0.5D);
    }

    public static EntityLeashKnot func_110129_a(World p_110129_0_, int p_110129_1_, int p_110129_2_, int p_110129_3_) {
        EntityLeashKnot var4 = new EntityLeashKnot(p_110129_0_, p_110129_1_, p_110129_2_, p_110129_3_);
        var4.forceSpawn = true;
        p_110129_0_.spawnEntityInWorld(var4);
        return var4;
    }

    public static EntityLeashKnot getKnotForBlock(World p_110130_0_, int p_110130_1_, int p_110130_2_, int p_110130_3_) {
        List var4 = p_110130_0_.getEntitiesWithinAABB(EntityLeashKnot.class, AxisAlignedBB.getBoundingBox((double) p_110130_1_ - 1.0D, (double) p_110130_2_ - 1.0D, (double) p_110130_3_ - 1.0D, (double) p_110130_1_ + 1.0D, (double) p_110130_2_ + 1.0D, (double) p_110130_3_ + 1.0D));
        if (var4 != null) {
            Iterator var5 = var4.iterator();

            while (var5.hasNext()) {
                EntityLeashKnot var6 = (EntityLeashKnot) var5.next();
                if (var6.field_146063_b == p_110130_1_ && var6.field_146064_c == p_110130_2_ && var6.field_146062_d == p_110130_3_) {
                    return var6;
                }
            }
        }

        return null;
    }

    protected void entityInit() {
        super.entityInit();
    }

    public void setDirection(int p_82328_1_) {
    }

    public int getWidthPixels() {
        return 9;
    }

    public int getHeightPixels() {
        return 9;
    }

    public boolean isInRangeToRenderDist(double p_70112_1_) {
        return p_70112_1_ < 1024.0D;
    }

    public void onBroken(Entity p_110128_1_) {
    }

    public boolean writeToNBTOptional(NBTTagCompound p_70039_1_) {
        return false;
    }

    public void writeEntityToNBT(NBTTagCompound p_70014_1_) {
    }

    public void readEntityFromNBT(NBTTagCompound p_70037_1_) {
    }

    public boolean interactFirst(EntityPlayer p_130002_1_) {
        ItemStack var2 = p_130002_1_.getHeldItem();
        boolean var3 = false;
        double var4;
        List var6;
        Iterator var7;
        EntityLiving var8;
        if (var2 != null && var2.getItem() == Items.lead && !this.worldObj.isClient) {
            var4 = 7.0D;
            var6 = this.worldObj.getEntitiesWithinAABB(EntityLiving.class, AxisAlignedBB.getBoundingBox(this.posX - var4, this.posY - var4, this.posZ - var4, this.posX + var4, this.posY + var4, this.posZ + var4));
            if (var6 != null) {
                var7 = var6.iterator();

                while (var7.hasNext()) {
                    var8 = (EntityLiving) var7.next();
                    if (var8.getLeashed() && var8.getLeashedToEntity() == p_130002_1_) {
                        var8.setLeashedToEntity(this, true);
                        var3 = true;
                    }
                }
            }
        }

        if (!this.worldObj.isClient && !var3) {
            this.setDead();
            if (p_130002_1_.capabilities.isCreativeMode) {
                var4 = 7.0D;
                var6 = this.worldObj.getEntitiesWithinAABB(EntityLiving.class, AxisAlignedBB.getBoundingBox(this.posX - var4, this.posY - var4, this.posZ - var4, this.posX + var4, this.posY + var4, this.posZ + var4));
                if (var6 != null) {
                    var7 = var6.iterator();

                    while (var7.hasNext()) {
                        var8 = (EntityLiving) var7.next();
                        if (var8.getLeashed() && var8.getLeashedToEntity() == this) {
                            var8.clearLeashed(true, false);
                        }
                    }
                }
            }
        }

        return true;
    }

    public boolean onValidSurface() {
        return this.worldObj.getBlock(this.field_146063_b, this.field_146064_c, this.field_146062_d).getRenderType() == 11;
    }
}
