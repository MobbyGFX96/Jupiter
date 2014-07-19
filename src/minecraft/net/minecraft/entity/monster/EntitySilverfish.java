package net.minecraft.entity.monster;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSilverfish;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.Facing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.ImmutablePair;

public class EntitySilverfish extends EntityMob {
    private static final String __OBFID = "CL_00001696";
    private int allySummonCooldown;

    public EntitySilverfish(World p_i1740_1_) {
        super(p_i1740_1_);
        this.setSize(0.3F, 0.7F);
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(8.0D);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.6000000238418579D);
        this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(1.0D);
    }

    protected boolean canTriggerWalking() {
        return false;
    }

    protected Entity findPlayerToAttack() {
        double var1 = 8.0D;
        return this.worldObj.getClosestVulnerablePlayerToEntity(this, var1);
    }

    protected String getLivingSound() {
        return "mob.silverfish.say";
    }

    protected String getHurtSound() {
        return "mob.silverfish.hit";
    }

    protected String getDeathSound() {
        return "mob.silverfish.kill";
    }

    public boolean attackEntityFrom(DamageSource p_70097_1_, float p_70097_2_) {
        if (this.isEntityInvulnerable()) {
            return false;
        } else {
            if (this.allySummonCooldown <= 0 && (p_70097_1_ instanceof EntityDamageSource || p_70097_1_ == DamageSource.magic)) {
                this.allySummonCooldown = 20;
            }

            return super.attackEntityFrom(p_70097_1_, p_70097_2_);
        }
    }

    protected void attackEntity(Entity p_70785_1_, float p_70785_2_) {
        if (this.attackTime <= 0 && p_70785_2_ < 1.2F && p_70785_1_.boundingBox.maxY > this.boundingBox.minY && p_70785_1_.boundingBox.minY < this.boundingBox.maxY) {
            this.attackTime = 20;
            this.attackEntityAsMob(p_70785_1_);
        }
    }

    protected void func_145780_a(int p_145780_1_, int p_145780_2_, int p_145780_3_, Block p_145780_4_) {
        this.playSound("mob.silverfish.step", 0.15F, 1.0F);
    }

    protected Item func_146068_u() {
        return Item.getItemById(0);
    }

    public void onUpdate() {
        this.renderYawOffset = this.rotationYaw;
        super.onUpdate();
    }

    protected void updateEntityActionState() {
        super.updateEntityActionState();
        if (!this.worldObj.isClient) {
            int var1;
            int var2;
            int var3;
            int var6;
            if (this.allySummonCooldown > 0) {
                --this.allySummonCooldown;
                if (this.allySummonCooldown == 0) {
                    var1 = MathHelper.floor_double(this.posX);
                    var2 = MathHelper.floor_double(this.posY);
                    var3 = MathHelper.floor_double(this.posZ);
                    boolean var4 = false;

                    for (int var5 = 0; !var4 && var5 <= 5 && var5 >= -5; var5 = var5 <= 0 ? 1 - var5 : 0 - var5) {
                        for (var6 = 0; !var4 && var6 <= 10 && var6 >= -10; var6 = var6 <= 0 ? 1 - var6 : 0 - var6) {
                            for (int var7 = 0; !var4 && var7 <= 10 && var7 >= -10; var7 = var7 <= 0 ? 1 - var7 : 0 - var7) {
                                if (this.worldObj.getBlock(var1 + var6, var2 + var5, var3 + var7) == Blocks.monster_egg) {
                                    if (!this.worldObj.getGameRules().getGameRuleBooleanValue("mobGriefing")) {
                                        int var8 = this.worldObj.getBlockMetadata(var1 + var6, var2 + var5, var3 + var7);
                                        ImmutablePair var9 = BlockSilverfish.func_150197_b(var8);
                                        this.worldObj.setBlock(var1 + var6, var2 + var5, var3 + var7, (Block) var9.getLeft(), ((Integer) var9.getRight()).intValue(), 3);
                                    } else {
                                        this.worldObj.func_147480_a(var1 + var6, var2 + var5, var3 + var7, false);
                                    }

                                    Blocks.monster_egg.onBlockDestroyedByPlayer(this.worldObj, var1 + var6, var2 + var5, var3 + var7, 0);
                                    if (this.rand.nextBoolean()) {
                                        var4 = true;
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if (this.entityToAttack == null && !this.hasPath()) {
                var1 = MathHelper.floor_double(this.posX);
                var2 = MathHelper.floor_double(this.posY + 0.5D);
                var3 = MathHelper.floor_double(this.posZ);
                int var10 = this.rand.nextInt(6);
                Block var11 = this.worldObj.getBlock(var1 + Facing.offsetsXForSide[var10], var2 + Facing.offsetsYForSide[var10], var3 + Facing.offsetsZForSide[var10]);
                var6 = this.worldObj.getBlockMetadata(var1 + Facing.offsetsXForSide[var10], var2 + Facing.offsetsYForSide[var10], var3 + Facing.offsetsZForSide[var10]);
                if (BlockSilverfish.func_150196_a(var11)) {
                    this.worldObj.setBlock(var1 + Facing.offsetsXForSide[var10], var2 + Facing.offsetsYForSide[var10], var3 + Facing.offsetsZForSide[var10], Blocks.monster_egg, BlockSilverfish.func_150195_a(var11, var6), 3);
                    this.spawnExplosionParticle();
                    this.setDead();
                } else {
                    this.updateWanderPath();
                }
            } else if (this.entityToAttack != null && !this.hasPath()) {
                this.entityToAttack = null;
            }
        }
    }

    public float getBlockPathWeight(int p_70783_1_, int p_70783_2_, int p_70783_3_) {
        return this.worldObj.getBlock(p_70783_1_, p_70783_2_ - 1, p_70783_3_) == Blocks.stone ? 10.0F : super.getBlockPathWeight(p_70783_1_, p_70783_2_, p_70783_3_);
    }

    protected boolean isValidLightLevel() {
        return true;
    }

    public boolean getCanSpawnHere() {
        if (super.getCanSpawnHere()) {
            EntityPlayer var1 = this.worldObj.getClosestPlayerToEntity(this, 5.0D);
            return var1 == null;
        } else {
            return false;
        }
    }

    public EnumCreatureAttribute getCreatureAttribute() {
        return EnumCreatureAttribute.ARTHROPOD;
    }
}
