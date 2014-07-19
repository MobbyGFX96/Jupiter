package net.minecraft.network.status.server;

import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.status.INetHandlerStatusClient;

import java.io.IOException;

public class S01PacketPong extends Packet {
    private static final String __OBFID = "CL_00001383";
    private long field_149293_a;

    public S01PacketPong() {
    }

    public S01PacketPong(long p_i45272_1_) {
        this.field_149293_a = p_i45272_1_;
    }

    public void readPacketData(PacketBuffer p_148837_1_) throws IOException {
        this.field_149293_a = p_148837_1_.readLong();
    }

    public void writePacketData(PacketBuffer p_148840_1_) throws IOException {
        p_148840_1_.writeLong(this.field_149293_a);
    }

    public void processPacket(INetHandlerStatusClient p_148833_1_) {
        p_148833_1_.handlePong(this);
    }

    public boolean hasPriority() {
        return true;
    }

    public long func_149292_c() {
        return this.field_149293_a;
    }

    public void processPacket(INetHandler p_148833_1_) {
        this.processPacket((INetHandlerStatusClient) p_148833_1_);
    }
}
