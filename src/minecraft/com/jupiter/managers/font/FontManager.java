package com.jupiter.managers.font;

/**
 * Created by corey on 15/07/14.
 */
public class FontManager {

    private TTFRenderer titleFont;
    private TTFRenderer clientFont;
    private TTFRenderer menuButtonFont;

    public void onStart() {
        titleFont = new TTFRenderer("Verdana Bold", 90);
        clientFont = new TTFRenderer("segeo ui", 16);
        menuButtonFont = new TTFRenderer("Comic Sans Bold", 18);
    }

    public TTFRenderer getTitleFont() {
        return titleFont;
    }

    public TTFRenderer getClientFont() {
        return clientFont;
    }

    public TTFRenderer getMenuButtonFont() {
        return menuButtonFont;
    }
}
