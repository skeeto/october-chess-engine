package com.nullprogram.chess.pieces;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

public class ImageServer {
    public static BufferedImage getTile(String name, int size) {
        BufferedImage orig = null, image = null;
        try {
            orig = ImageIO.read(ImageServer.class.getResource(name + ".png"));
            image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
            Graphics2D graphics2D = image.createGraphics();
            graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                                        RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            graphics2D.drawImage(orig, 0, 0, size, size, null);
            graphics2D.dispose();
        } catch (java.io.IOException e) {
            // TODO: handle it
            System.out.println("Failed to fetch image: " + name);
        }
        return image;
    }
}
