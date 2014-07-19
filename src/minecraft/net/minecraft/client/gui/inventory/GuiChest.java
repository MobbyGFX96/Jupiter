package net.minecraft.client.gui.inventory;

import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiChest extends GuiContainer {

    private static final ResourceLocation field_147017_u = new ResourceLocation("textures/gui/container/generic_54.png");
    private static final String __OBFID = "CL_00000749";
    private IInventory chestInventory;
    private IInventory playerInventory;
    private int field_147018_x;

    public GuiChest(IInventory chestInventory, IInventory inventory) {
        super(new ContainerChest(chestInventory, inventory));
        this.chestInventory = chestInventory;
        this.playerInventory = inventory;
        this.field_146291_p = false;
        short var3 = 222;
        int var4 = var3 - 108;
        this.field_147018_x = inventory.getSizeInventory() / 9;
        this.field_147000_g = var4 + this.field_147018_x * 18;
    }

    protected void func_146979_b(int p_146979_1_, int p_146979_2_) {
        this.fontRendererObj.drawString(this.playerInventory.isInventoryNameLocalized() ? this.playerInventory.getInventoryName() : I18n.format(this.playerInventory.getInventoryName(), new Object[0]), 8, 6, 4210752);
        this.fontRendererObj.drawString(this.chestInventory.isInventoryNameLocalized() ? this.chestInventory.getInventoryName() : I18n.format(this.chestInventory.getInventoryName(), new Object[0]), 8, this.field_147000_g - 96 + 2, 4210752);
    }

    protected void func_146976_a(float p_146976_1_, int p_146976_2_, int p_146976_3_) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(field_147017_u);
        int var4 = (this.width - this.field_146999_f) / 2;
        int var5 = (this.height - this.field_147000_g) / 2;
        this.drawTexturedModalRect(var4, var5, 0, 0, this.field_146999_f, this.field_147018_x * 18 + 17);
        this.drawTexturedModalRect(var4, var5 + this.field_147018_x * 18 + 17, 0, 126, this.field_146999_f, 96);
    }
}
