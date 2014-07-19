package net.minecraft.entity;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.ai.attributes.*;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;
import net.minecraft.network.play.server.S04PacketEntityEquipment;
import net.minecraft.network.play.server.S0BPacketAnimation;
import net.minecraft.network.play.server.S0DPacketCollectItem;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionHelper;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import java.util.*;

public abstract class EntityLivingBase extends Entity {
    private static final UUID sprintingSpeedBoostModifierUUID = UUID.fromString("662A6B8D-DA3E-4C1C-8813-96EA6097278D");
    private static final AttributeModifier sprintingSpeedBoostModifier = (new AttributeModifier(sprintingSpeedBoostModifierUUID, "Sprinting speed boost", 0.30000001192092896D, 2)).setSaved(false);
    private static final String __OBFID = "CL_00001549";
    private final CombatTracker _combatTracker = new CombatTracker(this);
    private final HashMap activePotionsMap = new HashMap();
    private final ItemStack[] previousEquipment = new ItemStack[5];
    public boolean isSwingInProgress;
    public int swingProgressInt;
    public int arrowHitTimer;
    public float prevHealth;
    public int hurtTime;
    public int maxHurtTime;
    public float attackedAtYaw;
    public int deathTime;
    public int attackTime;
    public float prevSwingProgress;
    public float swingProgress;
    public float prevLimbSwingAmount;
    public float limbSwingAmount;
    public float limbSwing;
    public int maxHurtResistantTime = 20;
    public float prevCameraPitch;
    public float cameraPitch;
    public float field_70769_ao;
    public float field_70770_ap;
    public float renderYawOffset;
    public float prevRenderYawOffset;
    public float rotationYawHead;
    public float prevRotationYawHead;
    public float jumpMovementFactor = 0.02F;
    public float moveStrafing;
    public float moveForward;
    protected EntityPlayer attackingPlayer;
    protected int recentlyHit;
    protected boolean dead;
    protected int entityAge;
    protected float field_70768_au;
    protected float field_110154_aX;
    protected float field_70764_aw;
    protected float field_70763_ax;
    protected float field_70741_aB;
    protected int scoreValue;
    protected float lastDamage;
    protected boolean isJumping;
    protected float randomYawVelocity;
    protected int newPosRotationIncrements;
    protected double newPosX;
    protected double newPosY;
    protected double newPosZ;
    protected double newRotationYaw;
    protected double newRotationPitch;
    private BaseAttributeMap attributeMap;
    private boolean potionsNeedUpdate = true;
    private EntityLivingBase entityLivingToAttack;
    private int revengeTimer;
    private EntityLivingBase lastAttacker;
    private int lastAttackerTime;
    private float landMovementFactor;
    private int jumpTicks;
    private float field_110151_bq;

    public EntityLivingBase(World p_i1594_1_) {
        super(p_i1594_1_);
        this.applyEntityAttributes();
        this.setHealth(this.getMaxHealth());
        this.preventEntitySpawning = true;
        this.field_70770_ap = (float) (Math.random() + 1.0D) * 0.01F;
        this.setPosition(this.posX, this.posY, this.posZ);
        this.field_70769_ao = (float) Math.random() * 12398.0F;
        this.rotationYaw = (float) (Math.random() * Math.PI * 2.0D);
        this.rotationYawHead = this.rotationYaw;
        this.stepHeight = 0.5F;
    }

    protected void entityInit() {
        this.dataWatcher.addObject(7, Integer.valueOf(0));
        this.dataWatcher.addObject(8, Byte.valueOf((byte) 0));
        this.dataWatcher.addObject(9, Byte.valueOf((byte) 0));
        this.dataWatcher.addObject(6, Float.valueOf(1.0F));
    }

    protected void applyEntityAttributes() {
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.maxHealth);
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.knockbackResistance);
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.movementSpeed);
        if (!this.isAIEnabled()) {
            this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.10000000149011612D);
        }
    }

    protected void updateFallState(double p_70064_1_, boolean p_70064_3_) {
        if (!this.isInWater()) {
            this.handleWaterMovement();
        }

        if (p_70064_3_ && this.fallDistance > 0.0F) {
            int var4 = MathHelper.floor_double(this.posX);
            int var5 = MathHelper.floor_double(this.posY - 0.20000000298023224D - (double) this.yOffset);
            int var6 = MathHelper.floor_double(this.posZ);
            Block var7 = this.worldObj.getBlock(var4, var5, var6);
            if (var7.getMaterial() == Material.air) {
                int var8 = this.worldObj.getBlock(var4, var5 - 1, var6).getRenderType();
                if (var8 == 11 || var8 == 32 || var8 == 21) {
                    var7 = this.worldObj.getBlock(var4, var5 - 1, var6);
                }
            } else if (!this.worldObj.isClient && this.fallDistance > 3.0F) {
                this.worldObj.playAuxSFX(2006, var4, var5, var6, MathHelper.ceiling_float_int(this.fallDistance - 3.0F));
            }

            var7.onFallenUpon(this.worldObj, var4, var5, var6, this, this.fallDistance);
        }

        super.updateFallState(p_70064_1_, p_70064_3_);
    }

    public boolean canBreatheUnderwater() {
        return false;
    }

    public void onEntityUpdate() {
        this.prevSwingProgress = this.swingProgress;
        super.onEntityUpdate();
        this.worldObj.theProfiler.startSection("livingEntityBaseTick");
        if (this.isEntityAlive() && this.isEntityInsideOpaqueBlock()) {
            this.attackEntityFrom(DamageSource.inWall, 1.0F);
        }

        if (this.isImmuneToFire() || this.worldObj.isClient) {
            this.extinguish();
        }

        boolean var1 = this instanceof EntityPlayer && ((EntityPlayer) this).capabilities.disableDamage;
        if (this.isEntityAlive() && this.isInsideOfMaterial(Material.water)) {
            if (!this.canBreatheUnderwater() && !this.isPotionActive(Potion.waterBreathing.id) && !var1) {
                this.setAir(this.decreaseAirSupply(this.getAir()));
                if (this.getAir() == -20) {
                    this.setAir(0);

                    for (int var2 = 0; var2 < 8; ++var2) {
                        float var3 = this.rand.nextFloat() - this.rand.nextFloat();
                        float var4 = this.rand.nextFloat() - this.rand.nextFloat();
                        float var5 = this.rand.nextFloat() - this.rand.nextFloat();
                        this.worldObj.spawnParticle("bubble", this.posX + (double) var3, this.posY + (double) var4, this.posZ + (double) var5, this.motionX, this.motionY, this.motionZ);
                    }

                    this.attackEntityFrom(DamageSource.drown, 2.0F);
                }
            }

            if (!this.worldObj.isClient && this.isRiding() && this.ridingEntity instanceof EntityLivingBase) {
                this.mountEntity((Entity) null);
            }
        } else {
            this.setAir(300);
        }

        if (this.isEntityAlive() && this.isWet()) {
            this.extinguish();
        }

        this.prevCameraPitch = this.cameraPitch;
        if (this.attackTime > 0) {
            --this.attackTime;
        }

        if (this.hurtTime > 0) {
            --this.hurtTime;
        }

        if (this.hurtResistantTime > 0 && !(this instanceof EntityPlayerMP)) {
            --this.hurtResistantTime;
        }

        if (this.getHealth() <= 0.0F) {
            this.onDeathUpdate();
        }

        if (this.recentlyHit > 0) {
            --this.recentlyHit;
        } else {
            this.attackingPlayer = null;
        }

        if (this.lastAttacker != null && !this.lastAttacker.isEntityAlive()) {
            this.lastAttacker = null;
        }

        if (this.entityLivingToAttack != null) {
            if (!this.entityLivingToAttack.isEntityAlive()) {
                this.setRevengeTarget((EntityLivingBase) null);
            } else if (this.ticksExisted - this.revengeTimer > 100) {
                this.setRevengeTarget((EntityLivingBase) null);
            }
        }

        this.updatePotionEffects();
        this.field_70763_ax = this.field_70764_aw;
        this.prevRenderYawOffset = this.renderYawOffset;
        this.prevRotationYawHead = this.rotationYawHead;
        this.prevRotationYaw = this.rotationYaw;
        this.prevRotationPitch = this.rotationPitch;
        this.worldObj.theProfiler.endSection();
    }

    public boolean isChild() {
        return false;
    }

    protected void onDeathUpdate() {
        ++this.deathTime;
        if (this.deathTime == 20) {
            int var1;
            if (!this.worldObj.isClient && (this.recentlyHit > 0 || this.isPlayer()) && this.func_146066_aG() && this.worldObj.getGameRules().getGameRuleBooleanValue("doMobLoot")) {
                var1 = this.getExperiencePoints(this.attackingPlayer);

                while (var1 > 0) {
                    int var2 = EntityXPOrb.getXPSplit(var1);
                    var1 -= var2;
                    this.worldObj.spawnEntityInWorld(new EntityXPOrb(this.worldObj, this.posX, this.posY, this.posZ, var2));
                }
            }

            this.setDead();

            for (var1 = 0; var1 < 20; ++var1) {
                double var8 = this.rand.nextGaussian() * 0.02D;
                double var4 = this.rand.nextGaussian() * 0.02D;
                double var6 = this.rand.nextGaussian() * 0.02D;
                this.worldObj.spawnParticle("explode", this.posX + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, this.posY + (double) (this.rand.nextFloat() * this.height), this.posZ + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, var8, var4, var6);
            }
        }
    }

    protected boolean func_146066_aG() {
        return !this.isChild();
    }

    protected int decreaseAirSupply(int p_70682_1_) {
        int var2 = EnchantmentHelper.getRespiration(this);
        return var2 > 0 && this.rand.nextInt(var2 + 1) > 0 ? p_70682_1_ : p_70682_1_ - 1;
    }

    protected int getExperiencePoints(EntityPlayer p_70693_1_) {
        return 0;
    }

    protected boolean isPlayer() {
        return false;
    }

    public Random getRNG() {
        return this.rand;
    }

    public EntityLivingBase getAITarget() {
        return this.entityLivingToAttack;
    }

    public int func_142015_aE() {
        return this.revengeTimer;
    }

    public void setRevengeTarget(EntityLivingBase p_70604_1_) {
        this.entityLivingToAttack = p_70604_1_;
        this.revengeTimer = this.ticksExisted;
    }

    public EntityLivingBase getLastAttacker() {
        return this.lastAttacker;
    }

    public void setLastAttacker(Entity p_130011_1_) {
        if (p_130011_1_ instanceof EntityLivingBase) {
            this.lastAttacker = (EntityLivingBase) p_130011_1_;
        } else {
            this.lastAttacker = null;
        }

        this.lastAttackerTime = this.ticksExisted;
    }

    public int getLastAttackerTime() {
        return this.lastAttackerTime;
    }

    public int getAge() {
        return this.entityAge;
    }

    public void writeEntityToNBT(NBTTagCompound p_70014_1_) {
        p_70014_1_.setFloat("HealF", this.getHealth());
        p_70014_1_.setShort("Health", (short) ((int) Math.ceil((double) this.getHealth())));
        p_70014_1_.setShort("HurtTime", (short) this.hurtTime);
        p_70014_1_.setShort("DeathTime", (short) this.deathTime);
        p_70014_1_.setShort("AttackTime", (short) this.attackTime);
        p_70014_1_.setFloat("AbsorptionAmount", this.getAbsorptionAmount());
        ItemStack[] var2 = this.getLastActiveItems();
        int var3 = var2.length;

        int var4;
        ItemStack var5;
        for (var4 = 0; var4 < var3; ++var4) {
            var5 = var2[var4];
            if (var5 != null) {
                this.attributeMap.removeAttributeModifiers(var5.getAttributeModifiers());
            }
        }

        p_70014_1_.setTag("Attributes", SharedMonsterAttributes.writeBaseAttributeMapToNBT(this.getAttributeMap()));
        var2 = this.getLastActiveItems();
        var3 = var2.length;

        for (var4 = 0; var4 < var3; ++var4) {
            var5 = var2[var4];
            if (var5 != null) {
                this.attributeMap.applyAttributeModifiers(var5.getAttributeModifiers());
            }
        }

        if (!this.activePotionsMap.isEmpty()) {
            NBTTagList var6 = new NBTTagList();
            Iterator var7 = this.activePotionsMap.values().iterator();

            while (var7.hasNext()) {
                PotionEffect var8 = (PotionEffect) var7.next();
                var6.appendTag(var8.writeCustomPotionEffectToNBT(new NBTTagCompound()));
            }

            p_70014_1_.setTag("ActiveEffects", var6);
        }
    }

    public void readEntityFromNBT(NBTTagCompound p_70037_1_) {
        this.setAbsorptionAmount(p_70037_1_.getFloat("AbsorptionAmount"));
        if (p_70037_1_.func_150297_b("Attributes", 9) && this.worldObj != null && !this.worldObj.isClient) {
            SharedMonsterAttributes.func_151475_a(this.getAttributeMap(), p_70037_1_.getTagList("Attributes", 10));
        }

        if (p_70037_1_.func_150297_b("ActiveEffects", 9)) {
            NBTTagList var2 = p_70037_1_.getTagList("ActiveEffects", 10);

            for (int var3 = 0; var3 < var2.tagCount(); ++var3) {
                NBTTagCompound var4 = var2.getCompoundTagAt(var3);
                PotionEffect var5 = PotionEffect.readCustomPotionEffectFromNBT(var4);
                if (var5 != null) {
                    this.activePotionsMap.put(Integer.valueOf(var5.getPotionID()), var5);
                }
            }
        }

        if (p_70037_1_.func_150297_b("HealF", 99)) {
            this.setHealth(p_70037_1_.getFloat("HealF"));
        } else {
            NBTBase var6 = p_70037_1_.getTag("Health");
            if (var6 == null) {
                this.setHealth(this.getMaxHealth());
            } else if (var6.getId() == 5) {
                this.setHealth(((NBTTagFloat) var6).func_150288_h());
            } else if (var6.getId() == 2) {
                this.setHealth((float) ((NBTTagShort) var6).func_150289_e());
            }
        }

        this.hurtTime = p_70037_1_.getShort("HurtTime");
        this.deathTime = p_70037_1_.getShort("DeathTime");
        this.attackTime = p_70037_1_.getShort("AttackTime");
    }

    protected void updatePotionEffects() {
        Iterator var1 = this.activePotionsMap.keySet().iterator();

        while (var1.hasNext()) {
            Integer var2 = (Integer) var1.next();
            PotionEffect var3 = (PotionEffect) this.activePotionsMap.get(var2);
            if (!var3.onUpdate(this)) {
                if (!this.worldObj.isClient) {
                    var1.remove();
                    this.onFinishedPotionEffect(var3);
                }
            } else if (var3.getDuration() % 600 == 0) {
                this.onChangedPotionEffect(var3, false);
            }
        }

        int var11;
        if (this.potionsNeedUpdate) {
            if (!this.worldObj.isClient) {
                if (this.activePotionsMap.isEmpty()) {
                    this.dataWatcher.updateObject(8, Byte.valueOf((byte) 0));
                    this.dataWatcher.updateObject(7, Integer.valueOf(0));
                    this.setInvisible(false);
                } else {
                    var11 = PotionHelper.calcPotionLiquidColor(this.activePotionsMap.values());
                    this.dataWatcher.updateObject(8, Byte.valueOf((byte) (PotionHelper.func_82817_b(this.activePotionsMap.values()) ? 1 : 0)));
                    this.dataWatcher.updateObject(7, Integer.valueOf(var11));
                    this.setInvisible(this.isPotionActive(Potion.invisibility.id));
                }
            }

            this.potionsNeedUpdate = false;
        }

        var11 = this.dataWatcher.getWatchableObjectInt(7);
        boolean var12 = this.dataWatcher.getWatchableObjectByte(8) > 0;
        if (var11 > 0) {
            boolean var4 = false;
            if (!this.isInvisible()) {
                var4 = this.rand.nextBoolean();
            } else {
                var4 = this.rand.nextInt(15) == 0;
            }

            if (var12) {
                var4 &= this.rand.nextInt(5) == 0;
            }

            if (var4 && var11 > 0) {
                double var5 = (double) (var11 >> 16 & 255) / 255.0D;
                double var7 = (double) (var11 >> 8 & 255) / 255.0D;
                double var9 = (double) (var11 >> 0 & 255) / 255.0D;
                this.worldObj.spawnParticle(var12 ? "mobSpellAmbient" : "mobSpell", this.posX + (this.rand.nextDouble() - 0.5D) * (double) this.width, this.posY + this.rand.nextDouble() * (double) this.height - (double) this.yOffset, this.posZ + (this.rand.nextDouble() - 0.5D) * (double) this.width, var5, var7, var9);
            }
        }
    }

    public void clearActivePotions() {
        Iterator var1 = this.activePotionsMap.keySet().iterator();

        while (var1.hasNext()) {
            Integer var2 = (Integer) var1.next();
            PotionEffect var3 = (PotionEffect) this.activePotionsMap.get(var2);
            if (!this.worldObj.isClient) {
                var1.remove();
                this.onFinishedPotionEffect(var3);
            }
        }
    }

    public Collection getActivePotionEffects() {
        return this.activePotionsMap.values();
    }

    public boolean isPotionActive(int p_82165_1_) {
        return this.activePotionsMap.containsKey(Integer.valueOf(p_82165_1_));
    }

    public boolean isPotionActive(Potion p_70644_1_) {
        return this.activePotionsMap.containsKey(Integer.valueOf(p_70644_1_.id));
    }

    public PotionEffect getActivePotionEffect(Potion p_70660_1_) {
        return (PotionEffect) this.activePotionsMap.get(Integer.valueOf(p_70660_1_.id));
    }

    public void addPotionEffect(PotionEffect p_70690_1_) {
        if (this.isPotionApplicable(p_70690_1_)) {
            if (this.activePotionsMap.containsKey(Integer.valueOf(p_70690_1_.getPotionID()))) {
                ((PotionEffect) this.activePotionsMap.get(Integer.valueOf(p_70690_1_.getPotionID()))).combine(p_70690_1_);
                this.onChangedPotionEffect((PotionEffect) this.activePotionsMap.get(Integer.valueOf(p_70690_1_.getPotionID())), true);
            } else {
                this.activePotionsMap.put(Integer.valueOf(p_70690_1_.getPotionID()), p_70690_1_);
                this.onNewPotionEffect(p_70690_1_);
            }
        }
    }

    public boolean isPotionApplicable(PotionEffect p_70687_1_) {
        if (this.getCreatureAttribute() == EnumCreatureAttribute.UNDEAD) {
            int var2 = p_70687_1_.getPotionID();
            if (var2 == Potion.regeneration.id || var2 == Potion.poison.id) {
                return false;
            }
        }

        return true;
    }

    public boolean isEntityUndead() {
        return this.getCreatureAttribute() == EnumCreatureAttribute.UNDEAD;
    }

    public void removePotionEffectClient(int p_70618_1_) {
        this.activePotionsMap.remove(Integer.valueOf(p_70618_1_));
    }

    public void removePotionEffect(int p_82170_1_) {
        PotionEffect var2 = (PotionEffect) this.activePotionsMap.remove(Integer.valueOf(p_82170_1_));
        if (var2 != null) {
            this.onFinishedPotionEffect(var2);
        }
    }

    protected void onNewPotionEffect(PotionEffect p_70670_1_) {
        this.potionsNeedUpdate = true;
        if (!this.worldObj.isClient) {
            Potion.potionTypes[p_70670_1_.getPotionID()].applyAttributesModifiersToEntity(this, this.getAttributeMap(), p_70670_1_.getAmplifier());
        }
    }

    protected void onChangedPotionEffect(PotionEffect p_70695_1_, boolean p_70695_2_) {
        this.potionsNeedUpdate = true;
        if (p_70695_2_ && !this.worldObj.isClient) {
            Potion.potionTypes[p_70695_1_.getPotionID()].removeAttributesModifiersFromEntity(this, this.getAttributeMap(), p_70695_1_.getAmplifier());
            Potion.potionTypes[p_70695_1_.getPotionID()].applyAttributesModifiersToEntity(this, this.getAttributeMap(), p_70695_1_.getAmplifier());
        }
    }

    protected void onFinishedPotionEffect(PotionEffect p_70688_1_) {
        this.potionsNeedUpdate = true;
        if (!this.worldObj.isClient) {
            Potion.potionTypes[p_70688_1_.getPotionID()].removeAttributesModifiersFromEntity(this, this.getAttributeMap(), p_70688_1_.getAmplifier());
        }
    }

    public void heal(float p_70691_1_) {
        float var2 = this.getHealth();
        if (var2 > 0.0F) {
            this.setHealth(var2 + p_70691_1_);
        }
    }

    public final float getHealth() {
        return this.dataWatcher.getWatchableObjectFloat(6);
    }

    public void setHealth(float p_70606_1_) {
        this.dataWatcher.updateObject(6, Float.valueOf(MathHelper.clamp_float(p_70606_1_, 0.0F, this.getMaxHealth())));
    }

    public boolean attackEntityFrom(DamageSource p_70097_1_, float p_70097_2_) {
        if (this.isEntityInvulnerable()) {
            return false;
        } else if (this.worldObj.isClient) {
            return false;
        } else {
            this.entityAge = 0;
            if (this.getHealth() <= 0.0F) {
                return false;
            } else if (p_70097_1_.isFireDamage() && this.isPotionActive(Potion.fireResistance)) {
                return false;
            } else {
                if ((p_70097_1_ == DamageSource.anvil || p_70097_1_ == DamageSource.fallingBlock) && this.getEquipmentInSlot(4) != null) {
                    this.getEquipmentInSlot(4).damageItem((int) (p_70097_2_ * 4.0F + this.rand.nextFloat() * p_70097_2_ * 2.0F), this);
                    p_70097_2_ *= 0.75F;
                }

                this.limbSwingAmount = 1.5F;
                boolean var3 = true;
                if ((float) this.hurtResistantTime > (float) this.maxHurtResistantTime / 2.0F) {
                    if (p_70097_2_ <= this.lastDamage) {
                        return false;
                    }

                    this.damageEntity(p_70097_1_, p_70097_2_ - this.lastDamage);
                    this.lastDamage = p_70097_2_;
                    var3 = false;
                } else {
                    this.lastDamage = p_70097_2_;
                    this.prevHealth = this.getHealth();
                    this.hurtResistantTime = this.maxHurtResistantTime;
                    this.damageEntity(p_70097_1_, p_70097_2_);
                    this.hurtTime = this.maxHurtTime = 10;
                }

                this.attackedAtYaw = 0.0F;
                Entity var4 = p_70097_1_.getEntity();
                if (var4 != null) {
                    if (var4 instanceof EntityLivingBase) {
                        this.setRevengeTarget((EntityLivingBase) var4);
                    }

                    if (var4 instanceof EntityPlayer) {
                        this.recentlyHit = 100;
                        this.attackingPlayer = (EntityPlayer) var4;
                    } else if (var4 instanceof EntityWolf) {
                        EntityWolf var5 = (EntityWolf) var4;
                        if (var5.isTamed()) {
                            this.recentlyHit = 100;
                            this.attackingPlayer = null;
                        }
                    }
                }

                if (var3) {
                    this.worldObj.setEntityState(this, (byte) 2);
                    if (p_70097_1_ != DamageSource.drown) {
                        this.setBeenAttacked();
                    }

                    if (var4 != null) {
                        double var9 = var4.posX - this.posX;

                        double var7;
                        for (var7 = var4.posZ - this.posZ; var9 * var9 + var7 * var7 < 1.0E-4D; var7 = (Math.random() - Math.random()) * 0.01D) {
                            var9 = (Math.random() - Math.random()) * 0.01D;
                        }

                        this.attackedAtYaw = (float) (Math.atan2(var7, var9) * 180.0D / Math.PI) - this.rotationYaw;
                        this.knockBack(var4, p_70097_2_, var9, var7);
                    } else {
                        this.attackedAtYaw = (float) ((int) (Math.random() * 2.0D) * 180);
                    }
                }

                String var10;
                if (this.getHealth() <= 0.0F) {
                    var10 = this.getDeathSound();
                    if (var3 && var10 != null) {
                        this.playSound(var10, this.getSoundVolume(), this.getSoundPitch());
                    }

                    this.onDeath(p_70097_1_);
                } else {
                    var10 = this.getHurtSound();
                    if (var3 && var10 != null) {
                        this.playSound(var10, this.getSoundVolume(), this.getSoundPitch());
                    }
                }

                return true;
            }
        }
    }

    public void renderBrokenItemStack(ItemStack p_70669_1_) {
        this.playSound("random.break", 0.8F, 0.8F + this.worldObj.rand.nextFloat() * 0.4F);

        for (int var2 = 0; var2 < 5; ++var2) {
            Vec3 var3 = Vec3.createVectorHelper(((double) this.rand.nextFloat() - 0.5D) * 0.1D, Math.random() * 0.1D + 0.1D, 0.0D);
            var3.rotateAroundX(-this.rotationPitch * (float) Math.PI / 180.0F);
            var3.rotateAroundY(-this.rotationYaw * (float) Math.PI / 180.0F);
            Vec3 var4 = Vec3.createVectorHelper(((double) this.rand.nextFloat() - 0.5D) * 0.3D, (double) (-this.rand.nextFloat()) * 0.6D - 0.3D, 0.6D);
            var4.rotateAroundX(-this.rotationPitch * (float) Math.PI / 180.0F);
            var4.rotateAroundY(-this.rotationYaw * (float) Math.PI / 180.0F);
            var4 = var4.addVector(this.posX, this.posY + (double) this.getEyeHeight(), this.posZ);
            this.worldObj.spawnParticle("iconcrack_" + Item.getIdFromItem(p_70669_1_.getItem()), var4.xCoord, var4.yCoord, var4.zCoord, var3.xCoord, var3.yCoord + 0.05D, var3.zCoord);
        }
    }

    public void onDeath(DamageSource p_70645_1_) {
        Entity var2 = p_70645_1_.getEntity();
        EntityLivingBase var3 = this.func_94060_bK();
        if (this.scoreValue >= 0 && var3 != null) {
            var3.addToPlayerScore(this, this.scoreValue);
        }

        if (var2 != null) {
            var2.onKillEntity(this);
        }

        this.dead = true;
        this.func_110142_aN().func_94549_h();
        if (!this.worldObj.isClient) {
            int var4 = 0;
            if (var2 instanceof EntityPlayer) {
                var4 = EnchantmentHelper.getLootingModifier((EntityLivingBase) var2);
            }

            if (this.func_146066_aG() && this.worldObj.getGameRules().getGameRuleBooleanValue("doMobLoot")) {
                this.dropFewItems(this.recentlyHit > 0, var4);
                this.dropEquipment(this.recentlyHit > 0, var4);
                if (this.recentlyHit > 0) {
                    int var5 = this.rand.nextInt(200) - var4;
                    if (var5 < 5) {
                        this.dropRareDrop(var5 <= 0 ? 1 : 0);
                    }
                }
            }
        }

        this.worldObj.setEntityState(this, (byte) 3);
    }

    protected void dropEquipment(boolean p_82160_1_, int p_82160_2_) {
    }

    public void knockBack(Entity p_70653_1_, float p_70653_2_, double p_70653_3_, double p_70653_5_) {
        if (this.rand.nextDouble() >= this.getEntityAttribute(SharedMonsterAttributes.knockbackResistance).getAttributeValue()) {
            this.isAirBorne = true;
            float var7 = MathHelper.sqrt_double(p_70653_3_ * p_70653_3_ + p_70653_5_ * p_70653_5_);
            float var8 = 0.4F;
            this.motionX /= 2.0D;
            this.motionY /= 2.0D;
            this.motionZ /= 2.0D;
            this.motionX -= p_70653_3_ / (double) var7 * (double) var8;
            this.motionY += (double) var8;
            this.motionZ -= p_70653_5_ / (double) var7 * (double) var8;
            if (this.motionY > 0.4000000059604645D) {
                this.motionY = 0.4000000059604645D;
            }
        }
    }

    protected String getHurtSound() {
        return "game.neutral.hurt";
    }

    protected String getDeathSound() {
        return "game.neutral.die";
    }

    protected void dropRareDrop(int p_70600_1_) {
    }

    protected void dropFewItems(boolean p_70628_1_, int p_70628_2_) {
    }

    public boolean isOnLadder() {
        int var1 = MathHelper.floor_double(this.posX);
        int var2 = MathHelper.floor_double(this.boundingBox.minY);
        int var3 = MathHelper.floor_double(this.posZ);
        Block var4 = this.worldObj.getBlock(var1, var2, var3);
        return var4 == Blocks.ladder || var4 == Blocks.vine;
    }

    public boolean isEntityAlive() {
        return !this.isDead && this.getHealth() > 0.0F;
    }

    protected void fall(float p_70069_1_) {
        super.fall(p_70069_1_);
        PotionEffect var2 = this.getActivePotionEffect(Potion.jump);
        float var3 = var2 != null ? (float) (var2.getAmplifier() + 1) : 0.0F;
        int var4 = MathHelper.ceiling_float_int(p_70069_1_ - 3.0F - var3);
        if (var4 > 0) {
            this.playSound(this.func_146067_o(var4), 1.0F, 1.0F);
            this.attackEntityFrom(DamageSource.fall, (float) var4);
            int var5 = MathHelper.floor_double(this.posX);
            int var6 = MathHelper.floor_double(this.posY - 0.20000000298023224D - (double) this.yOffset);
            int var7 = MathHelper.floor_double(this.posZ);
            Block var8 = this.worldObj.getBlock(var5, var6, var7);
            if (var8.getMaterial() != Material.air) {
                Block.SoundType var9 = var8.stepSound;
                this.playSound(var9.func_150498_e(), var9.func_150497_c() * 0.5F, var9.func_150494_d() * 0.75F);
            }
        }
    }

    protected String func_146067_o(int p_146067_1_) {
        return p_146067_1_ > 4 ? "game.neutral.hurt.fall.big" : "game.neutral.hurt.fall.small";
    }

    public void performHurtAnimation() {
        this.hurtTime = this.maxHurtTime = 10;
        this.attackedAtYaw = 0.0F;
    }

    public int getTotalArmorValue() {
        int var1 = 0;
        ItemStack[] var2 = this.getLastActiveItems();
        int var3 = var2.length;

        for (int var4 = 0; var4 < var3; ++var4) {
            ItemStack var5 = var2[var4];
            if (var5 != null && var5.getItem() instanceof ItemArmor) {
                int var6 = ((ItemArmor) var5.getItem()).damageReduceAmount;
                var1 += var6;
            }
        }

        return var1;
    }

    protected void damageArmor(float p_70675_1_) {
    }

    protected float applyArmorCalculations(DamageSource p_70655_1_, float p_70655_2_) {
        if (!p_70655_1_.isUnblockable()) {
            int var3 = 25 - this.getTotalArmorValue();
            float var4 = p_70655_2_ * (float) var3;
            this.damageArmor(p_70655_2_);
            p_70655_2_ = var4 / 25.0F;
        }

        return p_70655_2_;
    }

    protected float applyPotionDamageCalculations(DamageSource p_70672_1_, float p_70672_2_) {
        if (p_70672_1_.isDamageAbsolute()) {
            return p_70672_2_;
        } else {
            if (this instanceof EntityZombie) {
                p_70672_2_ = p_70672_2_;
            }

            int var3;
            int var4;
            float var5;
            if (this.isPotionActive(Potion.resistance) && p_70672_1_ != DamageSource.outOfWorld) {
                var3 = (this.getActivePotionEffect(Potion.resistance).getAmplifier() + 1) * 5;
                var4 = 25 - var3;
                var5 = p_70672_2_ * (float) var4;
                p_70672_2_ = var5 / 25.0F;
            }

            if (p_70672_2_ <= 0.0F) {
                return 0.0F;
            } else {
                var3 = EnchantmentHelper.getEnchantmentModifierDamage(this.getLastActiveItems(), p_70672_1_);
                if (var3 > 20) {
                    var3 = 20;
                }

                if (var3 > 0 && var3 <= 20) {
                    var4 = 25 - var3;
                    var5 = p_70672_2_ * (float) var4;
                    p_70672_2_ = var5 / 25.0F;
                }

                return p_70672_2_;
            }
        }
    }

    protected void damageEntity(DamageSource p_70665_1_, float p_70665_2_) {
        if (!this.isEntityInvulnerable()) {
            p_70665_2_ = this.applyArmorCalculations(p_70665_1_, p_70665_2_);
            p_70665_2_ = this.applyPotionDamageCalculations(p_70665_1_, p_70665_2_);
            float var3 = p_70665_2_;
            p_70665_2_ = Math.max(p_70665_2_ - this.getAbsorptionAmount(), 0.0F);
            this.setAbsorptionAmount(this.getAbsorptionAmount() - (var3 - p_70665_2_));
            if (p_70665_2_ != 0.0F) {
                float var4 = this.getHealth();
                this.setHealth(var4 - p_70665_2_);
                this.func_110142_aN().func_94547_a(p_70665_1_, var4, p_70665_2_);
                this.setAbsorptionAmount(this.getAbsorptionAmount() - p_70665_2_);
            }
        }
    }

    public CombatTracker func_110142_aN() {
        return this._combatTracker;
    }

    public EntityLivingBase func_94060_bK() {
        return (EntityLivingBase) (this._combatTracker.func_94550_c() != null ? this._combatTracker.func_94550_c() : (this.attackingPlayer != null ? this.attackingPlayer : (this.entityLivingToAttack != null ? this.entityLivingToAttack : null)));
    }

    public final float getMaxHealth() {
        return (float) this.getEntityAttribute(SharedMonsterAttributes.maxHealth).getAttributeValue();
    }

    public final int getArrowCountInEntity() {
        return this.dataWatcher.getWatchableObjectByte(9);
    }

    public final void setArrowCountInEntity(int p_85034_1_) {
        this.dataWatcher.updateObject(9, Byte.valueOf((byte) p_85034_1_));
    }

    private int getArmSwingAnimationEnd() {
        return this.isPotionActive(Potion.digSpeed) ? 6 - (1 + this.getActivePotionEffect(Potion.digSpeed).getAmplifier()) * 1 : (this.isPotionActive(Potion.digSlowdown) ? 6 + (1 + this.getActivePotionEffect(Potion.digSlowdown).getAmplifier()) * 2 : 6);
    }

    public void swingItem() {
        if (!this.isSwingInProgress || this.swingProgressInt >= this.getArmSwingAnimationEnd() / 2 || this.swingProgressInt < 0) {
            this.swingProgressInt = -1;
            this.isSwingInProgress = true;
            if (this.worldObj instanceof WorldServer) {
                ((WorldServer) this.worldObj).getEntityTracker().func_151247_a(this, new S0BPacketAnimation(this, 0));
            }
        }
    }

    public void handleHealthUpdate(byte p_70103_1_) {
        if (p_70103_1_ == 2) {
            this.limbSwingAmount = 1.5F;
            this.hurtResistantTime = this.maxHurtResistantTime;
            this.hurtTime = this.maxHurtTime = 10;
            this.attackedAtYaw = 0.0F;
            this.playSound(this.getHurtSound(), this.getSoundVolume(), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
            this.attackEntityFrom(DamageSource.generic, 0.0F);
        } else if (p_70103_1_ == 3) {
            this.playSound(this.getDeathSound(), this.getSoundVolume(), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
            this.setHealth(0.0F);
            this.onDeath(DamageSource.generic);
        } else {
            super.handleHealthUpdate(p_70103_1_);
        }
    }

    protected void kill() {
        this.attackEntityFrom(DamageSource.outOfWorld, 4.0F);
    }

    protected void updateArmSwingProgress() {
        int var1 = this.getArmSwingAnimationEnd();
        if (this.isSwingInProgress) {
            ++this.swingProgressInt;
            if (this.swingProgressInt >= var1) {
                this.swingProgressInt = 0;
                this.isSwingInProgress = false;
            }
        } else {
            this.swingProgressInt = 0;
        }

        this.swingProgress = (float) this.swingProgressInt / (float) var1;
    }

    public IAttributeInstance getEntityAttribute(IAttribute p_110148_1_) {
        return this.getAttributeMap().getAttributeInstance(p_110148_1_);
    }

    public BaseAttributeMap getAttributeMap() {
        if (this.attributeMap == null) {
            this.attributeMap = new ServersideAttributeMap();
        }

        return this.attributeMap;
    }

    public EnumCreatureAttribute getCreatureAttribute() {
        return EnumCreatureAttribute.UNDEFINED;
    }

    public abstract ItemStack getHeldItem();

    public abstract ItemStack getEquipmentInSlot(int p_71124_1_);

    public abstract void setCurrentItemOrArmor(int p_70062_1_, ItemStack p_70062_2_);

    public void setSprinting(boolean p_70031_1_) {
        super.setSprinting(p_70031_1_);
        IAttributeInstance var2 = this.getEntityAttribute(SharedMonsterAttributes.movementSpeed);
        if (var2.getModifier(sprintingSpeedBoostModifierUUID) != null) {
            var2.removeModifier(sprintingSpeedBoostModifier);
        }

        if (p_70031_1_) {
            var2.applyModifier(sprintingSpeedBoostModifier);
        }
    }

    public abstract ItemStack[] getLastActiveItems();

    protected float getSoundVolume() {
        return 1.0F;
    }

    protected float getSoundPitch() {
        return this.isChild() ? (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.5F : (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F;
    }

    protected boolean isMovementBlocked() {
        return this.getHealth() <= 0.0F;
    }

    public void setPositionAndUpdate(double p_70634_1_, double p_70634_3_, double p_70634_5_) {
        this.setLocationAndAngles(p_70634_1_, p_70634_3_, p_70634_5_, this.rotationYaw, this.rotationPitch);
    }

    public void dismountEntity(Entity p_110145_1_) {
        double var3 = p_110145_1_.posX;
        double var5 = p_110145_1_.boundingBox.minY + (double) p_110145_1_.height;
        double var7 = p_110145_1_.posZ;
        byte var9 = 1;

        for (int var10 = -var9; var10 <= var9; ++var10) {
            for (int var11 = -var9; var11 < var9; ++var11) {
                if (var10 != 0 || var11 != 0) {
                    int var12 = (int) (this.posX + (double) var10);
                    int var13 = (int) (this.posZ + (double) var11);
                    AxisAlignedBB var2 = this.boundingBox.getOffsetBoundingBox((double) var10, 1.0D, (double) var11);
                    if (this.worldObj.func_147461_a(var2).isEmpty()) {
                        if (World.doesBlockHaveSolidTopSurface(this.worldObj, var12, (int) this.posY, var13)) {
                            this.setPositionAndUpdate(this.posX + (double) var10, this.posY + 1.0D, this.posZ + (double) var11);
                            return;
                        }

                        if (World.doesBlockHaveSolidTopSurface(this.worldObj, var12, (int) this.posY - 1, var13) || this.worldObj.getBlock(var12, (int) this.posY - 1, var13).getMaterial() == Material.water) {
                            var3 = this.posX + (double) var10;
                            var5 = this.posY + 1.0D;
                            var7 = this.posZ + (double) var11;
                        }
                    }
                }
            }
        }

        this.setPositionAndUpdate(var3, var5, var7);
    }

    public boolean getAlwaysRenderNameTagForRender() {
        return false;
    }

    public IIcon getItemIcon(ItemStack p_70620_1_, int p_70620_2_) {
        return p_70620_1_.getItem().requiresMultipleRenderPasses() ? p_70620_1_.getItem().getIconFromDamageForRenderPass(p_70620_1_.getItemDamage(), p_70620_2_) : p_70620_1_.getIconIndex();
    }

    protected void jump() {
        this.motionY = 0.41999998688697815D;
        if (this.isPotionActive(Potion.jump)) {
            this.motionY += (double) ((float) (this.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1F);
        }

        if (this.isSprinting()) {
            float var1 = this.rotationYaw * 0.017453292F;
            this.motionX -= (double) (MathHelper.sin(var1) * 0.2F);
            this.motionZ += (double) (MathHelper.cos(var1) * 0.2F);
        }

        this.isAirBorne = true;
    }

    public void moveEntityWithHeading(float p_70612_1_, float p_70612_2_) {
        double var8;
        if (this.isInWater() && (!(this instanceof EntityPlayer) || !((EntityPlayer) this).capabilities.isFlying)) {
            var8 = this.posY;
            this.moveFlying(p_70612_1_, p_70612_2_, this.isAIEnabled() ? 0.04F : 0.02F);
            this.moveEntity(this.motionX, this.motionY, this.motionZ);
            this.motionX *= 0.800000011920929D;
            this.motionY *= 0.800000011920929D;
            this.motionZ *= 0.800000011920929D;
            this.motionY -= 0.02D;
            if (this.isCollidedHorizontally && this.isOffsetPositionInLiquid(this.motionX, this.motionY + 0.6000000238418579D - this.posY + var8, this.motionZ)) {
                this.motionY = 0.30000001192092896D;
            }
        } else if (this.handleLavaMovement() && (!(this instanceof EntityPlayer) || !((EntityPlayer) this).capabilities.isFlying)) {
            var8 = this.posY;
            this.moveFlying(p_70612_1_, p_70612_2_, 0.02F);
            this.moveEntity(this.motionX, this.motionY, this.motionZ);
            this.motionX *= 0.5D;
            this.motionY *= 0.5D;
            this.motionZ *= 0.5D;
            this.motionY -= 0.02D;
            if (this.isCollidedHorizontally && this.isOffsetPositionInLiquid(this.motionX, this.motionY + 0.6000000238418579D - this.posY + var8, this.motionZ)) {
                this.motionY = 0.30000001192092896D;
            }
        } else {
            float var3 = 0.91F;
            if (this.onGround) {
                var3 = this.worldObj.getBlock(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.boundingBox.minY) - 1, MathHelper.floor_double(this.posZ)).slipperiness * 0.91F;
            }

            float var4 = 0.16277136F / (var3 * var3 * var3);
            float var5;
            if (this.onGround) {
                var5 = this.getAIMoveSpeed() * var4;
            } else {
                var5 = this.jumpMovementFactor;
            }

            this.moveFlying(p_70612_1_, p_70612_2_, var5);
            var3 = 0.91F;
            if (this.onGround) {
                var3 = this.worldObj.getBlock(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.boundingBox.minY) - 1, MathHelper.floor_double(this.posZ)).slipperiness * 0.91F;
            }

            if (this.isOnLadder()) {
                float var6 = 0.15F;
                if (this.motionX < (double) (-var6)) {
                    this.motionX = (double) (-var6);
                }

                if (this.motionX > (double) var6) {
                    this.motionX = (double) var6;
                }

                if (this.motionZ < (double) (-var6)) {
                    this.motionZ = (double) (-var6);
                }

                if (this.motionZ > (double) var6) {
                    this.motionZ = (double) var6;
                }

                this.fallDistance = 0.0F;
                if (this.motionY < -0.15D) {
                    this.motionY = -0.15D;
                }

                boolean var7 = this.isSneaking() && this instanceof EntityPlayer;
                if (var7 && this.motionY < 0.0D) {
                    this.motionY = 0.0D;
                }
            }

            this.moveEntity(this.motionX, this.motionY, this.motionZ);
            if (this.isCollidedHorizontally && this.isOnLadder()) {
                this.motionY = 0.2D;
            }

            if (this.worldObj.isClient && (!this.worldObj.blockExists((int) this.posX, 0, (int) this.posZ) || !this.worldObj.getChunkFromBlockCoords((int) this.posX, (int) this.posZ).isChunkLoaded)) {
                if (this.posY > 0.0D) {
                    this.motionY = -0.1D;
                } else {
                    this.motionY = 0.0D;
                }
            } else {
                this.motionY -= 0.08D;
            }

            this.motionY *= 0.9800000190734863D;
            this.motionX *= (double) var3;
            this.motionZ *= (double) var3;
        }

        this.prevLimbSwingAmount = this.limbSwingAmount;
        var8 = this.posX - this.prevPosX;
        double var9 = this.posZ - this.prevPosZ;
        float var10 = MathHelper.sqrt_double(var8 * var8 + var9 * var9) * 4.0F;
        if (var10 > 1.0F) {
            var10 = 1.0F;
        }

        this.limbSwingAmount += (var10 - this.limbSwingAmount) * 0.4F;
        this.limbSwing += this.limbSwingAmount;
    }

    protected boolean isAIEnabled() {
        return false;
    }

    public float getAIMoveSpeed() {
        return this.isAIEnabled() ? this.landMovementFactor : 0.1F;
    }

    public void setAIMoveSpeed(float p_70659_1_) {
        this.landMovementFactor = p_70659_1_;
    }

    public boolean attackEntityAsMob(Entity p_70652_1_) {
        this.setLastAttacker(p_70652_1_);
        return false;
    }

    public boolean isPlayerSleeping() {
        return false;
    }

    public void onUpdate() {
        super.onUpdate();
        if (!this.worldObj.isClient) {
            int var1 = this.getArrowCountInEntity();
            if (var1 > 0) {
                if (this.arrowHitTimer <= 0) {
                    this.arrowHitTimer = 20 * (30 - var1);
                }

                --this.arrowHitTimer;
                if (this.arrowHitTimer <= 0) {
                    this.setArrowCountInEntity(var1 - 1);
                }
            }

            for (int var2 = 0; var2 < 5; ++var2) {
                ItemStack var3 = this.previousEquipment[var2];
                ItemStack var4 = this.getEquipmentInSlot(var2);
                if (!ItemStack.areItemStacksEqual(var4, var3)) {
                    ((WorldServer) this.worldObj).getEntityTracker().func_151247_a(this, new S04PacketEntityEquipment(this.getEntityId(), var2, var4));
                    if (var3 != null) {
                        this.attributeMap.removeAttributeModifiers(var3.getAttributeModifiers());
                    }

                    if (var4 != null) {
                        this.attributeMap.applyAttributeModifiers(var4.getAttributeModifiers());
                    }

                    this.previousEquipment[var2] = var4 == null ? null : var4.copy();
                }
            }

            if (this.ticksExisted % 20 == 0) {
                this.func_110142_aN().func_94549_h();
            }
        }

        this.onLivingUpdate();
        double var9 = this.posX - this.prevPosX;
        double var10 = this.posZ - this.prevPosZ;
        float var5 = (float) (var9 * var9 + var10 * var10);
        float var6 = this.renderYawOffset;
        float var7 = 0.0F;
        this.field_70768_au = this.field_110154_aX;
        float var8 = 0.0F;
        if (var5 > 0.0025000002F) {
            var8 = 1.0F;
            var7 = (float) Math.sqrt((double) var5) * 3.0F;
            var6 = (float) Math.atan2(var10, var9) * 180.0F / (float) Math.PI - 90.0F;
        }

        if (this.swingProgress > 0.0F) {
            var6 = this.rotationYaw;
        }

        if (!this.onGround) {
            var8 = 0.0F;
        }

        this.field_110154_aX += (var8 - this.field_110154_aX) * 0.3F;
        this.worldObj.theProfiler.startSection("headTurn");
        var7 = this.func_110146_f(var6, var7);
        this.worldObj.theProfiler.endSection();
        this.worldObj.theProfiler.startSection("rangeChecks");

        while (this.rotationYaw - this.prevRotationYaw < -180.0F) {
            this.prevRotationYaw -= 360.0F;
        }

        while (this.rotationYaw - this.prevRotationYaw >= 180.0F) {
            this.prevRotationYaw += 360.0F;
        }

        while (this.renderYawOffset - this.prevRenderYawOffset < -180.0F) {
            this.prevRenderYawOffset -= 360.0F;
        }

        while (this.renderYawOffset - this.prevRenderYawOffset >= 180.0F) {
            this.prevRenderYawOffset += 360.0F;
        }

        while (this.rotationPitch - this.prevRotationPitch < -180.0F) {
            this.prevRotationPitch -= 360.0F;
        }

        while (this.rotationPitch - this.prevRotationPitch >= 180.0F) {
            this.prevRotationPitch += 360.0F;
        }

        while (this.rotationYawHead - this.prevRotationYawHead < -180.0F) {
            this.prevRotationYawHead -= 360.0F;
        }

        while (this.rotationYawHead - this.prevRotationYawHead >= 180.0F) {
            this.prevRotationYawHead += 360.0F;
        }

        this.worldObj.theProfiler.endSection();
        this.field_70764_aw += var7;
    }

    protected float func_110146_f(float p_110146_1_, float p_110146_2_) {
        float var3 = MathHelper.wrapAngleTo180_float(p_110146_1_ - this.renderYawOffset);
        this.renderYawOffset += var3 * 0.3F;
        float var4 = MathHelper.wrapAngleTo180_float(this.rotationYaw - this.renderYawOffset);
        boolean var5 = var4 < -90.0F || var4 >= 90.0F;
        if (var4 < -75.0F) {
            var4 = -75.0F;
        }

        if (var4 >= 75.0F) {
            var4 = 75.0F;
        }

        this.renderYawOffset = this.rotationYaw - var4;
        if (var4 * var4 > 2500.0F) {
            this.renderYawOffset += var4 * 0.2F;
        }

        if (var5) {
            p_110146_2_ *= -1.0F;
        }

        return p_110146_2_;
    }

    public void onLivingUpdate() {
        if (this.jumpTicks > 0) {
            --this.jumpTicks;
        }

        if (this.newPosRotationIncrements > 0) {
            double var1 = this.posX + (this.newPosX - this.posX) / (double) this.newPosRotationIncrements;
            double var3 = this.posY + (this.newPosY - this.posY) / (double) this.newPosRotationIncrements;
            double var5 = this.posZ + (this.newPosZ - this.posZ) / (double) this.newPosRotationIncrements;
            double var7 = MathHelper.wrapAngleTo180_double(this.newRotationYaw - (double) this.rotationYaw);
            this.rotationYaw = (float) ((double) this.rotationYaw + var7 / (double) this.newPosRotationIncrements);
            this.rotationPitch = (float) ((double) this.rotationPitch + (this.newRotationPitch - (double) this.rotationPitch) / (double) this.newPosRotationIncrements);
            --this.newPosRotationIncrements;
            this.setPosition(var1, var3, var5);
            this.setRotation(this.rotationYaw, this.rotationPitch);
        } else if (!this.isClientWorld()) {
            this.motionX *= 0.98D;
            this.motionY *= 0.98D;
            this.motionZ *= 0.98D;
        }

        if (Math.abs(this.motionX) < 0.005D) {
            this.motionX = 0.0D;
        }

        if (Math.abs(this.motionY) < 0.005D) {
            this.motionY = 0.0D;
        }

        if (Math.abs(this.motionZ) < 0.005D) {
            this.motionZ = 0.0D;
        }

        this.worldObj.theProfiler.startSection("ai");
        if (this.isMovementBlocked()) {
            this.isJumping = false;
            this.moveStrafing = 0.0F;
            this.moveForward = 0.0F;
            this.randomYawVelocity = 0.0F;
        } else if (this.isClientWorld()) {
            if (this.isAIEnabled()) {
                this.worldObj.theProfiler.startSection("newAi");
                this.updateAITasks();
                this.worldObj.theProfiler.endSection();
            } else {
                this.worldObj.theProfiler.startSection("oldAi");
                this.updateEntityActionState();
                this.worldObj.theProfiler.endSection();
                this.rotationYawHead = this.rotationYaw;
            }
        }

        this.worldObj.theProfiler.endSection();
        this.worldObj.theProfiler.startSection("jump");
        if (this.isJumping) {
            if (!this.isInWater() && !this.handleLavaMovement()) {
                if (this.onGround && this.jumpTicks == 0) {
                    this.jump();
                    this.jumpTicks = 10;
                }
            } else {
                this.motionY += 0.03999999910593033D;
            }
        } else {
            this.jumpTicks = 0;
        }

        this.worldObj.theProfiler.endSection();
        this.worldObj.theProfiler.startSection("travel");
        this.moveStrafing *= 0.98F;
        this.moveForward *= 0.98F;
        this.randomYawVelocity *= 0.9F;
        this.moveEntityWithHeading(this.moveStrafing, this.moveForward);
        this.worldObj.theProfiler.endSection();
        this.worldObj.theProfiler.startSection("push");
        if (!this.worldObj.isClient) {
            this.collideWithNearbyEntities();
        }

        this.worldObj.theProfiler.endSection();
    }

    protected void updateAITasks() {
    }

    protected void collideWithNearbyEntities() {
        List var1 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand(0.20000000298023224D, 0.0D, 0.20000000298023224D));
        if (var1 != null && !var1.isEmpty()) {
            for (int var2 = 0; var2 < var1.size(); ++var2) {
                Entity var3 = (Entity) var1.get(var2);
                if (var3.canBePushed()) {
                    this.collideWithEntity(var3);
                }
            }
        }
    }

    protected void collideWithEntity(Entity p_82167_1_) {
        p_82167_1_.applyEntityCollision(this);
    }

    public void updateRidden() {
        super.updateRidden();
        this.field_70768_au = this.field_110154_aX;
        this.field_110154_aX = 0.0F;
        this.fallDistance = 0.0F;
    }

    public void setPositionAndRotation2(double p_70056_1_, double p_70056_3_, double p_70056_5_, float p_70056_7_, float p_70056_8_, int p_70056_9_) {
        this.yOffset = 0.0F;
        this.newPosX = p_70056_1_;
        this.newPosY = p_70056_3_;
        this.newPosZ = p_70056_5_;
        this.newRotationYaw = (double) p_70056_7_;
        this.newRotationPitch = (double) p_70056_8_;
        this.newPosRotationIncrements = p_70056_9_;
    }

    protected void updateAITick() {
    }

    protected void updateEntityActionState() {
        ++this.entityAge;
    }

    public void setJumping(boolean p_70637_1_) {
        this.isJumping = p_70637_1_;
    }

    public void onItemPickup(Entity p_71001_1_, int p_71001_2_) {
        if (!p_71001_1_.isDead && !this.worldObj.isClient) {
            EntityTracker var3 = ((WorldServer) this.worldObj).getEntityTracker();
            if (p_71001_1_ instanceof EntityItem) {
                var3.func_151247_a(p_71001_1_, new S0DPacketCollectItem(p_71001_1_.getEntityId(), this.getEntityId()));
            }

            if (p_71001_1_ instanceof EntityArrow) {
                var3.func_151247_a(p_71001_1_, new S0DPacketCollectItem(p_71001_1_.getEntityId(), this.getEntityId()));
            }

            if (p_71001_1_ instanceof EntityXPOrb) {
                var3.func_151247_a(p_71001_1_, new S0DPacketCollectItem(p_71001_1_.getEntityId(), this.getEntityId()));
            }
        }
    }

    public boolean canEntityBeSeen(Entity p_70685_1_) {
        return this.worldObj.rayTraceBlocks(Vec3.createVectorHelper(this.posX, this.posY + (double) this.getEyeHeight(), this.posZ), Vec3.createVectorHelper(p_70685_1_.posX, p_70685_1_.posY + (double) p_70685_1_.getEyeHeight(), p_70685_1_.posZ)) == null;
    }

    public Vec3 getLookVec() {
        return this.getLook(1.0F);
    }

    public Vec3 getLook(float p_70676_1_) {
        float var2;
        float var3;
        float var4;
        float var5;
        if (p_70676_1_ == 1.0F) {
            var2 = MathHelper.cos(-this.rotationYaw * 0.017453292F - (float) Math.PI);
            var3 = MathHelper.sin(-this.rotationYaw * 0.017453292F - (float) Math.PI);
            var4 = -MathHelper.cos(-this.rotationPitch * 0.017453292F);
            var5 = MathHelper.sin(-this.rotationPitch * 0.017453292F);
            return Vec3.createVectorHelper((double) (var3 * var4), (double) var5, (double) (var2 * var4));
        } else {
            var2 = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * p_70676_1_;
            var3 = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * p_70676_1_;
            var4 = MathHelper.cos(-var3 * 0.017453292F - (float) Math.PI);
            var5 = MathHelper.sin(-var3 * 0.017453292F - (float) Math.PI);
            float var6 = -MathHelper.cos(-var2 * 0.017453292F);
            float var7 = MathHelper.sin(-var2 * 0.017453292F);
            return Vec3.createVectorHelper((double) (var5 * var6), (double) var7, (double) (var4 * var6));
        }
    }

    public float getSwingProgress(float p_70678_1_) {
        float var2 = this.swingProgress - this.prevSwingProgress;
        if (var2 < 0.0F) {
            ++var2;
        }

        return this.prevSwingProgress + var2 * p_70678_1_;
    }

    public Vec3 getPosition(float p_70666_1_) {
        if (p_70666_1_ == 1.0F) {
            return Vec3.createVectorHelper(this.posX, this.posY, this.posZ);
        } else {
            double var2 = this.prevPosX + (this.posX - this.prevPosX) * (double) p_70666_1_;
            double var4 = this.prevPosY + (this.posY - this.prevPosY) * (double) p_70666_1_;
            double var6 = this.prevPosZ + (this.posZ - this.prevPosZ) * (double) p_70666_1_;
            return Vec3.createVectorHelper(var2, var4, var6);
        }
    }

    public MovingObjectPosition rayTrace(double p_70614_1_, float p_70614_3_) {
        Vec3 var4 = this.getPosition(p_70614_3_);
        Vec3 var5 = this.getLook(p_70614_3_);
        Vec3 var6 = var4.addVector(var5.xCoord * p_70614_1_, var5.yCoord * p_70614_1_, var5.zCoord * p_70614_1_);
        return this.worldObj.func_147447_a(var4, var6, false, false, true);
    }

    public boolean isClientWorld() {
        return !this.worldObj.isClient;
    }

    public boolean canBeCollidedWith() {
        return !this.isDead;
    }

    public boolean canBePushed() {
        return !this.isDead;
    }

    public float getEyeHeight() {
        return this.height * 0.85F;
    }

    protected void setBeenAttacked() {
        this.velocityChanged = this.rand.nextDouble() >= this.getEntityAttribute(SharedMonsterAttributes.knockbackResistance).getAttributeValue();
    }

    public float getRotationYawHead() {
        return this.rotationYawHead;
    }

    public void setRotationYawHead(float p_70034_1_) {
        this.rotationYawHead = p_70034_1_;
    }

    public float getAbsorptionAmount() {
        return this.field_110151_bq;
    }

    public void setAbsorptionAmount(float p_110149_1_) {
        if (p_110149_1_ < 0.0F) {
            p_110149_1_ = 0.0F;
        }

        this.field_110151_bq = p_110149_1_;
    }

    public Team getTeam() {
        return null;
    }

    public boolean isOnSameTeam(EntityLivingBase p_142014_1_) {
        return this.isOnTeam(p_142014_1_.getTeam());
    }

    public boolean isOnTeam(Team p_142012_1_) {
        return this.getTeam() != null ? this.getTeam().isSameTeam(p_142012_1_) : false;
    }

    public void func_152111_bt() {
    }

    public void func_152112_bu() {
    }
}
