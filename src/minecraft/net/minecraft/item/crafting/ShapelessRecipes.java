package net.minecraft.item.crafting;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ShapelessRecipes implements IRecipe {
    private static final String __OBFID = "CL_00000094";
    private final ItemStack recipeOutput;
    private final List recipeItems;

    public ShapelessRecipes(ItemStack p_i1918_1_, List p_i1918_2_) {
        this.recipeOutput = p_i1918_1_;
        this.recipeItems = p_i1918_2_;
    }

    public ItemStack getRecipeOutput() {
        return this.recipeOutput;
    }

    public boolean matches(InventoryCrafting p_77569_1_, World p_77569_2_) {
        ArrayList var3 = new ArrayList(this.recipeItems);

        for (int var4 = 0; var4 < 3; ++var4) {
            for (int var5 = 0; var5 < 3; ++var5) {
                ItemStack var6 = p_77569_1_.getStackInRowAndColumn(var5, var4);
                if (var6 != null) {
                    boolean var7 = false;
                    Iterator var8 = var3.iterator();

                    while (var8.hasNext()) {
                        ItemStack var9 = (ItemStack) var8.next();
                        if (var6.getItem() == var9.getItem() && (var9.getItemDamage() == 32767 || var6.getItemDamage() == var9.getItemDamage())) {
                            var7 = true;
                            var3.remove(var9);
                            break;
                        }
                    }

                    if (!var7) {
                        return false;
                    }
                }
            }
        }

        return var3.isEmpty();
    }

    public ItemStack getCraftingResult(InventoryCrafting p_77572_1_) {
        return this.recipeOutput.copy();
    }

    public int getRecipeSize() {
        return this.recipeItems.size();
    }
}
