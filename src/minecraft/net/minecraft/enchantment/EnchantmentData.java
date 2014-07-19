package net.minecraft.enchantment;

import net.minecraft.util.WeightedRandom;

public class EnchantmentData extends WeightedRandom.Item {
    private static final String __OBFID = "CL_00000115";
    public final Enchantment enchantmentobj;
    public final int enchantmentLevel;

    public EnchantmentData(Enchantment p_i1930_1_, int p_i1930_2_) {
        super(p_i1930_1_.getWeight());
        this.enchantmentobj = p_i1930_1_;
        this.enchantmentLevel = p_i1930_2_;
    }

    public EnchantmentData(int p_i1931_1_, int p_i1931_2_) {
        this(Enchantment.enchantmentsList[p_i1931_1_], p_i1931_2_);
    }
}
