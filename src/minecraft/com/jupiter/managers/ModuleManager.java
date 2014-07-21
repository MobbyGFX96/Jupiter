package com.jupiter.managers;

import com.jupiter.Jupiter;
import com.jupiter.mods.Module;
import com.jupiter.mods.impl.Dolphin;
import com.jupiter.mods.impl.JupiterGui;
import com.jupiter.mods.impl.NoFall;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by corey on 15/07/14.
 */
public class ModuleManager extends GuiScreen {

    public List<Module> modules = new ArrayList<>();
    private List<Module> activeModules = new ArrayList<>();

    public void init() {
        modules.add(new NoFall());
        modules.add(new Dolphin());
        modules.add(new JupiterGui());
    }

    public void onKeyPressed() {
        int key = Keyboard.getEventKey();
        for (Module module : getModules())
            module.onKeyPressed(key);
        for (int i = 0; i < modules.size(); i++) {
            Module module = modules.get(i);
            if (module.getState())
                activeModules.add(module);
            else if (!module.getState())
                activeModules.remove(module);
        }
    }

    public List<Module> getModules() {
        return modules;
    }

    public List<Module> getActiveModules() {
        return activeModules;
    }
}
