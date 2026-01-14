package ImageEditor.Filters.Implem;

import ImageEditor.Filters.FilterStrategy;

import java.awt.image.BufferedImage;

public class NoiseFilter extends FilterStrategy {
    //filtry po porstu nakładają filr na obraz

    /**
     * funkcja nakłada na image filtr
     *
     * @param image obraz
     */
    public void applyFilter(BufferedImage image) {
        int w = image.getWidth();
        int h = image.getHeight();

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {

                if ((x + y) % 3 == 0) {
                    int p = image.getRGB(x, y);
                    int a = (p >> 24) & 0xFF;
                    int r = (p >> 16) & 0xFF;
                    int g = (p >> 8) & 0xFF;
                    int b = p & 0xFF;


                    int szum = 60;

                    r = Math.max(0, Math.min(255, r + szum));
                    g = Math.max(0, Math.min(255, g + szum));
                    b = Math.max(0, Math.min(255, b + szum));

                    int np = (a << 24) | (r << 16) | (g << 8) | b;
                    image.setRGB(x, y, np);
                }
            }
        }
    }
}