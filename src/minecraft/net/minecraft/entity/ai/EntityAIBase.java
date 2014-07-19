package net.minecraft.entity.ai;

public abstract class EntityAIBase {
    private static final String __OBFID = "CL_00001587";
    private int mutexBits;

    public abstract boolean shouldExecute();

    public boolean continueExecuting() {
        return this.shouldExecute();
    }

    public boolean isInterruptible() {
        return true;
    }

    public void startExecuting() {
    }

    public void resetTask() {
    }

    public void updateTask() {
    }

    public int getMutexBits() {
        return this.mutexBits;
    }

    public void setMutexBits(int p_75248_1_) {
        this.mutexBits = p_75248_1_;
    }
}
