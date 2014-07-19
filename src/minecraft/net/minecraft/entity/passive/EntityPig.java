package net.minecraft.entity.passive;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.AchievementList;
import net.minecraft.world.World;

public class EntityPig extends EntityAnimal {
    private static final String __OBFID = "CL_00001647";
    private final EntityAIControlledByPlayer aiControlledByPlayer;

    public EntityPig(World p_i1689_1_) {
        super(p_i1689_1_);
        this.setSize(0.9F, 0.9F);
        this.getNavigator().setAvoidsWater(true);
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIPanic(this, 1.25D));
        this.tasks.addTask(2, this.aiControlledByPlayer = new EntityAIControlledByPlayer(this, 0.3F));
        this.tasks.addTask(3, new EntityAIMate(this, 1.0D));
        this.tasks.addTask(4, new EntityAITempt(this, 1.2D, Items.carrot_on_a_stick, false));
        this.tasks.addTask(4, new EntityAITempt(this, 1.2D, Items.carrot, false));
        this.tasks.addTask(5, new EntityAIFollowParent(this, 1.1D));
        this.tasks.addTask(6, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));
    }

    public boolean isAIEnabled() {
        return true;
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(10.0D);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.25D);
    }

    protected void updateAITasks() {
        super.updateAITasks();
    }

    public boolean canBeSteered() {
        ItemStack var1 = ((EntityPlayer) this.riddenByEntity).getHeldItem();
        return var1 != null && var1.getItem() == Items.carrot_on_a_stick;
    }

    protected void entityInit() {
        super.entityInit();
        this.dataWatcher.addObject(16, Byte.valueOf((byte) 0));
    }

    public void writeEntityToNBT(NBTTagCompound p_70014_1_) {
        super.writeEntityToNBT(p_70014_1_);
        p_70014_1_.setBoolean("Saddle", this.getSaddled());
    }

    public void readEntityFromNBT(NBTTagCompound p_70037_1_) {
        super.readEntityFromNBT(p_70037_1_);
        this.setSaddled(p_70037_1_.getBoolean("Saddle"));
    }

    protected String getLivingSound() {
        return "mob.pig.say";
    }

    protected String getHurtSound() {
        return "mob.pig.say";
    }

    protected String getDeathSound() {
        return "mob.pig.death";
    }

    protected void func_145780_a(int p_145780_1_, int p_145780_2_, int p_145780_3_, Block p_145780_4_) {
        this.playSound("mob.pig.step", 0.15F, 1.0F);
    }

    public boolean interact(EntityPlayer p_70085_1_) {
        if (super.interact(p_70085_1_)) {
            return true;
        } else if (this.getSaddled() && !this.worldObj.isClient && (this.riddenByEntity == null || this.riddenByEntity == p_70085_1_)) {
            p_70085_1_.mountEntity(this);
            return true;
        } else {
            return false;
        }
    }

    protected Item func_146068_u() {
        return this.isBurning() ? Items.cooked_porkchop : Items.porkchop;
    }

    protected void dropFewItems(boolean p_70628_1_, int p_70628_2_) {
        int var3 = this.rand.nextInt(3) + 1 + this.rand.nextInt(1 + p_70628_2_);

        for (int var4 = 0; var4 < var3; ++var4) {
            if (this.isBurning()) {
                this.func_145779_a(Items.cooked_porkchop, 1);
            } else {
                this.func_145779_a(Items.porkchop, 1);
            }
        }

        if (this.getSaddled()) {
            this.func_145779_a(Items.saddle, 1);
        }
    }

    public boolean getSaddled() {
        return (this.dataWatcher.getWatchableObjectByte(16) & 1) != 0;
    }

    public void setSaddled(boolean p_70900_1_) {
        if (p_70900_1_) {
            this.dataWatcher.updateObject(16, Byte.valueOf((byte) 1));
        } else {
            this.dataWatcher.updateObject(16, Byte.valueOf((byte) 0));
        }
    }

    public void onStruckByLightning(EntityLightningBolt p_70077_1_) {
        if (!this.worldObj.isClient) {
            EntityPigZombie var2 = new EntityPigZombie(this.worldObj);
            var2.setCurrentItemOrArmor(0, new ItemStack(Items.golden_sword));
            var2.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
            this.worldObj.spawnEntityInWorld(var2);
            this.setDead();
        }
    }

    protected void fall(float p_70069_1_) {
        super.fall(p_70069_1_);
        if (p_70069_1_ > 5.0F && this.riddenByEntity instanceof EntityPlayer) {
            ((EntityPlayer) this.riddenByEntity).triggerAchievement(AchievementList.flyPig);
        }
    }

    public EntityPig createChild(EntityAgeable p_90011_1_) {
        return new EntityPig(this.worldObj);
    }

    public boolean isBreedingItem(ItemStack p_70877_1_) {
        return p_70877_1_ != null && p_70877_1_.getItem() == Items.carrot;
    }

    public EntityAIControlledByPlayer getAIControlledByPlayer() {
        return this.aiControlledByPlayer;
    }
}
