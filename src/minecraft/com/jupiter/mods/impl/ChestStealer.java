package com.jupiter.mods.impl;

import com.jupiter.mods.Module;
import com.jupiter.mods.ModuleType;
import org.lwjgl.input.Keyboard;

import java.util.concurrent.Callable;

/**
 * Created by Corey on the 20th of July 2014
 */
public class ChestStealer extends Module {

    public ChestStealer() {
        super("Chest Stealer", "Steals items from chests", Keyboard.KEY_C, ModuleType.PLAYER);
    }

    @Override
    public void onEnable() {
        //TODO what?
    }

    @Override
    public void onDisable() {
        //TODO: what? :3
    }

    @Override
    public int getColour() {
        return 0xff00ff;
    }
}
