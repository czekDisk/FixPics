package ImageEditor.Filters;

import ImageEditor.Core.OperationType;

public enum FilterType implements OperationType {
    NOFILTER,
    BLUR,
    GRAYSCALE,
    INVERT,
    NOISE,
    PIXELBLUR,
    SEPIA,
}
