package net.minecraft.world.chunk;

public class NibbleArray {
    private static final String __OBFID = "CL_00000371";
    public final byte[] data;
    private final int depthBits;
    private final int depthBitsPlusFour;

    public NibbleArray(int p_i1992_1_, int p_i1992_2_) {
        this.data = new byte[p_i1992_1_ >> 1];
        this.depthBits = p_i1992_2_;
        this.depthBitsPlusFour = p_i1992_2_ + 4;
    }

    public NibbleArray(byte[] p_i1993_1_, int p_i1993_2_) {
        this.data = p_i1993_1_;
        this.depthBits = p_i1993_2_;
        this.depthBitsPlusFour = p_i1993_2_ + 4;
    }

    public int get(int p_76582_1_, int p_76582_2_, int p_76582_3_) {
        int var4 = p_76582_2_ << this.depthBitsPlusFour | p_76582_3_ << this.depthBits | p_76582_1_;
        int var5 = var4 >> 1;
        int var6 = var4 & 1;
        return var6 == 0 ? this.data[var5] & 15 : this.data[var5] >> 4 & 15;
    }

    public void set(int p_76581_1_, int p_76581_2_, int p_76581_3_, int p_76581_4_) {
        int var5 = p_76581_2_ << this.depthBitsPlusFour | p_76581_3_ << this.depthBits | p_76581_1_;
        int var6 = var5 >> 1;
        int var7 = var5 & 1;
        if (var7 == 0) {
            this.data[var6] = (byte) (this.data[var6] & 240 | p_76581_4_ & 15);
        } else {
            this.data[var6] = (byte) (this.data[var6] & 15 | (p_76581_4_ & 15) << 4);
        }
    }
}
