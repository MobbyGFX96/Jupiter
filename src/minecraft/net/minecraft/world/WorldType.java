package net.minecraft.world;

public class WorldType {
    public static final WorldType[] worldTypes = new WorldType[16];
    public static final WorldType DEFAULT = (new WorldType(0, "default", 1)).setVersioned();
    public static final WorldType FLAT = new WorldType(1, "flat");
    public static final WorldType LARGE_BIOMES = new WorldType(2, "largeBiomes");
    public static final WorldType field_151360_e = (new WorldType(3, "amplified")).func_151358_j();
    public static final WorldType DEFAULT_1_1 = (new WorldType(8, "default_1_1", 0)).setCanBeCreated(false);
    private static final String __OBFID = "CL_00000150";
    private final int worldTypeId;
    private final String worldType;
    private final int generatorVersion;
    private boolean canBeCreated;
    private boolean isWorldTypeVersioned;
    private boolean field_151361_l;

    private WorldType(int p_i1959_1_, String p_i1959_2_) {
        this(p_i1959_1_, p_i1959_2_, 0);
    }

    private WorldType(int p_i1960_1_, String p_i1960_2_, int p_i1960_3_) {
        this.worldType = p_i1960_2_;
        this.generatorVersion = p_i1960_3_;
        this.canBeCreated = true;
        this.worldTypeId = p_i1960_1_;
        worldTypes[p_i1960_1_] = this;
    }

    public static WorldType parseWorldType(String p_77130_0_) {
        for (int var1 = 0; var1 < worldTypes.length; ++var1) {
            if (worldTypes[var1] != null && worldTypes[var1].worldType.equalsIgnoreCase(p_77130_0_)) {
                return worldTypes[var1];
            }
        }

        return null;
    }

    public String getWorldTypeName() {
        return this.worldType;
    }

    public String getTranslateName() {
        return "generator." + this.worldType;
    }

    public String func_151359_c() {
        return this.getTranslateName() + ".info";
    }

    public int getGeneratorVersion() {
        return this.generatorVersion;
    }

    public WorldType getWorldTypeForGeneratorVersion(int p_77132_1_) {
        return this == DEFAULT && p_77132_1_ == 0 ? DEFAULT_1_1 : this;
    }

    public boolean getCanBeCreated() {
        return this.canBeCreated;
    }

    private WorldType setCanBeCreated(boolean p_77124_1_) {
        this.canBeCreated = p_77124_1_;
        return this;
    }

    private WorldType setVersioned() {
        this.isWorldTypeVersioned = true;
        return this;
    }

    public boolean isVersioned() {
        return this.isWorldTypeVersioned;
    }

    public int getWorldTypeID() {
        return this.worldTypeId;
    }

    public boolean func_151357_h() {
        return this.field_151361_l;
    }

    private WorldType func_151358_j() {
        this.field_151361_l = true;
        return this;
    }
}
