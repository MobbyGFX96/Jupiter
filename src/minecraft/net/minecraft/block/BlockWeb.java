package net.minecraft.block;

import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

import java.util.Random;

public class BlockWeb extends Block {
    private static final String __OBFID = "CL_00000333";

    public BlockWeb() {
        super(Material.field_151569_G);
        this.setCreativeTab(CreativeTabs.tabDecorations);
    }

    public void onEntityCollidedWithBlock(World p_149670_1_, int p_149670_2_, int p_149670_3_, int p_149670_4_, Entity p_149670_5_) {
        p_149670_5_.setInWeb();
    }

    public boolean isOpaqueCube() {
        return false;
    }

    public AxisAlignedBB getCollisionBoundingBoxFromPool(World p_149668_1_, int p_149668_2_, int p_149668_3_, int p_149668_4_) {
        return null;
    }

    public int getRenderType() {
        return 1;
    }

    public boolean renderAsNormalBlock() {
        return false;
    }

    public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_) {
        return Items.string;
    }

    protected boolean canSilkHarvest() {
        return true;
    }
}
