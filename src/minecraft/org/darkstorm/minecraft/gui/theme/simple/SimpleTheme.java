package org.darkstorm.minecraft.gui.theme.simple;

import com.jupiter.managers.font.TTFRenderer;
import net.minecraft.client.gui.FontRenderer;
import org.darkstorm.minecraft.gui.theme.AbstractTheme;

public class SimpleTheme extends AbstractTheme {
    private final FontRenderer fontRenderer;

    public SimpleTheme() {
        fontRenderer = new TTFRenderer("Trebuchet MS", 15);

        installUI(new SimpleFrameUI(this));
        installUI(new SimplePanelUI(this));
        installUI(new SimpleLabelUI(this));
        installUI(new SimpleButtonUI(this));
        installUI(new SimpleCheckButtonUI(this));
        installUI(new SimpleComboBoxUI(this));
        installUI(new SimpleSliderUI(this));
        installUI(new SimpleProgressBarUI(this));
    }

    public FontRenderer getFontRenderer() {
        return fontRenderer;
    }
}
