package net.minecraft.client.renderer.tileentity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelEnderCrystal;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderEnderCrystal extends Render {
    private static final ResourceLocation enderCrystalTextures = new ResourceLocation("textures/entity/endercrystal/endercrystal.png");
    private static final String __OBFID = "CL_00000987";
    private ModelBase field_76995_b;

    public RenderEnderCrystal() {
        this.shadowSize = 0.5F;
        this.field_76995_b = new ModelEnderCrystal(0.0F, true);
    }

    public void doRender(EntityEnderCrystal p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_) {
        float var10 = (float) p_76986_1_.innerRotation + p_76986_9_;
        GL11.glPushMatrix();
        GL11.glTranslatef((float) p_76986_2_, (float) p_76986_4_, (float) p_76986_6_);
        this.bindTexture(enderCrystalTextures);
        float var11 = MathHelper.sin(var10 * 0.2F) / 2.0F + 0.5F;
        var11 += var11 * var11;
        this.field_76995_b.render(p_76986_1_, 0.0F, var10 * 3.0F, var11 * 0.2F, 0.0F, 0.0F, 0.0625F);
        GL11.glPopMatrix();
    }

    protected ResourceLocation getEntityTexture(EntityEnderCrystal p_110775_1_) {
        return enderCrystalTextures;
    }

    protected ResourceLocation getEntityTexture(Entity p_110775_1_) {
        return this.getEntityTexture((EntityEnderCrystal) p_110775_1_);
    }

    public void doRender(Entity p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_) {
        this.doRender((EntityEnderCrystal) p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
    }
}
