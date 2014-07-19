package net.minecraft.entity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public abstract class EntityAgeable extends EntityCreature {
    private static final String __OBFID = "CL_00001530";
    private float field_98056_d = -1.0F;
    private float field_98057_e;

    public EntityAgeable(World p_i1578_1_) {
        super(p_i1578_1_);
    }

    public abstract EntityAgeable createChild(EntityAgeable p_90011_1_);

    public boolean interact(EntityPlayer p_70085_1_) {
        ItemStack var2 = p_70085_1_.inventory.getCurrentItem();
        if (var2 != null && var2.getItem() == Items.spawn_egg) {
            if (!this.worldObj.isClient) {
                Class var3 = EntityList.getClassFromID(var2.getItemDamage());
                if (var3 != null && var3.isAssignableFrom(this.getClass())) {
                    EntityAgeable var4 = this.createChild(this);
                    if (var4 != null) {
                        var4.setGrowingAge(-24000);
                        var4.setLocationAndAngles(this.posX, this.posY, this.posZ, 0.0F, 0.0F);
                        this.worldObj.spawnEntityInWorld(var4);
                        if (var2.hasDisplayName()) {
                            var4.setCustomNameTag(var2.getDisplayName());
                        }

                        if (!p_70085_1_.capabilities.isCreativeMode) {
                            --var2.stackSize;
                            if (var2.stackSize <= 0) {
                                p_70085_1_.inventory.setInventorySlotContents(p_70085_1_.inventory.currentItem, (ItemStack) null);
                            }
                        }
                    }
                }
            }

            return true;
        } else {
            return false;
        }
    }

    protected void entityInit() {
        super.entityInit();
        this.dataWatcher.addObject(12, new Integer(0));
    }

    public int getGrowingAge() {
        return this.dataWatcher.getWatchableObjectInt(12);
    }

    public void setGrowingAge(int p_70873_1_) {
        this.dataWatcher.updateObject(12, Integer.valueOf(p_70873_1_));
        this.setScaleForAge(this.isChild());
    }

    public void addGrowth(int p_110195_1_) {
        int var2 = this.getGrowingAge();
        var2 += p_110195_1_ * 20;
        if (var2 > 0) {
            var2 = 0;
        }

        this.setGrowingAge(var2);
    }

    public void writeEntityToNBT(NBTTagCompound p_70014_1_) {
        super.writeEntityToNBT(p_70014_1_);
        p_70014_1_.setInteger("Age", this.getGrowingAge());
    }

    public void readEntityFromNBT(NBTTagCompound p_70037_1_) {
        super.readEntityFromNBT(p_70037_1_);
        this.setGrowingAge(p_70037_1_.getInteger("Age"));
    }

    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (this.worldObj.isClient) {
            this.setScaleForAge(this.isChild());
        } else {
            int var1 = this.getGrowingAge();
            if (var1 < 0) {
                ++var1;
                this.setGrowingAge(var1);
            } else if (var1 > 0) {
                --var1;
                this.setGrowingAge(var1);
            }
        }
    }

    public boolean isChild() {
        return this.getGrowingAge() < 0;
    }

    public void setScaleForAge(boolean p_98054_1_) {
        this.setScale(p_98054_1_ ? 0.5F : 1.0F);
    }

    protected final void setSize(float p_70105_1_, float p_70105_2_) {
        boolean var3 = this.field_98056_d > 0.0F;
        this.field_98056_d = p_70105_1_;
        this.field_98057_e = p_70105_2_;
        if (!var3) {
            this.setScale(1.0F);
        }
    }

    protected final void setScale(float p_98055_1_) {
        super.setSize(this.field_98056_d * p_98055_1_, this.field_98057_e * p_98055_1_);
    }
}
