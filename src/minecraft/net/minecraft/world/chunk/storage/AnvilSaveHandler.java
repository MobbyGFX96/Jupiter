package net.minecraft.world.chunk.storage;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldProviderEnd;
import net.minecraft.world.WorldProviderHell;
import net.minecraft.world.storage.SaveHandler;
import net.minecraft.world.storage.ThreadedFileIOBase;
import net.minecraft.world.storage.WorldInfo;

import java.io.File;

public class AnvilSaveHandler extends SaveHandler {
    private static final String __OBFID = "CL_00000581";

    public AnvilSaveHandler(File p_i2142_1_, String p_i2142_2_, boolean p_i2142_3_) {
        super(p_i2142_1_, p_i2142_2_, p_i2142_3_);
    }

    public IChunkLoader getChunkLoader(WorldProvider p_75763_1_) {
        File var2 = this.getWorldDirectory();
        File var3;
        if (p_75763_1_ instanceof WorldProviderHell) {
            var3 = new File(var2, "DIM-1");
            var3.mkdirs();
            return new AnvilChunkLoader(var3);
        } else if (p_75763_1_ instanceof WorldProviderEnd) {
            var3 = new File(var2, "DIM1");
            var3.mkdirs();
            return new AnvilChunkLoader(var3);
        } else {
            return new AnvilChunkLoader(var2);
        }
    }

    public void saveWorldInfoWithPlayer(WorldInfo p_75755_1_, NBTTagCompound p_75755_2_) {
        p_75755_1_.setSaveVersion(19133);
        super.saveWorldInfoWithPlayer(p_75755_1_, p_75755_2_);
    }

    public void flush() {
        try {
            ThreadedFileIOBase.threadedIOInstance.waitForFinish();
        } catch (InterruptedException var2) {
            var2.printStackTrace();
        }

        RegionFileCache.clearRegionFileReferences();
    }
}
