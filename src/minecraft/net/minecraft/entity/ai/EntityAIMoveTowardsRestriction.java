package net.minecraft.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.Vec3;

public class EntityAIMoveTowardsRestriction extends EntityAIBase {
    private static final String __OBFID = "CL_00001598";
    private EntityCreature theEntity;
    private double movePosX;
    private double movePosY;
    private double movePosZ;
    private double movementSpeed;

    public EntityAIMoveTowardsRestriction(EntityCreature p_i2347_1_, double p_i2347_2_) {
        this.theEntity = p_i2347_1_;
        this.movementSpeed = p_i2347_2_;
        this.setMutexBits(1);
    }

    public boolean shouldExecute() {
        if (this.theEntity.isWithinHomeDistanceCurrentPosition()) {
            return false;
        } else {
            ChunkCoordinates var1 = this.theEntity.getHomePosition();
            Vec3 var2 = RandomPositionGenerator.findRandomTargetBlockTowards(this.theEntity, 16, 7, Vec3.createVectorHelper((double) var1.posX, (double) var1.posY, (double) var1.posZ));
            if (var2 == null) {
                return false;
            } else {
                this.movePosX = var2.xCoord;
                this.movePosY = var2.yCoord;
                this.movePosZ = var2.zCoord;
                return true;
            }
        }
    }

    public boolean continueExecuting() {
        return !this.theEntity.getNavigator().noPath();
    }

    public void startExecuting() {
        this.theEntity.getNavigator().tryMoveToXYZ(this.movePosX, this.movePosY, this.movePosZ, this.movementSpeed);
    }
}
