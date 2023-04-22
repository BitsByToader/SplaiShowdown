package org.tudor.Graphics.Assets;

import java.awt.image.BufferedImage;

/**
 * Static class that keeps all the assets for the games.
 */
public class Assets {
    /** Test texture for a small body part. */
    public static BufferedImage partSmall;
    /** Test texture for a large body part. */
    public static BufferedImage partLarge;

    /**
     * Private constructor since this is a static class.
     */
    private Assets() {};

    /**
     * Initialize the assets used in the game.
     */
    public static void init() {
        partSmall = ImageLoader.loadImage("/part_small.png");
        partLarge = ImageLoader.loadImage("/part.png");
    }
}
