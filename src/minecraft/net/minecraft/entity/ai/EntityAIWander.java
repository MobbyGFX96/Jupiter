package net.minecraft.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.util.Vec3;

public class EntityAIWander extends EntityAIBase {
    private static final String __OBFID = "CL_00001608";
    private EntityCreature entity;
    private double xPosition;
    private double yPosition;
    private double zPosition;
    private double speed;

    public EntityAIWander(EntityCreature p_i1648_1_, double p_i1648_2_) {
        this.entity = p_i1648_1_;
        this.speed = p_i1648_2_;
        this.setMutexBits(1);
    }

    public boolean shouldExecute() {
        if (this.entity.getAge() >= 100) {
            return false;
        } else if (this.entity.getRNG().nextInt(120) != 0) {
            return false;
        } else {
            Vec3 var1 = RandomPositionGenerator.findRandomTarget(this.entity, 10, 7);
            if (var1 == null) {
                return false;
            } else {
                this.xPosition = var1.xCoord;
                this.yPosition = var1.yCoord;
                this.zPosition = var1.zCoord;
                return true;
            }
        }
    }

    public boolean continueExecuting() {
        return !this.entity.getNavigator().noPath();
    }

    public void startExecuting() {
        this.entity.getNavigator().tryMoveToXYZ(this.xPosition, this.yPosition, this.zPosition, this.speed);
    }
}
