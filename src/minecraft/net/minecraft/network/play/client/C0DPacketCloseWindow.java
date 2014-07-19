package net.minecraft.network.play.client;

import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

import java.io.IOException;

public class C0DPacketCloseWindow extends Packet {
    private static final String __OBFID = "CL_00001354";
    private int field_149556_a;

    public C0DPacketCloseWindow() {
    }

    public C0DPacketCloseWindow(int p_i45247_1_) {
        this.field_149556_a = p_i45247_1_;
    }

    public void processPacket(INetHandlerPlayServer p_148833_1_) {
        p_148833_1_.processCloseWindow(this);
    }

    public void readPacketData(PacketBuffer p_148837_1_) throws IOException {
        this.field_149556_a = p_148837_1_.readByte();
    }

    public void writePacketData(PacketBuffer p_148840_1_) throws IOException {
        p_148840_1_.writeByte(this.field_149556_a);
    }

    public void processPacket(INetHandler p_148833_1_) {
        this.processPacket((INetHandlerPlayServer) p_148833_1_);
    }
}
