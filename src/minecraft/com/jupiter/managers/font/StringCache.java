package com.jupiter.managers.font;

import net.minecraft.client.renderer.Tessellator;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.awt.font.GlyphVector;
import java.lang.ref.WeakReference;
import java.text.Bidi;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.WeakHashMap;

public class StringCache {
    private static final int BASELINE_OFFSET = 7;
    private static final int UNDERLINE_OFFSET = 1;
    private static final int UNDERLINE_THICKNESS = 2;
    private static final int STRIKETHROUGH_OFFSET = -6;
    private static final int STRIKETHROUGH_THICKNESS = 2;
    private final GlyphCache glyphCache;
    private final int colorTable[];
    private final WeakHashMap<Key, Entry> stringCache = new WeakHashMap();
    private final WeakHashMap<String, Key> weakRefCache = new WeakHashMap();
    private final Key lookupKey = new Key();
    private final Glyph[][] digitGlyphs = new Glyph[4][];
    private final Thread mainThread;
    private boolean digitGlyphsReady = false;
    private boolean antiAliasEnabled = false;

    public StringCache(final int colors[]) {
        mainThread = Thread.currentThread();

        glyphCache = new GlyphCache();
        colorTable = colors;

        cacheDightGlyphs();
    }

    public void setDefaultFont(final String name, final int size,
                               final boolean antiAlias) {
        glyphCache.setDefaultFont(name, size, antiAlias);
        antiAliasEnabled = antiAlias;
        weakRefCache.clear();
        stringCache.clear();
        cacheDightGlyphs();
    }

    private void cacheDightGlyphs() {
        digitGlyphsReady = false;
        digitGlyphs[Font.PLAIN] = cacheString("0123456789").glyphs;
        digitGlyphs[Font.BOLD] = cacheString("???l0123456789").glyphs;
        digitGlyphs[Font.ITALIC] = cacheString("???o0123456789").glyphs;
        digitGlyphs[Font.BOLD | Font.ITALIC] = cacheString("???l???o0123456789").glyphs;
        digitGlyphsReady = true;
    }

    public int renderString(final String str, final float startX, float startY,
                            final int initialColor, final boolean shadowFlag) {
        if ((str == null) || str.isEmpty()) {
            return 0;
        }
        final Entry entry = cacheString(str);
        startY += BASELINE_OFFSET;
        int color = initialColor;
        int boundTextureName = 0;
        GL11.glColor3f((color >> 16) & 0xff, (color >> 8) & 0xff, color & 0xff);
        if (antiAliasEnabled) {
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        }
        final Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.setColorRGBA((color >> 16) & 0xff, (color >> 8) & 0xff,
                color & 0xff, (color >> 24) & 0xff);
        int fontStyle = Font.PLAIN;

        for (int glyphIndex = 0, colorIndex = 0; glyphIndex < entry.glyphs.length; glyphIndex++) {
            while ((colorIndex < entry.colors.length)
                    && (entry.glyphs[glyphIndex].stringIndex >= entry.colors[colorIndex].stringIndex)) {
                color = applyColorCode(entry.colors[colorIndex].colorCode,
                        initialColor, shadowFlag);
                fontStyle = entry.colors[colorIndex].fontStyle;
                colorIndex++;
            }
            final Glyph glyph = entry.glyphs[glyphIndex];
            GlyphCache.Entry texture = glyph.texture;
            int glyphX = glyph.x;
            final char c = str.charAt(glyph.stringIndex);
            if ((c >= '0') && (c <= '9')) {
                final int oldWidth = texture.width;
                texture = digitGlyphs[fontStyle][c - '0'].texture;
                final int newWidth = texture.width;
                glyphX += (oldWidth - newWidth) >> 1;
            }
            if (boundTextureName != texture.textureName) {
                tessellator.draw();
                tessellator.startDrawingQuads();
                tessellator
                        .setColorRGBA((color >> 16) & 0xff,
                                (color >> 8) & 0xff, color & 0xff,
                                (color >> 24) & 0xff);

                GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.textureName);
                boundTextureName = texture.textureName;
            }
            final float x1 = startX + ((glyphX) / 2.0F);
            final float x2 = startX + ((glyphX + texture.width) / 2.0F);
            final float y1 = startY + ((glyph.y) / 2.0F);
            final float y2 = startY + ((glyph.y + texture.height) / 2.0F);

            tessellator.addVertexWithUV(x1, y1, 0, texture.u1, texture.v1);
            tessellator.addVertexWithUV(x1, y2, 0, texture.u1, texture.v2);
            tessellator.addVertexWithUV(x2, y2, 0, texture.u2, texture.v2);
            tessellator.addVertexWithUV(x2, y1, 0, texture.u2, texture.v1);
        }
        tessellator.draw();
        if (entry.specialRender) {
            int renderStyle = 0;
            color = initialColor;
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            tessellator.startDrawingQuads();
            tessellator.setColorRGBA((color >> 16) & 0xff, (color >> 8) & 0xff,
                    color & 0xff, (color >> 24) & 0xff);

            for (int glyphIndex = 0, colorIndex = 0; glyphIndex < entry.glyphs.length; glyphIndex++) {
                while ((colorIndex < entry.colors.length)
                        && (entry.glyphs[glyphIndex].stringIndex >= entry.colors[colorIndex].stringIndex)) {
                    color = applyColorCode(entry.colors[colorIndex].colorCode,
                            initialColor, shadowFlag);
                    renderStyle = entry.colors[colorIndex].renderStyle;
                    colorIndex++;
                }
                final Glyph glyph = entry.glyphs[glyphIndex];
                final int glyphSpace = glyph.advance - glyph.texture.width;
                if ((renderStyle & ColorCode.UNDERLINE) != 0) {
                    final float x1 = startX + ((glyph.x - glyphSpace) / 2.0F);
                    final float x2 = startX
                            + ((glyph.x + glyph.advance) / 2.0F);
                    final float y1 = startY + ((UNDERLINE_OFFSET) / 2.0F);
                    final float y2 = startY
                            + ((UNDERLINE_OFFSET + UNDERLINE_THICKNESS) / 2.0F);

                    tessellator.addVertex(x1, y1, 0);
                    tessellator.addVertex(x1, y2, 0);
                    tessellator.addVertex(x2, y2, 0);
                    tessellator.addVertex(x2, y1, 0);
                }
                if ((renderStyle & ColorCode.STRIKETHROUGH) != 0) {
                    final float x1 = startX + ((glyph.x - glyphSpace) / 2.0F);
                    final float x2 = startX
                            + ((glyph.x + glyph.advance) / 2.0F);
                    final float y1 = startY + ((STRIKETHROUGH_OFFSET) / 2.0F);
                    final float y2 = startY
                            + ((STRIKETHROUGH_OFFSET + STRIKETHROUGH_THICKNESS) / 2.0F);

                    tessellator.addVertex(x1, y1, 0);
                    tessellator.addVertex(x1, y2, 0);
                    tessellator.addVertex(x2, y2, 0);
                    tessellator.addVertex(x2, y1, 0);
                }
            }
            tessellator.draw();
            GL11.glEnable(GL11.GL_TEXTURE_2D);
        }

        if (antiAliasEnabled) {
            GL11.glDisable(GL11.GL_BLEND);
        }
        return entry.advance / 2;
    }

    public int getStringWidth(final String str) {
        if ((str == null) || str.isEmpty()) {
            return 0;
        }
        final Entry entry = cacheString(str);
        return entry.advance / 2;
    }

    public int getStringHeight() {
        final Entry entry = cacheString("|");
        final Glyph glyph = entry.glyphs[0];
        return (glyph.texture.height / 2) - 1;
    }

    private int sizeString(final String str, int width,
                           final boolean breakAtSpaces) {
        if ((str == null) || str.isEmpty()) {
            return 0;
        }
        width += width;
        final Glyph glyphs[] = cacheString(str).glyphs;
        int wsIndex = -1;
        int advance = 0, index = 0;
        while ((index < glyphs.length) && (advance <= width)) {
            if (breakAtSpaces) {
                final char c = str.charAt(glyphs[index].stringIndex);
                if (c == ' ') {
                    wsIndex = index;
                } else if (c == '\n') {
                    wsIndex = index;
                    break;
                }
            }

            advance += glyphs[index].advance;
            index++;
        }
        if ((index < glyphs.length) && (wsIndex != -1) && (wsIndex < index)) {
            index = wsIndex;
        }
        return index < glyphs.length ? glyphs[index].stringIndex : str.length();
    }

    public int sizeStringToWidth(final String str, final int width) {
        return sizeString(str, width, true);
    }

    public String trimStringToWidth(String str, final int width,
                                    final boolean reverse) {
        final int length = sizeString(str, width, false);
        str = str.substring(0, length);

        if (reverse) {
            str = (new StringBuilder(str)).reverse().toString();
        }

        return str;
    }

    private int applyColorCode(int colorCode, int color,
                               final boolean shadowFlag) {

        if (colorCode != -1) {
            colorCode = shadowFlag ? colorCode + 16 : colorCode;
            color = (colorTable[colorCode] & 0xffffff) | (color & 0xff000000);
        }

        Tessellator.instance.setColorRGBA((color >> 16) & 0xff,
                (color >> 8) & 0xff, color & 0xff, (color >> 24) & 0xff);
        return color;
    }

    private Entry cacheString(final String str) {
        Key key;
        Entry entry = null;
        if (mainThread == Thread.currentThread()) {
            lookupKey.str = str;
            entry = stringCache.get(lookupKey);
        }
        if (entry == null) {
            final char text[] = str.toCharArray();
            entry = new Entry();
            final int length = stripColorCodes(entry, str, text);
            final List<Glyph> glyphList = new ArrayList();
            entry.advance = layoutBidiString(glyphList, text, 0, length,
                    entry.colors);
            entry.glyphs = new Glyph[glyphList.size()];
            entry.glyphs = glyphList.toArray(entry.glyphs);
            Arrays.sort(entry.glyphs);
            int colorIndex = 0, shift = 0;
            for (final Glyph glyph2 : entry.glyphs) {
                final Glyph glyph = glyph2;
                while ((colorIndex < entry.colors.length)
                        && ((glyph.stringIndex + shift) >= entry.colors[colorIndex].stringIndex)) {
                    shift += 2;
                    colorIndex++;
                }
                glyph.stringIndex += shift;
            }
            if (mainThread == Thread.currentThread()) {
                key = new Key();
                key.str = new String(str);
                entry.keyRef = new WeakReference(key);
                stringCache.put(key, entry);
            }
        }
        if (mainThread == Thread.currentThread()) {
            final Key oldKey = entry.keyRef.get();
            if (oldKey != null) {
                weakRefCache.put(str, oldKey);
            }
            lookupKey.str = null;
        }
        return entry;
    }

    private int stripColorCodes(final Entry cacheEntry, final String str,
                                final char text[]) {
        final List<ColorCode> colorList = new ArrayList();
        int start = 0, shift = 0, next;

        byte fontStyle = Font.PLAIN;
        byte renderStyle = 0;
        byte colorCode = -1;
        while (((next = str.indexOf('\u00A7', start)) != -1)
                && ((next + 1) < str.length())) {
            System.arraycopy(text, (next - shift) + 2, text, next - shift,
                    text.length - next - 2);
            final int code = "0123456789abcdefklmnor".indexOf(Character
                    .toLowerCase(str.charAt(next + 1)));
            switch (code) {
                case 16:
                    break;
                case 17:
                    fontStyle |= Font.BOLD;
                    break;
                case 18:
                    renderStyle |= ColorCode.STRIKETHROUGH;
                    cacheEntry.specialRender = true;
                    break;
                case 19:
                    renderStyle |= ColorCode.UNDERLINE;
                    cacheEntry.specialRender = true;
                    break;
                case 20:
                    fontStyle |= Font.ITALIC;
                    break;
                case 21:
                    fontStyle = Font.PLAIN;
                    renderStyle = 0;
                    colorCode = -1;
                    break;
                default:
                    if ((code >= 0) && (code <= 15)) {
                        colorCode = (byte) code;
                        fontStyle = Font.PLAIN;
                        renderStyle = 0;
                    }
                    break;
            }
            final ColorCode entry = new ColorCode();
            entry.stringIndex = next;
            entry.stripIndex = next - shift;
            entry.colorCode = colorCode;
            entry.fontStyle = fontStyle;
            entry.renderStyle = renderStyle;
            colorList.add(entry);
            start = next + 2;
            shift += 2;
        }
        cacheEntry.colors = new ColorCode[colorList.size()];
        cacheEntry.colors = colorList.toArray(cacheEntry.colors);
        return text.length - shift;
    }

    private int layoutBidiString(final List<Glyph> glyphList,
                                 final char text[], final int start, final int limit,
                                 final ColorCode colors[]) {
        int advance = 0;
        if (Bidi.requiresBidi(text, start, limit)) {
            final Bidi bidi = new Bidi(text, start, null, 0, limit - start,
                    Bidi.DIRECTION_DEFAULT_LEFT_TO_RIGHT);
            if (bidi.isRightToLeft()) {
                return layoutStyle(glyphList, text, start, limit,
                        Font.LAYOUT_RIGHT_TO_LEFT, advance, colors);
            } else {
                final int runCount = bidi.getRunCount();
                final byte levels[] = new byte[runCount];
                final Integer ranges[] = new Integer[runCount];
                for (int index = 0; index < runCount; index++) {
                    levels[index] = (byte) bidi.getRunLevel(index);
                    ranges[index] = new Integer(index);
                }
                Bidi.reorderVisually(levels, 0, ranges, 0, runCount);
                for (int visualIndex = 0; visualIndex < runCount; visualIndex++) {
                    final int logicalIndex = ranges[visualIndex];
                    final int layoutFlag = (bidi.getRunLevel(logicalIndex) & 1) == 1 ? Font.LAYOUT_RIGHT_TO_LEFT
                            : Font.LAYOUT_LEFT_TO_RIGHT;
                    advance = layoutStyle(glyphList, text,
                            start + bidi.getRunStart(logicalIndex), start
                                    + bidi.getRunLimit(logicalIndex),
                            layoutFlag, advance, colors);
                }
            }

            return advance;
        } else {
            return layoutStyle(glyphList, text, start, limit,
                    Font.LAYOUT_LEFT_TO_RIGHT, advance, colors);
        }
    }

    private int layoutStyle(final List<Glyph> glyphList, final char text[],
                            int start, final int limit, final int layoutFlags, int advance,
                            final ColorCode colors[]) {
        int currentFontStyle = Font.PLAIN;
        int colorIndex = Arrays.binarySearch(colors, start);
        if (colorIndex < 0) {
            colorIndex = -colorIndex - 2;
        }
        while (start < limit) {
            int next = limit;
            while ((colorIndex >= 0)
                    && (colorIndex < (colors.length - 1))
                    && (colors[colorIndex].stripIndex == colors[colorIndex + 1].stripIndex)) {
                colorIndex++;
            }
            if ((colorIndex >= 0) && (colorIndex < colors.length)) {
                currentFontStyle = colors[colorIndex].fontStyle;
            }
            while (++colorIndex < colors.length) {
                if (colors[colorIndex].fontStyle != currentFontStyle) {
                    next = colors[colorIndex].stripIndex;
                    break;
                }
            }
            advance = layoutString(glyphList, text, start, next, layoutFlags,
                    advance, currentFontStyle);
            start = next;
        }

        return advance;
    }

    private int layoutString(final List<Glyph> glyphList, final char text[],
                             int start, final int limit, final int layoutFlags, int advance,
                             final int style) {
        if (digitGlyphsReady) {
            for (int index = start; index < limit; index++) {
                if ((text[index] >= '0') && (text[index] <= '9')) {
                    text[index] = '0';
                }
            }
        }
        while (start < limit) {
            final Font font = glyphCache.lookupFont(text, start, limit, style);
            int next = font.canDisplayUpTo(text, start, limit);
            if (next == -1) {
                next = limit;
            }
            if (next == start) {
                next++;
            }

            advance = layoutFont(glyphList, text, start, next, layoutFlags,
                    advance, font);
            start = next;
        }

        return advance;
    }

    private int layoutFont(final List<Glyph> glyphList, final char text[],
                           final int start, final int limit, final int layoutFlags,
                           int advance, final Font font) {
        if (mainThread == Thread.currentThread()) {
            glyphCache.cacheGlyphs(font, text, start, limit, layoutFlags);
        }
        final GlyphVector vector = glyphCache.layoutGlyphVector(font, text,
                start, limit, layoutFlags);
        Glyph glyph = null;
        final int numGlyphs = vector.getNumGlyphs();
        for (int index = 0; index < numGlyphs; index++) {
            final Point position = vector.getGlyphPixelBounds(index, null,
                    advance, 0).getLocation();
            if (glyph != null) {
                glyph.advance = position.x - glyph.x;
            }
            glyph = new Glyph();
            glyph.stringIndex = start + vector.getGlyphCharIndex(index);
            glyph.texture = glyphCache.lookupGlyph(font,
                    vector.getGlyphCode(index));
            glyph.x = position.x;
            glyph.y = position.y;
            glyphList.add(glyph);
        }
        advance += (int) vector.getGlyphPosition(numGlyphs).getX();
        if (glyph != null) {
            glyph.advance = advance - glyph.x;
        }
        return advance;
    }

    static private class Key {
        public String str;

        @Override
        public int hashCode() {
            int code = 0;
            final int length = str.length();
            boolean colorCode = false;

            for (int index = 0; index < length; index++) {
                char c = str.charAt(index);
                if ((c >= '0') && (c <= '9') && !colorCode) {
                    c = '0';
                }
                code = (code * 31) + c;
                colorCode = (c == '\u00A7');
            }

            return code;
        }

        @Override
        public boolean equals(final Object o) {
            if (o == null) {
                return false;
            }
            final String other = o.toString();
            final int length = str.length();

            if (length != other.length()) {
                return false;
            }
            boolean colorCode = false;

            for (int index = 0; index < length; index++) {
                final char c1 = str.charAt(index);
                final char c2 = other.charAt(index);

                if ((c1 != c2)
                        && ((c1 < '0') || (c1 > '9') || (c2 < '0')
                        || (c2 > '9') || colorCode)) {
                    return false;
                }
                colorCode = (c1 == '\u00A7');
            }

            return true;
        }

        @Override
        public String toString() {
            return str;
        }
    }

    static private class Entry {
        public WeakReference<Key> keyRef;
        public int advance;
        public Glyph glyphs[];
        public ColorCode colors[];
        public boolean specialRender;
    }

    static private class ColorCode implements Comparable<Integer> {
        public static final byte UNDERLINE = 1;
        public static final byte STRIKETHROUGH = 2;
        public int stringIndex;
        public int stripIndex;
        public byte colorCode;
        public byte fontStyle;
        public byte renderStyle;

        @Override
        public int compareTo(final Integer i) {
            return (stringIndex == i.intValue()) ? 0 : (stringIndex < i
                    .intValue()) ? -1 : 1;
        }
    }

    static private class Glyph implements Comparable<Glyph> {
        public int stringIndex;
        public GlyphCache.Entry texture;
        public int x;
        public int y;
        public int advance;

        @Override
        public int compareTo(final Glyph o) {
            return (stringIndex == o.stringIndex) ? 0
                    : (stringIndex < o.stringIndex) ? -1 : 1;
        }
    }
}