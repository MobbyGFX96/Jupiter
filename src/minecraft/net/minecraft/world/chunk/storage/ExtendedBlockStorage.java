package net.minecraft.world.chunk.storage;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.chunk.NibbleArray;

public class ExtendedBlockStorage {
    private static final String __OBFID = "CL_00000375";
    private int yBase;
    private int blockRefCount;
    private int tickRefCount;
    private byte[] blockLSBArray;
    private NibbleArray blockMSBArray;
    private NibbleArray blockMetadataArray;
    private NibbleArray blocklightArray;
    private NibbleArray skylightArray;

    public ExtendedBlockStorage(int p_i1997_1_, boolean p_i1997_2_) {
        this.yBase = p_i1997_1_;
        this.blockLSBArray = new byte[4096];
        this.blockMetadataArray = new NibbleArray(this.blockLSBArray.length, 4);
        this.blocklightArray = new NibbleArray(this.blockLSBArray.length, 4);
        if (p_i1997_2_) {
            this.skylightArray = new NibbleArray(this.blockLSBArray.length, 4);
        }
    }

    public Block func_150819_a(int p_150819_1_, int p_150819_2_, int p_150819_3_) {
        int var4 = this.blockLSBArray[p_150819_2_ << 8 | p_150819_3_ << 4 | p_150819_1_] & 255;
        if (this.blockMSBArray != null) {
            var4 |= this.blockMSBArray.get(p_150819_1_, p_150819_2_, p_150819_3_) << 8;
        }

        return Block.getBlockById(var4);
    }

    public void func_150818_a(int p_150818_1_, int p_150818_2_, int p_150818_3_, Block p_150818_4_) {
        int var5 = this.blockLSBArray[p_150818_2_ << 8 | p_150818_3_ << 4 | p_150818_1_] & 255;
        if (this.blockMSBArray != null) {
            var5 |= this.blockMSBArray.get(p_150818_1_, p_150818_2_, p_150818_3_) << 8;
        }

        Block var6 = Block.getBlockById(var5);
        if (var6 != Blocks.air) {
            --this.blockRefCount;
            if (var6.getTickRandomly()) {
                --this.tickRefCount;
            }
        }

        if (p_150818_4_ != Blocks.air) {
            ++this.blockRefCount;
            if (p_150818_4_.getTickRandomly()) {
                ++this.tickRefCount;
            }
        }

        int var7 = Block.getIdFromBlock(p_150818_4_);
        this.blockLSBArray[p_150818_2_ << 8 | p_150818_3_ << 4 | p_150818_1_] = (byte) (var7 & 255);
        if (var7 > 255) {
            if (this.blockMSBArray == null) {
                this.blockMSBArray = new NibbleArray(this.blockLSBArray.length, 4);
            }

            this.blockMSBArray.set(p_150818_1_, p_150818_2_, p_150818_3_, (var7 & 3840) >> 8);
        } else if (this.blockMSBArray != null) {
            this.blockMSBArray.set(p_150818_1_, p_150818_2_, p_150818_3_, 0);
        }
    }

    public int getExtBlockMetadata(int p_76665_1_, int p_76665_2_, int p_76665_3_) {
        return this.blockMetadataArray.get(p_76665_1_, p_76665_2_, p_76665_3_);
    }

    public void setExtBlockMetadata(int p_76654_1_, int p_76654_2_, int p_76654_3_, int p_76654_4_) {
        this.blockMetadataArray.set(p_76654_1_, p_76654_2_, p_76654_3_, p_76654_4_);
    }

    public boolean isEmpty() {
        return this.blockRefCount == 0;
    }

    public boolean getNeedsRandomTick() {
        return this.tickRefCount > 0;
    }

    public int getYLocation() {
        return this.yBase;
    }

    public void setExtSkylightValue(int p_76657_1_, int p_76657_2_, int p_76657_3_, int p_76657_4_) {
        this.skylightArray.set(p_76657_1_, p_76657_2_, p_76657_3_, p_76657_4_);
    }

    public int getExtSkylightValue(int p_76670_1_, int p_76670_2_, int p_76670_3_) {
        return this.skylightArray.get(p_76670_1_, p_76670_2_, p_76670_3_);
    }

    public void setExtBlocklightValue(int p_76677_1_, int p_76677_2_, int p_76677_3_, int p_76677_4_) {
        this.blocklightArray.set(p_76677_1_, p_76677_2_, p_76677_3_, p_76677_4_);
    }

    public int getExtBlocklightValue(int p_76674_1_, int p_76674_2_, int p_76674_3_) {
        return this.blocklightArray.get(p_76674_1_, p_76674_2_, p_76674_3_);
    }

    public void removeInvalidBlocks() {
        this.blockRefCount = 0;
        this.tickRefCount = 0;

        for (int var1 = 0; var1 < 16; ++var1) {
            for (int var2 = 0; var2 < 16; ++var2) {
                for (int var3 = 0; var3 < 16; ++var3) {
                    Block var4 = this.func_150819_a(var1, var2, var3);
                    if (var4 != Blocks.air) {
                        ++this.blockRefCount;
                        if (var4.getTickRandomly()) {
                            ++this.tickRefCount;
                        }
                    }
                }
            }
        }
    }

    public byte[] getBlockLSBArray() {
        return this.blockLSBArray;
    }

    public void setBlockLSBArray(byte[] p_76664_1_) {
        this.blockLSBArray = p_76664_1_;
    }

    public void clearMSBArray() {
        this.blockMSBArray = null;
    }

    public NibbleArray getBlockMSBArray() {
        return this.blockMSBArray;
    }

    public void setBlockMSBArray(NibbleArray p_76673_1_) {
        this.blockMSBArray = p_76673_1_;
    }

    public NibbleArray getMetadataArray() {
        return this.blockMetadataArray;
    }

    public NibbleArray getBlocklightArray() {
        return this.blocklightArray;
    }

    public void setBlocklightArray(NibbleArray p_76659_1_) {
        this.blocklightArray = p_76659_1_;
    }

    public NibbleArray getSkylightArray() {
        return this.skylightArray;
    }

    public void setSkylightArray(NibbleArray p_76666_1_) {
        this.skylightArray = p_76666_1_;
    }

    public void setBlockMetadataArray(NibbleArray p_76668_1_) {
        this.blockMetadataArray = p_76668_1_;
    }

    public NibbleArray createBlockMSBArray() {
        this.blockMSBArray = new NibbleArray(this.blockLSBArray.length, 4);
        return this.blockMSBArray;
    }
}
