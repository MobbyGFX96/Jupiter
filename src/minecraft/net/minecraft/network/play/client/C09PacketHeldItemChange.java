package net.minecraft.network.play.client;

import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

import java.io.IOException;

public class C09PacketHeldItemChange extends Packet {
    private static final String __OBFID = "CL_00001368";
    private int field_149615_a;

    public C09PacketHeldItemChange() {
    }

    public C09PacketHeldItemChange(int p_i45262_1_) {
        this.field_149615_a = p_i45262_1_;
    }

    public void readPacketData(PacketBuffer p_148837_1_) throws IOException {
        this.field_149615_a = p_148837_1_.readShort();
    }

    public void writePacketData(PacketBuffer p_148840_1_) throws IOException {
        p_148840_1_.writeShort(this.field_149615_a);
    }

    public void processPacket(INetHandlerPlayServer p_148833_1_) {
        p_148833_1_.processHeldItemChange(this);
    }

    public int func_149614_c() {
        return this.field_149615_a;
    }

    public void processPacket(INetHandler p_148833_1_) {
        this.processPacket((INetHandlerPlayServer) p_148833_1_);
    }
}
