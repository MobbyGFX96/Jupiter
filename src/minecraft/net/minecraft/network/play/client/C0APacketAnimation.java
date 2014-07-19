package net.minecraft.network.play.client;

import net.minecraft.entity.Entity;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

import java.io.IOException;

public class C0APacketAnimation extends Packet {
    private static final String __OBFID = "CL_00001345";
    private int field_149424_a;
    private int field_149423_b;

    public C0APacketAnimation() {
    }

    public C0APacketAnimation(Entity p_i45238_1_, int p_i45238_2_) {
        this.field_149424_a = p_i45238_1_.getEntityId();
        this.field_149423_b = p_i45238_2_;
    }

    public void readPacketData(PacketBuffer p_148837_1_) throws IOException {
        this.field_149424_a = p_148837_1_.readInt();
        this.field_149423_b = p_148837_1_.readByte();
    }

    public void writePacketData(PacketBuffer p_148840_1_) throws IOException {
        p_148840_1_.writeInt(this.field_149424_a);
        p_148840_1_.writeByte(this.field_149423_b);
    }

    public void processPacket(INetHandlerPlayServer p_148833_1_) {
        p_148833_1_.processAnimation(this);
    }

    public String serialize() {
        return String.format("id=%d, type=%d", new Object[]{Integer.valueOf(this.field_149424_a), Integer.valueOf(this.field_149423_b)});
    }

    public int func_149421_d() {
        return this.field_149423_b;
    }

    public void processPacket(INetHandler p_148833_1_) {
        this.processPacket((INetHandlerPlayServer) p_148833_1_);
    }
}
