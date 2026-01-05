package ImageEditor.Filters.CustomFilters;

import ImageEditor.Adjustments.AdjustmentStrategy;
import ImageEditor.Core.Utils;
import ImageEditor.Filters.FilterStrategy;
import ImageEditor.ImageState;
import ImageEditor.OperationTags.AdjustmentType;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

import java.awt.image.BufferedImage;
import java.util.EnumMap;

@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class CustomFilter extends FilterStrategy {
    private EnumMap<AdjustmentType, Integer> presets;
    private transient final EnumMap<AdjustmentType, AdjustmentStrategy> adjustmentStrategies;


    public CustomFilter() {
        adjustmentStrategies = new EnumMap<>(AdjustmentType.class);
        for (AdjustmentType type : AdjustmentType.values()) {
            adjustmentStrategies.put(type, Utils.adjustment_factory(type));
        }

        presets = new EnumMap<>(AdjustmentType.class);
        // Możesz pozostawić puste lub inicjalizować defaultowo
        for (AdjustmentType type : AdjustmentType.values()) {
            presets.put(type, 0);
        }
    }

    public CustomFilter(ImageState state){
        adjustmentStrategies = new EnumMap<>(AdjustmentType.class);
        for (AdjustmentType type : AdjustmentType.values())
            adjustmentStrategies.put(type, Utils.adjustment_factory(type));

        presets = new EnumMap<>(AdjustmentType.class);
        for (AdjustmentType type : AdjustmentType.values())
            presets.put(type, state.getAdjustmentState().get(type));
    }


    public void applyFilter(BufferedImage image) {
        for (AdjustmentType type : AdjustmentType.values())
            adjustmentStrategies.get(type).applyAdjustment(image, presets.get(type));
    }




}
