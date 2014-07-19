package net.minecraft.client.renderer;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class ActiveRenderInfo {
    private static final String __OBFID = "CL_00000626";
    public static float objectX;
    public static float objectY;
    public static float objectZ;
    public static float rotationX;
    public static float rotationXZ;
    public static float rotationZ;
    public static float rotationYZ;
    public static float rotationXY;
    private static IntBuffer viewport = GLAllocation.createDirectIntBuffer(16);
    private static FloatBuffer modelview = GLAllocation.createDirectFloatBuffer(16);
    private static FloatBuffer projection = GLAllocation.createDirectFloatBuffer(16);
    private static FloatBuffer objectCoords = GLAllocation.createDirectFloatBuffer(3);

    public static void updateRenderInfo(EntityPlayer p_74583_0_, boolean p_74583_1_) {
        GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, modelview);
        GL11.glGetFloat(GL11.GL_PROJECTION_MATRIX, projection);
        GL11.glGetInteger(GL11.GL_VIEWPORT, viewport);
        float var2 = (float) ((viewport.get(0) + viewport.get(2)) / 2);
        float var3 = (float) ((viewport.get(1) + viewport.get(3)) / 2);
        GLU.gluUnProject(var2, var3, 0.0F, modelview, projection, viewport, objectCoords);
        objectX = objectCoords.get(0);
        objectY = objectCoords.get(1);
        objectZ = objectCoords.get(2);
        int var4 = p_74583_1_ ? 1 : 0;
        float var5 = p_74583_0_.rotationPitch;
        float var6 = p_74583_0_.rotationYaw;
        rotationX = MathHelper.cos(var6 * (float) Math.PI / 180.0F) * (float) (1 - var4 * 2);
        rotationZ = MathHelper.sin(var6 * (float) Math.PI / 180.0F) * (float) (1 - var4 * 2);
        rotationYZ = -rotationZ * MathHelper.sin(var5 * (float) Math.PI / 180.0F) * (float) (1 - var4 * 2);
        rotationXY = rotationX * MathHelper.sin(var5 * (float) Math.PI / 180.0F) * (float) (1 - var4 * 2);
        rotationXZ = MathHelper.cos(var5 * (float) Math.PI / 180.0F);
    }

    public static Vec3 projectViewFromEntity(EntityLivingBase p_74585_0_, double p_74585_1_) {
        double var3 = p_74585_0_.prevPosX + (p_74585_0_.posX - p_74585_0_.prevPosX) * p_74585_1_;
        double var5 = p_74585_0_.prevPosY + (p_74585_0_.posY - p_74585_0_.prevPosY) * p_74585_1_ + (double) p_74585_0_.getEyeHeight();
        double var7 = p_74585_0_.prevPosZ + (p_74585_0_.posZ - p_74585_0_.prevPosZ) * p_74585_1_;
        double var9 = var3 + (double) (objectX * 1.0F);
        double var11 = var5 + (double) (objectY * 1.0F);
        double var13 = var7 + (double) (objectZ * 1.0F);
        return Vec3.createVectorHelper(var9, var11, var13);
    }

    public static Block getBlockAtEntityViewpoint(World p_151460_0_, EntityLivingBase p_151460_1_, float p_151460_2_) {
        Vec3 var3 = projectViewFromEntity(p_151460_1_, (double) p_151460_2_);
        ChunkPosition var4 = new ChunkPosition(var3);
        Block var5 = p_151460_0_.getBlock(var4.field_151329_a, var4.field_151327_b, var4.field_151328_c);
        if (var5.getMaterial().isLiquid()) {
            float var6 = BlockLiquid.func_149801_b(p_151460_0_.getBlockMetadata(var4.field_151329_a, var4.field_151327_b, var4.field_151328_c)) - 0.11111111F;
            float var7 = (float) (var4.field_151327_b + 1) - var6;
            if (var3.yCoord >= (double) var7) {
                var5 = p_151460_0_.getBlock(var4.field_151329_a, var4.field_151327_b + 1, var4.field_151328_c);
            }
        }

        return var5;
    }
}
