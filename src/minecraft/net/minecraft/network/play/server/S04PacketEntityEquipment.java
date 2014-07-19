package net.minecraft.network.play.server;

import net.minecraft.item.ItemStack;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

import java.io.IOException;

public class S04PacketEntityEquipment extends Packet {
    private static final String __OBFID = "CL_00001330";
    private int field_149394_a;
    private int field_149392_b;
    private ItemStack field_149393_c;

    public S04PacketEntityEquipment() {
    }

    public S04PacketEntityEquipment(int p_i45221_1_, int p_i45221_2_, ItemStack p_i45221_3_) {
        this.field_149394_a = p_i45221_1_;
        this.field_149392_b = p_i45221_2_;
        this.field_149393_c = p_i45221_3_ == null ? null : p_i45221_3_.copy();
    }

    public void readPacketData(PacketBuffer p_148837_1_) throws IOException {
        this.field_149394_a = p_148837_1_.readInt();
        this.field_149392_b = p_148837_1_.readShort();
        this.field_149393_c = p_148837_1_.readItemStackFromBuffer();
    }

    public void writePacketData(PacketBuffer p_148840_1_) throws IOException {
        p_148840_1_.writeInt(this.field_149394_a);
        p_148840_1_.writeShort(this.field_149392_b);
        p_148840_1_.writeItemStackToBuffer(this.field_149393_c);
    }

    public void processPacket(INetHandlerPlayClient p_148833_1_) {
        p_148833_1_.handleEntityEquipment(this);
    }

    public ItemStack func_149390_c() {
        return this.field_149393_c;
    }

    public String serialize() {
        return String.format("entity=%d, slot=%d, item=%s", new Object[]{Integer.valueOf(this.field_149394_a), Integer.valueOf(this.field_149392_b), this.field_149393_c});
    }

    public int func_149389_d() {
        return this.field_149394_a;
    }

    public int func_149388_e() {
        return this.field_149392_b;
    }

    public void processPacket(INetHandler p_148833_1_) {
        this.processPacket((INetHandlerPlayClient) p_148833_1_);
    }
}
