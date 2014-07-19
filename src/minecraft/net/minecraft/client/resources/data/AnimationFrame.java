package net.minecraft.client.resources.data;

public class AnimationFrame {
    private static final String __OBFID = "CL_00001104";
    private final int frameIndex;
    private final int frameTime;

    public AnimationFrame(int p_i1307_1_) {
        this(p_i1307_1_, -1);
    }

    public AnimationFrame(int p_i1308_1_, int p_i1308_2_) {
        this.frameIndex = p_i1308_1_;
        this.frameTime = p_i1308_2_;
    }

    public boolean hasNoTime() {
        return this.frameTime == -1;
    }

    public int getFrameTime() {
        return this.frameTime;
    }

    public int getFrameIndex() {
        return this.frameIndex;
    }
}
