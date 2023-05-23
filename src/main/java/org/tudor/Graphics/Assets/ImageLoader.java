package org.tudor.Graphics.Assets;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.Objects;

/**
 * Static class for loading an image for non-volatile storage.
 */
public class ImageLoader {
    /**
     * Private default constructor since this is a static class.
     */
    private ImageLoader() {}

    /**
     * Loads an image from the given resource path.
     * @param path The image path.
     * @return A BufferedImage of the image.
     */
    public static BufferedImage loadImage(String path) {
        try {
            return ImageIO.read(Objects.requireNonNull(ImageLoader.class.getResource(path)));
        } catch (Exception e ) {
            e.printStackTrace();
        }

        return null;
    }
}
