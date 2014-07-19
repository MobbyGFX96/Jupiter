package net.minecraft.entity.ai;

import net.minecraft.entity.EntityLiving;

public class EntityAISwimming extends EntityAIBase {
    private static final String __OBFID = "CL_00001584";
    private EntityLiving theEntity;

    public EntityAISwimming(EntityLiving p_i1624_1_) {
        this.theEntity = p_i1624_1_;
        this.setMutexBits(4);
        p_i1624_1_.getNavigator().setCanSwim(true);
    }

    public boolean shouldExecute() {
        return this.theEntity.isInWater() || this.theEntity.handleLavaMovement();
    }

    public void updateTask() {
        if (this.theEntity.getRNG().nextFloat() < 0.8F) {
            this.theEntity.getJumpHelper().setJumping();
        }
    }
}
