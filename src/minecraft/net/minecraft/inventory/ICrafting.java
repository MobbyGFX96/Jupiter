package net.minecraft.inventory;

import net.minecraft.item.ItemStack;

import java.util.List;

public interface ICrafting {
    void sendContainerAndContentsToPlayer(Container p_71110_1_, List p_71110_2_);

    void sendSlotContents(Container p_71111_1_, int p_71111_2_, ItemStack p_71111_3_);

    void sendProgressBarUpdate(Container p_71112_1_, int p_71112_2_, int p_71112_3_);
}
