package ImageEditor;

import ImageEditor.Filters.FilterType;

import java.util.ArrayList;

public class ImageState {

    //Adjustments state
    // Do accessowania elementow uzywac enuma AdjustmentType
    // TODO: Uzyc mapy? <AdjustmentType, int>
    public int[] adjustmentsUsed = {
            0,  // exposureValue
            0,  // brightnessValue
            0,  // contrastValue
            0,  // illuminationValue
            0,  // saturationValue
            0,  // temperatureValue
            0,  // hueValue
            0,  // sharpnessValue
    };

    // Filters used
    public ArrayList<FilterType> filtersUsed;
}
