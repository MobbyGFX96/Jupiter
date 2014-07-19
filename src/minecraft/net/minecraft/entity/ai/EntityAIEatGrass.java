package net.minecraft.entity.ai;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLiving;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityAIEatGrass extends EntityAIBase {
    private static final String __OBFID = "CL_00001582";
    int field_151502_a;
    private EntityLiving field_151500_b;
    private World field_151501_c;

    public EntityAIEatGrass(EntityLiving p_i45314_1_) {
        this.field_151500_b = p_i45314_1_;
        this.field_151501_c = p_i45314_1_.worldObj;
        this.setMutexBits(7);
    }

    public boolean shouldExecute() {
        if (this.field_151500_b.getRNG().nextInt(this.field_151500_b.isChild() ? 50 : 1000) != 0) {
            return false;
        } else {
            int var1 = MathHelper.floor_double(this.field_151500_b.posX);
            int var2 = MathHelper.floor_double(this.field_151500_b.posY);
            int var3 = MathHelper.floor_double(this.field_151500_b.posZ);
            return this.field_151501_c.getBlock(var1, var2, var3) == Blocks.tallgrass && this.field_151501_c.getBlockMetadata(var1, var2, var3) == 1 ? true : this.field_151501_c.getBlock(var1, var2 - 1, var3) == Blocks.grass;
        }
    }

    public void startExecuting() {
        this.field_151502_a = 40;
        this.field_151501_c.setEntityState(this.field_151500_b, (byte) 10);
        this.field_151500_b.getNavigator().clearPathEntity();
    }

    public void resetTask() {
        this.field_151502_a = 0;
    }

    public boolean continueExecuting() {
        return this.field_151502_a > 0;
    }

    public int func_151499_f() {
        return this.field_151502_a;
    }

    public void updateTask() {
        this.field_151502_a = Math.max(0, this.field_151502_a - 1);
        if (this.field_151502_a == 4) {
            int var1 = MathHelper.floor_double(this.field_151500_b.posX);
            int var2 = MathHelper.floor_double(this.field_151500_b.posY);
            int var3 = MathHelper.floor_double(this.field_151500_b.posZ);
            if (this.field_151501_c.getBlock(var1, var2, var3) == Blocks.tallgrass) {
                if (this.field_151501_c.getGameRules().getGameRuleBooleanValue("mobGriefing")) {
                    this.field_151501_c.func_147480_a(var1, var2, var3, false);
                }

                this.field_151500_b.eatGrassBonus();
            } else if (this.field_151501_c.getBlock(var1, var2 - 1, var3) == Blocks.grass) {
                if (this.field_151501_c.getGameRules().getGameRuleBooleanValue("mobGriefing")) {
                    this.field_151501_c.playAuxSFX(2001, var1, var2 - 1, var3, Block.getIdFromBlock(Blocks.grass));
                    this.field_151501_c.setBlock(var1, var2 - 1, var3, Blocks.dirt, 0, 2);
                }

                this.field_151500_b.eatGrassBonus();
            }
        }
    }
}
