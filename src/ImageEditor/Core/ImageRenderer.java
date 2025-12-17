package ImageEditor.Core;

import ImageEditor.Adjustments.AdjustmentType;

import java.awt.image.BufferedImage;

public class ImageRenderer {
//    private imageDag
    private BufferedImage imagePreview;

    private BufferedImage render(BufferedImage image) {
        BufferedImage copiedImage = Utils.deepCopyImage(image);  // nie zmieniamy oryginalu

        for (AdjustmentType type : AdjustmentType.values()) {
            if (state.getAdjustmentState().get(type) != 0)
                adjustmentStrategies[type.ordinal()].applyAdjustment(copiedImage, state.getAdjustmentState().get(type));
        }

        filter.applyFilter(copiedImage);

        return copiedImage;
    }
}
