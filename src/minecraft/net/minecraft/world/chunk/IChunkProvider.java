package net.minecraft.world.chunk;

import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;

import java.util.List;

public interface IChunkProvider {
    boolean chunkExists(int p_73149_1_, int p_73149_2_);

    Chunk provideChunk(int p_73154_1_, int p_73154_2_);

    Chunk loadChunk(int p_73158_1_, int p_73158_2_);

    void populate(IChunkProvider p_73153_1_, int p_73153_2_, int p_73153_3_);

    boolean saveChunks(boolean p_73151_1_, IProgressUpdate p_73151_2_);

    boolean unloadQueuedChunks();

    boolean canSave();

    String makeString();

    List getPossibleCreatures(EnumCreatureType p_73155_1_, int p_73155_2_, int p_73155_3_, int p_73155_4_);

    ChunkPosition func_147416_a(World p_147416_1_, String p_147416_2_, int p_147416_3_, int p_147416_4_, int p_147416_5_);

    int getLoadedChunkCount();

    void recreateStructures(int p_82695_1_, int p_82695_2_);

    void saveExtraData();
}
