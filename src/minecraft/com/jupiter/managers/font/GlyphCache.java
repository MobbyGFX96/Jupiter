package com.jupiter.managers.font;

import net.minecraft.client.renderer.GLAllocation;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.*;
import java.util.List;

public class GlyphCache {
    private static final int TEXTURE_WIDTH = 256;
    private static final int TEXTURE_HEIGHT = 256;
    private final BufferedImage glyphCacheImage = new BufferedImage(
            TEXTURE_WIDTH, TEXTURE_HEIGHT, BufferedImage.TYPE_INT_ARGB);
    private final int imageData[] = new int[TEXTURE_WIDTH * TEXTURE_HEIGHT];
    private static final int STRING_WIDTH = 256;
    private static final int STRING_HEIGHT = 64;
    private static final int GLYPH_BORDER = 1;
    private int cachePosX = GLYPH_BORDER;
    private int cachePosY = GLYPH_BORDER;
    private static Color BACK_COLOR = new Color(255, 255, 255, 0);
    private final Graphics2D glyphCacheGraphics = glyphCacheImage
            .createGraphics();
    private final FontRenderContext fontRenderContext = glyphCacheGraphics
            .getFontRenderContext();
    private final IntBuffer imageBuffer = ByteBuffer
            .allocateDirect(4 * TEXTURE_WIDTH * TEXTURE_HEIGHT)
            .order(ByteOrder.BIG_ENDIAN).asIntBuffer();
    private final IntBuffer singleIntBuffer = GLAllocation
            .createDirectIntBuffer(1);
    private final List<Font> allFonts = Arrays.asList(GraphicsEnvironment
            .getLocalGraphicsEnvironment().getAllFonts());
    private final List<Font> usedFonts = new ArrayList();
    private final LinkedHashMap<Font, Integer> fontCache = new LinkedHashMap();
    private final LinkedHashMap<Long, Entry> glyphCache = new LinkedHashMap();
    private int fontSize = 18;
    private boolean antiAliasEnabled = false;
    private BufferedImage stringImage;
    private Graphics2D stringGraphics;
    private int textureName;
    private int cacheLineHeight = 0;

    public GlyphCache() {
        glyphCacheGraphics.setBackground(BACK_COLOR);
        glyphCacheGraphics.setComposite(AlphaComposite.Src);

        allocateGlyphCacheTexture();
        allocateStringImage(STRING_WIDTH, STRING_HEIGHT);
        GraphicsEnvironment.getLocalGraphicsEnvironment().preferLocaleFonts();
        usedFonts.add(new Font(Font.SANS_SERIF, Font.PLAIN, 1));
    }

    void setDefaultFont(final String name, final int size,
                        final boolean antiAlias) {
        //System.out.println("[XClient] Loading Font:  \"" + name + "\"");
        usedFonts.clear();
        usedFonts.add(new Font(name, Font.PLAIN, 1));

        fontSize = size;
        antiAliasEnabled = antiAlias;
        setRenderingHints();
    }

    GlyphVector layoutGlyphVector(final Font font, final char text[],
                                  final int start, final int limit, final int layoutFlags) {
        if (!fontCache.containsKey(font)) {
            fontCache.put(font, fontCache.size());
        }
        return font.layoutGlyphVector(fontRenderContext, text, start, limit,
                layoutFlags);
    }

    Font lookupFont(final char text[], final int start, final int limit,
                    final int style) {
        Iterator<Font> iterator = usedFonts.iterator();
        while (iterator.hasNext()) {

            final Font font = iterator.next();
            if (font.canDisplayUpTo(text, start, limit) != start) {
                return font.deriveFont(style, fontSize);
            }
        }

        iterator = allFonts.iterator();
        while (iterator.hasNext()) {
            final Font font = iterator.next();
            if (font.canDisplayUpTo(text, start, limit) != start) {
                //System.out.println("[YouAlwaysWin] Loading Font: \""
                //                + font.getFontName() + "\"");
                usedFonts.add(font);
                return font.deriveFont(style, fontSize);
            }
        }

        final Font font = usedFonts.get(0);

        return font.deriveFont(style, fontSize);
    }

    Entry lookupGlyph(final Font font, final int glyphCode) {
        final long fontKey = (long) fontCache.get(font) << 32;
        return glyphCache.get(fontKey | glyphCode);
    }

    void cacheGlyphs(final Font font, final char text[], final int start,
                     final int limit, final int layoutFlags) {

        final GlyphVector vector = layoutGlyphVector(font, text, start, limit,
                layoutFlags);

        Rectangle vectorBounds = null;
        final long fontKey = (long) fontCache.get(font) << 32;

        final int numGlyphs = vector.getNumGlyphs();
        Rectangle dirty = null;
        boolean vectorRendered = false;

        for (int index = 0; index < numGlyphs; index++) {
            final int glyphCode = vector.getGlyphCode(index);
            if (glyphCache.containsKey(fontKey | glyphCode)) {
                continue;
            }
            if (!vectorRendered) {
                vectorRendered = true;
                for (int i = 0; i < numGlyphs; i++) {
                    final Point2D pos = vector.getGlyphPosition(i);
                    pos.setLocation(pos.getX() + (2 * i), pos.getY());
                    vector.setGlyphPosition(i, pos);
                }
                vectorBounds = vector.getPixelBounds(fontRenderContext, 0, 0);
                if ((stringImage == null)
                        || (vectorBounds.width > stringImage.getWidth())
                        || (vectorBounds.height > stringImage.getHeight())) {
                    final int width = Math.max(vectorBounds.width,
                            stringImage.getWidth());
                    final int height = Math.max(vectorBounds.height,
                            stringImage.getHeight());
                    allocateStringImage(width, height);
                }
                stringGraphics.clearRect(0, 0, vectorBounds.width,
                        vectorBounds.height);
                stringGraphics.drawGlyphVector(vector, -vectorBounds.x,
                        -vectorBounds.y);
            }
            final Rectangle rect = vector.getGlyphPixelBounds(index, null,
                    -vectorBounds.x, -vectorBounds.y);
            if ((cachePosX + rect.width + GLYPH_BORDER) > TEXTURE_WIDTH) {
                cachePosX = GLYPH_BORDER;
                cachePosY += cacheLineHeight + GLYPH_BORDER;
                cacheLineHeight = 0;
            }
            if ((cachePosY + rect.height + GLYPH_BORDER) > TEXTURE_HEIGHT) {
                updateTexture(dirty);
                dirty = null;
                allocateGlyphCacheTexture();
                cachePosY = cachePosX = GLYPH_BORDER;
                cacheLineHeight = 0;
            }
            if (rect.height > cacheLineHeight) {
                cacheLineHeight = rect.height;
            }
            glyphCacheGraphics.drawImage(stringImage, cachePosX, cachePosY,
                    cachePosX + rect.width, cachePosY + rect.height, rect.x,
                    rect.y, rect.x + rect.width, rect.y + rect.height, null);

            rect.setLocation(cachePosX, cachePosY);
            final Entry entry = new Entry();
            entry.textureName = textureName;
            entry.width = rect.width;
            entry.height = rect.height;
            entry.u1 = ((float) rect.x) / TEXTURE_WIDTH;
            entry.v1 = ((float) rect.y) / TEXTURE_HEIGHT;
            entry.u2 = ((float) (rect.x + rect.width)) / TEXTURE_WIDTH;
            entry.v2 = ((float) (rect.y + rect.height)) / TEXTURE_HEIGHT;
            glyphCache.put(fontKey | glyphCode, entry);
            if (dirty == null) {
                dirty = new Rectangle(cachePosX, cachePosY, rect.width,
                        rect.height);
            } else {
                dirty.add(rect);
            }
            cachePosX += rect.width + GLYPH_BORDER;
        }

        updateTexture(dirty);
    }

    private void updateTexture(final Rectangle dirty) {
        if (dirty != null) {
            updateImageBuffer(dirty.x, dirty.y, dirty.width, dirty.height);

            GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureName);
            GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, 0, dirty.x, dirty.y,
                    dirty.width, dirty.height, GL11.GL_RGBA,
                    GL11.GL_UNSIGNED_BYTE, imageBuffer);
        }
    }

    private void allocateStringImage(final int width, final int height) {
        stringImage = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_ARGB);
        stringGraphics = stringImage.createGraphics();
        setRenderingHints();
        stringGraphics.setBackground(BACK_COLOR);
        stringGraphics.setPaint(Color.WHITE);
    }

    private void setRenderingHints() {
        stringGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                antiAliasEnabled ? RenderingHints.VALUE_ANTIALIAS_ON
                        : RenderingHints.VALUE_ANTIALIAS_OFF);
        stringGraphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                antiAliasEnabled ? RenderingHints.VALUE_TEXT_ANTIALIAS_ON
                        : RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);

        stringGraphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
                RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
    }

    private void allocateGlyphCacheTexture() {
        glyphCacheGraphics.clearRect(0, 0, TEXTURE_WIDTH, TEXTURE_HEIGHT);
        singleIntBuffer.clear();
        GL11.glGenTextures(singleIntBuffer);
        textureName = singleIntBuffer.get(0);
        updateImageBuffer(0, 0, TEXTURE_WIDTH, TEXTURE_HEIGHT);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureName);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_ALPHA8, TEXTURE_WIDTH,
                TEXTURE_HEIGHT, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE,
                imageBuffer);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER,
                GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER,
                GL11.GL_NEAREST);
    }

    private void updateImageBuffer(final int x, final int y, final int width,
                                   final int height) {
        glyphCacheImage.getRGB(x, y, width, height, imageData, 0, width);
        for (int i = 0; i < (width * height); i++) {
            final int color = imageData[i];
            imageData[i] = (color << 8) | (color >>> 24);
        }

        imageBuffer.clear();
        imageBuffer.put(imageData);
        imageBuffer.flip();
    }

    static class Entry {
        public int textureName;
        public int width;
        public int height;
        public float u1;
        public float v1;
        public float u2;
        public float v2;
    }
}