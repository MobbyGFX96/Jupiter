package net.minecraft.client.gui;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.jupiter.Jupiter;
import net.minecraft.client.gui.stream.GuiTwitchUserMode;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.client.C14PacketTabComplete;
import net.minecraft.stats.Achievement;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatList;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import tv.twitch.chat.ChatUserInfo;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class GuiChat extends GuiScreen implements GuiYesNoCallback {

    private static final Set field_152175_f = Sets.newHashSet(new String[]{"http", "https"});
    private static final Logger logger = LogManager.getLogger();
    private static final String __OBFID = "CL_00000682";
    protected GuiTextField guiTextField;
    private String field_146410_g = "";
    private int field_146416_h = -1;
    private boolean field_146417_i;
    private boolean field_146414_r;
    private int field_146413_s;
    private List messageList = new ArrayList();
    private URI field_146411_u;
    private String field_146409_v = "";

    public GuiChat() {
    }

    public GuiChat(String p_i1024_1_) {
        this.field_146409_v = p_i1024_1_;
    }

    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        this.field_146416_h = this.mc.ingameGUI.getChatGUI().getSentMessages().size();
        this.guiTextField = new GuiTextField(Jupiter.getInstance().getFontManager().getClientFont(), 4, this.height - 12, this.width - 4, 12);
        this.guiTextField.func_146203_f(100);
        this.guiTextField.func_146185_a(false);
        this.guiTextField.setFocused(true);
        this.guiTextField.setText(this.field_146409_v);
        this.guiTextField.func_146205_d(false);
    }

    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
        this.mc.ingameGUI.getChatGUI().resetScroll();
    }

    public void updateScreen() {
        this.guiTextField.updateCursorCounter();
    }

    protected void keyTyped(char par1, int par2) {
        this.field_146414_r = false;
        if (par2 == 15) {
            this.completePlayerName();
        } else {
            this.field_146417_i = false;
        }

        if (par2 == 1) {
            this.mc.displayGuiScreen((GuiScreen) null);
        } else if (par2 != 28 && par2 != 156) {
            if (par2 == 200) {
                this.func_146402_a(-1);
            } else if (par2 == 208) {
                this.func_146402_a(1);
            } else if (par2 == 201) {
                this.mc.ingameGUI.getChatGUI().func_146229_b(this.mc.ingameGUI.getChatGUI().func_146232_i() - 1);
            } else if (par2 == 209) {
                this.mc.ingameGUI.getChatGUI().func_146229_b(-this.mc.ingameGUI.getChatGUI().func_146232_i() + 1);
            } else {
                this.guiTextField.textboxKeyTyped(par1, par2);
            }
        } else {
            String var3 = this.guiTextField.getText().trim();
            if (var3.length() > 0) {
                this.func_146403_a(var3);
            }

            this.mc.displayGuiScreen(null);
        }
    }

    public void func_146403_a(String p_146403_1_) {
        this.mc.ingameGUI.getChatGUI().addSentMessage(p_146403_1_);
        this.mc.thePlayer.sendChatMessage(p_146403_1_);
    }

    public void handleMouseInput() {
        super.handleMouseInput();
        int var1 = Mouse.getEventDWheel();
        if (var1 != 0) {
            if (var1 > 1) {
                var1 = 1;
            }

            if (var1 < -1) {
                var1 = -1;
            }

            if (!isShiftKeyDown()) {
                var1 *= 7;
            }

            this.mc.ingameGUI.getChatGUI().func_146229_b(var1);
        }
    }

    protected void mouseClicked(int x, int y, int b) {
        mc.ingameGUI.getChatGUI().mouseClicked(x, y, b);
        if (b == 0 && this.mc.gameSettings.chatLinks) {
            IChatComponent chatComponent = this.mc.ingameGUI.getChatGUI().func_146236_a(Mouse.getX(), Mouse.getY());
            if (chatComponent != null) {
                ClickEvent chatClickEvent = chatComponent.getChatStyle().getChatClickEvent();
                if (chatClickEvent != null) {
                    if (isShiftKeyDown()) {
                        this.guiTextField.func_146191_b(chatComponent.getUnformattedTextForChat());
                    } else {
                        URI uri;
                        if (chatClickEvent.getAction() == ClickEvent.Action.OPEN_URL) {
                            try {
                                uri = new URI(chatClickEvent.getValue());
                                if (!field_152175_f.contains(uri.getScheme().toLowerCase())) {
                                    throw new URISyntaxException(chatClickEvent.getValue(), "Unsupported protocol: " + uri.getScheme().toLowerCase());
                                }

                                if (this.mc.gameSettings.chatLinksPrompt) {
                                    this.field_146411_u = uri;
                                    this.mc.displayGuiScreen(new GuiConfirmOpenLink(this, chatClickEvent.getValue(), 0, false));
                                } else {
                                    this.openUrl(uri);
                                }
                            } catch (URISyntaxException var7) {
                                logger.error("Can\'t open url for " + chatClickEvent, var7);
                            }
                        } else if (chatClickEvent.getAction() == ClickEvent.Action.OPEN_FILE) {
                            uri = (new File(chatClickEvent.getValue())).toURI();
                            this.openUrl(uri);
                        } else if (chatClickEvent.getAction() == ClickEvent.Action.SUGGEST_COMMAND) {
                            this.guiTextField.setText(chatClickEvent.getValue());
                        } else if (chatClickEvent.getAction() == ClickEvent.Action.RUN_COMMAND) {
                            this.func_146403_a(chatClickEvent.getValue());
                        } else if (chatClickEvent.getAction() == ClickEvent.Action.TWITCH_USER_INFO) {
                            ChatUserInfo var8 = this.mc.func_152346_Z().func_152926_a(chatClickEvent.getValue());
                            if (var8 != null) {
                                this.mc.displayGuiScreen(new GuiTwitchUserMode(this.mc.func_152346_Z(), var8));
                            } else {
                                logger.error("Tried to handle twitch user but couldn\'t find them!");
                            }
                        } else {
                            logger.error("Don\'t know how to handle " + chatClickEvent);
                        }
                    }

                    return;
                }
            }
        }

        this.guiTextField.mouseClicked(x, y, b);
        super.mouseClicked(x, y, b);
    }

    @Override
    protected void mouseMovedOrUp(int x, int y, int b) {
        mc.ingameGUI.getChatGUI().mouseMoved(x, y, b);
    }

    public void confirmClicked(boolean p_73878_1_, int p_73878_2_) {
        if (p_73878_2_ == 0) {
            if (p_73878_1_) {
                this.openUrl(this.field_146411_u);
            }

            this.field_146411_u = null;
            this.mc.displayGuiScreen(this);
        }
    }

    private void openUrl(URI url) {
        try {
            Class var2 = Class.forName("java.awt.Desktop");
            Object var3 = var2.getMethod("getDesktop", new Class[0]).invoke((Object) null, new Object[0]);
            var2.getMethod("browse", new Class[]{URI.class}).invoke(var3, new Object[]{url});
        } catch (Throwable var4) {
            logger.error("Couldn\'t open link", var4);
        }
    }

    public void completePlayerName() {
        String var3;
        if (this.field_146417_i) {
            this.guiTextField.func_146175_b(this.guiTextField.func_146197_a(-1, this.guiTextField.func_146198_h(), false) - this.guiTextField.func_146198_h());
            if (this.field_146413_s >= this.messageList.size()) {
                this.field_146413_s = 0;
            }
        } else {
            int var1 = this.guiTextField.func_146197_a(-1, this.guiTextField.func_146198_h(), false);
            this.messageList.clear();
            this.field_146413_s = 0;
            String var2 = this.guiTextField.getText().substring(var1).toLowerCase();
            var3 = this.guiTextField.getText().substring(0, this.guiTextField.func_146198_h());
            this.func_146405_a(var3, var2);
            if (this.messageList.isEmpty()) {
                return;
            }

            this.field_146417_i = true;
            this.guiTextField.func_146175_b(var1 - this.guiTextField.func_146198_h());
        }

        if (this.messageList.size() > 1) {
            StringBuilder var4 = new StringBuilder();

            for (Iterator var5 = this.messageList.iterator(); var5.hasNext(); var4.append(var3)) {
                var3 = (String) var5.next();
                if (var4.length() > 0) {
                    var4.append(", ");
                }
            }

            this.mc.ingameGUI.getChatGUI().func_146234_a(new ChatComponentText(var4.toString()), 1);
        }

        this.guiTextField.func_146191_b((String) this.messageList.get(this.field_146413_s++));
    }

    private void func_146405_a(String p_146405_1_, String p_146405_2_) {
        if (p_146405_1_.length() >= 1) {
            this.mc.thePlayer.sendQueue.addToSendQueue(new C14PacketTabComplete(p_146405_1_));
            this.field_146414_r = true;
        }
    }

    public void func_146402_a(int p_146402_1_) {
        int var2 = this.field_146416_h + p_146402_1_;
        int var3 = this.mc.ingameGUI.getChatGUI().getSentMessages().size();
        if (var2 < 0) {
            var2 = 0;
        }

        if (var2 > var3) {
            var2 = var3;
        }

        if (var2 != this.field_146416_h) {
            if (var2 == var3) {
                this.field_146416_h = var3;
                this.guiTextField.setText(this.field_146410_g);
            } else {
                if (this.field_146416_h == var3) {
                    this.field_146410_g = this.guiTextField.getText();
                }

                this.guiTextField.setText((String) this.mc.ingameGUI.getChatGUI().getSentMessages().get(var2));
                this.field_146416_h = var2;
            }
        }
    }

    public void drawScreen(int x, int y, float f) {
        drawRect(2, this.height - 14, this.width - 2, this.height - 2, Integer.MIN_VALUE);
        this.guiTextField.drawTextBox();

        if (mc.ingameGUI.getChatGUI().dragging) {
            mc.ingameGUI.getChatGUI().chatDragged(x, y);
        }

        IChatComponent var4 = this.mc.ingameGUI.getChatGUI().func_146236_a(Mouse.getX(), Mouse.getY());
        if (var4 != null && var4.getChatStyle().getChatHoverEvent() != null) {
            HoverEvent var5 = var4.getChatStyle().getChatHoverEvent();
            if (var5.getAction() == HoverEvent.Action.SHOW_ITEM) {
                ItemStack var6 = null;

                try {
                    NBTBase var7 = JsonToNBT.func_150315_a(var5.getValue().getUnformattedText());
                    if (var7 != null && var7 instanceof NBTTagCompound) {
                        var6 = ItemStack.loadItemStackFromNBT((NBTTagCompound) var7);
                    }
                } catch (NBTException var11) {
                    ;
                }

                if (var6 != null) {
                    this.func_146285_a(var6, x, y);
                } else {
                    this.func_146279_a(EnumChatFormatting.RED + "Invalid Item!", x, y);
                }
            } else if (var5.getAction() == HoverEvent.Action.SHOW_TEXT) {
                this.func_146283_a(Splitter.on("\n").splitToList(var5.getValue().getFormattedText()), x, y);
            } else if (var5.getAction() == HoverEvent.Action.SHOW_ACHIEVEMENT) {
                StatBase var12 = StatList.func_151177_a(var5.getValue().getUnformattedText());
                if (var12 != null) {
                    IChatComponent var13 = var12.func_150951_e();
                    ChatComponentTranslation var8 = new ChatComponentTranslation("stats.tooltip.type." + (var12.isAchievement() ? "achievement" : "statistic"), new Object[0]);
                    var8.getChatStyle().setItalic(Boolean.valueOf(true));
                    String var9 = var12 instanceof Achievement ? ((Achievement) var12).getDescription() : null;
                    ArrayList var10 = Lists.newArrayList(new String[]{var13.getFormattedText(), var8.getFormattedText()});
                    if (var9 != null) {
                        var10.addAll(this.fontRendererObj.listFormattedStringToWidth(var9, 150));
                    }

                    this.func_146283_a(var10, x, y);
                } else {
                    this.func_146279_a(EnumChatFormatting.RED + "Invalid statistic/achievement!", x, y);
                }
            }

            GL11.glDisable(GL11.GL_LIGHTING);
        }

        super.drawScreen(x, y, f);
    }

    public void func_146406_a(String[] p_146406_1_) {
        if (this.field_146414_r) {
            this.field_146417_i = false;
            this.messageList.clear();
            String[] var2 = p_146406_1_;
            int var3 = p_146406_1_.length;

            for (int var4 = 0; var4 < var3; ++var4) {
                String var5 = var2[var4];
                if (var5.length() > 0) {
                    this.messageList.add(var5);
                }
            }

            String var6 = this.guiTextField.getText().substring(this.guiTextField.func_146197_a(-1, this.guiTextField.func_146198_h(), false));
            String var7 = StringUtils.getCommonPrefix(p_146406_1_);
            if (var7.length() > 0 && !var6.equalsIgnoreCase(var7)) {
                this.guiTextField.func_146175_b(this.guiTextField.func_146197_a(-1, this.guiTextField.func_146198_h(), false) - this.guiTextField.func_146198_h());
                this.guiTextField.func_146191_b(var7);
            } else if (this.messageList.size() > 0) {
                this.field_146417_i = true;
                this.completePlayerName();
            }
        }
    }

    public boolean doesGuiPauseGame() {
        return false;
    }
}
