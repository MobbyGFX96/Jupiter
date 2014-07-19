package net.minecraft.network.play.server;

import net.minecraft.entity.DataWatcher;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

import java.io.IOException;
import java.util.List;

public class S1CPacketEntityMetadata extends Packet {
    private static final String __OBFID = "CL_00001326";
    private int field_149379_a;
    private List field_149378_b;

    public S1CPacketEntityMetadata() {
    }

    public S1CPacketEntityMetadata(int p_i45217_1_, DataWatcher p_i45217_2_, boolean p_i45217_3_) {
        this.field_149379_a = p_i45217_1_;
        if (p_i45217_3_) {
            this.field_149378_b = p_i45217_2_.getAllWatched();
        } else {
            this.field_149378_b = p_i45217_2_.getChanged();
        }
    }

    public void readPacketData(PacketBuffer p_148837_1_) throws IOException {
        this.field_149379_a = p_148837_1_.readInt();
        this.field_149378_b = DataWatcher.readWatchedListFromPacketBuffer(p_148837_1_);
    }

    public void writePacketData(PacketBuffer p_148840_1_) throws IOException {
        p_148840_1_.writeInt(this.field_149379_a);
        DataWatcher.writeWatchedListToPacketBuffer(this.field_149378_b, p_148840_1_);
    }

    public void processPacket(INetHandlerPlayClient p_148833_1_) {
        p_148833_1_.handleEntityMetadata(this);
    }

    public List func_149376_c() {
        return this.field_149378_b;
    }

    public int func_149375_d() {
        return this.field_149379_a;
    }

    public void processPacket(INetHandler p_148833_1_) {
        this.processPacket((INetHandlerPlayClient) p_148833_1_);
    }
}