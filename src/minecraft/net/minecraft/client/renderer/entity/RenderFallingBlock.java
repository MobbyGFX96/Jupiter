package net.minecraft.client.renderer.entity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAnvil;
import net.minecraft.block.BlockDragonEgg;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

public class RenderFallingBlock extends Render {
    private static final String __OBFID = "CL_00000994";
    private final RenderBlocks field_147920_a = new RenderBlocks();

    public RenderFallingBlock() {
        this.shadowSize = 0.5F;
    }

    public void doRender(EntityFallingBlock p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_) {
        World var10 = p_76986_1_.func_145807_e();
        Block var11 = p_76986_1_.func_145805_f();
        int var12 = MathHelper.floor_double(p_76986_1_.posX);
        int var13 = MathHelper.floor_double(p_76986_1_.posY);
        int var14 = MathHelper.floor_double(p_76986_1_.posZ);
        if (var11 != null && var11 != var10.getBlock(var12, var13, var14)) {
            GL11.glPushMatrix();
            GL11.glTranslatef((float) p_76986_2_, (float) p_76986_4_, (float) p_76986_6_);
            this.bindEntityTexture(p_76986_1_);
            GL11.glDisable(GL11.GL_LIGHTING);
            Tessellator var15;
            if (var11 instanceof BlockAnvil) {
                this.field_147920_a.blockAccess = var10;
                var15 = Tessellator.instance;
                var15.startDrawingQuads();
                var15.setTranslation((double) ((float) (-var12) - 0.5F), (double) ((float) (-var13) - 0.5F), (double) ((float) (-var14) - 0.5F));
                this.field_147920_a.renderBlockAnvilMetadata((BlockAnvil) var11, var12, var13, var14, p_76986_1_.field_145814_a);
                var15.setTranslation(0.0D, 0.0D, 0.0D);
                var15.draw();
            } else if (var11 instanceof BlockDragonEgg) {
                this.field_147920_a.blockAccess = var10;
                var15 = Tessellator.instance;
                var15.startDrawingQuads();
                var15.setTranslation((double) ((float) (-var12) - 0.5F), (double) ((float) (-var13) - 0.5F), (double) ((float) (-var14) - 0.5F));
                this.field_147920_a.renderBlockDragonEgg((BlockDragonEgg) var11, var12, var13, var14);
                var15.setTranslation(0.0D, 0.0D, 0.0D);
                var15.draw();
            } else {
                this.field_147920_a.setRenderBoundsFromBlock(var11);
                this.field_147920_a.renderBlockSandFalling(var11, var10, var12, var13, var14, p_76986_1_.field_145814_a);
            }

            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glPopMatrix();
        }
    }

    protected ResourceLocation getEntityTexture(EntityFallingBlock p_110775_1_) {
        return TextureMap.locationBlocksTexture;
    }

    protected ResourceLocation getEntityTexture(Entity p_110775_1_) {
        return this.getEntityTexture((EntityFallingBlock) p_110775_1_);
    }

    public void doRender(Entity p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_) {
        this.doRender((EntityFallingBlock) p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
    }
}
