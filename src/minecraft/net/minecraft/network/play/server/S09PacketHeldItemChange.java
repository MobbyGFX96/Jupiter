package net.minecraft.network.play.server;

import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

import java.io.IOException;

public class S09PacketHeldItemChange extends Packet {
    private static final String __OBFID = "CL_00001324";
    private int field_149387_a;

    public S09PacketHeldItemChange() {
    }

    public S09PacketHeldItemChange(int p_i45215_1_) {
        this.field_149387_a = p_i45215_1_;
    }

    public void readPacketData(PacketBuffer p_148837_1_) throws IOException {
        this.field_149387_a = p_148837_1_.readByte();
    }

    public void writePacketData(PacketBuffer p_148840_1_) throws IOException {
        p_148840_1_.writeByte(this.field_149387_a);
    }

    public void processPacket(INetHandlerPlayClient p_148833_1_) {
        p_148833_1_.handleHeldItemChange(this);
    }

    public int func_149385_c() {
        return this.field_149387_a;
    }

    public void processPacket(INetHandler p_148833_1_) {
        this.processPacket((INetHandlerPlayClient) p_148833_1_);
    }
}
