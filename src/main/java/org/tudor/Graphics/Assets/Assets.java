package org.tudor.Graphics.Assets;

import java.awt.image.BufferedImage;

/**
 * Static class that keeps all the assets for the games.
 */
public class Assets {
    /** Texture for a small body part. */
    public static BufferedImage partSmall;
    /** Texture for a normal body part. */
    public static BufferedImage part;
    /** Texture for a large body part. */
    public static BufferedImage partBig;
    /** Texture for a head, i.e. a filled in circle. */
    public static BufferedImage head;
    /** Logo of the game. */
    public static BufferedImage logo;
    /** Texture for the play button. */
    public static BufferedImage multiplayerButton;
    /** Texture for the leaderboard button. */
    public static BufferedImage scoresButton;
    /** Texture for the quit button. */
    public static BufferedImage quitButton;
    /** Texture for the menu selector. */
    public static BufferedImage menuSelector;

    /**
     * Private constructor since this is a static class.
     */
    private Assets() {}

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
        scoresButton = ImageLoader.loadImage("/scores_btn.png");
        quitButton = ImageLoader.loadImage("/quit_btn.png");
        menuSelector = ImageLoader.loadImage("/menu_selector.png");
    }
}
