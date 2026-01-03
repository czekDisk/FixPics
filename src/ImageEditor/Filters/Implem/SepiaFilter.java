package ImageEditor.Filters.Implem;

import ImageEditor.Filters.FilterStrategy;

import java.awt.image.BufferedImage;

import com.aparapi.Kernel;
import com.aparapi.Range;


public class SepiaFilter extends FilterStrategy {

    /**
     *funkcja nakłada na image filtr
     * @param image obraz
     */
    public void applyFilter(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        int[] pixels = image.getRGB(0, 0, width, height, null, 0, width);

        SepiaKernel kernel = new SepiaKernel(pixels, pixels, height, width);

        Range range = Range.create(width * height);
        kernel.execute(range);

        image.setRGB(0, 0, width, height, pixels, 0, width);

        kernel.dispose();
    }
}


class SepiaKernel extends Kernel {
    private int [] src;
    private int [] dst;
    private int height;
    private int width;

    public SepiaKernel(int [] src, int [] dst, int height, int width){
        this.src = src;
        this.dst = dst;
        this.height = height;
        this.width = width;
    }

    @Override
    public void run(){
        int i = getGlobalId();

        int argb = src[i];

        int a = (argb >> 24) & 0xFF;
        int r = (argb >> 16) & 0xFF;
        int g = (argb >> 8) & 0xFF;
        int b = argb & 0xFF;

        int outR = (int)(0.393*r + 0.769*g + 0.189*b);
        outR = min(outR, 255);

        int outG = (int)(0.349*r + 0.686*g + 0.168*b);
        outG = min(outG, 255);

        int outB = (int)(0.272*r + 0.534*g + 0.131*b);
        outB = min(outB, 255);

        dst[i] = (a << 24) | (outR << 16) | (outG << 8) | (outB);
    }
}
