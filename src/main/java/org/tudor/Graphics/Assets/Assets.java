package org.tudor.Graphics.Assets;

import java.awt.image.BufferedImage;

/**
 * Static class that keeps all the assets for the games.
 */
public class Assets {
    /** Test texture for a small body part. */
    public static BufferedImage partSmall;
    /** Test texture for a large body part. */
    public static BufferedImage part;
    public static BufferedImage partBig;
    /** Test texture for a head, i.e. a filled in circle. */
    public static BufferedImage head;
    public static BufferedImage logo;
    public static BufferedImage multiplayerButton;
    public static BufferedImage quitButton;
    public static BufferedImage menuSelector;

    /**
     * Private constructor since this is a static class.
     */
    private Assets() {};

    /**
     * Initialize the assets used in the game.
     */
    public static void init() {
        partSmall = ImageLoader.loadImage("/part_small.png");
        part = ImageLoader.loadImage("/part.png");
        partBig = ImageLoader.loadImage("/part_big.png");
        head = ImageLoader.loadImage("/head.png");
        logo = ImageLoader.loadImage("/logo.png");
        multiplayerButton = ImageLoader.loadImage("/multiplayer_button.png");
        quitButton = ImageLoader.loadImage("/quit_btn.png");
        menuSelector = ImageLoader.loadImage("/menu_selector.png");
    }
}
