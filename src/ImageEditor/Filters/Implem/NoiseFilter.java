package ImageEditor.Filters.Implem;

import ImageEditor.Filters.FilterStrategy;

import java.awt.image.BufferedImage;
import java.util.Random;

public class NoiseFilter extends FilterStrategy {
    //filtry po porstu nakładają filr na obraz

    /**
     * funkcja nakłada na image filtr
     *
     * @param image obraz
     */
    private Random r = new Random();

    public void applyFilter(BufferedImage image) {
        int w = image.getWidth();
        int h = image.getHeight();
        int ni = 10;

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                if (r.nextInt(100) < ni) {
                    int p = image.getRGB(x, y);
                    int a = (p >> 24) & 0xFF;
                    int pr = (p >> 16) & 0xFF;
                    int pg = (p >> 8) & 0xFF;
                    int pb = p & 0xFF;

                    int n = r.nextInt(61) - 30;
                    pr = Math.max(0, Math.min(255, pr + n));
                    pg = Math.max(0, Math.min(255, pg + n));
                    pb = Math.max(0, Math.min(255, pb + n));

                    int np = (a << 24) | (pr << 16) | (pg << 8) | pb;
                    image.setRGB(x, y, np);
                }
            }

        }

    }
}
