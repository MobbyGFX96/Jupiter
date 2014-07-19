package net.minecraft.entity.monster;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

public class EntityMagmaCube extends EntitySlime {
    private static final String __OBFID = "CL_00001691";

    public EntityMagmaCube(World p_i1737_1_) {
        super(p_i1737_1_);
        this.isImmuneToFire = true;
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.20000000298023224D);
    }

    public boolean getCanSpawnHere() {
        return this.worldObj.difficultySetting != EnumDifficulty.PEACEFUL && this.worldObj.checkNoEntityCollision(this.boundingBox) && this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox).isEmpty() && !this.worldObj.isAnyLiquid(this.boundingBox);
    }

    public int getTotalArmorValue() {
        return this.getSlimeSize() * 3;
    }

    public int getBrightnessForRender(float p_70070_1_) {
        return 15728880;
    }

    public float getBrightness(float p_70013_1_) {
        return 1.0F;
    }

    protected String getSlimeParticle() {
        return "flame";
    }

    protected EntitySlime createInstance() {
        return new EntityMagmaCube(this.worldObj);
    }

    protected Item func_146068_u() {
        return Items.magma_cream;
    }

    protected void dropFewItems(boolean p_70628_1_, int p_70628_2_) {
        Item var3 = this.func_146068_u();
        if (var3 != null && this.getSlimeSize() > 1) {
            int var4 = this.rand.nextInt(4) - 2;
            if (p_70628_2_ > 0) {
                var4 += this.rand.nextInt(p_70628_2_ + 1);
            }

            for (int var5 = 0; var5 < var4; ++var5) {
                this.func_145779_a(var3, 1);
            }
        }
    }

    public boolean isBurning() {
        return false;
    }

    protected int getJumpDelay() {
        return super.getJumpDelay() * 4;
    }

    protected void alterSquishAmount() {
        this.squishAmount *= 0.9F;
    }

    protected void jump() {
        this.motionY = (double) (0.42F + (float) this.getSlimeSize() * 0.1F);
        this.isAirBorne = true;
    }

    protected void fall(float p_70069_1_) {
    }

    protected boolean canDamagePlayer() {
        return true;
    }

    protected int getAttackStrength() {
        return super.getAttackStrength() + 2;
    }

    protected String getJumpSound() {
        return this.getSlimeSize() > 1 ? "mob.magmacube.big" : "mob.magmacube.small";
    }

    public boolean handleLavaMovement() {
        return false;
    }

    protected boolean makesSoundOnLand() {
        return true;
    }
}
