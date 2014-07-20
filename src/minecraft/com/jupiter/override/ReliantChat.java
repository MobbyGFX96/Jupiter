package com.jupiter.override;

import com.jupiter.Jupiter;
import com.jupiter.utils.GuiUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.StringUtils;
import org.lwjgl.opengl.GL11;

/**
 * Created by corey on 18/07/14.
 */
public class ReliantChat extends GuiNewChat {

    public int dragX = 0, dragY = 0, lastDragX = 0, lastDragY = 0;
    public boolean dragging = false;
    private int startY = 0;

    public ReliantChat(Minecraft minecraft) {
        super(minecraft);
    }

    public void chatDragged(int x, int y) {
        dragX = x - lastDragX;
        dragY = y - lastDragY;
    }

    @Override
    public void drawChat(int time) {
        ScaledResolution sr = new ScaledResolution(minecraft, minecraft.displayWidth, minecraft.displayHeight);

        if (this.minecraft.gameSettings.chatVisibility != EntityPlayer.EnumChatVisibility.HIDDEN) {
            byte var2 = 10;
            boolean var3 = false;
            int var4 = 0;
            int var5 = this.chatLines.size();
            float var6 = this.minecraft.gameSettings.chatOpacity * 0.9F + 0.1F;

            if (var5 > 0) {
                if (this.isChatOpen()) {
                    var2 = 20;
                    var3 = true;
                }

                int var7;
                int var9;
                int var12 = 0;

                for (var7 = 0; var7 + this.field_146250_j < (this.chatLines.size()) && var7 < var2; ++var7) {
                    ChatLine chatLine = (ChatLine) this.chatLines.get(var7 + this.field_146250_j);


                    if (chatLine != null) {
                        var9 = time - chatLine.getUpdatedCounter();

                        if (var9 < 200 || var3) {
                            double var10 = (double) var9 / 200.0D;
                            var10 = 1.0D - var10;
                            var10 *= 10.0D;

                            if (var10 < 0.0D) {
                                var10 = 0.0D;
                            }

                            if (var10 > 1.0D) {
                                var10 = 1.0D;
                            }

                            var12 = (int) (255.0D * 1);

                            if (var3) {
                                var12 = 255;
                            }

                            var12 = (int) ((float) var12 * var6);
                            ++var4;
                            if (var12 > 3) {
                                if (minecraft.thePlayer.getTotalArmorValue() > 0 && !minecraft.playerController.isInCreativeMode())
                                    GL11.glTranslatef(0.0F, -10F, 0.0F);
                                byte var13 = 3;
                                int var14 = -var7 * 9;
                                startY = var14 - 1;
                                String var15 = chatLine.getChatLineString();
                                GL11.glEnable(GL11.GL_BLEND);

                                if (!this.minecraft.gameSettings.chatColours) {
                                    var15 = StringUtils.stripControlCodes(var15);
                                }
                                if (minecraft.thePlayer.getTotalArmorValue() > 0 && !minecraft.playerController.isInCreativeMode())
                                    GL11.glTranslatef(0.0F, 10F, 0.0F);
                            }
                        }
                    }
                }

                if (var12 > 0) {
                    if (isChatOpen()) {
                        GuiUtils.getInstance().drawBorderedRect(3 + dragX, startY - 15 + dragY, 337 + dragX, startY - 3 + dragY, 0xFF000000, 0x7F000000);
                        Jupiter.getInstance().getFontManager().getClientFont().drawString("Chat", 5 + dragX, startY - 14 + dragY, 0xFFFFFF);
                    }
                    GuiUtils.getInstance().drawBorderedRect(3 + dragX, startY - 2 + dragY, 337 + dragX, 11 + dragY, 0xFF000000, 0x7F000000);
                }
                for (var7 = 0; var7 + this.field_146250_j < (this.chatLines.size()) && var7 < var2; ++var7) {
                    ChatLine chatLine = (ChatLine) this.chatLines.get(var7 + this.field_146250_j);

                    if (chatLine != null) {
                        var9 = time - chatLine.getUpdatedCounter();

                        if (var9 < 200 || var3) {
                            double var10 = (double) var9 / 200.0D;
                            var10 = 1.0D - var10;
                            var10 *= 10.0D;

                            if (var10 < 0.0D) {
                                var10 = 0.0D;
                            }

                            if (var10 > 1.0D) {
                                var10 = 1.0D;
                            }

                            var12 = (int) (255.0D);

                            if (var3) {
                                var12 = 255;
                            }

                            var12 = (int) ((float) var12 * var6);
                            ++var4;

                            if (var12 > 3) {
                                String chatLineString = chatLine.getChatLineString();
                                byte chatWidth = 3;
                                int chatHeight = -var7 * 9;
                                startY = chatHeight - 1;
                                GL11.glEnable(GL11.GL_BLEND);

                                if (!this.minecraft.gameSettings.chatColours) {
                                    chatLineString = StringUtils.stripControlCodes(chatLineString);
                                }
                                Jupiter.getInstance().getFontManager().getClientFont().drawString(chatLineString, chatWidth + 3 + dragX, chatHeight - 1 + dragY, 0xFFFFFF);
                            }
                        }
                    }
                }

            }
        }
    }

    public void mouseClicked(int x, int y, int b) {
        ScaledResolution sr = new ScaledResolution(minecraft, minecraft.displayWidth, minecraft.displayHeight);
        if (b == 0) {
            if (x >= 3 + dragX && y >= startY - 15 + sr.getScaledHeight() - 48 + dragY && x <= 327 + dragX && y <= startY - 3 + sr.getScaledHeight() - 48 + dragY) {
                //System.out.println("WAT");
                lastDragX = x - dragX;
                lastDragY = y - dragY;
                dragging = true;
            }
        }
    }

    public void mouseMoved(int x, int y, int b) {
        if (b == 0)
            dragging = false;
    }

}
