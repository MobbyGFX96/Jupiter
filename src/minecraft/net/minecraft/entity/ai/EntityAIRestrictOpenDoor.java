package net.minecraft.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.util.MathHelper;
import net.minecraft.village.Village;
import net.minecraft.village.VillageDoorInfo;

public class EntityAIRestrictOpenDoor extends EntityAIBase {
    private static final String __OBFID = "CL_00001610";
    private EntityCreature entityObj;
    private VillageDoorInfo frontDoor;

    public EntityAIRestrictOpenDoor(EntityCreature p_i1651_1_) {
        this.entityObj = p_i1651_1_;
    }

    public boolean shouldExecute() {
        if (this.entityObj.worldObj.isDaytime()) {
            return false;
        } else {
            Village var1 = this.entityObj.worldObj.villageCollectionObj.findNearestVillage(MathHelper.floor_double(this.entityObj.posX), MathHelper.floor_double(this.entityObj.posY), MathHelper.floor_double(this.entityObj.posZ), 16);
            if (var1 == null) {
                return false;
            } else {
                this.frontDoor = var1.findNearestDoor(MathHelper.floor_double(this.entityObj.posX), MathHelper.floor_double(this.entityObj.posY), MathHelper.floor_double(this.entityObj.posZ));
                return this.frontDoor == null ? false : (double) this.frontDoor.getInsideDistanceSquare(MathHelper.floor_double(this.entityObj.posX), MathHelper.floor_double(this.entityObj.posY), MathHelper.floor_double(this.entityObj.posZ)) < 2.25D;
            }
        }
    }

    public boolean continueExecuting() {
        return this.entityObj.worldObj.isDaytime() ? false : !this.frontDoor.isDetachedFromVillageFlag && this.frontDoor.isInside(MathHelper.floor_double(this.entityObj.posX), MathHelper.floor_double(this.entityObj.posZ));
    }

    public void startExecuting() {
        this.entityObj.getNavigator().setBreakDoors(false);
        this.entityObj.getNavigator().setEnterDoors(false);
    }

    public void resetTask() {
        this.entityObj.getNavigator().setBreakDoors(true);
        this.entityObj.getNavigator().setEnterDoors(true);
        this.frontDoor = null;
    }

    public void updateTask() {
        this.frontDoor.incrementDoorOpeningRestrictionCounter();
    }
}
