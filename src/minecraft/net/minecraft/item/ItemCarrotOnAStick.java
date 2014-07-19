package net.minecraft.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.world.World;

public class ItemCarrotOnAStick extends Item {
    private static final String __OBFID = "CL_00000001";

    public ItemCarrotOnAStick() {
        this.setCreativeTab(CreativeTabs.tabTransport);
        this.setMaxStackSize(1);
        this.setMaxDamage(25);
    }

    public boolean isFull3D() {
        return true;
    }

    public boolean shouldRotateAroundWhenRendering() {
        return true;
    }

    public ItemStack onItemRightClick(ItemStack p_77659_1_, World p_77659_2_, EntityPlayer p_77659_3_) {
        if (p_77659_3_.isRiding() && p_77659_3_.ridingEntity instanceof EntityPig) {
            EntityPig var4 = (EntityPig) p_77659_3_.ridingEntity;
            if (var4.getAIControlledByPlayer().isControlledByPlayer() && p_77659_1_.getMaxDamage() - p_77659_1_.getItemDamage() >= 7) {
                var4.getAIControlledByPlayer().boostSpeed();
                p_77659_1_.damageItem(7, p_77659_3_);
                if (p_77659_1_.stackSize == 0) {
                    ItemStack var5 = new ItemStack(Items.fishing_rod);
                    var5.setTagCompound(p_77659_1_.stackTagCompound);
                    return var5;
                }
            }
        }

        return p_77659_1_;
    }
}
