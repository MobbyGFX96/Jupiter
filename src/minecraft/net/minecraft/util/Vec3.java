package net.minecraft.util;

public class Vec3 {
    private static final String __OBFID = "CL_00000612";
    public double xCoord;
    public double yCoord;
    public double zCoord;

    protected Vec3(double p_i1108_1_, double p_i1108_3_, double p_i1108_5_) {
        if (p_i1108_1_ == -0.0D) {
            p_i1108_1_ = 0.0D;
        }

        if (p_i1108_3_ == -0.0D) {
            p_i1108_3_ = 0.0D;
        }

        if (p_i1108_5_ == -0.0D) {
            p_i1108_5_ = 0.0D;
        }

        this.xCoord = p_i1108_1_;
        this.yCoord = p_i1108_3_;
        this.zCoord = p_i1108_5_;
    }

    public static Vec3 createVectorHelper(double p_72443_0_, double p_72443_2_, double p_72443_4_) {
        return new Vec3(p_72443_0_, p_72443_2_, p_72443_4_);
    }

    protected Vec3 setComponents(double p_72439_1_, double p_72439_3_, double p_72439_5_) {
        this.xCoord = p_72439_1_;
        this.yCoord = p_72439_3_;
        this.zCoord = p_72439_5_;
        return this;
    }

    public Vec3 subtract(Vec3 p_72444_1_) {
        return createVectorHelper(p_72444_1_.xCoord - this.xCoord, p_72444_1_.yCoord - this.yCoord, p_72444_1_.zCoord - this.zCoord);
    }

    public Vec3 normalize() {
        double var1 = (double) MathHelper.sqrt_double(this.xCoord * this.xCoord + this.yCoord * this.yCoord + this.zCoord * this.zCoord);
        return var1 < 1.0E-4D ? createVectorHelper(0.0D, 0.0D, 0.0D) : createVectorHelper(this.xCoord / var1, this.yCoord / var1, this.zCoord / var1);
    }

    public double dotProduct(Vec3 p_72430_1_) {
        return this.xCoord * p_72430_1_.xCoord + this.yCoord * p_72430_1_.yCoord + this.zCoord * p_72430_1_.zCoord;
    }

    public Vec3 crossProduct(Vec3 p_72431_1_) {
        return createVectorHelper(this.yCoord * p_72431_1_.zCoord - this.zCoord * p_72431_1_.yCoord, this.zCoord * p_72431_1_.xCoord - this.xCoord * p_72431_1_.zCoord, this.xCoord * p_72431_1_.yCoord - this.yCoord * p_72431_1_.xCoord);
    }

    public Vec3 addVector(double p_72441_1_, double p_72441_3_, double p_72441_5_) {
        return createVectorHelper(this.xCoord + p_72441_1_, this.yCoord + p_72441_3_, this.zCoord + p_72441_5_);
    }

    public double distanceTo(Vec3 p_72438_1_) {
        double var2 = p_72438_1_.xCoord - this.xCoord;
        double var4 = p_72438_1_.yCoord - this.yCoord;
        double var6 = p_72438_1_.zCoord - this.zCoord;
        return (double) MathHelper.sqrt_double(var2 * var2 + var4 * var4 + var6 * var6);
    }

    public double squareDistanceTo(Vec3 p_72436_1_) {
        double var2 = p_72436_1_.xCoord - this.xCoord;
        double var4 = p_72436_1_.yCoord - this.yCoord;
        double var6 = p_72436_1_.zCoord - this.zCoord;
        return var2 * var2 + var4 * var4 + var6 * var6;
    }

    public double squareDistanceTo(double p_72445_1_, double p_72445_3_, double p_72445_5_) {
        double var7 = p_72445_1_ - this.xCoord;
        double var9 = p_72445_3_ - this.yCoord;
        double var11 = p_72445_5_ - this.zCoord;
        return var7 * var7 + var9 * var9 + var11 * var11;
    }

    public double lengthVector() {
        return (double) MathHelper.sqrt_double(this.xCoord * this.xCoord + this.yCoord * this.yCoord + this.zCoord * this.zCoord);
    }

    public Vec3 getIntermediateWithXValue(Vec3 p_72429_1_, double p_72429_2_) {
        double var4 = p_72429_1_.xCoord - this.xCoord;
        double var6 = p_72429_1_.yCoord - this.yCoord;
        double var8 = p_72429_1_.zCoord - this.zCoord;
        if (var4 * var4 < 1.0000000116860974E-7D) {
            return null;
        } else {
            double var10 = (p_72429_2_ - this.xCoord) / var4;
            return var10 >= 0.0D && var10 <= 1.0D ? createVectorHelper(this.xCoord + var4 * var10, this.yCoord + var6 * var10, this.zCoord + var8 * var10) : null;
        }
    }

    public Vec3 getIntermediateWithYValue(Vec3 p_72435_1_, double p_72435_2_) {
        double var4 = p_72435_1_.xCoord - this.xCoord;
        double var6 = p_72435_1_.yCoord - this.yCoord;
        double var8 = p_72435_1_.zCoord - this.zCoord;
        if (var6 * var6 < 1.0000000116860974E-7D) {
            return null;
        } else {
            double var10 = (p_72435_2_ - this.yCoord) / var6;
            return var10 >= 0.0D && var10 <= 1.0D ? createVectorHelper(this.xCoord + var4 * var10, this.yCoord + var6 * var10, this.zCoord + var8 * var10) : null;
        }
    }

    public Vec3 getIntermediateWithZValue(Vec3 p_72434_1_, double p_72434_2_) {
        double var4 = p_72434_1_.xCoord - this.xCoord;
        double var6 = p_72434_1_.yCoord - this.yCoord;
        double var8 = p_72434_1_.zCoord - this.zCoord;
        if (var8 * var8 < 1.0000000116860974E-7D) {
            return null;
        } else {
            double var10 = (p_72434_2_ - this.zCoord) / var8;
            return var10 >= 0.0D && var10 <= 1.0D ? createVectorHelper(this.xCoord + var4 * var10, this.yCoord + var6 * var10, this.zCoord + var8 * var10) : null;
        }
    }

    public String toString() {
        return "(" + this.xCoord + ", " + this.yCoord + ", " + this.zCoord + ")";
    }

    public void rotateAroundX(float p_72440_1_) {
        float var2 = MathHelper.cos(p_72440_1_);
        float var3 = MathHelper.sin(p_72440_1_);
        double var4 = this.xCoord;
        double var6 = this.yCoord * (double) var2 + this.zCoord * (double) var3;
        double var8 = this.zCoord * (double) var2 - this.yCoord * (double) var3;
        this.setComponents(var4, var6, var8);
    }

    public void rotateAroundY(float p_72442_1_) {
        float var2 = MathHelper.cos(p_72442_1_);
        float var3 = MathHelper.sin(p_72442_1_);
        double var4 = this.xCoord * (double) var2 + this.zCoord * (double) var3;
        double var6 = this.yCoord;
        double var8 = this.zCoord * (double) var2 - this.xCoord * (double) var3;
        this.setComponents(var4, var6, var8);
    }

    public void rotateAroundZ(float p_72446_1_) {
        float var2 = MathHelper.cos(p_72446_1_);
        float var3 = MathHelper.sin(p_72446_1_);
        double var4 = this.xCoord * (double) var2 + this.yCoord * (double) var3;
        double var6 = this.yCoord * (double) var2 - this.xCoord * (double) var3;
        double var8 = this.zCoord;
        this.setComponents(var4, var6, var8);
    }
}
