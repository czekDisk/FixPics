package modules;

import java.awt.image.BufferedImage;

public class Utils {

    //może dodaj dynamiczne skalowanie w zależności od wymiarów obrazu
    static public BufferedImage scaleDown(BufferedImage image, int value) {
        //TODO or use from std lib if too hard
    }

    static public BufferedImage deepCopyImage(BufferedImage image) {
        // TODO: Sprawdzic czy nie da sie optymalniej zrobic kopii
        BufferedImage copy = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        for (int x=0; x<image.getWidth(); x++) {
            for (int y=0; y<image.getHeight(); y++) {
                copy.setRGB(x, y, image.getRGB(x, y));
            }
        }

        return copy;
    }
}
