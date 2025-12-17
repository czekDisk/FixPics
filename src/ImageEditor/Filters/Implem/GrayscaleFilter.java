package ImageEditor.Filters.Implem;

import ImageEditor.Filters.FilterStrategy;

import java.awt.image.BufferedImage;


public class GrayscaleFilter extends FilterStrategy {
    //filtry po porstu nakładają filr na obraz

    /**
     *funkcja nakłada na image filtr
     * @param image obraz
     */
    public void applyFilter(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = image.getRGB(x, y);

                int a = (rgb >> 24) & 0xFF;
                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = rgb & 0xFF;

                int gray = (int)(0.299 * r + 0.587 * g + 0.114 * b);

                int grayRgb =
                        (a << 24) |
                                (gray << 16) |
                                (gray << 8) |
                                gray;

                image.setRGB(x, y, grayRgb);
            }
        }
    }
}
