package com.jupiter.mods;

import com.jupiter.Jupiter;
import com.jupiter.Wrapper;
import net.minecraft.client.entity.EntityPlayerSP;
import org.lwjgl.input.Keyboard;

/**
 * Created by corey on 15/07/14.
 */
public abstract class Module extends Wrapper {

    private String moduleName;
    private String description;
    private int keybind;
    private int colour;
    private ModuleType type;
    private boolean isEnabled;
    private boolean isVisible;
    private boolean toggleable;

    public Module(String moduleName, String description, int keybind, ModuleType type) {
        setModuleName(moduleName);
        setDescription(description);
        setKeybind(keybind);
        setColour(getColour());
        setModuleType(type);
        setVisible(true);
        setToggleable(true);
    }

    protected Module(String moduleName, int keybind, ModuleType type, boolean toggleable) {
        this.moduleName = moduleName;
        this.keybind = keybind;
        this.type = type;
        setToggleable(toggleable);
    }

    public Module(String moduleName, ModuleType type) {
        setModuleName(moduleName);
        setColour(getColour());
        setModuleType(type);
        setKeybind(Keyboard.KEY_NONE);
        setToggleable(true);
        setVisible(true);
    }

    public abstract int getColour();

    public void setColour(int colour) {
        this.colour = colour;
    }

    public void onEnable() {
    }

    public void onDisable() {
    }

    public void onToggled() {
    }

    public void beforeUpdate(EntityPlayerSP player) {
    }

    public void onUpdate(EntityPlayerSP player) {
    }

    public void onPlayerUpdate(EntityPlayerSP player) {
    }

    public void afterUpdate(EntityPlayerSP player) {

    }

    public void runTick() {
    }

    public void onRender() {
    }

    public void onBlockClicked(int x, int y, int z) {
    }

    public void onKeyPressed(int key) {
        if (key == keybind) {
            toggle();
        }
    }

    public final void toggle() {
        setState(!isEnabled);
        onToggled();
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getKeybind() {
        return keybind;
    }

    public void setKeybind(int keybind) {
        this.keybind = keybind;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean isVisible) {
        this.isVisible = isVisible;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public ModuleType getModuleType() {
        return type;
    }

    public void setModuleType(ModuleType type) {
        this.type = type;
    }

    public final void setState(boolean flag) {
        isEnabled = flag;
        if (isEnabled()) {
            onEnable();
            if (isVisible) {
                Jupiter.getInstance().getModuleManager().getActiveModules().add(this);
            }
        } else {
            onDisable();
            Jupiter.getInstance().getModuleManager().getActiveModules().remove(this);
        }
    }

    public boolean isToggleable() {
        return toggleable;
    }

    public void setToggleable(boolean toggleable) {
        this.toggleable = toggleable;
    }

}
