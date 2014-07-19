package com.jupiter.mods;

/**
 * Created by corey on 15/07/14.
 */
public enum ModuleType {

    NONE, PLAYER, WORLD, RENDER, MOVEMENT;

    @Override
    public String toString() {
        String name = name().toLowerCase();
        String formattedName = Character.toUpperCase(name.charAt(0))
                + name.substring(1);
        return formattedName;
    }


}
