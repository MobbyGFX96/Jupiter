package net.minecraft.network.play.server;

import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

import java.io.IOException;

public class S00PacketKeepAlive extends Packet {
    private static final String __OBFID = "CL_00001303";
    private int field_149136_a;

    public S00PacketKeepAlive() {
    }

    public S00PacketKeepAlive(int p_i45195_1_) {
        this.field_149136_a = p_i45195_1_;
    }

    public void processPacket(INetHandlerPlayClient p_148833_1_) {
        p_148833_1_.handleKeepAlive(this);
    }

    public void readPacketData(PacketBuffer p_148837_1_) throws IOException {
        this.field_149136_a = p_148837_1_.readInt();
    }

    public void writePacketData(PacketBuffer p_148840_1_) throws IOException {
        p_148840_1_.writeInt(this.field_149136_a);
    }

    public boolean hasPriority() {
        return true;
    }

    public int func_149134_c() {
        return this.field_149136_a;
    }

    public void processPacket(INetHandler p_148833_1_) {
        this.processPacket((INetHandlerPlayClient) p_148833_1_);
    }
}
