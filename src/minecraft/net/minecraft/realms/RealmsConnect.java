package net.minecraft.realms;

import io.netty.util.concurrent.GenericFutureListener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerLoginClient;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.login.client.C00PacketLoginStart;
import net.minecraft.util.ChatComponentTranslation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class RealmsConnect {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final String __OBFID = "CL_00001844";
    private final RealmsScreen onlineScreen;
    private volatile boolean aborted = false;
    private NetworkManager connection;

    public RealmsConnect(RealmsScreen p_i1079_1_) {
        this.onlineScreen = p_i1079_1_;
    }

    public void connect(final String p_connect_1_, final int p_connect_2_) {
        (new Thread("Realms-connect-task") {
            private static final String __OBFID = "CL_00001808";

            public void run() {
                InetAddress var1 = null;

                try {
                    var1 = InetAddress.getByName(p_connect_1_);
                    if (RealmsConnect.this.aborted) {
                        return;
                    }

                    RealmsConnect.this.connection = NetworkManager.provideLanClient(var1, p_connect_2_);
                    if (RealmsConnect.this.aborted) {
                        return;
                    }

                    RealmsConnect.this.connection.setNetHandler(new NetHandlerLoginClient(RealmsConnect.this.connection, Minecraft.getMinecraft(), RealmsConnect.this.onlineScreen.getProxy()));
                    if (RealmsConnect.this.aborted) {
                        return;
                    }

                    RealmsConnect.this.connection.scheduleOutboundPacket(new C00Handshake(5, p_connect_1_, p_connect_2_, EnumConnectionState.LOGIN), new GenericFutureListener[0]);
                    if (RealmsConnect.this.aborted) {
                        return;
                    }

                    RealmsConnect.this.connection.scheduleOutboundPacket(new C00PacketLoginStart(Minecraft.getMinecraft().getSession().func_148256_e()), new GenericFutureListener[0]);
                } catch (UnknownHostException var5) {
                    if (RealmsConnect.this.aborted) {
                        return;
                    }

                    RealmsConnect.LOGGER.error("Couldn\'t connect to world", var5);
                    Realms.setScreen(new DisconnectedOnlineScreen(RealmsConnect.this.onlineScreen, "connect.failed", new ChatComponentTranslation("disconnect.genericReason", new Object[]{"Unknown host \'" + p_connect_1_ + "\'"})));
                } catch (Exception var6) {
                    if (RealmsConnect.this.aborted) {
                        return;
                    }

                    RealmsConnect.LOGGER.error("Couldn\'t connect to world", var6);
                    String var3 = var6.toString();
                    if (var1 != null) {
                        String var4 = var1.toString() + ":" + p_connect_2_;
                        var3 = var3.replaceAll(var4, "");
                    }

                    Realms.setScreen(new DisconnectedOnlineScreen(RealmsConnect.this.onlineScreen, "connect.failed", new ChatComponentTranslation("disconnect.genericReason", new Object[]{var3})));
                }
            }
        }).start();
    }

    public void abort() {
        this.aborted = true;
    }

    public void tick() {
        if (this.connection != null) {
            if (this.connection.isChannelOpen()) {
                this.connection.processReceivedPackets();
            } else if (this.connection.getExitMessage() != null) {
                this.connection.getNetHandler().onDisconnect(this.connection.getExitMessage());
            }
        }
    }
}
