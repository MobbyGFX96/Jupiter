package net.minecraft.network.play.client;

import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

import java.io.IOException;

public class C0FPacketConfirmTransaction extends Packet {
    private static final String __OBFID = "CL_00001351";
    private int field_149536_a;
    private short field_149534_b;
    private boolean field_149535_c;

    public C0FPacketConfirmTransaction() {
    }

    public C0FPacketConfirmTransaction(int p_i45244_1_, short p_i45244_2_, boolean p_i45244_3_) {
        this.field_149536_a = p_i45244_1_;
        this.field_149534_b = p_i45244_2_;
        this.field_149535_c = p_i45244_3_;
    }

    public void processPacket(INetHandlerPlayServer p_148833_1_) {
        p_148833_1_.processConfirmTransaction(this);
    }

    public void readPacketData(PacketBuffer p_148837_1_) throws IOException {
        this.field_149536_a = p_148837_1_.readByte();
        this.field_149534_b = p_148837_1_.readShort();
        this.field_149535_c = p_148837_1_.readByte() != 0;
    }

    public void writePacketData(PacketBuffer p_148840_1_) throws IOException {
        p_148840_1_.writeByte(this.field_149536_a);
        p_148840_1_.writeShort(this.field_149534_b);
        p_148840_1_.writeByte(this.field_149535_c ? 1 : 0);
    }

    public String serialize() {
        return String.format("id=%d, uid=%d, accepted=%b", new Object[]{Integer.valueOf(this.field_149536_a), Short.valueOf(this.field_149534_b), Boolean.valueOf(this.field_149535_c)});
    }

    public int func_149532_c() {
        return this.field_149536_a;
    }

    public short func_149533_d() {
        return this.field_149534_b;
    }

    public void processPacket(INetHandler p_148833_1_) {
        this.processPacket((INetHandlerPlayServer) p_148833_1_);
    }
}
