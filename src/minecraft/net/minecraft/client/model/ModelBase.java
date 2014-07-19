package net.minecraft.client.model;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

import java.util.*;

public abstract class ModelBase {
    private static final String __OBFID = "CL_00000845";
    public float onGround;
    public boolean isRiding;
    public List boxList = new ArrayList();
    public boolean isChild = true;
    public int textureWidth = 64;
    public int textureHeight = 32;
    private Map modelTextureMap = new HashMap();

    public void render(Entity p_78088_1_, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float p_78088_7_) {
    }

    public void setRotationAngles(float p_78087_1_, float p_78087_2_, float p_78087_3_, float p_78087_4_, float p_78087_5_, float p_78087_6_, Entity p_78087_7_) {
    }

    public void setLivingAnimations(EntityLivingBase p_78086_1_, float p_78086_2_, float p_78086_3_, float p_78086_4_) {
    }

    public ModelRenderer getRandomModelBox(Random p_85181_1_) {
        return (ModelRenderer) this.boxList.get(p_85181_1_.nextInt(this.boxList.size()));
    }

    protected void setTextureOffset(String p_78085_1_, int p_78085_2_, int p_78085_3_) {
        this.modelTextureMap.put(p_78085_1_, new TextureOffset(p_78085_2_, p_78085_3_));
    }

    public TextureOffset getTextureOffset(String p_78084_1_) {
        return (TextureOffset) this.modelTextureMap.get(p_78084_1_);
    }
}
