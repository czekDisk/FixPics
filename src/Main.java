import ImageEditor.Filters.FilterType;
import ImageEditor.ImageEditor;

void main() throws IOException {
    ImageEditor imageEditor = new ImageEditor("pobierz.jpg");
    imageEditor.applyFilter(FilterType.GRAYSCALE);
    imageEditor.export("jpg", "pobierz1.jpg");
}