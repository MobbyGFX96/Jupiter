package net.minecraft.item;

import net.minecraft.block.BlockLeaves;
import net.minecraft.util.IIcon;

public class ItemLeaves extends ItemBlock {
    private static final String __OBFID = "CL_00000046";
    private final BlockLeaves field_150940_b;

    public ItemLeaves(BlockLeaves p_i45344_1_) {
        super(p_i45344_1_);
        this.field_150940_b = p_i45344_1_;
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }

    public int getMetadata(int p_77647_1_) {
        return p_77647_1_ | 4;
    }

    public IIcon getIconFromDamage(int p_77617_1_) {
        return this.field_150940_b.getIcon(0, p_77617_1_);
    }

    public int getColorFromItemStack(ItemStack p_82790_1_, int p_82790_2_) {
        return this.field_150940_b.getRenderColor(p_82790_1_.getItemDamage());
    }

    public String getUnlocalizedName(ItemStack p_77667_1_) {
        int var2 = p_77667_1_.getItemDamage();
        if (var2 < 0 || var2 >= this.field_150940_b.func_150125_e().length) {
            var2 = 0;
        }

        return super.getUnlocalizedName() + "." + this.field_150940_b.func_150125_e()[var2];
    }
}
