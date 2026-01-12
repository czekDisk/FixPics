package ImageEditor.Core;

import ImageEditor.Adjustments.AdjustmentStrategy;
import ImageEditor.OperationTags.AdjustmentType;
import ImageEditor.Adjustments.Implem.*;
import ImageEditor.Filters.FilterStrategy;
import ImageEditor.OperationTags.FilterPresets;
import ImageEditor.Filters.Implem.*;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Utils {
    final public static int MAX_COLOR_VAL = 255;
    final public static int MID_COLOR_VAL = 128;
    final public static int MIN_COLOR_VAL = 0;

    static public BufferedImage scaleImage(BufferedImage image, int scale){
        assert(scale > 0 && scale <=100);

        int newW = Math.max((int)(image.getWidth() * scale / 100f), 1);
        int newH = Math.max((int)(image.getHeight() * scale / 100f), 1);

        BufferedImage scaled = new BufferedImage(
                newW, newH, image.getType()
        );

        Graphics2D g2 = scaled.createGraphics();
        g2.setRenderingHint(
                RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR
        );
        g2.drawImage(image, 0, 0, newW, newH, null);
        g2.dispose();

        return scaled;


    }

    static public BufferedImage deepCopyImage(BufferedImage image) {
        BufferedImage copy = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        copy.setData(image.getData());

        return copy;
    }


    static public FilterStrategy filter_factory(FilterPresets type){
        return switch (type) {
            case NOFILTER -> null;//?
            case BLUR -> new BlurFilter();
            case GRAYSCALE -> new GrayscaleFilter();
            case INVERT -> new InvertFilter();
            case NOISE -> new NoiseFilter();
            case PIXELBLUR -> new PixelBlurFilter();
            case SEPIA -> new SepiaFilter();
            case SEGMENTATION -> new SegmentationFilter();
        };
    }

    static public AdjustmentStrategy adjustment_factory(AdjustmentType type){
        return switch (type){
            case  BRIGHTNESS -> new BrightnessAdjustment();
            case CONTRAST -> new ContrastAdjustment();
            case EXPOSURE -> new ExposureAdjustment();
            case HUE -> new HueAdjustment();
            case ILLUMINATION -> new IlluminationAdjustment();
            case SATURATION -> new SaturationAdjustment();
            case SHARPNESS -> new SharpnessAdjustment();
            case TEMPERATURE -> new TemperatureAdjustment();
        };
    }

    public static int clamp(int v) {
        if (v < MIN_COLOR_VAL) return MIN_COLOR_VAL;
        if (v > MAX_COLOR_VAL) return MAX_COLOR_VAL;
        return v;
    }

    public static int mirror(int x, int max) {
        if (x < 0) return -x - 1;
        if (x >= max) return 2 * max - x - 1;
        return x;
    }

    public static double calc_luminance(int r, int g, int b) {
        return 0.299 * r + 0.587 * g + 0.114 * b;
    }
}
