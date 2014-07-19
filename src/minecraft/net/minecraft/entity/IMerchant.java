package net.minecraft.entity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;

public interface IMerchant {
    EntityPlayer getCustomer();

    void setCustomer(EntityPlayer p_70932_1_);

    MerchantRecipeList getRecipes(EntityPlayer p_70934_1_);

    void setRecipes(MerchantRecipeList p_70930_1_);

    void useRecipe(MerchantRecipe p_70933_1_);

    void func_110297_a_(ItemStack p_110297_1_);
}