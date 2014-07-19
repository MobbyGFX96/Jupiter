package net.minecraft.client.gui;

import com.jupiter.utils.CustomButton;
import com.jupiter.utils.GuiUtils;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.ISaveFormat;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GLContext;

import java.io.BufferedReader;
import java.net.URI;
import java.util.Random;

public class GuiMainMenu extends GuiScreen implements GuiYesNoCallback {

    public static final String field_96138_a = "Please click " + EnumChatFormatting.UNDERLINE + "here" + EnumChatFormatting.RESET + " for more information.";
    private static final Logger logger = LogManager.getLogger();
    private static final Random rand = new Random();
    private static final ResourceLocation[] titlePanoramaPaths = new ResourceLocation[]{new ResourceLocation("textures/gui/title/background/panorama_0.png"), new ResourceLocation("textures/gui/title/background/panorama_1.png"), new ResourceLocation("textures/gui/title/background/panorama_2.png"), new ResourceLocation("textures/gui/title/background/panorama_3.png"), new ResourceLocation("textures/gui/title/background/panorama_4.png"), new ResourceLocation("textures/gui/title/background/panorama_5.png")};
    private static final ResourceLocation logoBackground = new ResourceLocation("textures/gui/title/logoBackground.jpg");
    private static final String __OBFID = "CL_00001154";
    private final Object field_104025_t = new Object();
    private float updateCounter;
    private int panoramaTimer;
    private DynamicTexture viewportTexture;
    private String field_92025_p;
    private String field_146972_A;
    private String field_104024_v;
    private int field_92024_r;
    private int field_92023_s;
    private int field_92022_t;
    private int field_92021_u;
    private int field_92020_v;
    private int field_92019_w;
    private ResourceLocation field_110351_G;

    public GuiMainMenu() {
        this.field_146972_A = field_96138_a;
        BufferedReader var1 = null;

        this.updateCounter = rand.nextFloat();
        this.field_92025_p = "";
        if (!GLContext.getCapabilities().OpenGL20 && !OpenGlHelper.func_153193_b()) {
            this.field_92025_p = I18n.format("title.oldgl1", new Object[0]);
            this.field_146972_A = I18n.format("title.oldgl2", new Object[0]);
            this.field_104024_v = "https://help.mojang.com/customer/portal/articles/325948?ref=game";
        }
    }

    public void updateScreen() {
        ++this.panoramaTimer;
    }

    public boolean doesGuiPauseGame() {
        return false;
    }

    protected void keyTyped(char p_73869_1_, int p_73869_2_) {
    }

    public void initGui() {
        this.viewportTexture = new DynamicTexture(256, 256);
        this.field_110351_G = this.mc.getTextureManager().getDynamicTextureLocation("background", this.viewportTexture);

        int var3 = this.height / 4 + 48;
        this.addButtons(var3, 24);

        Object var4 = this.field_104025_t;
        synchronized (this.field_104025_t) {
            this.field_92023_s = this.fontRendererObj.getStringWidth(this.field_92025_p);
            this.field_92024_r = this.fontRendererObj.getStringWidth(this.field_146972_A);
            int var5 = Math.max(this.field_92023_s, this.field_92024_r);
            this.field_92022_t = (this.width - var5) / 2;
            this.field_92021_u = ((GuiButton) this.buttonList.get(0)).y - 24;
            this.field_92020_v = this.field_92022_t + var5;
            this.field_92019_w = this.field_92021_u + 24;
        }
    }

    private void addButtons(int par1, int par2) {
        buttonList.add(new CustomButton("Singleplayer", 1, width / 2 - 100, par1, 200, 20));
        buttonList.add(new CustomButton("Multiplayer", 2, width / 2 - 100, par1 + par2 * 1, 200, 20));
        buttonList.add(new CustomButton("Managers", 3, width / 2 - 100, par1 + par2 * 2));
        buttonList.add(new CustomButton("Options", 0, width / 2 - 100, par1 + par2 * 3, 98, 20));
        buttonList.add(new CustomButton("Quit Game", 4, width / 2 + 2, par1 + par2 * 3, 98, 20));
    }

    protected void actionPerformed(GuiButton button) {
        if (button.id == 0) {
            this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
        }

        if (button.id == 1) {
            this.mc.displayGuiScreen(new GuiSelectWorld(this));
        }

        if (button.id == 2) {
            this.mc.displayGuiScreen(new GuiMultiplayer(this));
        }

        if (button.id == 4) {
            this.mc.shutdown();
        }
    }

    public void confirmClicked(boolean p_73878_1_, int p_73878_2_) {
        if (p_73878_1_ && p_73878_2_ == 12) {
            ISaveFormat var6 = this.mc.getSaveLoader();
            var6.flushCache();
            var6.deleteWorldDirectory("Demo_World");
            this.mc.displayGuiScreen(this);
        } else if (p_73878_2_ == 13) {
            if (p_73878_1_) {
                try {
                    Class var3 = Class.forName("java.awt.Desktop");
                    Object var4 = var3.getMethod("getDesktop", new Class[0]).invoke((Object) null, new Object[0]);
                    var3.getMethod("browse", new Class[]{URI.class}).invoke(var4, new Object[]{new URI(this.field_104024_v)});
                } catch (Throwable var5) {
                    logger.error("Couldn\'t open link", var5);
                }
            }

            this.mc.displayGuiScreen(this);
        }
    }

    public void drawScreen(int x, int y, float f) {
        mc.getTextureManager().bindTexture(logoBackground);
        GuiUtils.getInstance().drawTexturedRectangle(logoBackground.getResourcePath(), 0, 0, width, height, 0, 0, 248, 228);

        if (this.field_92025_p != null && this.field_92025_p.length() > 0) {
            drawRect(this.field_92022_t - 2, this.field_92021_u - 2, this.field_92020_v + 2, this.field_92019_w - 1, 1428160512);
            this.drawString(this.fontRendererObj, this.field_92025_p, this.field_92022_t, this.field_92021_u, -1);
            this.drawString(this.fontRendererObj, this.field_146972_A, (this.width - this.field_92024_r) / 2, ((GuiButton) this.buttonList.get(0)).y - 12, -1);
        }

        super.drawScreen(x, y, f);
    }

    protected void mouseClicked(int p_73864_1_, int p_73864_2_, int p_73864_3_) {
        super.mouseClicked(p_73864_1_, p_73864_2_, p_73864_3_);
        Object var4 = this.field_104025_t;
        synchronized (this.field_104025_t) {
            if (this.field_92025_p.length() > 0 && p_73864_1_ >= this.field_92022_t && p_73864_1_ <= this.field_92020_v && p_73864_2_ >= this.field_92021_u && p_73864_2_ <= this.field_92019_w) {
                GuiConfirmOpenLink var5 = new GuiConfirmOpenLink(this, this.field_104024_v, 13, true);
                var5.func_146358_g();
                this.mc.displayGuiScreen(var5);
            }
        }
    }
}
