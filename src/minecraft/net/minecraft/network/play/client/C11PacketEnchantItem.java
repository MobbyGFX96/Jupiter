package net.minecraft.network.play.client;

import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

import java.io.IOException;

public class C11PacketEnchantItem extends Packet {
    private static final String __OBFID = "CL_00001352";
    private int field_149541_a;
    private int field_149540_b;

    public C11PacketEnchantItem() {
    }

    public C11PacketEnchantItem(int p_i45245_1_, int p_i45245_2_) {
        this.field_149541_a = p_i45245_1_;
        this.field_149540_b = p_i45245_2_;
    }

    public void processPacket(INetHandlerPlayServer p_148833_1_) {
        p_148833_1_.processEnchantItem(this);
    }

    public void readPacketData(PacketBuffer p_148837_1_) throws IOException {
        this.field_149541_a = p_148837_1_.readByte();
        this.field_149540_b = p_148837_1_.readByte();
    }

    public void writePacketData(PacketBuffer p_148840_1_) throws IOException {
        p_148840_1_.writeByte(this.field_149541_a);
        p_148840_1_.writeByte(this.field_149540_b);
    }

    public String serialize() {
        return String.format("id=%d, button=%d", new Object[]{Integer.valueOf(this.field_149541_a), Integer.valueOf(this.field_149540_b)});
    }

    public int func_149539_c() {
        return this.field_149541_a;
    }

    public int func_149537_d() {
        return this.field_149540_b;
    }

    public void processPacket(INetHandler p_148833_1_) {
        this.processPacket((INetHandlerPlayServer) p_148833_1_);
    }
}
