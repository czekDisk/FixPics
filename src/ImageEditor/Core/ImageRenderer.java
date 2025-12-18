package ImageEditor.Core;

import ImageEditor.Adjustments.AdjustmentStrategy;
import ImageEditor.Adjustments.AdjustmentType;
import ImageEditor.Filters.FilterStrategy;
import ImageEditor.Filters.FilterType;
import ImageEditor.ImageState;

import java.awt.image.BufferedImage;
import java.util.EnumMap;


public class ImageRenderer {
//    private Node imageDAGcurr;
    private BufferedImage imageOriginal;
//    private BufferedImage imagePreview;

    private final EnumMap<FilterType, FilterStrategy> filterStrategies;
    private final EnumMap<AdjustmentType, AdjustmentStrategy> adjustmentStrategies;

    public ImageRenderer(BufferedImage imageOriginal){
//        imageDAGcurr = null;
        this.imageOriginal = imageOriginal;
//        imagePreview = Utils.scaleDown(imageOriginal);

        adjustmentStrategies = new EnumMap<>(AdjustmentType.class);
        for(AdjustmentType type : AdjustmentType.values())
            adjustmentStrategies.put(type, Utils.adjustment_factory(type));

        filterStrategies = new EnumMap<>(FilterType.class);
        for(FilterType type : FilterType.values())
            filterStrategies.put(type, Utils.filter_factory(type));
    }


//
//    public void update(AdjustmentType type){
//        if (imageDAGcurr == null){
//            imageDAGcurr = new Node();
//
//
//        }
//        if (!imageDAG.is_dirty){
//
//        }
//    }





    public BufferedImage render( ImageState state) {
        BufferedImage copiedImage = Utils.deepCopyImage(imageOriginal);  // nie zmieniamy oryginalu

        for (AdjustmentType type : AdjustmentType.values()) {
            if (state.getAdjustmentState().get(type) != 0){
                adjustmentStrategies.get(type).applyAdjustment(copiedImage, state.getAdjustmentState().get(type));
            }

        }

        if (filterStrategies.get(state.getFilter()) != null){
            filterStrategies.get(state.getFilter()).applyFilter(copiedImage);
        }

        return copiedImage;
    }
}
