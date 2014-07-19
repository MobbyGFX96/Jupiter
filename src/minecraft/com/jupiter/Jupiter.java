package com.jupiter;

import com.jupiter.managers.JupiterGuiManager;
import com.jupiter.managers.ModuleManager;
import com.jupiter.managers.font.FontManager;
import net.minecraft.util.ChatComponentText;
import org.darkstorm.minecraft.gui.theme.Jupiter.JupiterTheme;

/**
 * Created by corey on 13/07/14.
 */
public class Jupiter extends Wrapper {

    /**
     * Constants
     */
    private static Jupiter instance;
    private String CLIENT_NAME = "Jupiter";

    /**
     * Classes References
     */
    private FontManager fontManager;
    private ModuleManager moduleManager;
    private JupiterGuiManager guiManager;

    public static Jupiter getInstance() {
        if (instance == null)
            instance = new Jupiter();
        return instance;
    }

    public void init() {
        getFontManager().onStart();
        getModuleManager().init();
        getGuiManager().setTheme(new JupiterTheme());
        getGuiManager().setup();
    }

    public void sendMessage(String message) {
        if (getPlayer() != null && getWorld() != null
                && getMinecraft().ingameGUI != null && getMinecraft().ingameGUI.getChatGUI() != null) {
            message = String.format("\247f[\2479%s\247f]" + "\247r %s", getClientName(), message);
            getPlayer().addChatMessage(new ChatComponentText(message));
        }
    }

    public String getClientName() {
        return CLIENT_NAME;
    }

    public FontManager getFontManager() {
        if (fontManager == null)
            fontManager = new FontManager();
        return fontManager;
    }

    public ModuleManager getModuleManager() {
        if (moduleManager == null)
            moduleManager = new ModuleManager();
        return moduleManager;
    }

    public JupiterGuiManager getGuiManager() {
        if (guiManager == null)
            guiManager = new JupiterGuiManager();
        return guiManager;
    }
}
