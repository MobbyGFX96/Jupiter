package net.minecraft.network.play.server;

import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

import java.io.IOException;

public class S13PacketDestroyEntities extends Packet {
    private static final String __OBFID = "CL_00001320";
    private int[] field_149100_a;

    public S13PacketDestroyEntities() {
    }

    public S13PacketDestroyEntities(int... p_i45211_1_) {
        this.field_149100_a = p_i45211_1_;
    }

    public void readPacketData(PacketBuffer p_148837_1_) throws IOException {
        this.field_149100_a = new int[p_148837_1_.readByte()];

        for (int var2 = 0; var2 < this.field_149100_a.length; ++var2) {
            this.field_149100_a[var2] = p_148837_1_.readInt();
        }
    }

    public void writePacketData(PacketBuffer p_148840_1_) throws IOException {
        p_148840_1_.writeByte(this.field_149100_a.length);

        for (int var2 = 0; var2 < this.field_149100_a.length; ++var2) {
            p_148840_1_.writeInt(this.field_149100_a[var2]);
        }
    }

    public void processPacket(INetHandlerPlayClient p_148833_1_) {
        p_148833_1_.handleDestroyEntities(this);
    }

    public String serialize() {
        StringBuilder var1 = new StringBuilder();

        for (int var2 = 0; var2 < this.field_149100_a.length; ++var2) {
            if (var2 > 0) {
                var1.append(", ");
            }

            var1.append(this.field_149100_a[var2]);
        }

        return String.format("entities=%d[%s]", new Object[]{Integer.valueOf(this.field_149100_a.length), var1});
    }

    public int[] func_149098_c() {
        return this.field_149100_a;
    }

    public void processPacket(INetHandler p_148833_1_) {
        this.processPacket((INetHandlerPlayClient) p_148833_1_);
    }
}
