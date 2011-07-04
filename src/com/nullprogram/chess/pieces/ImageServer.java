package com.nullprogram.chess.pieces;

import java.util.Map;
import java.util.HashMap;
import java.util.logging.Logger;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import com.nullprogram.chess.LoggerUtils;

/**
 * Serves cached images of requsted size.
 *
 * This will cache the recent requests so it's not hitting the disk
 * every time the display needs an image.
 */
public class ImageServer {
    /** This class's Logger. */
    private static final Logger LOG = LoggerUtils.getLogger();

    /** The image cache. */
    private static Map<String, BufferedImage> cache
    = new HashMap<String, BufferedImage>();

    /**
     * Hidden constructor.
     */
    protected ImageServer() {
    }

    /**
     * Return named image scaled to given size.
     *
     * @param name name of the image
     * @return     the requested image
     */
    public static BufferedImage getTile(final String name) {
        BufferedImage cached = cache.get(name);
        if (cached != null) {
            return cached;
        }

        String file = name + ".png";
        try {
            BufferedImage i = ImageIO.read(ImageServer.class.getResource(file));
            cache.put(name, i);
            return i;
        } catch (java.io.IOException e) {
            String message = "Failed to read image: " + file + ": " + e;
            LOG.severe(message);
            System.exit(1);
        } catch (IllegalArgumentException e) {
            String message = "Failed to find image: " + file + ": " + e;
            LOG.severe(message);
            System.exit(1);
        }
        return null;
    }
}
