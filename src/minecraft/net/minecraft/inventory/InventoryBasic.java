package net.minecraft.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class InventoryBasic implements IInventory {
    private static final String __OBFID = "CL_00001514";
    private String inventoryTitle;
    private int slotsCount;
    private ItemStack[] inventoryContents;
    private List field_70480_d;
    private boolean field_94051_e;

    public InventoryBasic(String p_i1561_1_, boolean p_i1561_2_, int p_i1561_3_) {
        this.inventoryTitle = p_i1561_1_;
        this.field_94051_e = p_i1561_2_;
        this.slotsCount = p_i1561_3_;
        this.inventoryContents = new ItemStack[p_i1561_3_];
    }

    public void func_110134_a(IInvBasic p_110134_1_) {
        if (this.field_70480_d == null) {
            this.field_70480_d = new ArrayList();
        }

        this.field_70480_d.add(p_110134_1_);
    }

    public void func_110132_b(IInvBasic p_110132_1_) {
        this.field_70480_d.remove(p_110132_1_);
    }

    public ItemStack getStackInSlot(int p_70301_1_) {
        return p_70301_1_ >= 0 && p_70301_1_ < this.inventoryContents.length ? this.inventoryContents[p_70301_1_] : null;
    }

    public ItemStack decrStackSize(int p_70298_1_, int p_70298_2_) {
        if (this.inventoryContents[p_70298_1_] != null) {
            ItemStack var3;
            if (this.inventoryContents[p_70298_1_].stackSize <= p_70298_2_) {
                var3 = this.inventoryContents[p_70298_1_];
                this.inventoryContents[p_70298_1_] = null;
                this.onInventoryChanged();
                return var3;
            } else {
                var3 = this.inventoryContents[p_70298_1_].splitStack(p_70298_2_);
                if (this.inventoryContents[p_70298_1_].stackSize == 0) {
                    this.inventoryContents[p_70298_1_] = null;
                }

                this.onInventoryChanged();
                return var3;
            }
        } else {
            return null;
        }
    }

    public ItemStack getStackInSlotOnClosing(int p_70304_1_) {
        if (this.inventoryContents[p_70304_1_] != null) {
            ItemStack var2 = this.inventoryContents[p_70304_1_];
            this.inventoryContents[p_70304_1_] = null;
            return var2;
        } else {
            return null;
        }
    }

    public void setInventorySlotContents(int p_70299_1_, ItemStack p_70299_2_) {
        this.inventoryContents[p_70299_1_] = p_70299_2_;
        if (p_70299_2_ != null && p_70299_2_.stackSize > this.getInventoryStackLimit()) {
            p_70299_2_.stackSize = this.getInventoryStackLimit();
        }

        this.onInventoryChanged();
    }

    public int getSizeInventory() {
        return this.slotsCount;
    }

    public String getInventoryName() {
        return this.inventoryTitle;
    }

    public boolean isInventoryNameLocalized() {
        return this.field_94051_e;
    }

    public void func_110133_a(String p_110133_1_) {
        this.field_94051_e = true;
        this.inventoryTitle = p_110133_1_;
    }

    public int getInventoryStackLimit() {
        return 64;
    }

    public void onInventoryChanged() {
        if (this.field_70480_d != null) {
            for (int var1 = 0; var1 < this.field_70480_d.size(); ++var1) {
                ((IInvBasic) this.field_70480_d.get(var1)).onInventoryChanged(this);
            }
        }
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
