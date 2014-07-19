package net.minecraft.item;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class ItemFishingRod extends Item {
    private static final String __OBFID = "CL_00000034";
    private IIcon theIcon;

    public ItemFishingRod() {
        this.setMaxDamage(64);
        this.setMaxStackSize(1);
        this.setCreativeTab(CreativeTabs.tabTools);
    }

    public boolean isFull3D() {
        return true;
    }

    public boolean shouldRotateAroundWhenRendering() {
        return true;
    }

    public ItemStack onItemRightClick(ItemStack p_77659_1_, World p_77659_2_, EntityPlayer p_77659_3_) {
        if (p_77659_3_.fishEntity != null) {
            int var4 = p_77659_3_.fishEntity.func_146034_e();
            p_77659_1_.damageItem(var4, p_77659_3_);
            p_77659_3_.swingItem();
        } else {
            p_77659_2_.playSoundAtEntity(p_77659_3_, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
            if (!p_77659_2_.isClient) {
                p_77659_2_.spawnEntityInWorld(new EntityFishHook(p_77659_2_, p_77659_3_));
            }

            p_77659_3_.swingItem();
        }

        return p_77659_1_;
    }

    public void registerIcons(IIconRegister p_94581_1_) {
        this.itemIcon = p_94581_1_.registerIcon(this.getIconString() + "_uncast");
        this.theIcon = p_94581_1_.registerIcon(this.getIconString() + "_cast");
    }

    public IIcon func_94597_g() {
        return this.theIcon;
    }

    public boolean isItemTool(ItemStack p_77616_1_) {
        return super.isItemTool(p_77616_1_);
    }

    public int getItemEnchantability() {
        return 1;
    }
}
