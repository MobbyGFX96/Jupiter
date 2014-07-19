package net.minecraft.world.storage;

import net.minecraft.client.AnvilConverterException;
import net.minecraft.util.IProgressUpdate;

import java.util.List;

public interface ISaveFormat {
    String func_154333_a();

    ISaveHandler getSaveLoader(String p_75804_1_, boolean p_75804_2_);

    List getSaveList() throws AnvilConverterException;

    void flushCache();

    WorldInfo getWorldInfo(String p_75803_1_);

    boolean func_154335_d(String p_154335_1_);

    boolean deleteWorldDirectory(String p_75802_1_);

    void renameWorld(String p_75806_1_, String p_75806_2_);

    boolean func_154334_a(String p_154334_1_);

    boolean isOldMapFormat(String p_75801_1_);

    boolean convertMapFormat(String p_75805_1_, IProgressUpdate p_75805_2_);

    boolean canLoadWorld(String p_90033_1_);
}
