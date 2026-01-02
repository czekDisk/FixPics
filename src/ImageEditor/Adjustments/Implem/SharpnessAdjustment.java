package ImageEditor.Adjustments.Implem;

import ImageEditor.Adjustments.AdjustmentStrategy;
import ImageEditor.Filters.FilterStrategy;
import com.aparapi.Kernel;
import com.aparapi.Range;

import java.awt.image.BufferedImage;

class SharpenARGBKernel extends Kernel {

    private final int[] input;
    private final int[] output;
    private final int width;
    private final int height;
    private final int value;

    public SharpenARGBKernel(int[] input, int[] output,
                             int width, int height, int value) {
        this.input = input;
        this.output = output;
        this.width = width;
        this.height = height;
        this.value = value;
    }

    private int mirror(int x, int max) {
        if (x < 0) return -x - 1;
        if (x >= max) return 2 * max - x - 1;
        return x;
    }

    private int clamp(int v) {
        if (v < 0) return 0;
        if (v > 255) return 255;
        return v;
    }

    @Override
    public void run() {
        int x = getGlobalId(0);
        int y = getGlobalId(1);

        if (x >= width || y >= height)
            return;

        int idx = y * width + x;

        int center = input[idx];

        int a = (center >>> 24) & 0xFF;

        int r = 0;
        int g = 0;
        int b = 0;

        // środek
        r += 5 * ((center >>> 16) & 0xFF);
        g += 5 * ((center >>> 8)  & 0xFF);
        b += 5 * ( center         & 0xFF);

        // współrzędne mirror
        int xL = mirror(x - 1, width);
        int xR = mirror(x + 1, width);
        int yU = mirror(y - 1, height);
        int yD = mirror(y + 1, height);

        // góra
        int p = input[yU * width + x];
        r -= (p >>> 16) & 0xFF;
        g -= (p >>> 8)  & 0xFF;
        b -=  p         & 0xFF;

        // dół
        p = input[yD * width + x];
        r -= (p >>> 16) & 0xFF;
        g -= (p >>> 8)  & 0xFF;
        b -=  p         & 0xFF;

        // lewo
        p = input[y * width + xL];
        r -= (p >>> 16) & 0xFF;
        g -= (p >>> 8)  & 0xFF;
        b -=  p         & 0xFF;

        // prawo
        p = input[y * width + xR];
        r -= (p >>> 16) & 0xFF;
        g -= (p >>> 8)  & 0xFF;
        b -=  p         & 0xFF;

        r = clamp(r);
        g = clamp(g);
        b = clamp(b);



        int inR = (center >>> 16) & 0xFF;
        int inG = (center >>> 8)  & 0xFF;
        int inB =  center         & 0xFF;

        r = inR + (r - inR) * value / 100;
        g = inG + (g - inG) * value / 100;
        b = inB + (b - inB) * value / 100;

        r = clamp(r);
        g = clamp(g);
        b = clamp(b);

        output[idx] = (a << 24) | (r << 16) | (g << 8) | b;

    }
}

public class SharpnessAdjustment extends AdjustmentStrategy {
    //integer zmiany przyjmuje wartości od 0 do 100 jako procent zmiany
    //dodaj więc skalowanie w sumacyjnych funkcjach


    /**
     * funkcja aplikuje korektę na image o poziomie value
     * @param image obraz
     * @param value 0 - 100%
     */
    public void applyAdjustment(BufferedImage image, int value) {
        int width = image.getWidth();
        int height = image.getHeight();
        int[] pixels = image.getRGB(0, 0, width, height, null, 0, width);
        int [] dst = new int[width * height];

        SharpenARGBKernel kernel = new SharpenARGBKernel(pixels, dst, width, height, value);

        Range range = Range.create2D(width, height);
        kernel.execute(range);


        image.setRGB(0, 0, width, height, dst, 0, width);

        kernel.dispose();
    }
}











