package net.minecraft.network.handshake.client;

import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.handshake.INetHandlerHandshakeServer;

import java.io.IOException;

public class C00Handshake extends Packet {
    private static final String __OBFID = "CL_00001372";
    private int field_149600_a;
    private String field_149598_b;
    private int field_149599_c;
    private EnumConnectionState field_149597_d;

    public C00Handshake() {
    }

    public C00Handshake(int p_i45266_1_, String p_i45266_2_, int p_i45266_3_, EnumConnectionState p_i45266_4_) {
        this.field_149600_a = p_i45266_1_;
        this.field_149598_b = p_i45266_2_;
        this.field_149599_c = p_i45266_3_;
        this.field_149597_d = p_i45266_4_;
    }

    public void readPacketData(PacketBuffer p_148837_1_) throws IOException {
        this.field_149600_a = p_148837_1_.readVarIntFromBuffer();
        this.field_149598_b = p_148837_1_.readStringFromBuffer(255);
        this.field_149599_c = p_148837_1_.readUnsignedShort();
        this.field_149597_d = EnumConnectionState.func_150760_a(p_148837_1_.readVarIntFromBuffer());
    }

    public void writePacketData(PacketBuffer p_148840_1_) throws IOException {
        p_148840_1_.writeVarIntToBuffer(this.field_149600_a);
        p_148840_1_.writeStringToBuffer(this.field_149598_b);
        p_148840_1_.writeShort(this.field_149599_c);
        p_148840_1_.writeVarIntToBuffer(this.field_149597_d.func_150759_c());
    }

    public void processPacket(INetHandlerHandshakeServer p_148833_1_) {
        p_148833_1_.processHandshake(this);
    }

    public boolean hasPriority() {
        return true;
    }

    public EnumConnectionState func_149594_c() {
        return this.field_149597_d;
    }

    public int func_149595_d() {
        return this.field_149600_a;
    }

    public void processPacket(INetHandler p_148833_1_) {
        this.processPacket((INetHandlerHandshakeServer) p_148833_1_);
    }
}
