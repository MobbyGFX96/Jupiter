package net.minecraft.entity.monster;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;

public class EntitySlime extends EntityLiving implements IMob {
    private static final String __OBFID = "CL_00001698";
    public float squishAmount;
    public float squishFactor;
    public float prevSquishFactor;
    private int slimeJumpDelay;

    public EntitySlime(World p_i1742_1_) {
        super(p_i1742_1_);
        int var2 = 1 << this.rand.nextInt(3);
        this.yOffset = 0.0F;
        this.slimeJumpDelay = this.rand.nextInt(20) + 10;
        this.setSlimeSize(var2);
    }

    protected void entityInit() {
        super.entityInit();
        this.dataWatcher.addObject(16, new Byte((byte) 1));
    }

    public int getSlimeSize() {
        return this.dataWatcher.getWatchableObjectByte(16);
    }

    protected void setSlimeSize(int p_70799_1_) {
        this.dataWatcher.updateObject(16, new Byte((byte) p_70799_1_));
        this.setSize(0.6F * (float) p_70799_1_, 0.6F * (float) p_70799_1_);
        this.setPosition(this.posX, this.posY, this.posZ);
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue((double) (p_70799_1_ * p_70799_1_));
        this.setHealth(this.getMaxHealth());
        this.experienceValue = p_70799_1_;
    }

    public void writeEntityToNBT(NBTTagCompound p_70014_1_) {
        super.writeEntityToNBT(p_70014_1_);
        p_70014_1_.setInteger("Size", this.getSlimeSize() - 1);
    }

    public void readEntityFromNBT(NBTTagCompound p_70037_1_) {
        super.readEntityFromNBT(p_70037_1_);
        int var2 = p_70037_1_.getInteger("Size");
        if (var2 < 0) {
            var2 = 0;
        }

        this.setSlimeSize(var2 + 1);
    }

    protected String getSlimeParticle() {
        return "slime";
    }

    protected String getJumpSound() {
        return "mob.slime." + (this.getSlimeSize() > 1 ? "big" : "small");
    }

    public void onUpdate() {
        if (!this.worldObj.isClient && this.worldObj.difficultySetting == EnumDifficulty.PEACEFUL && this.getSlimeSize() > 0) {
            this.isDead = true;
        }

        this.squishFactor += (this.squishAmount - this.squishFactor) * 0.5F;
        this.prevSquishFactor = this.squishFactor;
        boolean var1 = this.onGround;
        super.onUpdate();
        int var2;
        if (this.onGround && !var1) {
            var2 = this.getSlimeSize();

            for (int var3 = 0; var3 < var2 * 8; ++var3) {
                float var4 = this.rand.nextFloat() * (float) Math.PI * 2.0F;
                float var5 = this.rand.nextFloat() * 0.5F + 0.5F;
                float var6 = MathHelper.sin(var4) * (float) var2 * 0.5F * var5;
                float var7 = MathHelper.cos(var4) * (float) var2 * 0.5F * var5;
                this.worldObj.spawnParticle(this.getSlimeParticle(), this.posX + (double) var6, this.boundingBox.minY, this.posZ + (double) var7, 0.0D, 0.0D, 0.0D);
            }

            if (this.makesSoundOnLand()) {
                this.playSound(this.getJumpSound(), this.getSoundVolume(), ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F) / 0.8F);
            }

            this.squishAmount = -0.5F;
        } else if (!this.onGround && var1) {
            this.squishAmount = 1.0F;
        }

        this.alterSquishAmount();
        if (this.worldObj.isClient) {
            var2 = this.getSlimeSize();
            this.setSize(0.6F * (float) var2, 0.6F * (float) var2);
        }
    }

    protected void updateEntityActionState() {
        this.despawnEntity();
        EntityPlayer var1 = this.worldObj.getClosestVulnerablePlayerToEntity(this, 16.0D);
        if (var1 != null) {
            this.faceEntity(var1, 10.0F, 20.0F);
        }

        if (this.onGround && this.slimeJumpDelay-- <= 0) {
            this.slimeJumpDelay = this.getJumpDelay();
            if (var1 != null) {
                this.slimeJumpDelay /= 3;
            }

            this.isJumping = true;
            if (this.makesSoundOnJump()) {
                this.playSound(this.getJumpSound(), this.getSoundVolume(), ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F) * 0.8F);
            }

            this.moveStrafing = 1.0F - this.rand.nextFloat() * 2.0F;
            this.moveForward = (float) (1 * this.getSlimeSize());
        } else {
            this.isJumping = false;
            if (this.onGround) {
                this.moveStrafing = this.moveForward = 0.0F;
            }
        }
    }

    protected void alterSquishAmount() {
        this.squishAmount *= 0.6F;
    }

    protected int getJumpDelay() {
        return this.rand.nextInt(20) + 10;
    }

    protected EntitySlime createInstance() {
        return new EntitySlime(this.worldObj);
    }

    public void setDead() {
        int var1 = this.getSlimeSize();
        if (!this.worldObj.isClient && var1 > 1 && this.getHealth() <= 0.0F) {
            int var2 = 2 + this.rand.nextInt(3);

            for (int var3 = 0; var3 < var2; ++var3) {
                float var4 = ((float) (var3 % 2) - 0.5F) * (float) var1 / 4.0F;
                float var5 = ((float) (var3 / 2) - 0.5F) * (float) var1 / 4.0F;
                EntitySlime var6 = this.createInstance();
                var6.setSlimeSize(var1 / 2);
                var6.setLocationAndAngles(this.posX + (double) var4, this.posY + 0.5D, this.posZ + (double) var5, this.rand.nextFloat() * 360.0F, 0.0F);
                this.worldObj.spawnEntityInWorld(var6);
            }
        }

        super.setDead();
    }

    public void onCollideWithPlayer(EntityPlayer p_70100_1_) {
        if (this.canDamagePlayer()) {
            int var2 = this.getSlimeSize();
            if (this.canEntityBeSeen(p_70100_1_) && this.getDistanceSqToEntity(p_70100_1_) < 0.6D * (double) var2 * 0.6D * (double) var2 && p_70100_1_.attackEntityFrom(DamageSource.causeMobDamage(this), (float) this.getAttackStrength())) {
                this.playSound("mob.attack", 1.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
            }
        }
    }

    protected boolean canDamagePlayer() {
        return this.getSlimeSize() > 1;
    }

    protected int getAttackStrength() {
        return this.getSlimeSize();
    }

    protected String getHurtSound() {
        return "mob.slime." + (this.getSlimeSize() > 1 ? "big" : "small");
    }

    protected String getDeathSound() {
        return "mob.slime." + (this.getSlimeSize() > 1 ? "big" : "small");
    }

    protected Item func_146068_u() {
        return this.getSlimeSize() == 1 ? Items.slime_ball : Item.getItemById(0);
    }

    public boolean getCanSpawnHere() {
        Chunk var1 = this.worldObj.getChunkFromBlockCoords(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posZ));
        if (this.worldObj.getWorldInfo().getTerrainType() == WorldType.FLAT && this.rand.nextInt(4) != 1) {
            return false;
        } else {
            if (this.getSlimeSize() == 1 || this.worldObj.difficultySetting != EnumDifficulty.PEACEFUL) {
                BiomeGenBase var2 = this.worldObj.getBiomeGenForCoords(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posZ));
                if (var2 == BiomeGenBase.swampland && this.posY > 50.0D && this.posY < 70.0D && this.rand.nextFloat() < 0.5F && this.rand.nextFloat() < this.worldObj.getCurrentMoonPhaseFactor() && this.worldObj.getBlockLightValue(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ)) <= this.rand.nextInt(8)) {
                    return super.getCanSpawnHere();
                }

                if (this.rand.nextInt(10) == 0 && var1.getRandomWithSeed(987234911L).nextInt(10) == 0 && this.posY < 40.0D) {
                    return super.getCanSpawnHere();
                }
            }

            return false;
        }
    }

    protected float getSoundVolume() {
        return 0.4F * (float) this.getSlimeSize();
    }

    public int getVerticalFaceSpeed() {
        return 0;
    }

    protected boolean makesSoundOnJump() {
        return this.getSlimeSize() > 0;
    }

    protected boolean makesSoundOnLand() {
        return this.getSlimeSize() > 2;
    }
}
