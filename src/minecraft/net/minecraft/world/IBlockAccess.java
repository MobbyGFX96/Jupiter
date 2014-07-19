package net.minecraft.world;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.biome.BiomeGenBase;

public interface IBlockAccess {
    Block getBlock(int p_147439_1_, int p_147439_2_, int p_147439_3_);

    TileEntity getTileEntity(int p_147438_1_, int p_147438_2_, int p_147438_3_);

    int getLightBrightnessForSkyBlocks(int p_72802_1_, int p_72802_2_, int p_72802_3_, int p_72802_4_);

    int getBlockMetadata(int p_72805_1_, int p_72805_2_, int p_72805_3_);

    boolean isAirBlock(int p_147437_1_, int p_147437_2_, int p_147437_3_);

    BiomeGenBase getBiomeGenForCoords(int p_72807_1_, int p_72807_2_);

    int getHeight();

    boolean extendedLevelsInChunkCache();

    int isBlockProvidingPowerTo(int p_72879_1_, int p_72879_2_, int p_72879_3_, int p_72879_4_);
}
