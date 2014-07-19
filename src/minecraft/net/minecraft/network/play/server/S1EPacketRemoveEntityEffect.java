package net.minecraft.network.play.server;

import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.potion.PotionEffect;

import java.io.IOException;

public class S1EPacketRemoveEntityEffect extends Packet {
    private static final String __OBFID = "CL_00001321";
    private int field_149079_a;
    private int field_149078_b;

    public S1EPacketRemoveEntityEffect() {
    }

    public S1EPacketRemoveEntityEffect(int p_i45212_1_, PotionEffect p_i45212_2_) {
        this.field_149079_a = p_i45212_1_;
        this.field_149078_b = p_i45212_2_.getPotionID();
    }

    public void readPacketData(PacketBuffer p_148837_1_) throws IOException {
        this.field_149079_a = p_148837_1_.readInt();
        this.field_149078_b = p_148837_1_.readUnsignedByte();
    }

    public void writePacketData(PacketBuffer p_148840_1_) throws IOException {
        p_148840_1_.writeInt(this.field_149079_a);
        p_148840_1_.writeByte(this.field_149078_b);
    }

    public void processPacket(INetHandlerPlayClient p_148833_1_) {
        p_148833_1_.handleRemoveEntityEffect(this);
    }

    public int func_149076_c() {
        return this.field_149079_a;
    }

    public int func_149075_d() {
        return this.field_149078_b;
    }

    public void processPacket(INetHandler p_148833_1_) {
        this.processPacket((INetHandlerPlayClient) p_148833_1_);
    }
}
