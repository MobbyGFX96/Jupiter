package net.minecraft.client.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityFireworkStarterFX extends EntityFX {
    private static final String __OBFID = "CL_00000906";
    private final EffectRenderer theEffectRenderer;
    boolean twinkle;
    private int fireworkAge;
    private NBTTagList fireworkExplosions;

    public EntityFireworkStarterFX(World p_i1208_1_, double p_i1208_2_, double p_i1208_4_, double p_i1208_6_, double p_i1208_8_, double p_i1208_10_, double p_i1208_12_, EffectRenderer p_i1208_14_, NBTTagCompound p_i1208_15_) {
        super(p_i1208_1_, p_i1208_2_, p_i1208_4_, p_i1208_6_, 0.0D, 0.0D, 0.0D);
        this.motionX = p_i1208_8_;
        this.motionY = p_i1208_10_;
        this.motionZ = p_i1208_12_;
        this.theEffectRenderer = p_i1208_14_;
        this.particleMaxAge = 8;
        if (p_i1208_15_ != null) {
            this.fireworkExplosions = p_i1208_15_.getTagList("Explosions", 10);
            if (this.fireworkExplosions.tagCount() == 0) {
                this.fireworkExplosions = null;
            } else {
                this.particleMaxAge = this.fireworkExplosions.tagCount() * 2 - 1;

                for (int var16 = 0; var16 < this.fireworkExplosions.tagCount(); ++var16) {
                    NBTTagCompound var17 = this.fireworkExplosions.getCompoundTagAt(var16);
                    if (var17.getBoolean("Flicker")) {
                        this.twinkle = true;
                        this.particleMaxAge += 15;
                        break;
                    }
                }
            }
        }
    }

    public void renderParticle(Tessellator p_70539_1_, float p_70539_2_, float p_70539_3_, float p_70539_4_, float p_70539_5_, float p_70539_6_, float p_70539_7_) {
    }

    public void onUpdate() {
        boolean var1;
        if (this.fireworkAge == 0 && this.fireworkExplosions != null) {
            var1 = this.func_92037_i();
            boolean var2 = false;
            if (this.fireworkExplosions.tagCount() >= 3) {
                var2 = true;
            } else {
                for (int var3 = 0; var3 < this.fireworkExplosions.tagCount(); ++var3) {
                    NBTTagCompound var4 = this.fireworkExplosions.getCompoundTagAt(var3);
                    if (var4.getByte("Type") == 1) {
                        var2 = true;
                        break;
                    }
                }
            }

            String var16 = "fireworks." + (var2 ? "largeBlast" : "blast") + (var1 ? "_far" : "");
            this.worldObj.playSound(this.posX, this.posY, this.posZ, var16, 20.0F, 0.95F + this.rand.nextFloat() * 0.1F, true);
        }

        if (this.fireworkAge % 2 == 0 && this.fireworkExplosions != null && this.fireworkAge / 2 < this.fireworkExplosions.tagCount()) {
            int var13 = this.fireworkAge / 2;
            NBTTagCompound var14 = this.fireworkExplosions.getCompoundTagAt(var13);
            byte var17 = var14.getByte("Type");
            boolean var18 = var14.getBoolean("Trail");
            boolean var5 = var14.getBoolean("Flicker");
            int[] var6 = var14.getIntArray("Colors");
            int[] var7 = var14.getIntArray("FadeColors");
            if (var17 == 1) {
                this.createBall(0.5D, 4, var6, var7, var18, var5);
            } else if (var17 == 2) {
                this.createShaped(0.5D, new double[][]{{0.0D, 1.0D}, {0.3455D, 0.309D}, {0.9511D, 0.309D}, {0.3795918367346939D, -0.12653061224489795D}, {0.6122448979591837D, -0.8040816326530612D}, {0.0D, -0.35918367346938773D}}, var6, var7, var18, var5, false);
            } else if (var17 == 3) {
                this.createShaped(0.5D, new double[][]{{0.0D, 0.2D}, {0.2D, 0.2D}, {0.2D, 0.6D}, {0.6D, 0.6D}, {0.6D, 0.2D}, {0.2D, 0.2D}, {0.2D, 0.0D}, {0.4D, 0.0D}, {0.4D, -0.6D}, {0.2D, -0.6D}, {0.2D, -0.4D}, {0.0D, -0.4D}}, var6, var7, var18, var5, true);
            } else if (var17 == 4) {
                this.createBurst(var6, var7, var18, var5);
            } else {
                this.createBall(0.25D, 2, var6, var7, var18, var5);
            }

            int var8 = var6[0];
            float var9 = (float) ((var8 & 16711680) >> 16) / 255.0F;
            float var10 = (float) ((var8 & 65280) >> 8) / 255.0F;
            float var11 = (float) ((var8 & 255) >> 0) / 255.0F;
            EntityFireworkOverlayFX var12 = new EntityFireworkOverlayFX(this.worldObj, this.posX, this.posY, this.posZ);
            var12.setRBGColorF(var9, var10, var11);
            this.theEffectRenderer.addEffect(var12);
        }

        ++this.fireworkAge;
        if (this.fireworkAge > this.particleMaxAge) {
            if (this.twinkle) {
                var1 = this.func_92037_i();
                String var15 = "fireworks." + (var1 ? "twinkle_far" : "twinkle");
                this.worldObj.playSound(this.posX, this.posY, this.posZ, var15, 20.0F, 0.9F + this.rand.nextFloat() * 0.15F, true);
            }

            this.setDead();
        }
    }

    private boolean func_92037_i() {
        Minecraft var1 = Minecraft.getMinecraft();
        return var1 == null || var1.renderViewEntity == null || var1.renderViewEntity.getDistanceSq(this.posX, this.posY, this.posZ) >= 256.0D;
    }

    private void createParticle(double p_92034_1_, double p_92034_3_, double p_92034_5_, double p_92034_7_, double p_92034_9_, double p_92034_11_, int[] p_92034_13_, int[] p_92034_14_, boolean p_92034_15_, boolean p_92034_16_) {
        EntityFireworkSparkFX var17 = new EntityFireworkSparkFX(this.worldObj, p_92034_1_, p_92034_3_, p_92034_5_, p_92034_7_, p_92034_9_, p_92034_11_, this.theEffectRenderer);
        var17.setTrail(p_92034_15_);
        var17.setTwinkle(p_92034_16_);
        int var18 = this.rand.nextInt(p_92034_13_.length);
        var17.setColour(p_92034_13_[var18]);
        if (p_92034_14_ != null && p_92034_14_.length > 0) {
            var17.setFadeColour(p_92034_14_[this.rand.nextInt(p_92034_14_.length)]);
        }

        this.theEffectRenderer.addEffect(var17);
    }

    private void createBall(double p_92035_1_, int p_92035_3_, int[] p_92035_4_, int[] p_92035_5_, boolean p_92035_6_, boolean p_92035_7_) {
        double var8 = this.posX;
        double var10 = this.posY;
        double var12 = this.posZ;

        for (int var14 = -p_92035_3_; var14 <= p_92035_3_; ++var14) {
            for (int var15 = -p_92035_3_; var15 <= p_92035_3_; ++var15) {
                for (int var16 = -p_92035_3_; var16 <= p_92035_3_; ++var16) {
                    double var17 = (double) var15 + (this.rand.nextDouble() - this.rand.nextDouble()) * 0.5D;
                    double var19 = (double) var14 + (this.rand.nextDouble() - this.rand.nextDouble()) * 0.5D;
                    double var21 = (double) var16 + (this.rand.nextDouble() - this.rand.nextDouble()) * 0.5D;
                    double var23 = (double) MathHelper.sqrt_double(var17 * var17 + var19 * var19 + var21 * var21) / p_92035_1_ + this.rand.nextGaussian() * 0.05D;
                    this.createParticle(var8, var10, var12, var17 / var23, var19 / var23, var21 / var23, p_92035_4_, p_92035_5_, p_92035_6_, p_92035_7_);
                    if (var14 != -p_92035_3_ && var14 != p_92035_3_ && var15 != -p_92035_3_ && var15 != p_92035_3_) {
                        var16 += p_92035_3_ * 2 - 1;
                    }
                }
            }
        }
    }

    private void createShaped(double p_92038_1_, double[][] p_92038_3_, int[] p_92038_4_, int[] p_92038_5_, boolean p_92038_6_, boolean p_92038_7_, boolean p_92038_8_) {
        double var9 = p_92038_3_[0][0];
        double var11 = p_92038_3_[0][1];
        this.createParticle(this.posX, this.posY, this.posZ, var9 * p_92038_1_, var11 * p_92038_1_, 0.0D, p_92038_4_, p_92038_5_, p_92038_6_, p_92038_7_);
        float var13 = this.rand.nextFloat() * (float) Math.PI;
        double var14 = p_92038_8_ ? 0.034D : 0.34D;

        for (int var16 = 0; var16 < 3; ++var16) {
            double var17 = (double) var13 + (double) ((float) var16 * (float) Math.PI) * var14;
            double var19 = var9;
            double var21 = var11;

            for (int var23 = 1; var23 < p_92038_3_.length; ++var23) {
                double var24 = p_92038_3_[var23][0];
                double var26 = p_92038_3_[var23][1];

                for (double var28 = 0.25D; var28 <= 1.0D; var28 += 0.25D) {
                    double var30 = (var19 + (var24 - var19) * var28) * p_92038_1_;
                    double var32 = (var21 + (var26 - var21) * var28) * p_92038_1_;
                    double var34 = var30 * Math.sin(var17);
                    var30 *= Math.cos(var17);

                    for (double var36 = -1.0D; var36 <= 1.0D; var36 += 2.0D) {
                        this.createParticle(this.posX, this.posY, this.posZ, var30 * var36, var32, var34 * var36, p_92038_4_, p_92038_5_, p_92038_6_, p_92038_7_);
                    }
                }

                var19 = var24;
                var21 = var26;
            }
        }
    }

    private void createBurst(int[] p_92036_1_, int[] p_92036_2_, boolean p_92036_3_, boolean p_92036_4_) {
        double var5 = this.rand.nextGaussian() * 0.05D;
        double var7 = this.rand.nextGaussian() * 0.05D;

        for (int var9 = 0; var9 < 70; ++var9) {
            double var10 = this.motionX * 0.5D + this.rand.nextGaussian() * 0.15D + var5;
            double var12 = this.motionZ * 0.5D + this.rand.nextGaussian() * 0.15D + var7;
            double var14 = this.motionY * 0.5D + this.rand.nextDouble() * 0.5D;
            this.createParticle(this.posX, this.posY, this.posZ, var10, var14, var12, p_92036_1_, p_92036_2_, p_92036_3_, p_92036_4_);
        }
    }

    public int getFXLayer() {
        return 0;
    }
}