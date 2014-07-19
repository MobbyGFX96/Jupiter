package net.minecraft.util;

import com.google.gson.*;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;

import java.lang.reflect.Type;

public class ChatStyle {
    private static final ChatStyle rootStyle = new ChatStyle() {
        private static final String __OBFID = "CL_00001267";

        public EnumChatFormatting getColor() {
            return null;
        }

        public boolean getBold() {
            return false;
        }

        public boolean getItalic() {
            return false;
        }

        public boolean getStrikethrough() {
            return false;
        }

        public boolean getUnderlined() {
            return false;
        }

        public boolean getObfuscated() {
            return false;
        }

        public ClickEvent getChatClickEvent() {
            return null;
        }

        public HoverEvent getChatHoverEvent() {
            return null;
        }

        public ChatStyle setColor(EnumChatFormatting p_150238_1_) {
            throw new UnsupportedOperationException();
        }

        public ChatStyle setBold(Boolean p_150227_1_) {
            throw new UnsupportedOperationException();
        }

        public ChatStyle setItalic(Boolean p_150217_1_) {
            throw new UnsupportedOperationException();
        }

        public ChatStyle setStrikethrough(Boolean p_150225_1_) {
            throw new UnsupportedOperationException();
        }

        public ChatStyle setUnderlined(Boolean p_150228_1_) {
            throw new UnsupportedOperationException();
        }

        public ChatStyle setObfuscated(Boolean p_150237_1_) {
            throw new UnsupportedOperationException();
        }

        public ChatStyle setChatClickEvent(ClickEvent p_150241_1_) {
            throw new UnsupportedOperationException();
        }

        public ChatStyle setChatHoverEvent(HoverEvent p_150209_1_) {
            throw new UnsupportedOperationException();
        }

        public ChatStyle setParentStyle(ChatStyle p_150221_1_) {
            throw new UnsupportedOperationException();
        }

        public String toString() {
            return "Style.ROOT";
        }

        public ChatStyle createShallowCopy() {
            return this;
        }

        public ChatStyle createDeepCopy() {
            return this;
        }

        public String getFormattingCode() {
            return "";
        }
    };
    private static final String __OBFID = "CL_00001266";
    private ChatStyle parentStyle;
    private EnumChatFormatting color;
    private Boolean bold;
    private Boolean italic;
    private Boolean underlined;
    private Boolean strikethrough;
    private Boolean obfuscated;
    private ClickEvent chatClickEvent;
    private HoverEvent chatHoverEvent;

    public EnumChatFormatting getColor() {
        return this.color == null ? this.getParent().getColor() : this.color;
    }

    public ChatStyle setColor(EnumChatFormatting p_150238_1_) {
        this.color = p_150238_1_;
        return this;
    }

    public boolean getBold() {
        return this.bold == null ? this.getParent().getBold() : this.bold.booleanValue();
    }

    public ChatStyle setBold(Boolean p_150227_1_) {
        this.bold = p_150227_1_;
        return this;
    }

    public boolean getItalic() {
        return this.italic == null ? this.getParent().getItalic() : this.italic.booleanValue();
    }

    public ChatStyle setItalic(Boolean p_150217_1_) {
        this.italic = p_150217_1_;
        return this;
    }

    public boolean getStrikethrough() {
        return this.strikethrough == null ? this.getParent().getStrikethrough() : this.strikethrough.booleanValue();
    }

    public ChatStyle setStrikethrough(Boolean p_150225_1_) {
        this.strikethrough = p_150225_1_;
        return this;
    }

    public boolean getUnderlined() {
        return this.underlined == null ? this.getParent().getUnderlined() : this.underlined.booleanValue();
    }

    public ChatStyle setUnderlined(Boolean p_150228_1_) {
        this.underlined = p_150228_1_;
        return this;
    }

    public boolean getObfuscated() {
        return this.obfuscated == null ? this.getParent().getObfuscated() : this.obfuscated.booleanValue();
    }

    public ChatStyle setObfuscated(Boolean p_150237_1_) {
        this.obfuscated = p_150237_1_;
        return this;
    }

    public boolean isEmpty() {
        return this.bold == null && this.italic == null && this.strikethrough == null && this.underlined == null && this.obfuscated == null && this.color == null && this.chatClickEvent == null && this.chatHoverEvent == null;
    }

    public ClickEvent getChatClickEvent() {
        return this.chatClickEvent == null ? this.getParent().getChatClickEvent() : this.chatClickEvent;
    }

    public ChatStyle setChatClickEvent(ClickEvent p_150241_1_) {
        this.chatClickEvent = p_150241_1_;
        return this;
    }

    public HoverEvent getChatHoverEvent() {
        return this.chatHoverEvent == null ? this.getParent().getChatHoverEvent() : this.chatHoverEvent;
    }

    public ChatStyle setChatHoverEvent(HoverEvent p_150209_1_) {
        this.chatHoverEvent = p_150209_1_;
        return this;
    }

    public ChatStyle setParentStyle(ChatStyle p_150221_1_) {
        this.parentStyle = p_150221_1_;
        return this;
    }

    public String getFormattingCode() {
        if (this.isEmpty()) {
            return this.parentStyle != null ? this.parentStyle.getFormattingCode() : "";
        } else {
            StringBuilder var1 = new StringBuilder();
            if (this.getColor() != null) {
                var1.append(this.getColor());
            }

            if (this.getBold()) {
                var1.append(EnumChatFormatting.BOLD);
            }

            if (this.getItalic()) {
                var1.append(EnumChatFormatting.ITALIC);
            }

            if (this.getUnderlined()) {
                var1.append(EnumChatFormatting.UNDERLINE);
            }

            if (this.getObfuscated()) {
                var1.append(EnumChatFormatting.OBFUSCATED);
            }

            if (this.getStrikethrough()) {
                var1.append(EnumChatFormatting.STRIKETHROUGH);
            }

            return var1.toString();
        }
    }

    private ChatStyle getParent() {
        return this.parentStyle == null ? rootStyle : this.parentStyle;
    }

    public String toString() {
        return "Style{hasParent=" + (this.parentStyle != null) + ", color=" + this.color + ", bold=" + this.bold + ", italic=" + this.italic + ", underlined=" + this.underlined + ", obfuscated=" + this.obfuscated + ", clickEvent=" + this.getChatClickEvent() + ", hoverEvent=" + this.getChatHoverEvent() + '}';
    }

    public boolean equals(Object p_equals_1_) {
        if (this == p_equals_1_) {
            return true;
        } else if (!(p_equals_1_ instanceof ChatStyle)) {
            return false;
        } else {
            ChatStyle var2 = (ChatStyle) p_equals_1_;
            boolean var10000;
            if (this.getBold() == var2.getBold() && this.getColor() == var2.getColor() && this.getItalic() == var2.getItalic() && this.getObfuscated() == var2.getObfuscated() && this.getStrikethrough() == var2.getStrikethrough() && this.getUnderlined() == var2.getUnderlined()) {
                label56:
                {
                    if (this.getChatClickEvent() != null) {
                        if (!this.getChatClickEvent().equals(var2.getChatClickEvent())) {
                            break label56;
                        }
                    } else if (var2.getChatClickEvent() != null) {
                        break label56;
                    }

                    if (this.getChatHoverEvent() != null) {
                        if (!this.getChatHoverEvent().equals(var2.getChatHoverEvent())) {
                            break label56;
                        }
                    } else if (var2.getChatHoverEvent() != null) {
                        break label56;
                    }

                    var10000 = true;
                    return var10000;
                }
            }

            var10000 = false;
            return var10000;
        }
    }

    public int hashCode() {
        int var1 = this.color.hashCode();
        var1 = 31 * var1 + this.bold.hashCode();
        var1 = 31 * var1 + this.italic.hashCode();
        var1 = 31 * var1 + this.underlined.hashCode();
        var1 = 31 * var1 + this.strikethrough.hashCode();
        var1 = 31 * var1 + this.obfuscated.hashCode();
        var1 = 31 * var1 + this.chatClickEvent.hashCode();
        var1 = 31 * var1 + this.chatHoverEvent.hashCode();
        return var1;
    }

    public ChatStyle createShallowCopy() {
        ChatStyle var1 = new ChatStyle();
        var1.bold = this.bold;
        var1.italic = this.italic;
        var1.strikethrough = this.strikethrough;
        var1.underlined = this.underlined;
        var1.obfuscated = this.obfuscated;
        var1.color = this.color;
        var1.chatClickEvent = this.chatClickEvent;
        var1.chatHoverEvent = this.chatHoverEvent;
        var1.parentStyle = this.parentStyle;
        return var1;
    }

    public ChatStyle createDeepCopy() {
        ChatStyle var1 = new ChatStyle();
        var1.setBold(Boolean.valueOf(this.getBold()));
        var1.setItalic(Boolean.valueOf(this.getItalic()));
        var1.setStrikethrough(Boolean.valueOf(this.getStrikethrough()));
        var1.setUnderlined(Boolean.valueOf(this.getUnderlined()));
        var1.setObfuscated(Boolean.valueOf(this.getObfuscated()));
        var1.setColor(this.getColor());
        var1.setChatClickEvent(this.getChatClickEvent());
        var1.setChatHoverEvent(this.getChatHoverEvent());
        return var1;
    }

    public static class Serializer implements JsonDeserializer, JsonSerializer {
        private static final String __OBFID = "CL_00001268";

        public ChatStyle deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_) {
            if (p_deserialize_1_.isJsonObject()) {
                ChatStyle var4 = new ChatStyle();
                JsonObject var5 = p_deserialize_1_.getAsJsonObject();
                if (var5 == null) {
                    return null;
                } else {
                    if (var5.has("bold")) {
                        var4.bold = Boolean.valueOf(var5.get("bold").getAsBoolean());
                    }

                    if (var5.has("italic")) {
                        var4.italic = Boolean.valueOf(var5.get("italic").getAsBoolean());
                    }

                    if (var5.has("underlined")) {
                        var4.underlined = Boolean.valueOf(var5.get("underlined").getAsBoolean());
                    }

                    if (var5.has("strikethrough")) {
                        var4.strikethrough = Boolean.valueOf(var5.get("strikethrough").getAsBoolean());
                    }

                    if (var5.has("obfuscated")) {
                        var4.obfuscated = Boolean.valueOf(var5.get("obfuscated").getAsBoolean());
                    }

                    if (var5.has("color")) {
                        var4.color = (EnumChatFormatting) p_deserialize_3_.deserialize(var5.get("color"), EnumChatFormatting.class);
                    }

                    JsonObject var6;
                    JsonPrimitive var7;
                    if (var5.has("clickEvent")) {
                        var6 = var5.getAsJsonObject("clickEvent");
                        if (var6 != null) {
                            var7 = var6.getAsJsonPrimitive("action");
                            ClickEvent.Action var8 = var7 == null ? null : ClickEvent.Action.getValueByCanonicalName(var7.getAsString());
                            JsonPrimitive var9 = var6.getAsJsonPrimitive("value");
                            String var10 = var9 == null ? null : var9.getAsString();
                            if (var8 != null && var10 != null && var8.shouldAllowInChat()) {
                                var4.chatClickEvent = new ClickEvent(var8, var10);
                            }
                        }
                    }

                    if (var5.has("hoverEvent")) {
                        var6 = var5.getAsJsonObject("hoverEvent");
                        if (var6 != null) {
                            var7 = var6.getAsJsonPrimitive("action");
                            HoverEvent.Action var11 = var7 == null ? null : HoverEvent.Action.getValueByCanonicalName(var7.getAsString());
                            IChatComponent var12 = (IChatComponent) p_deserialize_3_.deserialize(var6.get("value"), IChatComponent.class);
                            if (var11 != null && var12 != null && var11.shouldAllowInChat()) {
                                var4.chatHoverEvent = new HoverEvent(var11, var12);
                            }
                        }
                    }

                    return var4;
                }
            } else {
                return null;
            }
        }

        public JsonElement serialize(ChatStyle p_serialize_1_, Type p_serialize_2_, JsonSerializationContext p_serialize_3_) {
            if (p_serialize_1_.isEmpty()) {
                return null;
            } else {
                JsonObject var4 = new JsonObject();
                if (p_serialize_1_.bold != null) {
                    var4.addProperty("bold", p_serialize_1_.bold);
                }

                if (p_serialize_1_.italic != null) {
                    var4.addProperty("italic", p_serialize_1_.italic);
                }

                if (p_serialize_1_.underlined != null) {
                    var4.addProperty("underlined", p_serialize_1_.underlined);
                }

                if (p_serialize_1_.strikethrough != null) {
                    var4.addProperty("strikethrough", p_serialize_1_.strikethrough);
                }

                if (p_serialize_1_.obfuscated != null) {
                    var4.addProperty("obfuscated", p_serialize_1_.obfuscated);
                }

                if (p_serialize_1_.color != null) {
                    var4.add("color", p_serialize_3_.serialize(p_serialize_1_.color));
                }

                JsonObject var5;
                if (p_serialize_1_.chatClickEvent != null) {
                    var5 = new JsonObject();
                    var5.addProperty("action", p_serialize_1_.chatClickEvent.getAction().getCanonicalName());
                    var5.addProperty("value", p_serialize_1_.chatClickEvent.getValue());
                    var4.add("clickEvent", var5);
                }

                if (p_serialize_1_.chatHoverEvent != null) {
                    var5 = new JsonObject();
                    var5.addProperty("action", p_serialize_1_.chatHoverEvent.getAction().getCanonicalName());
                    var5.add("value", p_serialize_3_.serialize(p_serialize_1_.chatHoverEvent.getValue()));
                    var4.add("hoverEvent", var5);
                }

                return var4;
            }
        }

        public JsonElement serialize(Object p_serialize_1_, Type p_serialize_2_, JsonSerializationContext p_serialize_3_) {
            return this.serialize((ChatStyle) p_serialize_1_, p_serialize_2_, p_serialize_3_);
        }
    }
}
