package net.minecraft.block;

import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

import java.util.Random;

public class BlockPackedIce extends Block {
    private static final String __OBFID = "CL_00000283";

    public BlockPackedIce() {
        super(Material.field_151598_x);
        this.slipperiness = 0.98F;
        this.setCreativeTab(CreativeTabs.tabBlock);
    }

    public int quantityDropped(Random p_149745_1_) {
        return 0;
    }
}
