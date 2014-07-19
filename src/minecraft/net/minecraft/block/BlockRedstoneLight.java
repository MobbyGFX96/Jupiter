package net.minecraft.block;

import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.Random;

public class BlockRedstoneLight extends Block {
    private static final String __OBFID = "CL_00000297";
    private final boolean field_150171_a;

    public BlockRedstoneLight(boolean p_i45421_1_) {
        super(Material.redstoneLight);
        this.field_150171_a = p_i45421_1_;
        if (p_i45421_1_) {
            this.setLightLevel(1.0F);
        }
    }

    public void onBlockAdded(World p_149726_1_, int p_149726_2_, int p_149726_3_, int p_149726_4_) {
        if (!p_149726_1_.isClient) {
            if (this.field_150171_a && !p_149726_1_.isBlockIndirectlyGettingPowered(p_149726_2_, p_149726_3_, p_149726_4_)) {
                p_149726_1_.scheduleBlockUpdate(p_149726_2_, p_149726_3_, p_149726_4_, this, 4);
            } else if (!this.field_150171_a && p_149726_1_.isBlockIndirectlyGettingPowered(p_149726_2_, p_149726_3_, p_149726_4_)) {
                p_149726_1_.setBlock(p_149726_2_, p_149726_3_, p_149726_4_, Blocks.lit_redstone_lamp, 0, 2);
            }
        }
    }

    public void onNeighborBlockChange(World p_149695_1_, int p_149695_2_, int p_149695_3_, int p_149695_4_, Block p_149695_5_) {
        if (!p_149695_1_.isClient) {
            if (this.field_150171_a && !p_149695_1_.isBlockIndirectlyGettingPowered(p_149695_2_, p_149695_3_, p_149695_4_)) {
                p_149695_1_.scheduleBlockUpdate(p_149695_2_, p_149695_3_, p_149695_4_, this, 4);
            } else if (!this.field_150171_a && p_149695_1_.isBlockIndirectlyGettingPowered(p_149695_2_, p_149695_3_, p_149695_4_)) {
                p_149695_1_.setBlock(p_149695_2_, p_149695_3_, p_149695_4_, Blocks.lit_redstone_lamp, 0, 2);
            }
        }
    }

    public void updateTick(World p_149674_1_, int p_149674_2_, int p_149674_3_, int p_149674_4_, Random p_149674_5_) {
        if (!p_149674_1_.isClient && this.field_150171_a && !p_149674_1_.isBlockIndirectlyGettingPowered(p_149674_2_, p_149674_3_, p_149674_4_)) {
            p_149674_1_.setBlock(p_149674_2_, p_149674_3_, p_149674_4_, Blocks.redstone_lamp, 0, 2);
        }
    }

    public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_) {
        return Item.getItemFromBlock(Blocks.redstone_lamp);
    }

    public Item getItem(World p_149694_1_, int p_149694_2_, int p_149694_3_, int p_149694_4_) {
        return Item.getItemFromBlock(Blocks.redstone_lamp);
    }

    protected ItemStack createStackedBlock(int p_149644_1_) {
        return new ItemStack(Blocks.redstone_lamp);
    }
}
