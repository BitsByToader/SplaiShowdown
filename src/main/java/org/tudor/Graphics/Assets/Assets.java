package org.tudor.Graphics.Assets;

import java.awt.image.BufferedImage;

public class Assets {
    public static BufferedImage partSmall;
    public static BufferedImage partLarge;

    public static void init() {
        partSmall = ImageLoader.loadImage("/part_small.png");
        partLarge = ImageLoader.loadImage("/part.png");
    }
}
