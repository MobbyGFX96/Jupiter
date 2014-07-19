package net.minecraft.entity.monster;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;

public abstract class EntityMob extends EntityCreature implements IMob {
    private static final String __OBFID = "CL_00001692";

    public EntityMob(World p_i1738_1_) {
        super(p_i1738_1_);
        this.experienceValue = 5;
    }

    public void onLivingUpdate() {
        this.updateArmSwingProgress();
        float var1 = this.getBrightness(1.0F);
        if (var1 > 0.5F) {
            this.entityAge += 2;
        }

        super.onLivingUpdate();
    }

    public void onUpdate() {
        super.onUpdate();
        if (!this.worldObj.isClient && this.worldObj.difficultySetting == EnumDifficulty.PEACEFUL) {
            this.setDead();
        }
    }

    protected String getSwimSound() {
        return "game.hostile.swim";
    }

    protected String getSplashSound() {
        return "game.hostile.swim.splash";
    }

    protected Entity findPlayerToAttack() {
        EntityPlayer var1 = this.worldObj.getClosestVulnerablePlayerToEntity(this, 16.0D);
        return var1 != null && this.canEntityBeSeen(var1) ? var1 : null;
    }

    public boolean attackEntityFrom(DamageSource p_70097_1_, float p_70097_2_) {
        if (this.isEntityInvulnerable()) {
            return false;
        } else if (super.attackEntityFrom(p_70097_1_, p_70097_2_)) {
            Entity var3 = p_70097_1_.getEntity();
            if (this.riddenByEntity != var3 && this.ridingEntity != var3) {
                if (var3 != this) {
                    this.entityToAttack = var3;
                }

                return true;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    protected String getHurtSound() {
        return "game.hostile.hurt";
    }

    protected String getDeathSound() {
        return "game.hostile.die";
    }

    protected String func_146067_o(int p_146067_1_) {
        return p_146067_1_ > 4 ? "game.hostile.hurt.fall.big" : "game.hostile.hurt.fall.small";
    }

    public boolean attackEntityAsMob(Entity p_70652_1_) {
        float var2 = (float) this.getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue();
        int var3 = 0;
        if (p_70652_1_ instanceof EntityLivingBase) {
            var2 += EnchantmentHelper.getEnchantmentModifierLiving(this, (EntityLivingBase) p_70652_1_);
            var3 += EnchantmentHelper.getKnockbackModifier(this, (EntityLivingBase) p_70652_1_);
        }

        boolean var4 = p_70652_1_.attackEntityFrom(DamageSource.causeMobDamage(this), var2);
        if (var4) {
            if (var3 > 0) {
                p_70652_1_.addVelocity((double) (-MathHelper.sin(this.rotationYaw * (float) Math.PI / 180.0F) * (float) var3 * 0.5F), 0.1D, (double) (MathHelper.cos(this.rotationYaw * (float) Math.PI / 180.0F) * (float) var3 * 0.5F));
                this.motionX *= 0.6D;
                this.motionZ *= 0.6D;
            }

            int var5 = EnchantmentHelper.getFireAspectModifier(this);
            if (var5 > 0) {
                p_70652_1_.setFire(var5 * 4);
            }

            if (p_70652_1_ instanceof EntityLivingBase) {
                EnchantmentHelper.func_151384_a((EntityLivingBase) p_70652_1_, this);
            }

            EnchantmentHelper.func_151385_b(this, p_70652_1_);
        }

        return var4;
    }

    protected void attackEntity(Entity p_70785_1_, float p_70785_2_) {
        if (this.attackTime <= 0 && p_70785_2_ < 2.0F && p_70785_1_.boundingBox.maxY > this.boundingBox.minY && p_70785_1_.boundingBox.minY < this.boundingBox.maxY) {
            this.attackTime = 20;
            this.attackEntityAsMob(p_70785_1_);
        }
    }

    public float getBlockPathWeight(int p_70783_1_, int p_70783_2_, int p_70783_3_) {
        return 0.5F - this.worldObj.getLightBrightness(p_70783_1_, p_70783_2_, p_70783_3_);
    }

    protected boolean isValidLightLevel() {
        int var1 = MathHelper.floor_double(this.posX);
        int var2 = MathHelper.floor_double(this.boundingBox.minY);
        int var3 = MathHelper.floor_double(this.posZ);
        if (this.worldObj.getSavedLightValue(EnumSkyBlock.Sky, var1, var2, var3) > this.rand.nextInt(32)) {
            return false;
        } else {
            int var4 = this.worldObj.getBlockLightValue(var1, var2, var3);
            if (this.worldObj.isThundering()) {
                int var5 = this.worldObj.skylightSubtracted;
                this.worldObj.skylightSubtracted = 10;
                var4 = this.worldObj.getBlockLightValue(var1, var2, var3);
                this.worldObj.skylightSubtracted = var5;
            }

            return var4 <= this.rand.nextInt(8);
        }
    }

    public boolean getCanSpawnHere() {
        return this.worldObj.difficultySetting != EnumDifficulty.PEACEFUL && this.isValidLightLevel() && super.getCanSpawnHere();
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.attackDamage);
    }

    protected boolean func_146066_aG() {
        return true;
    }
}
