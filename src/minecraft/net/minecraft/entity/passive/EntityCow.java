package net.minecraft.entity.passive;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class EntityCow extends EntityAnimal {
    private static final String __OBFID = "CL_00001640";

    public EntityCow(World p_i1683_1_) {
        super(p_i1683_1_);
        this.setSize(0.9F, 1.3F);
        this.getNavigator().setAvoidsWater(true);
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIPanic(this, 2.0D));
        this.tasks.addTask(2, new EntityAIMate(this, 1.0D));
        this.tasks.addTask(3, new EntityAITempt(this, 1.25D, Items.wheat, false));
        this.tasks.addTask(4, new EntityAIFollowParent(this, 1.25D));
        this.tasks.addTask(5, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
        this.tasks.addTask(7, new EntityAILookIdle(this));
    }

    public boolean isAIEnabled() {
        return true;
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(10.0D);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.20000000298023224D);
    }

    protected String getLivingSound() {
        return "mob.cow.say";
    }

    protected String getHurtSound() {
        return "mob.cow.hurt";
    }

    protected String getDeathSound() {
        return "mob.cow.hurt";
    }

    protected void func_145780_a(int p_145780_1_, int p_145780_2_, int p_145780_3_, Block p_145780_4_) {
        this.playSound("mob.cow.step", 0.15F, 1.0F);
    }

    protected float getSoundVolume() {
        return 0.4F;
    }

    protected Item func_146068_u() {
        return Items.leather;
    }

    protected void dropFewItems(boolean p_70628_1_, int p_70628_2_) {
        int var3 = this.rand.nextInt(3) + this.rand.nextInt(1 + p_70628_2_);

        int var4;
        for (var4 = 0; var4 < var3; ++var4) {
            this.func_145779_a(Items.leather, 1);
        }

        var3 = this.rand.nextInt(3) + 1 + this.rand.nextInt(1 + p_70628_2_);

        for (var4 = 0; var4 < var3; ++var4) {
            if (this.isBurning()) {
                this.func_145779_a(Items.cooked_beef, 1);
            } else {
                this.func_145779_a(Items.beef, 1);
            }
        }
    }

    public boolean interact(EntityPlayer p_70085_1_) {
        ItemStack var2 = p_70085_1_.inventory.getCurrentItem();
        if (var2 != null && var2.getItem() == Items.bucket && !p_70085_1_.capabilities.isCreativeMode) {
            if (var2.stackSize-- == 1) {
                p_70085_1_.inventory.setInventorySlotContents(p_70085_1_.inventory.currentItem, new ItemStack(Items.milk_bucket));
            } else if (!p_70085_1_.inventory.addItemStackToInventory(new ItemStack(Items.milk_bucket))) {
                p_70085_1_.dropPlayerItemWithRandomChoice(new ItemStack(Items.milk_bucket, 1, 0), false);
            }

            return true;
        } else {
            return super.interact(p_70085_1_);
        }
    }

    public EntityCow createChild(EntityAgeable p_90011_1_) {
        return new EntityCow(this.worldObj);
    }
}
