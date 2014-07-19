package net.minecraft.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class InventoryCrafting implements IInventory {
    private static final String __OBFID = "CL_00001743";
    private ItemStack[] stackList;
    private int inventoryWidth;
    private Container eventHandler;

    public InventoryCrafting(Container p_i1807_1_, int p_i1807_2_, int p_i1807_3_) {
        int var4 = p_i1807_2_ * p_i1807_3_;
        this.stackList = new ItemStack[var4];
        this.eventHandler = p_i1807_1_;
        this.inventoryWidth = p_i1807_2_;
    }

    public int getSizeInventory() {
        return this.stackList.length;
    }

    public ItemStack getStackInSlot(int p_70301_1_) {
        return p_70301_1_ >= this.getSizeInventory() ? null : this.stackList[p_70301_1_];
    }

    public ItemStack getStackInRowAndColumn(int p_70463_1_, int p_70463_2_) {
        if (p_70463_1_ >= 0 && p_70463_1_ < this.inventoryWidth) {
            int var3 = p_70463_1_ + p_70463_2_ * this.inventoryWidth;
            return this.getStackInSlot(var3);
        } else {
            return null;
        }
    }

    public String getInventoryName() {
        return "container.crafting";
    }

    public boolean isInventoryNameLocalized() {
        return false;
    }

    public ItemStack getStackInSlotOnClosing(int p_70304_1_) {
        if (this.stackList[p_70304_1_] != null) {
            ItemStack var2 = this.stackList[p_70304_1_];
            this.stackList[p_70304_1_] = null;
            return var2;
        } else {
            return null;
        }
    }

    public ItemStack decrStackSize(int p_70298_1_, int p_70298_2_) {
        if (this.stackList[p_70298_1_] != null) {
            ItemStack var3;
            if (this.stackList[p_70298_1_].stackSize <= p_70298_2_) {
                var3 = this.stackList[p_70298_1_];
                this.stackList[p_70298_1_] = null;
                this.eventHandler.onCraftMatrixChanged(this);
                return var3;
            } else {
                var3 = this.stackList[p_70298_1_].splitStack(p_70298_2_);
                if (this.stackList[p_70298_1_].stackSize == 0) {
                    this.stackList[p_70298_1_] = null;
                }

                this.eventHandler.onCraftMatrixChanged(this);
                return var3;
            }
        } else {
            return null;
        }
    }

    public void setInventorySlotContents(int p_70299_1_, ItemStack p_70299_2_) {
        this.stackList[p_70299_1_] = p_70299_2_;
        this.eventHandler.onCraftMatrixChanged(this);
    }

    public int getInventoryStackLimit() {
        return 64;
    }

    public void onInventoryChanged() {
    }

    public boolean isUseableByPlayer(EntityPlayer p_70300_1_) {
        return true;
    }

    public void openInventory() {
    }

    public void closeInventory() {
    }

    public boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_) {
        return true;
    }
}
