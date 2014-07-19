package net.minecraft.world.gen.layer;

public class GenLayerRiverInit extends GenLayer {
    private static final String __OBFID = "CL_00000565";

    public GenLayerRiverInit(long p_i2127_1_, GenLayer p_i2127_3_) {
        super(p_i2127_1_);
        this.parent = p_i2127_3_;
    }

    public int[] getInts(int p_75904_1_, int p_75904_2_, int p_75904_3_, int p_75904_4_) {
        int[] var5 = this.parent.getInts(p_75904_1_, p_75904_2_, p_75904_3_, p_75904_4_);
        int[] var6 = IntCache.getIntCache(p_75904_3_ * p_75904_4_);

        for (int var7 = 0; var7 < p_75904_4_; ++var7) {
            for (int var8 = 0; var8 < p_75904_3_; ++var8) {
                this.initChunkSeed((long) (var8 + p_75904_1_), (long) (var7 + p_75904_2_));
                var6[var8 + var7 * p_75904_3_] = var5[var8 + var7 * p_75904_3_] > 0 ? this.nextInt(299999) + 2 : 0;
            }
        }

        return var6;
    }
}
