package net.minecraft.world;

import net.minecraft.init.Blocks;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.biome.WorldChunkManagerHell;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkProviderFlat;
import net.minecraft.world.gen.ChunkProviderGenerate;
import net.minecraft.world.gen.FlatGeneratorInfo;

public abstract class WorldProvider {
    public static final float[] moonPhaseFactors = new float[]{1.0F, 0.75F, 0.5F, 0.25F, 0.0F, 0.25F, 0.5F, 0.75F};
    private static final String __OBFID = "CL_00000386";
    public World worldObj;
    public WorldType terrainType;
    public String field_82913_c;
    public WorldChunkManager worldChunkMgr;
    public boolean isHellWorld;
    public boolean hasNoSky;
    public float[] lightBrightnessTable = new float[16];
    public int dimensionId;
    private float[] colorsSunriseSunset = new float[4];

    public static WorldProvider getProviderForDimension(int p_76570_0_) {
        return (WorldProvider) (p_76570_0_ == -1 ? new WorldProviderHell() : (p_76570_0_ == 0 ? new WorldProviderSurface() : (p_76570_0_ == 1 ? new WorldProviderEnd() : null)));
    }

    public final void registerWorld(World p_76558_1_) {
        this.worldObj = p_76558_1_;
        this.terrainType = p_76558_1_.getWorldInfo().getTerrainType();
        this.field_82913_c = p_76558_1_.getWorldInfo().getGeneratorOptions();
        this.registerWorldChunkManager();
        this.generateLightBrightnessTable();
    }

    protected void generateLightBrightnessTable() {
        float var1 = 0.0F;

        for (int var2 = 0; var2 <= 15; ++var2) {
            float var3 = 1.0F - (float) var2 / 15.0F;
            this.lightBrightnessTable[var2] = (1.0F - var3) / (var3 * 3.0F + 1.0F) * (1.0F - var1) + var1;
        }
    }

    protected void registerWorldChunkManager() {
        if (this.worldObj.getWorldInfo().getTerrainType() == WorldType.FLAT) {
            FlatGeneratorInfo var1 = FlatGeneratorInfo.createFlatGeneratorFromString(this.worldObj.getWorldInfo().getGeneratorOptions());
            this.worldChunkMgr = new WorldChunkManagerHell(BiomeGenBase.func_150568_d(var1.getBiome()), 0.5F);
        } else {
            this.worldChunkMgr = new WorldChunkManager(this.worldObj);
        }
    }

    public IChunkProvider createChunkGenerator() {
        return (IChunkProvider) (this.terrainType == WorldType.FLAT ? new ChunkProviderFlat(this.worldObj, this.worldObj.getSeed(), this.worldObj.getWorldInfo().isMapFeaturesEnabled(), this.field_82913_c) : new ChunkProviderGenerate(this.worldObj, this.worldObj.getSeed(), this.worldObj.getWorldInfo().isMapFeaturesEnabled()));
    }

    public boolean canCoordinateBeSpawn(int p_76566_1_, int p_76566_2_) {
        return this.worldObj.getTopBlock(p_76566_1_, p_76566_2_) == Blocks.grass;
    }

    public float calculateCelestialAngle(long p_76563_1_, float p_76563_3_) {
        int var4 = (int) (p_76563_1_ % 24000L);
        float var5 = ((float) var4 + p_76563_3_) / 24000.0F - 0.25F;
        if (var5 < 0.0F) {
            ++var5;
        }

        if (var5 > 1.0F) {
            --var5;
        }

        float var6 = var5;
        var5 = 1.0F - (float) ((Math.cos((double) var5 * Math.PI) + 1.0D) / 2.0D);
        var5 = var6 + (var5 - var6) / 3.0F;
        return var5;
    }

    public int getMoonPhase(long p_76559_1_) {
        return (int) (p_76559_1_ / 24000L % 8L + 8L) % 8;
    }

    public boolean isSurfaceWorld() {
        return true;
    }

    public float[] calcSunriseSunsetColors(float p_76560_1_, float p_76560_2_) {
        float var3 = 0.4F;
        float var4 = MathHelper.cos(p_76560_1_ * (float) Math.PI * 2.0F) - 0.0F;
        float var5 = -0.0F;
        if (var4 >= var5 - var3 && var4 <= var5 + var3) {
            float var6 = (var4 - var5) / var3 * 0.5F + 0.5F;
            float var7 = 1.0F - (1.0F - MathHelper.sin(var6 * (float) Math.PI)) * 0.99F;
            var7 *= var7;
            this.colorsSunriseSunset[0] = var6 * 0.3F + 0.7F;
            this.colorsSunriseSunset[1] = var6 * var6 * 0.7F + 0.2F;
            this.colorsSunriseSunset[2] = var6 * var6 * 0.0F + 0.2F;
            this.colorsSunriseSunset[3] = var7;
            return this.colorsSunriseSunset;
        } else {
            return null;
        }
    }

    public Vec3 getFogColor(float p_76562_1_, float p_76562_2_) {
        float var3 = MathHelper.cos(p_76562_1_ * (float) Math.PI * 2.0F) * 2.0F + 0.5F;
        if (var3 < 0.0F) {
            var3 = 0.0F;
        }

        if (var3 > 1.0F) {
            var3 = 1.0F;
        }

        float var4 = 0.7529412F;
        float var5 = 0.84705883F;
        float var6 = 1.0F;
        var4 *= var3 * 0.94F + 0.06F;
        var5 *= var3 * 0.94F + 0.06F;
        var6 *= var3 * 0.91F + 0.09F;
        return Vec3.createVectorHelper((double) var4, (double) var5, (double) var6);
    }

    public boolean canRespawnHere() {
        return true;
    }

    public float getCloudHeight() {
        return 128.0F;
    }

    public boolean isSkyColored() {
        return true;
    }

    public ChunkCoordinates getEntrancePortalLocation() {
        return null;
    }

    public int getAverageGroundLevel() {
        return this.terrainType == WorldType.FLAT ? 4 : 64;
    }

    public boolean getWorldHasVoidParticles() {
        return this.terrainType != WorldType.FLAT && !this.hasNoSky;
    }

    public double getVoidFogYFactor() {
        return this.terrainType == WorldType.FLAT ? 1.0D : 0.03125D;
    }

    public boolean doesXZShowFog(int p_76568_1_, int p_76568_2_) {
        return false;
    }

    public abstract String getDimensionName();
}
