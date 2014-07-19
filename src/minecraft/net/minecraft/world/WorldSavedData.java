package net.minecraft.world;

import net.minecraft.nbt.NBTTagCompound;

public abstract class WorldSavedData {
    private static final String __OBFID = "CL_00000580";
    public final String mapName;
    private boolean dirty;

    public WorldSavedData(String p_i2141_1_) {
        this.mapName = p_i2141_1_;
    }

    public abstract void readFromNBT(NBTTagCompound p_76184_1_);

    public abstract void writeToNBT(NBTTagCompound p_76187_1_);

    public void markDirty() {
        this.setDirty(true);
    }

    public boolean isDirty() {
        return this.dirty;
    }

    public void setDirty(boolean p_76186_1_) {
        this.dirty = p_76186_1_;
    }
}
