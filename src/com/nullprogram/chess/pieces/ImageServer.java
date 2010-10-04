package com.nullprogram.chess.pieces;

import java.util.WeakHashMap;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

public class ImageServer {

    private static WeakHashMap<CacheKey, BufferedImage> cache
    = new WeakHashMap<CacheKey, BufferedImage>();

    private static class CacheKey {
        String name;
        int size;

        public CacheKey(String name, int size) {
            this.name = name;
            this.size = size;
        }

        public int hashCode() {
            return name.hashCode() ^ size;
        }

        public boolean equals(Object that) {
            if (this == that) {
                return true;
            }
            if (!(that instanceof CacheKey)) {
                return false;
            }
            CacheKey key = (CacheKey)that;
            return key.hashCode() == hashCode();
        }
    }

    public static BufferedImage getTile(String name, int size) {
        CacheKey key = new CacheKey(name, size);
        BufferedImage cached = cache.get(key);
        if (cached != null) {
            return cached;
        }

        BufferedImage orig = null, image = null;
        try {
            orig = ImageIO.read(ImageServer.class.getResource(name + ".png"));
            image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = image.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                               RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.drawImage(orig, 0, 0, size, size, null);
            g.dispose();
        } catch (java.io.IOException e) {
            // TODO: handle it
            System.out.println("Failed to fetch image: " + name);
        }
        cache.put(key, image);
        return image;
    }
}
