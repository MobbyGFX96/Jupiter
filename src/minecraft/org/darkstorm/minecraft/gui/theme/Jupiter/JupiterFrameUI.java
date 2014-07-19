package org.darkstorm.minecraft.gui.theme.Jupiter;

import com.jupiter.utils.GuiUtils;
import org.darkstorm.minecraft.gui.component.Component;
import org.darkstorm.minecraft.gui.component.Frame;
import org.darkstorm.minecraft.gui.layout.Constraint;
import org.darkstorm.minecraft.gui.theme.AbstractComponentUI;
import org.darkstorm.minecraft.gui.util.RenderUtil;

import java.awt.*;

import static org.lwjgl.opengl.GL11.*;

public class JupiterFrameUI extends AbstractComponentUI<Frame> {
    private final JupiterTheme theme;

    JupiterFrameUI(JupiterTheme theme) {
        super(Frame.class);
        this.theme = theme;

        foreground = Color.WHITE;
        background = new Color(128, 128, 128, 128);
    }

    @Override
    protected void renderComponent(Frame component) {
        Rectangle area = new Rectangle(component.getArea());
        int fontHeight = theme.getFontRenderer().FONT_HEIGHT;
        translateComponent(component, false);
        glEnable(GL_BLEND);
        glDisable(GL_CULL_FACE);
        glDisable(GL_TEXTURE_2D);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        // Draw frame background
        if (component.isMinimized())
            area.height = fontHeight + 4;
        //RenderUtil.setColor(component.getBackgroundColor());
        GuiUtils.getInstance().drawGradientRect(0, 0, area.width, area.height, 0x59181818, 0x59181818);

        // Draw controls
        int offset = component.getWidth() - 2;
        Point mouse = RenderUtil.calculateMouseLocation();
        Component parent = component;
        while (parent != null) {
            mouse.x -= parent.getX();
            mouse.y -= parent.getY();
            parent = parent.getParent();
        }
        boolean[] checks = new boolean[]{component.isClosable(),
                component.isPinnable(), component.isMinimizable()};
        boolean[] overlays = new boolean[]{false, component.isPinned(),
                component.isMinimized()};
        for (int i = 0; i < checks.length; i++) {
            if (!checks[i])
                continue;
            //RenderUtil.setColor(component.getBackgroundColor());
            //GuiUtils.getInstance().drawGradientRect(offset - fontHeight, 2, offset, fontHeight + 2, 0x80EF8927, 0x80E4531C);
            if (overlays[i]) {
                GuiUtils.getInstance().drawGradientRect(offset - fontHeight, 2, offset, fontHeight + 2, 0xFFEF8927, 0xFFE4531C);
            } else {
                GuiUtils.getInstance().drawGradientRect(offset - fontHeight, 2, offset, fontHeight + 2, 0x80EF8927, 0x80E4531C);
            }
            if (mouse.x >= offset - fontHeight && mouse.x <= offset
                    && mouse.y >= 2 && mouse.y <= fontHeight + 2) {
                GuiUtils.getInstance().drawGradientRect(offset - fontHeight, 2, offset, fontHeight + 2, 0x80EF8927, 0x80E4531C);
            }
            //DONE
            offset -= fontHeight + 2;
        }

        glEnable(GL_TEXTURE_2D);
        theme.getFontRenderer().drawStringWithShadow(component.getTitle(), 2, 2, 0xFFEF8927);
        glEnable(GL_CULL_FACE);
        glDisable(GL_BLEND);
        translateComponent(component, true);
    }

    @Override
    protected Rectangle getContainerChildRenderArea(Frame container) {
        Rectangle area = new Rectangle(container.getArea());
        area.x = 2;
        area.y = theme.getFontRenderer().FONT_HEIGHT + 6;
        area.width -= 4;
        area.height -= theme.getFontRenderer().FONT_HEIGHT + 8;
        return area;
    }

    @Override
    protected Dimension getDefaultComponentSize(Frame component) {
        Component[] children = component.getChildren();
        Rectangle[] areas = new Rectangle[children.length];
        Constraint[][] constraints = new Constraint[children.length][];
        for (int i = 0; i < children.length; i++) {
            Component child = children[i];
            Dimension size = child.getTheme().getUIForComponent(child)
                    .getDefaultSize(child);
            areas[i] = new Rectangle(0, 0, size.width, size.height);
            constraints[i] = component.getConstraints(child);
        }
        Dimension size = component.getLayoutManager().getOptimalPositionedSize(
                areas, constraints);
        size.width += 4;
        size.height += theme.getFontRenderer().FONT_HEIGHT + 8;
        return size;
    }

    @Override
    protected Rectangle[] getInteractableComponentRegions(Frame component) {
        return new Rectangle[]{new Rectangle(0, 0, component.getWidth(),
                theme.getFontRenderer().FONT_HEIGHT + 4)};
    }

    @Override
    protected void handleComponentInteraction(Frame component, Point location,
                                              int button) {
        if (button != 0)
            return;
        int offset = component.getWidth() - 2;
        int textHeight = theme.getFontRenderer().FONT_HEIGHT;
        if (component.isClosable()) {
            if (location.x >= offset - textHeight && location.x <= offset
                    && location.y >= 2 && location.y <= textHeight + 2) {
                component.close();
                return;
            }
            offset -= textHeight + 2;
        }
        if (component.isPinnable()) {
            if (location.x >= offset - textHeight && location.x <= offset
                    && location.y >= 2 && location.y <= textHeight + 2) {
                component.setPinned(!component.isPinned());
                return;
            }
            offset -= textHeight + 2;
        }
        if (component.isMinimizable()) {
            if (location.x >= offset - textHeight && location.x <= offset
                    && location.y >= 2 && location.y <= textHeight + 2) {
                component.setMinimized(!component.isMinimized());
                return;
            }
            offset -= textHeight + 2;
        }
        if (location.x >= 0 && location.x <= offset && location.y >= 0
                && location.y <= textHeight + 4) {
            component.setDragging(true);
            return;
        }
    }
}
