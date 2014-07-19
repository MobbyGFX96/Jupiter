package net.minecraft.entity.ai.attributes;

import org.apache.commons.lang3.Validate;

import java.util.UUID;

public class AttributeModifier {
    private static final String __OBFID = "CL_00001564";
    private final double amount;
    private final int operation;
    private final String name;
    private final UUID id;
    private boolean isSaved;

    public AttributeModifier(String p_i1605_1_, double p_i1605_2_, int p_i1605_4_) {
        this(UUID.randomUUID(), p_i1605_1_, p_i1605_2_, p_i1605_4_);
    }

    public AttributeModifier(UUID p_i1606_1_, String p_i1606_2_, double p_i1606_3_, int p_i1606_5_) {
        this.isSaved = true;
        this.id = p_i1606_1_;
        this.name = p_i1606_2_;
        this.amount = p_i1606_3_;
        this.operation = p_i1606_5_;
        Validate.notEmpty(p_i1606_2_, "Modifier name cannot be empty", new Object[0]);
        Validate.inclusiveBetween(Integer.valueOf(0), Integer.valueOf(2), Integer.valueOf(p_i1606_5_), "Invalid operation", new Object[0]);
    }

    public UUID getID() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public int getOperation() {
        return this.operation;
    }

    public double getAmount() {
        return this.amount;
    }

    public boolean isSaved() {
        return this.isSaved;
    }

    public AttributeModifier setSaved(boolean p_111168_1_) {
        this.isSaved = p_111168_1_;
        return this;
    }

    public boolean equals(Object p_equals_1_) {
        if (this == p_equals_1_) {
            return true;
        } else if (p_equals_1_ != null && this.getClass() == p_equals_1_.getClass()) {
            AttributeModifier var2 = (AttributeModifier) p_equals_1_;
            if (this.id != null) {
                if (!this.id.equals(var2.id)) {
                    return false;
                }
            } else if (var2.id != null) {
                return false;
            }

            return true;
        } else {
            return false;
        }
    }

    public int hashCode() {
        return this.id != null ? this.id.hashCode() : 0;
    }

    public String toString() {
        return "AttributeModifier{amount=" + this.amount + ", operation=" + this.operation + ", name=\'" + this.name + '\'' + ", id=" + this.id + ", serialize=" + this.isSaved + '}';
    }
}
