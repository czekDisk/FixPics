import ImageEditor.Filters.FilterType;
import ImageEditor.ImageEditor;

void main() throws IOException {
    ImageEditor imageEditor = new ImageEditor("obrazek.jpg");
    imageEditor.applyFilter(FilterType.GRAYSCALE);
    imageEditor.export("jpg", "obrazek_grayscale.jpg");
}