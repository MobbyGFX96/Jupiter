package net.minecraft.client.renderer.texture;

public abstract class AbstractTexture implements ITextureObject {
    private static final String __OBFID = "CL_00001047";
    protected int glTextureId = -1;

    public int getGlTextureId() {
        if (this.glTextureId == -1) {
            this.glTextureId = TextureUtil.glGenTextures();
        }

        return this.glTextureId;
    }

    public void func_147631_c() {
        if (this.glTextureId != -1) {
            TextureUtil.deleteTexture(this.glTextureId);
            this.glTextureId = -1;
        }
    }
}
