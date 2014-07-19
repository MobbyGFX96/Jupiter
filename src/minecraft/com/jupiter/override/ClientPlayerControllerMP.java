package com.jupiter.override;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.*;
import net.minecraft.stats.StatFileWriter;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.WorldSettings;

/**
 * Created by corey on 15/07/14.
 */
public class ClientPlayerControllerMP {

    private static final String __OBFID = "CL_00000881";
    private final Minecraft mc;
    private final NetHandlerPlayClient netClientHandler;
    private int currentBlockX = -1;
    private int currentBlockY = -1;
    private int currentblockZ = -1;
    private ItemStack currentItemHittingBlock;
    private float curBlockDamageMP;
    private float stepSoundTickCounter;
    private int blockHitDelay;
    private boolean isHittingBlock;
    private WorldSettings.GameType currentGameType;
    private int currentPlayerItem;

    public ClientPlayerControllerMP(Minecraft minecraft, NetHandlerPlayClient netHandlerPlayClient) {
        this.currentGameType = WorldSettings.GameType.SURVIVAL;
        this.mc = minecraft;
        this.netClientHandler = netHandlerPlayClient;
    }

    public static void clickBlockCreative(Minecraft p_78744_0_, ClientPlayerControllerMP p_78744_1_, int p_78744_2_, int p_78744_3_, int p_78744_4_, int p_78744_5_) {
        if (!p_78744_0_.theWorld.extinguishFire(p_78744_0_.thePlayer, p_78744_2_, p_78744_3_, p_78744_4_, p_78744_5_)) {
            p_78744_1_.onPlayerDestroyBlock(p_78744_2_, p_78744_3_, p_78744_4_, p_78744_5_);
        }
    }

    public void setPlayerCapabilities(EntityPlayer p_78748_1_) {
        this.currentGameType.configurePlayerCapabilities(p_78748_1_.capabilities);
    }

    public boolean enableEverythingIsScrewedUpMode() {
        return false;
    }

    public void setGameType(WorldSettings.GameType p_78746_1_) {
        this.currentGameType = p_78746_1_;
        this.currentGameType.configurePlayerCapabilities(this.mc.thePlayer.capabilities);
    }

    public void flipPlayer(EntityPlayer p_78745_1_) {
        p_78745_1_.rotationYaw = -180.0F;
    }

    public boolean shouldDrawHUD() {
        return this.currentGameType.isSurvivalOrAdventure();
    }

    public boolean onPlayerDestroyBlock(int p_78751_1_, int p_78751_2_, int p_78751_3_, int p_78751_4_) {
        if (this.currentGameType.isAdventure() && !this.mc.thePlayer.isCurrentToolAdventureModeExempt(p_78751_1_, p_78751_2_, p_78751_3_)) {
            return false;
        } else if (this.currentGameType.isCreative() && this.mc.thePlayer.getHeldItem() != null && this.mc.thePlayer.getHeldItem().getItem() instanceof ItemSword) {
            return false;
        } else {
            WorldClient var5 = this.mc.theWorld;
            Block var6 = var5.getBlock(p_78751_1_, p_78751_2_, p_78751_3_);
            if (var6.getMaterial() == Material.air) {
                return false;
            } else {
                var5.playAuxSFX(2001, p_78751_1_, p_78751_2_, p_78751_3_, Block.getIdFromBlock(var6) + (var5.getBlockMetadata(p_78751_1_, p_78751_2_, p_78751_3_) << 12));
                int var7 = var5.getBlockMetadata(p_78751_1_, p_78751_2_, p_78751_3_);
                boolean var8 = var5.setBlockToAir(p_78751_1_, p_78751_2_, p_78751_3_);
                if (var8) {
                    var6.onBlockDestroyedByPlayer(var5, p_78751_1_, p_78751_2_, p_78751_3_, var7);
                }

                this.currentBlockY = -1;
                if (!this.currentGameType.isCreative()) {
                    ItemStack var9 = this.mc.thePlayer.getCurrentEquippedItem();
                    if (var9 != null) {
                        var9.func_150999_a(var5, var6, p_78751_1_, p_78751_2_, p_78751_3_, this.mc.thePlayer);
                        if (var9.stackSize == 0) {
                            this.mc.thePlayer.destroyCurrentEquippedItem();
                        }
                    }
                }

                return var8;
            }
        }
    }

    public void clickBlock(int p_78743_1_, int p_78743_2_, int p_78743_3_, int p_78743_4_) {
        if (!this.currentGameType.isAdventure() || this.mc.thePlayer.isCurrentToolAdventureModeExempt(p_78743_1_, p_78743_2_, p_78743_3_)) {
            if (this.currentGameType.isCreative()) {
                this.netClientHandler.addToSendQueue(new C07PacketPlayerDigging(0, p_78743_1_, p_78743_2_, p_78743_3_, p_78743_4_));
                clickBlockCreative(this.mc, this, p_78743_1_, p_78743_2_, p_78743_3_, p_78743_4_);
                this.blockHitDelay = 5;
            } else if (!this.isHittingBlock || !this.sameToolAndBlock(p_78743_1_, p_78743_2_, p_78743_3_)) {
                if (this.isHittingBlock) {
                    this.netClientHandler.addToSendQueue(new C07PacketPlayerDigging(1, this.currentBlockX, this.currentBlockY, this.currentblockZ, p_78743_4_));
                }

                this.netClientHandler.addToSendQueue(new C07PacketPlayerDigging(0, p_78743_1_, p_78743_2_, p_78743_3_, p_78743_4_));
                Block var5 = this.mc.theWorld.getBlock(p_78743_1_, p_78743_2_, p_78743_3_);
                boolean var6 = var5.getMaterial() != Material.air;
                if (var6 && this.curBlockDamageMP == 0.0F) {
                    var5.onBlockClicked(this.mc.theWorld, p_78743_1_, p_78743_2_, p_78743_3_, this.mc.thePlayer);
                }

                if (var6 && var5.getPlayerRelativeBlockHardness(this.mc.thePlayer, this.mc.thePlayer.worldObj, p_78743_1_, p_78743_2_, p_78743_3_) >= 1.0F) {
                    this.onPlayerDestroyBlock(p_78743_1_, p_78743_2_, p_78743_3_, p_78743_4_);
                } else {
                    this.isHittingBlock = true;
                    this.currentBlockX = p_78743_1_;
                    this.currentBlockY = p_78743_2_;
                    this.currentblockZ = p_78743_3_;
                    this.currentItemHittingBlock = this.mc.thePlayer.getHeldItem();
                    this.curBlockDamageMP = 0.0F;
                    this.stepSoundTickCounter = 0.0F;
                    this.mc.theWorld.destroyBlockInWorldPartially(this.mc.thePlayer.getEntityId(), this.currentBlockX, this.currentBlockY, this.currentblockZ, (int) (this.curBlockDamageMP * 10.0F) - 1);
                }
            }
        }
    }

    public void resetBlockRemoving() {
        if (this.isHittingBlock) {
            this.netClientHandler.addToSendQueue(new C07PacketPlayerDigging(1, this.currentBlockX, this.currentBlockY, this.currentblockZ, -1));
        }

        this.isHittingBlock = false;
        this.curBlockDamageMP = 0.0F;
        this.mc.theWorld.destroyBlockInWorldPartially(this.mc.thePlayer.getEntityId(), this.currentBlockX, this.currentBlockY, this.currentblockZ, -1);
    }

    public void onPlayerDamageBlock(int p_78759_1_, int p_78759_2_, int p_78759_3_, int p_78759_4_) {
        this.syncCurrentPlayItem();
        if (this.blockHitDelay > 0) {
            --this.blockHitDelay;
        } else if (this.currentGameType.isCreative()) {
            this.blockHitDelay = 5;
            this.netClientHandler.addToSendQueue(new C07PacketPlayerDigging(0, p_78759_1_, p_78759_2_, p_78759_3_, p_78759_4_));
            clickBlockCreative(this.mc, this, p_78759_1_, p_78759_2_, p_78759_3_, p_78759_4_);
        } else {
            if (this.sameToolAndBlock(p_78759_1_, p_78759_2_, p_78759_3_)) {
                Block var5 = this.mc.theWorld.getBlock(p_78759_1_, p_78759_2_, p_78759_3_);
                if (var5.getMaterial() == Material.air) {
                    this.isHittingBlock = false;
                    return;
                }

                this.curBlockDamageMP += var5.getPlayerRelativeBlockHardness(this.mc.thePlayer, this.mc.thePlayer.worldObj, p_78759_1_, p_78759_2_, p_78759_3_);
                if (this.stepSoundTickCounter % 4.0F == 0.0F) {
                    this.mc.getSoundHandler().playSound(new PositionedSoundRecord(new ResourceLocation(var5.stepSound.func_150498_e()), (var5.stepSound.func_150497_c() + 1.0F) / 8.0F, var5.stepSound.func_150494_d() * 0.5F, (float) p_78759_1_ + 0.5F, (float) p_78759_2_ + 0.5F, (float) p_78759_3_ + 0.5F));
                }

                ++this.stepSoundTickCounter;
                if (this.curBlockDamageMP >= 1.0F) {
                    this.isHittingBlock = false;
                    this.netClientHandler.addToSendQueue(new C07PacketPlayerDigging(2, p_78759_1_, p_78759_2_, p_78759_3_, p_78759_4_));
                    this.onPlayerDestroyBlock(p_78759_1_, p_78759_2_, p_78759_3_, p_78759_4_);
                    this.curBlockDamageMP = 0.0F;
                    this.stepSoundTickCounter = 0.0F;
                    this.blockHitDelay = 5;
                }

                this.mc.theWorld.destroyBlockInWorldPartially(this.mc.thePlayer.getEntityId(), this.currentBlockX, this.currentBlockY, this.currentblockZ, (int) (this.curBlockDamageMP * 10.0F) - 1);
            } else {
                this.clickBlock(p_78759_1_, p_78759_2_, p_78759_3_, p_78759_4_);
            }
        }
    }

    public float getBlockReachDistance() {
        return this.currentGameType.isCreative() ? 5.0F : 4.5F;
    }

    public void updateController() {
        this.syncCurrentPlayItem();
        if (this.netClientHandler.getNetworkManager().isChannelOpen()) {
            this.netClientHandler.getNetworkManager().processReceivedPackets();
        } else if (this.netClientHandler.getNetworkManager().getExitMessage() != null) {
            this.netClientHandler.getNetworkManager().getNetHandler().onDisconnect(this.netClientHandler.getNetworkManager().getExitMessage());
        } else {
            this.netClientHandler.getNetworkManager().getNetHandler().onDisconnect(new ChatComponentText("Disconnected from server"));
        }
    }

    private boolean sameToolAndBlock(int p_85182_1_, int p_85182_2_, int p_85182_3_) {
        ItemStack var4 = this.mc.thePlayer.getHeldItem();
        boolean var5 = this.currentItemHittingBlock == null && var4 == null;
        if (this.currentItemHittingBlock != null && var4 != null) {
            var5 = var4.getItem() == this.currentItemHittingBlock.getItem() && ItemStack.areItemStackTagsEqual(var4, this.currentItemHittingBlock) && (var4.isItemStackDamageable() || var4.getItemDamage() == this.currentItemHittingBlock.getItemDamage());
        }

        return p_85182_1_ == this.currentBlockX && p_85182_2_ == this.currentBlockY && p_85182_3_ == this.currentblockZ && var5;
    }

    private void syncCurrentPlayItem() {
        int var1 = this.mc.thePlayer.inventory.currentItem;
        if (var1 != this.currentPlayerItem) {
            this.currentPlayerItem = var1;
            this.netClientHandler.addToSendQueue(new C09PacketHeldItemChange(this.currentPlayerItem));
        }
    }

    public boolean onPlayerRightClick(EntityPlayer p_78760_1_, World p_78760_2_, ItemStack p_78760_3_, int p_78760_4_, int p_78760_5_, int p_78760_6_, int p_78760_7_, Vec3 p_78760_8_) {
        this.syncCurrentPlayItem();
        float var9 = (float) p_78760_8_.xCoord - (float) p_78760_4_;
        float var10 = (float) p_78760_8_.yCoord - (float) p_78760_5_;
        float var11 = (float) p_78760_8_.zCoord - (float) p_78760_6_;
        boolean var12 = false;
        if ((!p_78760_1_.isSneaking() || p_78760_1_.getHeldItem() == null) && p_78760_2_.getBlock(p_78760_4_, p_78760_5_, p_78760_6_).onBlockActivated(p_78760_2_, p_78760_4_, p_78760_5_, p_78760_6_, p_78760_1_, p_78760_7_, var9, var10, var11)) {
            var12 = true;
        }

        if (!var12 && p_78760_3_ != null && p_78760_3_.getItem() instanceof ItemBlock) {
            ItemBlock var13 = (ItemBlock) p_78760_3_.getItem();
            if (!var13.func_150936_a(p_78760_2_, p_78760_4_, p_78760_5_, p_78760_6_, p_78760_7_, p_78760_1_, p_78760_3_)) {
                return false;
            }
        }

        this.netClientHandler.addToSendQueue(new C08PacketPlayerBlockPlacement(p_78760_4_, p_78760_5_, p_78760_6_, p_78760_7_, p_78760_1_.inventory.getCurrentItem(), var9, var10, var11));
        if (var12) {
            return true;
        } else if (p_78760_3_ == null) {
            return false;
        } else if (this.currentGameType.isCreative()) {
            int var16 = p_78760_3_.getItemDamage();
            int var14 = p_78760_3_.stackSize;
            boolean var15 = p_78760_3_.tryPlaceItemIntoWorld(p_78760_1_, p_78760_2_, p_78760_4_, p_78760_5_, p_78760_6_, p_78760_7_, var9, var10, var11);
            p_78760_3_.setItemDamage(var16);
            p_78760_3_.stackSize = var14;
            return var15;
        } else {
            return p_78760_3_.tryPlaceItemIntoWorld(p_78760_1_, p_78760_2_, p_78760_4_, p_78760_5_, p_78760_6_, p_78760_7_, var9, var10, var11);
        }
    }

    public boolean sendUseItem(EntityPlayer p_78769_1_, World p_78769_2_, ItemStack p_78769_3_) {
        this.syncCurrentPlayItem();
        this.netClientHandler.addToSendQueue(new C08PacketPlayerBlockPlacement(-1, -1, -1, 255, p_78769_1_.inventory.getCurrentItem(), 0.0F, 0.0F, 0.0F));
        int var4 = p_78769_3_.stackSize;
        ItemStack var5 = p_78769_3_.useItemRightClick(p_78769_2_, p_78769_1_);
        if (var5 == p_78769_3_ && (var5 == null || var5.stackSize == var4)) {
            return false;
        } else {
            p_78769_1_.inventory.mainInventory[p_78769_1_.inventory.currentItem] = var5;
            if (var5.stackSize == 0) {
                p_78769_1_.inventory.mainInventory[p_78769_1_.inventory.currentItem] = null;
            }

            return true;
        }
    }

    public ClientPlayerMP func_147493_a(World world, StatFileWriter statFileWriter) {
        return new ClientPlayerMP(this.mc, world, this.mc.getSession(), this.netClientHandler, statFileWriter);
    }

    public void attackEntity(EntityPlayer entityPlayer, Entity entity) {
        this.syncCurrentPlayItem();
        this.netClientHandler.addToSendQueue(new C02PacketUseEntity(entity, C02PacketUseEntity.Action.ATTACK));
        entityPlayer.attackTargetEntityWithCurrentItem(entity);
    }

    public boolean interactWithEntitySendPacket(EntityPlayer entityPlayer, Entity entity) {
        this.syncCurrentPlayItem();
        this.netClientHandler.addToSendQueue(new C02PacketUseEntity(entity, C02PacketUseEntity.Action.INTERACT));
        return entityPlayer.interactWith(entity);
    }

    public ItemStack windowClick(int p_78753_1_, int p_78753_2_, int p_78753_3_, int p_78753_4_, EntityPlayer p_78753_5_) {
        short var6 = p_78753_5_.openContainer.getNextTransactionID(p_78753_5_.inventory);
        ItemStack var7 = p_78753_5_.openContainer.slotClick(p_78753_2_, p_78753_3_, p_78753_4_, p_78753_5_);
        this.netClientHandler.addToSendQueue(new C0EPacketClickWindow(p_78753_1_, p_78753_2_, p_78753_3_, p_78753_4_, var7, var6));
        return var7;
    }

    public void sendEnchantPacket(int p_78756_1_, int p_78756_2_) {
        this.netClientHandler.addToSendQueue(new C11PacketEnchantItem(p_78756_1_, p_78756_2_));
    }

    public void sendSlotPacket(ItemStack p_78761_1_, int p_78761_2_) {
        if (this.currentGameType.isCreative()) {
            this.netClientHandler.addToSendQueue(new C10PacketCreativeInventoryAction(p_78761_2_, p_78761_1_));
        }
    }

    public void sendPacketDropItem(ItemStack itemStack) {
        if (this.currentGameType.isCreative() && itemStack != null) {
            this.netClientHandler.addToSendQueue(new C10PacketCreativeInventoryAction(-1, itemStack));
        }
    }

    public void onStoppedUsingItem(EntityPlayer p_78766_1_) {
        this.syncCurrentPlayItem();
        this.netClientHandler.addToSendQueue(new C07PacketPlayerDigging(5, 0, 0, 0, 255));
        p_78766_1_.stopUsingItem();
    }

    public boolean gameIsSurvivalOrAdventure() {
        return this.currentGameType.isSurvivalOrAdventure();
    }

    public boolean isNotCreative() {
        return !this.currentGameType.isCreative();
    }

    public boolean isInCreativeMode() {
        return this.currentGameType.isCreative();
    }

    public boolean extendedReach() {
        return this.currentGameType.isCreative();
    }

    public boolean func_110738_j() {
        return this.mc.thePlayer.isRiding() && this.mc.thePlayer.ridingEntity instanceof EntityHorse;
    }

}
