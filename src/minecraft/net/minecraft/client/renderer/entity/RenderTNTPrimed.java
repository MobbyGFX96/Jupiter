package net.minecraft.client.renderer.entity;

import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderTNTPrimed extends Render {
    private static final String __OBFID = "CL_00001030";
    private RenderBlocks blockRenderer = new RenderBlocks();

    public RenderTNTPrimed() {
        this.shadowSize = 0.5F;
    }

    public void doRender(EntityTNTPrimed p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_) {
        GL11.glPushMatrix();
        GL11.glTranslatef((float) p_76986_2_, (float) p_76986_4_, (float) p_76986_6_);
        float var10;
        if ((float) p_76986_1_.fuse - p_76986_9_ + 1.0F < 10.0F) {
            var10 = 1.0F - ((float) p_76986_1_.fuse - p_76986_9_ + 1.0F) / 10.0F;
            if (var10 < 0.0F) {
                var10 = 0.0F;
            }

            if (var10 > 1.0F) {
                var10 = 1.0F;
            }

            var10 *= var10;
            var10 *= var10;
            float var11 = 1.0F + var10 * 0.3F;
            GL11.glScalef(var11, var11, var11);
        }

        var10 = (1.0F - ((float) p_76986_1_.fuse - p_76986_9_ + 1.0F) / 100.0F) * 0.8F;
        this.bindEntityTexture(p_76986_1_);
        this.blockRenderer.renderBlockAsItem(Blocks.tnt, 0, p_76986_1_.getBrightness(p_76986_9_));
        if (p_76986_1_.fuse / 5 % 2 == 0) {
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_DST_ALPHA);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, var10);
            this.blockRenderer.renderBlockAsItem(Blocks.tnt, 0, 1.0F);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
        }

        GL11.glPopMatrix();
    }

    protected ResourceLocation getEntityTexture(EntityTNTPrimed p_110775_1_) {
        return TextureMap.locationBlocksTexture;
    }

    protected ResourceLocation getEntityTexture(Entity p_110775_1_) {
        return this.getEntityTexture((EntityTNTPrimed) p_110775_1_);
    }

    public void doRender(Entity p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_) {
        this.doRender((EntityTNTPrimed) p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
    }
}
