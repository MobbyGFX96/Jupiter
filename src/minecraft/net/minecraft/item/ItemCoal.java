package net.minecraft.item;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.IIcon;

import java.util.List;

public class ItemCoal extends Item {
    private static final String __OBFID = "CL_00000002";
    private IIcon field_111220_a;

    public ItemCoal() {
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
        this.setCreativeTab(CreativeTabs.tabMaterials);
    }

    public String getUnlocalizedName(ItemStack p_77667_1_) {
        return p_77667_1_.getItemDamage() == 1 ? "item.charcoal" : "item.coal";
    }

    public void getSubItems(Item p_150895_1_, CreativeTabs p_150895_2_, List p_150895_3_) {
        p_150895_3_.add(new ItemStack(p_150895_1_, 1, 0));
        p_150895_3_.add(new ItemStack(p_150895_1_, 1, 1));
    }

    public IIcon getIconFromDamage(int p_77617_1_) {
        return p_77617_1_ == 1 ? this.field_111220_a : super.getIconFromDamage(p_77617_1_);
    }

    public void registerIcons(IIconRegister p_94581_1_) {
        super.registerIcons(p_94581_1_);
        this.field_111220_a = p_94581_1_.registerIcon("charcoal");
    }
}
