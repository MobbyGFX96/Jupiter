package net.minecraft.network.rcon;

import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;

public class RConConsoleSource implements ICommandSender {
    public static final RConConsoleSource field_70010_a = new RConConsoleSource();
    private static final String __OBFID = "CL_00001800";
    private StringBuffer field_70009_b = new StringBuffer();

    public String getCommandSenderName() {
        return "Rcon";
    }

    public IChatComponent func_145748_c_() {
        return new ChatComponentText(this.getCommandSenderName());
    }

    public void addChatMessage(IChatComponent p_145747_1_) {
        this.field_70009_b.append(p_145747_1_.getUnformattedText());
    }

    public boolean canCommandSenderUseCommand(int p_70003_1_, String p_70003_2_) {
        return true;
    }

    public ChunkCoordinates getPlayerCoordinates() {
        return new ChunkCoordinates(0, 0, 0);
    }

    public World getEntityWorld() {
        return MinecraftServer.getServer().getEntityWorld();
    }
}
