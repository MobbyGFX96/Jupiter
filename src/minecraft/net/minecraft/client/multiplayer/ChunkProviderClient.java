package net.minecraft.client.multiplayer;

import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.util.LongHashMap;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.EmptyChunk;
import net.minecraft.world.chunk.IChunkProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ChunkProviderClient implements IChunkProvider {
    private static final Logger logger = LogManager.getLogger();
    private static final String __OBFID = "CL_00000880";
    private Chunk blankChunk;
    private LongHashMap chunkMapping = new LongHashMap();
    private List chunkListing = new ArrayList();
    private World worldObj;

    public ChunkProviderClient(World p_i1184_1_) {
        this.blankChunk = new EmptyChunk(p_i1184_1_, 0, 0);
        this.worldObj = p_i1184_1_;
    }

    public boolean chunkExists(int p_73149_1_, int p_73149_2_) {
        return true;
    }

    public void unloadChunk(int p_73234_1_, int p_73234_2_) {
        Chunk var3 = this.provideChunk(p_73234_1_, p_73234_2_);
        if (!var3.isEmpty()) {
            var3.onChunkUnload();
        }

        this.chunkMapping.remove(ChunkCoordIntPair.chunkXZ2Int(p_73234_1_, p_73234_2_));
        this.chunkListing.remove(var3);
    }

    public Chunk loadChunk(int p_73158_1_, int p_73158_2_) {
        Chunk var3 = new Chunk(this.worldObj, p_73158_1_, p_73158_2_);
        this.chunkMapping.add(ChunkCoordIntPair.chunkXZ2Int(p_73158_1_, p_73158_2_), var3);
        this.chunkListing.add(var3);
        var3.isChunkLoaded = true;
        return var3;
    }

    public Chunk provideChunk(int p_73154_1_, int p_73154_2_) {
        Chunk var3 = (Chunk) this.chunkMapping.getValueByKey(ChunkCoordIntPair.chunkXZ2Int(p_73154_1_, p_73154_2_));
        return var3 == null ? this.blankChunk : var3;
    }

    public boolean saveChunks(boolean p_73151_1_, IProgressUpdate p_73151_2_) {
        return true;
    }

    public void saveExtraData() {
    }

    public boolean unloadQueuedChunks() {
        long var1 = System.currentTimeMillis();
        Iterator var3 = this.chunkListing.iterator();

        while (var3.hasNext()) {
            Chunk var4 = (Chunk) var3.next();
            var4.func_150804_b(System.currentTimeMillis() - var1 > 5L);
        }

        if (System.currentTimeMillis() - var1 > 100L) {
            logger.info("Warning: Clientside chunk ticking took {} ms", new Object[]{Long.valueOf(System.currentTimeMillis() - var1)});
        }

        return false;
    }

    public boolean canSave() {
        return false;
    }

    public void populate(IChunkProvider p_73153_1_, int p_73153_2_, int p_73153_3_) {
    }

    public String makeString() {
        return "MultiplayerChunkCache: " + this.chunkMapping.getNumHashElements() + ", " + this.chunkListing.size();
    }

    public List getPossibleCreatures(EnumCreatureType p_73155_1_, int p_73155_2_, int p_73155_3_, int p_73155_4_) {
        return null;
    }

    public ChunkPosition func_147416_a(World p_147416_1_, String p_147416_2_, int p_147416_3_, int p_147416_4_, int p_147416_5_) {
        return null;
    }

    public int getLoadedChunkCount() {
        return this.chunkListing.size();
    }

    public void recreateStructures(int p_82695_1_, int p_82695_2_) {
    }
}
