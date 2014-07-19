package net.minecraft.entity.ai;

import net.minecraft.entity.passive.EntityTameable;

public class EntityAITargetNonTamed extends EntityAINearestAttackableTarget {
    private static final String __OBFID = "CL_00001623";
    private EntityTameable theTameable;

    public EntityAITargetNonTamed(EntityTameable p_i1666_1_, Class p_i1666_2_, int p_i1666_3_, boolean p_i1666_4_) {
        super(p_i1666_1_, p_i1666_2_, p_i1666_3_, p_i1666_4_);
        this.theTameable = p_i1666_1_;
    }

    public boolean shouldExecute() {
        return !this.theTameable.isTamed() && super.shouldExecute();
    }
}
