package org.darkstorm.minecraft.gui.theme.Jupiter;

import com.jupiter.utils.GuiUtils;
import org.darkstorm.minecraft.gui.component.Button;
import org.darkstorm.minecraft.gui.component.Component;
import org.darkstorm.minecraft.gui.theme.AbstractComponentUI;
import org.darkstorm.minecraft.gui.util.RenderUtil;

import java.awt.*;

import static org.lwjgl.opengl.GL11.*;

public class JupiterButtonUI extends AbstractComponentUI<Button> {
    private final JupiterTheme theme;

    JupiterButtonUI(JupiterTheme theme) {
        super(Button.class);
        this.theme = theme;

        foreground = Color.WHITE;
        background = new Color(128, 128, 128, 128 + 128 / 2);
    }

    @Override
    protected void renderComponent(Button button) {
        translateComponent(button, false);
        Rectangle area = button.getArea();
        glEnable(GL_BLEND);
        glDisable(GL_CULL_FACE);

        glDisable(GL_TEXTURE_2D);
        //RenderUtil.setColor(button.getBackgroundColor());
        //GuiUtils.getInstance().drawGradientRect(0, 0, area.width, area.height, 0x59181818, 0x59181818);
        GuiUtils.getInstance().drawGradientRect(0, 0, area.width, area.height, 0x59181818, 0x59181818);
        Point mouse = RenderUtil.calculateMouseLocation();
        Component parent = button.getParent();
        while (parent != null) {
            mouse.x -= parent.getX();
            mouse.y -= parent.getY();
            parent = parent.getParent();
        }
        if (button.isPressed()) {
            GuiUtils.getInstance().drawGradientRect(0, 0, area.width, area.height, 0xFFEF8927, 0xFFE4531C);
        }
        if (area.contains(mouse)) {
            GuiUtils.getInstance().drawGradientRect(0, 0, area.width, area.height, 0x80EF8927, 0x80E4531C);
        }
        glEnable(GL_TEXTURE_2D);

        String text = button.getText();
        theme.getFontRenderer().drawString(
                text,
                area.width / 2 - theme.getFontRenderer().getStringWidth(text)
                        / 2,
                area.height / 2 - theme.getFontRenderer().FONT_HEIGHT / 2,
                RenderUtil.toRGBA(button.getForegroundColor()));

        glEnable(GL_CULL_FACE);
        glDisable(GL_BLEND);
        translateComponent(button, true);
    }

    @Override
    protected Dimension getDefaultComponentSize(Button component) {
        return new Dimension(theme.getFontRenderer().getStringWidth(
                component.getText()) + 4,
                theme.getFontRenderer().FONT_HEIGHT + 4);
    }

    @Override
    protected Rectangle[] getInteractableComponentRegions(Button component) {
        return new Rectangle[]{new Rectangle(0, 0, component.getWidth(),
                component.getHeight())};
    }

    @Override
    protected void handleComponentInteraction(Button component, Point location,
                                              int button) {
        if (location.x <= component.getWidth()
                && location.y <= component.getHeight() && button == 0)
            component.press();
    }
}