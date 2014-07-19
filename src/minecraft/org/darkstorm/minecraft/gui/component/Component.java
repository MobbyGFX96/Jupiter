package org.darkstorm.minecraft.gui.component;

import org.darkstorm.minecraft.gui.theme.Theme;

import java.awt.*;

public interface Component {
    public Theme getTheme();

    public void setTheme(Theme theme);

    public void render();

    public void update();

    public int getX();

    public void setX(int x);

    public int getY();

    public void setY(int y);

    public int getWidth();

    public void setWidth(int width);

    public int getHeight();

    public void setHeight(int height);

    public Point getLocation();

    public Dimension getSize();

    public Rectangle getArea();

    public Container getParent();

    public void setParent(Container parent);

    public Color getBackgroundColor();

    public void setBackgroundColor(Color color);

    public Color getForegroundColor();

    public void setForegroundColor(Color color);

    public void onMousePress(int x, int y, int button);

    public void onMouseRelease(int x, int y, int button);

    public void resize();

    public boolean isVisible();

    public void setVisible(boolean visible);

    public boolean isEnabled();

    public void setEnabled(boolean enabled);
}
