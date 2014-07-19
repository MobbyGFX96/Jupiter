package net.minecraft.entity.ai;

import net.minecraft.entity.EntityLiving;

public class EntityJumpHelper {
    private static final String __OBFID = "CL_00001571";
    private EntityLiving entity;
    private boolean isJumping;

    public EntityJumpHelper(EntityLiving p_i1612_1_) {
        this.entity = p_i1612_1_;
    }

    public void setJumping() {
        this.isJumping = true;
    }

    public void doJump() {
        this.entity.setJumping(this.isJumping);
        this.isJumping = false;
    }
}
