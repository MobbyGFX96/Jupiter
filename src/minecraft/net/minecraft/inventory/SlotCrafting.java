package net.minecraft.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.*;
import net.minecraft.stats.AchievementList;

public class SlotCrafting extends Slot {
    private static final String __OBFID = "CL_00001761";
    private final IInventory craftMatrix;
    private EntityPlayer thePlayer;
    private int amountCrafted;

    public SlotCrafting(EntityPlayer p_i1823_1_, IInventory p_i1823_2_, IInventory p_i1823_3_, int p_i1823_4_, int p_i1823_5_, int p_i1823_6_) {
        super(p_i1823_3_, p_i1823_4_, p_i1823_5_, p_i1823_6_);
        this.thePlayer = p_i1823_1_;
        this.craftMatrix = p_i1823_2_;
    }

    public boolean isItemValid(ItemStack p_75214_1_) {
        return false;
    }

    public ItemStack decrStackSize(int p_75209_1_) {
        if (this.getHasStack()) {
            this.amountCrafted += Math.min(p_75209_1_, this.getStack().stackSize);
        }

        return super.decrStackSize(p_75209_1_);
    }

    protected void onCrafting(ItemStack p_75210_1_, int p_75210_2_) {
        this.amountCrafted += p_75210_2_;
        this.onCrafting(p_75210_1_);
    }

    protected void onCrafting(ItemStack p_75208_1_) {
        p_75208_1_.onCrafting(this.thePlayer.worldObj, this.thePlayer, this.amountCrafted);
        this.amountCrafted = 0;
        if (p_75208_1_.getItem() == Item.getItemFromBlock(Blocks.crafting_table)) {
            this.thePlayer.addStat(AchievementList.buildWorkBench, 1);
        }

        if (p_75208_1_.getItem() instanceof ItemPickaxe) {
            this.thePlayer.addStat(AchievementList.buildPickaxe, 1);
        }

        if (p_75208_1_.getItem() == Item.getItemFromBlock(Blocks.furnace)) {
            this.thePlayer.addStat(AchievementList.buildFurnace, 1);
        }

        if (p_75208_1_.getItem() instanceof ItemHoe) {
            this.thePlayer.addStat(AchievementList.buildHoe, 1);
        }

        if (p_75208_1_.getItem() == Items.bread) {
            this.thePlayer.addStat(AchievementList.makeBread, 1);
        }

        if (p_75208_1_.getItem() == Items.cake) {
            this.thePlayer.addStat(AchievementList.bakeCake, 1);
        }

        if (p_75208_1_.getItem() instanceof ItemPickaxe && ((ItemPickaxe) p_75208_1_.getItem()).func_150913_i() != Item.ToolMaterial.WOOD) {
            this.thePlayer.addStat(AchievementList.buildBetterPickaxe, 1);
        }

        if (p_75208_1_.getItem() instanceof ItemSword) {
            this.thePlayer.addStat(AchievementList.buildSword, 1);
        }

        if (p_75208_1_.getItem() == Item.getItemFromBlock(Blocks.enchanting_table)) {
            this.thePlayer.addStat(AchievementList.enchantments, 1);
        }

        if (p_75208_1_.getItem() == Item.getItemFromBlock(Blocks.bookshelf)) {
            this.thePlayer.addStat(AchievementList.bookcase, 1);
        }
    }

    public void onPickupFromSlot(EntityPlayer p_82870_1_, ItemStack p_82870_2_) {
        this.onCrafting(p_82870_2_);

        for (int var3 = 0; var3 < this.craftMatrix.getSizeInventory(); ++var3) {
            ItemStack var4 = this.craftMatrix.getStackInSlot(var3);
            if (var4 != null) {
                this.craftMatrix.decrStackSize(var3, 1);
                if (var4.getItem().hasContainerItem()) {
                    ItemStack var5 = new ItemStack(var4.getItem().getContainerItem());
                    if (!var4.getItem().doesContainerItemLeaveCraftingGrid(var4) || !this.thePlayer.inventory.addItemStackToInventory(var5)) {
                        if (this.craftMatrix.getStackInSlot(var3) == null) {
                            this.craftMatrix.setInventorySlotContents(var3, var5);
                        } else {
                            this.thePlayer.dropPlayerItemWithRandomChoice(var5, false);
                        }
                    }
                }
            }
        }
    }
}
