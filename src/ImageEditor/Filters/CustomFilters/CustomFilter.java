package ImageEditor.Filters.CustomFilters;

import ImageEditor.Adjustments.AdjustmentStrategy;
import ImageEditor.OperationTags.AdjustmentType;
import ImageEditor.Core.Utils;
import ImageEditor.Filters.FilterStrategy;
import ImageEditor.ImageState;

import java.awt.image.BufferedImage;
import java.util.EnumMap;

public class CustomFilter extends FilterStrategy {
    private EnumMap<AdjustmentType, Integer> presets;
    private final EnumMap<AdjustmentType, AdjustmentStrategy> adjustmentStrategies;


    public CustomFilter(ImageState state){
        adjustmentStrategies = new EnumMap<>(AdjustmentType.class);
        for (AdjustmentType type : AdjustmentType.values())
            adjustmentStrategies.put(type, Utils.adjustment_factory(type));

        for (AdjustmentType type : AdjustmentType.values())
            presets.put(type, state.getAdjustmentState().get(type));
    }


    public void applyFilter(BufferedImage image) {
        for (AdjustmentType type : AdjustmentType.values())
            adjustmentStrategies.get(type).applyAdjustment(image, presets.get(type));
    }




}
