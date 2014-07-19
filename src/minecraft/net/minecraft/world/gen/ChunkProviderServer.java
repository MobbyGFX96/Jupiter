package net.minecraft.world.gen;

import com.google.common.collect.Lists;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.util.LongHashMap;
import net.minecraft.util.ReportedException;
import net.minecraft.world.*;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.EmptyChunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.chunk.storage.IChunkLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ChunkProviderServer implements IChunkProvider {
    private static final Logger logger = LogManager.getLogger();
    private static final String __OBFID = "CL_00001436";
    public boolean loadChunkOnProvideRequest = true;
    private Set chunksToUnload = Collections.newSetFromMap(new ConcurrentHashMap());
    private Chunk defaultEmptyChunk;
    private IChunkProvider currentChunkProvider;
    private IChunkLoader currentChunkLoader;
    private LongHashMap loadedChunkHashMap = new LongHashMap();
    private List loadedChunks = new ArrayList();
    private WorldServer worldObj;

    public ChunkProviderServer(WorldServer p_i1520_1_, IChunkLoader p_i1520_2_, IChunkProvider p_i1520_3_) {
        this.defaultEmptyChunk = new EmptyChunk(p_i1520_1_, 0, 0);
        this.worldObj = p_i1520_1_;
        this.currentChunkLoader = p_i1520_2_;
        this.currentChunkProvider = p_i1520_3_;
    }

    public boolean chunkExists(int p_73149_1_, int p_73149_2_) {
        return this.loadedChunkHashMap.containsItem(ChunkCoordIntPair.chunkXZ2Int(p_73149_1_, p_73149_2_));
    }

    public List func_152380_a() {
        return this.loadedChunks;
    }

    public void unloadChunksIfNotNearSpawn(int p_73241_1_, int p_73241_2_) {
        if (this.worldObj.provider.canRespawnHere()) {
            ChunkCoordinates var3 = this.worldObj.getSpawnPoint();
            int var4 = p_73241_1_ * 16 + 8 - var3.posX;
            int var5 = p_73241_2_ * 16 + 8 - var3.posZ;
            short var6 = 128;
            if (var4 < -var6 || var4 > var6 || var5 < -var6 || var5 > var6) {
                this.chunksToUnload.add(Long.valueOf(ChunkCoordIntPair.chunkXZ2Int(p_73241_1_, p_73241_2_)));
            }
        } else {
            this.chunksToUnload.add(Long.valueOf(ChunkCoordIntPair.chunkXZ2Int(p_73241_1_, p_73241_2_)));
        }
    }

    public void unloadAllChunks() {
        Iterator var1 = this.loadedChunks.iterator();

        while (var1.hasNext()) {
            Chunk var2 = (Chunk) var1.next();
            this.unloadChunksIfNotNearSpawn(var2.xPosition, var2.zPosition);
        }
    }

    public Chunk loadChunk(int p_73158_1_, int p_73158_2_) {
        long var3 = ChunkCoordIntPair.chunkXZ2Int(p_73158_1_, p_73158_2_);
        this.chunksToUnload.remove(Long.valueOf(var3));
        Chunk var5 = (Chunk) this.loadedChunkHashMap.getValueByKey(var3);
        if (var5 == null) {
            var5 = this.safeLoadChunk(p_73158_1_, p_73158_2_);
            if (var5 == null) {
                if (this.currentChunkProvider == null) {
                    var5 = this.defaultEmptyChunk;
                } else {
                    try {
                        var5 = this.currentChunkProvider.provideChunk(p_73158_1_, p_73158_2_);
                    } catch (Throwable var9) {
                        CrashReport var7 = CrashReport.makeCrashReport(var9, "Exception generating new chunk");
                        CrashReportCategory var8 = var7.makeCategory("Chunk to be generated");
                        var8.addCrashSection("Location", String.format("%d,%d", new Object[]{Integer.valueOf(p_73158_1_), Integer.valueOf(p_73158_2_)}));
                        var8.addCrashSection("Position hash", Long.valueOf(var3));
                        var8.addCrashSection("Generator", this.currentChunkProvider.makeString());
                        throw new ReportedException(var7);
                    }
                }
            }

            this.loadedChunkHashMap.add(var3, var5);
            this.loadedChunks.add(var5);
            var5.onChunkLoad();
            var5.populateChunk(this, this, p_73158_1_, p_73158_2_);
        }

        return var5;
    }

    public Chunk provideChunk(int p_73154_1_, int p_73154_2_) {
        Chunk var3 = (Chunk) this.loadedChunkHashMap.getValueByKey(ChunkCoordIntPair.chunkXZ2Int(p_73154_1_, p_73154_2_));
        return var3 == null ? (!this.worldObj.findingSpawnPoint && !this.loadChunkOnProvideRequest ? this.defaultEmptyChunk : this.loadChunk(p_73154_1_, p_73154_2_)) : var3;
    }

    private Chunk safeLoadChunk(int p_73239_1_, int p_73239_2_) {
        if (this.currentChunkLoader == null) {
            return null;
        } else {
            try {
                Chunk var3 = this.currentChunkLoader.loadChunk(this.worldObj, p_73239_1_, p_73239_2_);
                if (var3 != null) {
                    var3.lastSaveTime = this.worldObj.getTotalWorldTime();
                    if (this.currentChunkProvider != null) {
                        this.currentChunkProvider.recreateStructures(p_73239_1_, p_73239_2_);
                    }
                }

                return var3;
            } catch (Exception var4) {
                logger.error("Couldn\'t load chunk", var4);
                return null;
            }
        }
    }

    private void safeSaveExtraChunkData(Chunk p_73243_1_) {
        if (this.currentChunkLoader != null) {
            try {
                this.currentChunkLoader.saveExtraChunkData(this.worldObj, p_73243_1_);
            } catch (Exception var3) {
                logger.error("Couldn\'t save entities", var3);
            }
        }
    }

    private void safeSaveChunk(Chunk p_73242_1_) {
        if (this.currentChunkLoader != null) {
            try {
                p_73242_1_.lastSaveTime = this.worldObj.getTotalWorldTime();
                this.currentChunkLoader.saveChunk(this.worldObj, p_73242_1_);
            } catch (IOException var3) {
                logger.error("Couldn\'t save chunk", var3);
            } catch (MinecraftException var4) {
                logger.error("Couldn\'t save chunk; already in use by another instance of Minecraft?", var4);
            }
        }
    }

    public void populate(IChunkProvider p_73153_1_, int p_73153_2_, int p_73153_3_) {
        Chunk var4 = this.provideChunk(p_73153_2_, p_73153_3_);
        if (!var4.isTerrainPopulated) {
            var4.func_150809_p();
            if (this.currentChunkProvider != null) {
                this.currentChunkProvider.populate(p_73153_1_, p_73153_2_, p_73153_3_);
                var4.setChunkModified();
            }
        }
    }

    public boolean saveChunks(boolean p_73151_1_, IProgressUpdate p_73151_2_) {
        int var3 = 0;
        ArrayList var4 = Lists.newArrayList(this.loadedChunks);

        for (int var5 = 0; var5 < var4.size(); ++var5) {
            Chunk var6 = (Chunk) var4.get(var5);
            if (p_73151_1_) {
                this.safeSaveExtraChunkData(var6);
            }

            if (var6.needsSaving(p_73151_1_)) {
                this.safeSaveChunk(var6);
                var6.isModified = false;
                ++var3;
                if (var3 == 24 && !p_73151_1_) {
                    return false;
                }
            }
        }

        return true;
    }

    public void saveExtraData() {
        if (this.currentChunkLoader != null) {
            this.currentChunkLoader.saveExtraData();
        }
    }

    public boolean unloadQueuedChunks() {
        if (!this.worldObj.levelSaving) {
            for (int var1 = 0; var1 < 100; ++var1) {
                if (!this.chunksToUnload.isEmpty()) {
                    Long var2 = (Long) this.chunksToUnload.iterator().next();
                    Chunk var3 = (Chunk) this.loadedChunkHashMap.getValueByKey(var2.longValue());
                    if (var3 != null) {
                        var3.onChunkUnload();
                        this.safeSaveChunk(var3);
                        this.safeSaveExtraChunkData(var3);
                        this.loadedChunks.remove(var3);
                    }

                    this.chunksToUnload.remove(var2);
                    this.loadedChunkHashMap.remove(var2.longValue());
                }
            }

            if (this.currentChunkLoader != null) {
                this.currentChunkLoader.chunkTick();
            }
        }

        return this.currentChunkProvider.unloadQueuedChunks();
    }

    public boolean canSave() {
        return !this.worldObj.levelSaving;
    }

    public String makeString() {
        return "ServerChunkCache: " + this.loadedChunkHashMap.getNumHashElements() + " Drop: " + this.chunksToUnload.size();
    }

    public List getPossibleCreatures(EnumCreatureType p_73155_1_, int p_73155_2_, int p_73155_3_, int p_73155_4_) {
        return this.currentChunkProvider.getPossibleCreatures(p_73155_1_, p_73155_2_, p_73155_3_, p_73155_4_);
    }

    public ChunkPosition func_147416_a(World p_147416_1_, String p_147416_2_, int p_147416_3_, int p_147416_4_, int p_147416_5_) {
        return this.currentChunkProvider.func_147416_a(p_147416_1_, p_147416_2_, p_147416_3_, p_147416_4_, p_147416_5_);
    }

    public int getLoadedChunkCount() {
        return this.loadedChunkHashMap.getNumHashElements();
    }

    public void recreateStructures(int p_82695_1_, int p_82695_2_) {
    }
}
