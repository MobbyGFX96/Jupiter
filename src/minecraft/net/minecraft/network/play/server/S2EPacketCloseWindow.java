package net.minecraft.network.play.server;

import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

import java.io.IOException;

public class S2EPacketCloseWindow extends Packet {
    private static final String __OBFID = "CL_00001292";
    private int field_148896_a;

    public S2EPacketCloseWindow() {
    }

    public S2EPacketCloseWindow(int p_i45183_1_) {
        this.field_148896_a = p_i45183_1_;
    }

    public void processPacket(INetHandlerPlayClient p_148833_1_) {
        p_148833_1_.handleCloseWindow(this);
    }

    public void readPacketData(PacketBuffer p_148837_1_) throws IOException {
        this.field_148896_a = p_148837_1_.readUnsignedByte();
    }

    public void writePacketData(PacketBuffer p_148840_1_) throws IOException {
        p_148840_1_.writeByte(this.field_148896_a);
    }

    public void processPacket(INetHandler p_148833_1_) {
        this.processPacket((INetHandlerPlayClient) p_148833_1_);
    }
}
