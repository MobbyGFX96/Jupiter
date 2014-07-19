package net.minecraft.client.renderer;

import org.lwjgl.opengl.GL11;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.*;
import java.util.Map.Entry;

public class GLAllocation {
    private static final Map mapDisplayLists = new HashMap();
    private static final List listDummy = new ArrayList();
    private static final String __OBFID = "CL_00000630";

    public static synchronized int generateDisplayLists(int p_74526_0_) {
        int var1 = GL11.glGenLists(p_74526_0_);
        mapDisplayLists.put(Integer.valueOf(var1), Integer.valueOf(p_74526_0_));
        return var1;
    }

    public static synchronized void deleteDisplayLists(int p_74523_0_) {
        GL11.glDeleteLists(p_74523_0_, ((Integer) mapDisplayLists.remove(Integer.valueOf(p_74523_0_))).intValue());
    }

    public static synchronized void deleteTexturesAndDisplayLists() {
        Iterator var0 = mapDisplayLists.entrySet().iterator();

        while (var0.hasNext()) {
            Entry var1 = (Entry) var0.next();
            GL11.glDeleteLists(((Integer) var1.getKey()).intValue(), ((Integer) var1.getValue()).intValue());
        }

        mapDisplayLists.clear();
    }

    public static synchronized ByteBuffer createDirectByteBuffer(int p_74524_0_) {
        return ByteBuffer.allocateDirect(p_74524_0_).order(ByteOrder.nativeOrder());
    }

    public static IntBuffer createDirectIntBuffer(int p_74527_0_) {
        return createDirectByteBuffer(p_74527_0_ << 2).asIntBuffer();
    }

    public static FloatBuffer createDirectFloatBuffer(int p_74529_0_) {
        return createDirectByteBuffer(p_74529_0_ << 2).asFloatBuffer();
    }
}
