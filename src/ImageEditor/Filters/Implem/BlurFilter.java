package ImageEditor.Filters.Implem;

import ImageEditor.Filters.FilterStrategy;

import java.awt.image.BufferedImage;

public class BlurFilter extends FilterStrategy{
    //filtry po porstu nakładają filr na obraz

    /**
     *funkcja nakłada na image filtr
     * @param image obraz
     */
    public void applyFilter(BufferedImage image) {
        int w = image.getWidth();
        int h = image.getHeight();
        BufferedImage t = new BufferedImage(w, h, image.getType());

        int k = 3;
        int hk = k / 2;

        for (int y = hk; y < h - hk; y++) {
            for (int x = hk; x < w - hk; x++) {
                int r = 0, g = 0, b = 0, c = 0;

                for (int dy = -hk; dy <= hk; dy++) {
                    for (int dx = -hk; dx <= hk; dx++) {
                        int p = image.getRGB(x + dx, y + dy);
                        r += (p >> 16) & 0xFF;
                        g += (p >> 8) & 0xFF;
                        b += p & 0xFF;
                        c++;
                    }
                }

                int ar = r / c;
                int ag = g / c;
                int ab = b / c;
                int op = image.getRGB(x, y);
                int a = (op >> 24) & 0xFF;
                int np = (a << 24) | (ar << 16) | (ag << 8) | ab;
                t.setRGB(x, y, np);
            }
        }

        for (int y = hk; y < h - hk; y++) {
            for (int x = hk; x < w - hk; x++) {
                image.setRGB(x, y, t.getRGB(x, y));
            }
        }
    }
}