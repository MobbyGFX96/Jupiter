package net.minecraft.item;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class ItemSeedFood extends ItemFood {
    private static final String __OBFID = "CL_00000060";
    private Block field_150908_b;
    private Block soilId;

    public ItemSeedFood(int p_i45351_1_, float p_i45351_2_, Block p_i45351_3_, Block p_i45351_4_) {
        super(p_i45351_1_, p_i45351_2_, false);
        this.field_150908_b = p_i45351_3_;
        this.soilId = p_i45351_4_;
    }

    public boolean onItemUse(ItemStack p_77648_1_, EntityPlayer p_77648_2_, World p_77648_3_, int p_77648_4_, int p_77648_5_, int p_77648_6_, int p_77648_7_, float p_77648_8_, float p_77648_9_, float p_77648_10_) {
        if (p_77648_7_ != 1) {
            return false;
        } else if (p_77648_2_.canPlayerEdit(p_77648_4_, p_77648_5_, p_77648_6_, p_77648_7_, p_77648_1_) && p_77648_2_.canPlayerEdit(p_77648_4_, p_77648_5_ + 1, p_77648_6_, p_77648_7_, p_77648_1_)) {
            if (p_77648_3_.getBlock(p_77648_4_, p_77648_5_, p_77648_6_) == this.soilId && p_77648_3_.isAirBlock(p_77648_4_, p_77648_5_ + 1, p_77648_6_)) {
                p_77648_3_.setBlock(p_77648_4_, p_77648_5_ + 1, p_77648_6_, this.field_150908_b);
                --p_77648_1_.stackSize;
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}
