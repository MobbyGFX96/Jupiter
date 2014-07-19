package net.minecraft.item;

import net.minecraft.block.Block;
import net.minecraft.util.IIcon;

public class ItemBlockWithMetadata extends ItemBlock {
    private static final String __OBFID = "CL_00001769";
    private Block field_150950_b;

    public ItemBlockWithMetadata(Block p_i45326_1_, Block p_i45326_2_) {
        super(p_i45326_1_);
        this.field_150950_b = p_i45326_2_;
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }

    public IIcon getIconFromDamage(int p_77617_1_) {
        return this.field_150950_b.getIcon(2, p_77617_1_);
    }

    public int getMetadata(int p_77647_1_) {
        return p_77647_1_;
    }
}
