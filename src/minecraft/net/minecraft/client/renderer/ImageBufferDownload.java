package net.minecraft.client.renderer;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.ImageObserver;

public class ImageBufferDownload implements IImageBuffer {
    private static final String __OBFID = "CL_00000956";
    private int[] imageData;
    private int imageWidth;
    private int imageHeight;

    public BufferedImage parseUserSkin(BufferedImage p_78432_1_) {
        if (p_78432_1_ == null) {
            return null;
        } else {
            this.imageWidth = 64;
            this.imageHeight = 32;
            BufferedImage var2 = new BufferedImage(this.imageWidth, this.imageHeight, 2);
            Graphics var3 = var2.getGraphics();
            var3.drawImage(p_78432_1_, 0, 0, (ImageObserver) null);
            var3.dispose();
            this.imageData = ((DataBufferInt) var2.getRaster().getDataBuffer()).getData();
            this.setAreaOpaque(0, 0, 32, 16);
            this.setAreaTransparent(32, 0, 64, 32);
            this.setAreaOpaque(0, 16, 64, 32);
            return var2;
        }
    }

    public void func_152634_a() {
    }

    private void setAreaTransparent(int p_78434_1_, int p_78434_2_, int p_78434_3_, int p_78434_4_) {
        if (!this.hasTransparency(p_78434_1_, p_78434_2_, p_78434_3_, p_78434_4_)) {
            for (int var5 = p_78434_1_; var5 < p_78434_3_; ++var5) {
                for (int var6 = p_78434_2_; var6 < p_78434_4_; ++var6) {
                    this.imageData[var5 + var6 * this.imageWidth] &= 16777215;
                }
            }
        }
    }

    private void setAreaOpaque(int p_78433_1_, int p_78433_2_, int p_78433_3_, int p_78433_4_) {
        for (int var5 = p_78433_1_; var5 < p_78433_3_; ++var5) {
            for (int var6 = p_78433_2_; var6 < p_78433_4_; ++var6) {
                this.imageData[var5 + var6 * this.imageWidth] |= -16777216;
            }
        }
    }

    private boolean hasTransparency(int p_78435_1_, int p_78435_2_, int p_78435_3_, int p_78435_4_) {
        for (int var5 = p_78435_1_; var5 < p_78435_3_; ++var5) {
            for (int var6 = p_78435_2_; var6 < p_78435_4_; ++var6) {
                int var7 = this.imageData[var5 + var6 * this.imageWidth];
                if ((var7 >> 24 & 255) < 128) {
                    return true;
                }
            }
        }

        return false;
    }
}
