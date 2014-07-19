package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiButton extends Gui {
    protected static final ResourceLocation field_146122_a = new ResourceLocation("textures/gui/widgets.png");
    private static final String __OBFID = "CL_00000668";
    public int x;
    public int y;
    public String displayString;
    public int id;
    public boolean enabled;
    public boolean field_146125_m;
    protected int width;
    protected int height;
    protected boolean field_146123_n;

    /**
     * Creates a new button with the default width of 200, and height of 20
     *
     * @param text the displayed text.
     * @param id   the action button id number.
     * @param x    the x position of the button.
     * @param y    the y position of the button.
     */
    public GuiButton(String text, int id, int x, int y) {
        this(id, x, y, 200, 20, text);
    }

    public GuiButton(int id, int x, int y, int width, int height, String text) {
        this.width = 200;
        this.height = 20;
        this.enabled = true;
        this.field_146125_m = true;
        this.id = id;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.displayString = text;
    }

    public GuiButton(int id, int x, int y, String format) {
        this(id, x, y, 200, 20, format);
    }

    public int getHoverState(boolean flag) {
        byte var2 = 1;
        if (!this.enabled) {
            var2 = 0;
        } else if (flag) {
            var2 = 2;
        }

        return var2;
    }

    public void drawButton(Minecraft p_146112_1_, int p_146112_2_, int p_146112_3_) {
        if (this.field_146125_m) {
            FontRenderer var4 = p_146112_1_.fontRenderer;
            p_146112_1_.getTextureManager().bindTexture(field_146122_a);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.field_146123_n = p_146112_2_ >= this.x && p_146112_3_ >= this.y && p_146112_2_ < this.x + this.width && p_146112_3_ < this.y + this.height;
            int var5 = this.getHoverState(this.field_146123_n);
            GL11.glEnable(GL11.GL_BLEND);
            OpenGlHelper.glBlendFunc(770, 771, 1, 0);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            this.drawTexturedModalRect(this.x, this.y, 0, 46 + var5 * 20, this.width / 2, this.height);
            this.drawTexturedModalRect(this.x + this.width / 2, this.y, 200 - this.width / 2, 46 + var5 * 20, this.width / 2, this.height);
            this.mouseDragged(p_146112_1_, p_146112_2_, p_146112_3_);
            int var6 = 14737632;
            if (!this.enabled) {
                var6 = 10526880;
            } else if (this.field_146123_n) {
                var6 = 16777120;
            }

            this.drawCenteredString(var4, this.displayString, this.x + this.width / 2, this.y + (this.height - 8) / 2, var6);
        }
    }

    protected void mouseDragged(Minecraft p_146119_1_, int p_146119_2_, int p_146119_3_) {
    }

    public void mouseReleased(int p_146118_1_, int p_146118_2_) {
    }

    public boolean mousePressed(Minecraft p_146116_1_, int x, int y) {
        return this.enabled && this.field_146125_m && x >= this.x && y >= this.y && x < this.x + this.width && y < this.y + this.height;
    }

    public boolean func_146115_a() {
        return this.field_146123_n;
    }

    public void func_146111_b(int p_146111_1_, int p_146111_2_) {
    }

    public void func_146113_a(SoundHandler p_146113_1_) {
        p_146113_1_.playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0F));
    }

    public int func_146117_b() {
        return this.width;
    }

    public int func_154310_c() {
        return this.height;
    }
}
