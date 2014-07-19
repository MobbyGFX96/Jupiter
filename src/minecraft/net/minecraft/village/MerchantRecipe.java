package net.minecraft.village;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class MerchantRecipe {
    private static final String __OBFID = "CL_00000126";
    private ItemStack itemToBuy;
    private ItemStack secondItemToBuy;
    private ItemStack itemToSell;
    private int toolUses;
    private int maxTradeUses;

    public MerchantRecipe(NBTTagCompound p_i1940_1_) {
        this.readFromTags(p_i1940_1_);
    }

    public MerchantRecipe(ItemStack p_i1941_1_, ItemStack p_i1941_2_, ItemStack p_i1941_3_) {
        this.itemToBuy = p_i1941_1_;
        this.secondItemToBuy = p_i1941_2_;
        this.itemToSell = p_i1941_3_;
        this.maxTradeUses = 7;
    }

    public MerchantRecipe(ItemStack p_i1942_1_, ItemStack p_i1942_2_) {
        this(p_i1942_1_, (ItemStack) null, p_i1942_2_);
    }

    public MerchantRecipe(ItemStack p_i1943_1_, Item p_i1943_2_) {
        this(p_i1943_1_, new ItemStack(p_i1943_2_));
    }

    public ItemStack getItemToBuy() {
        return this.itemToBuy;
    }

    public ItemStack getSecondItemToBuy() {
        return this.secondItemToBuy;
    }

    public boolean hasSecondItemToBuy() {
        return this.secondItemToBuy != null;
    }

    public ItemStack getItemToSell() {
        return this.itemToSell;
    }

    public boolean hasSameIDsAs(MerchantRecipe p_77393_1_) {
        return this.itemToBuy.getItem() == p_77393_1_.itemToBuy.getItem() && this.itemToSell.getItem() == p_77393_1_.itemToSell.getItem() ? this.secondItemToBuy == null && p_77393_1_.secondItemToBuy == null || this.secondItemToBuy != null && p_77393_1_.secondItemToBuy != null && this.secondItemToBuy.getItem() == p_77393_1_.secondItemToBuy.getItem() : false;
    }

    public boolean hasSameItemsAs(MerchantRecipe p_77391_1_) {
        return this.hasSameIDsAs(p_77391_1_) && (this.itemToBuy.stackSize < p_77391_1_.itemToBuy.stackSize || this.secondItemToBuy != null && this.secondItemToBuy.stackSize < p_77391_1_.secondItemToBuy.stackSize);
    }

    public void incrementToolUses() {
        ++this.toolUses;
    }

    public void func_82783_a(int p_82783_1_) {
        this.maxTradeUses += p_82783_1_;
    }

    public boolean isRecipeDisabled() {
        return this.toolUses >= this.maxTradeUses;
    }

    public void func_82785_h() {
        this.toolUses = this.maxTradeUses;
    }

    public void readFromTags(NBTTagCompound p_77390_1_) {
        NBTTagCompound var2 = p_77390_1_.getCompoundTag("buy");
        this.itemToBuy = ItemStack.loadItemStackFromNBT(var2);
        NBTTagCompound var3 = p_77390_1_.getCompoundTag("sell");
        this.itemToSell = ItemStack.loadItemStackFromNBT(var3);
        if (p_77390_1_.func_150297_b("buyB", 10)) {
            this.secondItemToBuy = ItemStack.loadItemStackFromNBT(p_77390_1_.getCompoundTag("buyB"));
        }

        if (p_77390_1_.func_150297_b("uses", 99)) {
            this.toolUses = p_77390_1_.getInteger("uses");
        }

        if (p_77390_1_.func_150297_b("maxUses", 99)) {
            this.maxTradeUses = p_77390_1_.getInteger("maxUses");
        } else {
            this.maxTradeUses = 7;
        }
    }

    public NBTTagCompound writeToTags() {
        NBTTagCompound var1 = new NBTTagCompound();
        var1.setTag("buy", this.itemToBuy.writeToNBT(new NBTTagCompound()));
        var1.setTag("sell", this.itemToSell.writeToNBT(new NBTTagCompound()));
        if (this.secondItemToBuy != null) {
            var1.setTag("buyB", this.secondItemToBuy.writeToNBT(new NBTTagCompound()));
        }

        var1.setInteger("uses", this.toolUses);
        var1.setInteger("maxUses", this.maxTradeUses);
        return var1;
    }
}
