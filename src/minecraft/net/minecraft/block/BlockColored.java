package net.minecraft.block;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import java.util.List;

public class BlockColored extends Block {
    private static final String __OBFID = "CL_00000217";
    private IIcon[] field_150033_a;

    public BlockColored(Material p_i45398_1_) {
        super(p_i45398_1_);
        this.setCreativeTab(CreativeTabs.tabBlock);
    }

    public static int func_150032_b(int p_150032_0_) {
        return func_150031_c(p_150032_0_);
    }

    public static int func_150031_c(int p_150031_0_) {
        return ~p_150031_0_ & 15;
    }

    public IIcon getIcon(int p_149691_1_, int p_149691_2_) {
        return this.field_150033_a[p_149691_2_ % this.field_150033_a.length];
    }

    public int damageDropped(int p_149692_1_) {
        return p_149692_1_;
    }

    public void getSubBlocks(Item p_149666_1_, CreativeTabs p_149666_2_, List p_149666_3_) {
        for (int var4 = 0; var4 < 16; ++var4) {
            p_149666_3_.add(new ItemStack(p_149666_1_, 1, var4));
        }
    }

    public void registerBlockIcons(IIconRegister p_149651_1_) {
        this.field_150033_a = new IIcon[16];

        for (int var2 = 0; var2 < this.field_150033_a.length; ++var2) {
            this.field_150033_a[var2] = p_149651_1_.registerIcon(this.getTextureName() + "_" + ItemDye.field_150921_b[func_150031_c(var2)]);
        }
    }

    public MapColor getMapColor(int p_149728_1_) {
        return MapColor.func_151644_a(p_149728_1_);
    }
}
