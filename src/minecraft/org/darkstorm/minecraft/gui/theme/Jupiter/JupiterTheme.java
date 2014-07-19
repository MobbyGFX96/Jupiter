package org.darkstorm.minecraft.gui.theme.Jupiter;

import com.jupiter.managers.font.TTFRenderer;
import net.minecraft.client.gui.FontRenderer;
import org.darkstorm.minecraft.gui.theme.AbstractTheme;

public class JupiterTheme extends AbstractTheme {
    private final FontRenderer fontRenderer;

    public JupiterTheme() {
        fontRenderer = new TTFRenderer("Trebuchet MS Bold", 15);

        installUI(new JupiterFrameUI(this));
        installUI(new JupiterPanelUI(this));
        installUI(new JupiterLabelUI(this));
        installUI(new JupiterButtonUI(this));
        installUI(new JupiterCheckButtonUI(this));
        installUI(new JupiterComboBoxUI(this));
        installUI(new JupiterSliderUI(this));
        installUI(new JupiterProgressBarUI(this));
    }

    public FontRenderer getFontRenderer() {
        return fontRenderer;
    }
}
