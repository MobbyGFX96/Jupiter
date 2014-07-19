package net.minecraft.block;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.MathHelper;

import java.util.Random;

public class BlockGlowstone extends Block {
    private static final String __OBFID = "CL_00000250";

    public BlockGlowstone(Material p_i45409_1_) {
        super(p_i45409_1_);
        this.setCreativeTab(CreativeTabs.tabBlock);
    }

    public int quantityDroppedWithBonus(int p_149679_1_, Random p_149679_2_) {
        return MathHelper.clamp_int(this.quantityDropped(p_149679_2_) + p_149679_2_.nextInt(p_149679_1_ + 1), 1, 4);
    }

    public int quantityDropped(Random p_149745_1_) {
        return 2 + p_149745_1_.nextInt(3);
    }

    public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_) {
        return Items.glowstone_dust;
    }

    public MapColor getMapColor(int p_149728_1_) {
        return MapColor.field_151658_d;
    }
}
