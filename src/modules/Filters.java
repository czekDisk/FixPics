package modules;

import java.awt.image.BufferedImage;

public class Filters {
    /**
     *funkcja nakłada na image filtr
     * @param image obraz
     */
    static public void applyBlur (BufferedImage image, int value){
        BlurFilter filter = new BlurFilter();
        filter.applyFilter(image, value);
    }
    /**
     *funkcja nakłada na image filtr
     * @param image obraz
     */
    static public void applyGrayscale (BufferedImage image, int value){
        GrayscaleFilter filter = new GrayscaleFilter();
        filter.applyFilter(image, 0);
    }

    /**
     *funkcja nakłada na image filtr
     * @param image obraz
     */
    static public void applyInvert (BufferedImage image, int value){
        InvertFilter filter = new InvertFilter();
        filter.applyFilter(image, 0);
    }

    /**
     *funkcja nakłada na image filtr
     * @param image obraz
     */
    static public void applyNoise (BufferedImage image, int value){
        NoiseFilter filter = new NoiseFilter();
        filter.applyFilter(image, value);
    }

    /**
     *funkcja nakłada na image filtr
     * @param image obraz
     */
    static public void applyPixelBlur (BufferedImage image, int value){
        PixelBlurFilter filter = new PixelBlurFilter();
        filter.applyFilter(image, value);
    }

    /**
     *funkcja nakłada na image filtr
     * @param image obraz
     */
    static public void applySepia (BufferedImage image, int value){
        SepiaFilter filter = new SepiaFilter();
        filter.applyFilter(image, value);
    }
}