package net.minecraft.entity.ai;

import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;

public class EntityAILookAtTradePlayer extends EntityAIWatchClosest {
    private static final String __OBFID = "CL_00001593";
    private final EntityVillager theMerchant;

    public EntityAILookAtTradePlayer(EntityVillager p_i1633_1_) {
        super(p_i1633_1_, EntityPlayer.class, 8.0F);
        this.theMerchant = p_i1633_1_;
    }

    public boolean shouldExecute() {
        if (this.theMerchant.isTrading()) {
            this.closestEntity = this.theMerchant.getCustomer();
            return true;
        } else {
            return false;
        }
    }
}
