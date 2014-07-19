package net.minecraft.block;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;

import java.util.Random;

public class BlockMelon extends Block {
    private static final String __OBFID = "CL_00000267";
    private IIcon field_150201_a;

    protected BlockMelon() {
        super(Material.field_151572_C);
        this.setCreativeTab(CreativeTabs.tabBlock);
    }

    public IIcon getIcon(int p_149691_1_, int p_149691_2_) {
        return p_149691_1_ != 1 && p_149691_1_ != 0 ? this.blockIcon : this.field_150201_a;
    }

    public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_) {
        return Items.melon;
    }

    public int quantityDropped(Random p_149745_1_) {
        return 3 + p_149745_1_.nextInt(5);
    }

    public int quantityDroppedWithBonus(int p_149679_1_, Random p_149679_2_) {
        int var3 = this.quantityDropped(p_149679_2_) + p_149679_2_.nextInt(1 + p_149679_1_);
        if (var3 > 9) {
            var3 = 9;
        }

        return var3;
    }

    public void registerBlockIcons(IIconRegister p_149651_1_) {
        this.blockIcon = p_149651_1_.registerIcon(this.getTextureName() + "_side");
        this.field_150201_a = p_149651_1_.registerIcon(this.getTextureName() + "_top");
    }
}
