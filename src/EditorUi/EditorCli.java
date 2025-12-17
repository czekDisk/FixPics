package EditorUI;

import ImageEditor.Adjustments.AdjustmentType;
import ImageEditor.Filters.FilterType;

import java.util.ArrayList;
import java.util.Scanner;

public class ImageEditorCLI {

    private ImageEditor editor;
    private final Scanner scanner = new Scanner(System.in);

    public void start() {
        while (true) {
            printMainMenu();
            int choice = readInt();

            switch (choice) {
                case 1 -> loadImage();
                case 2 -> filtersMenu();
                case 3 -> adjustmentsMenu();
                case 4 -> renderPreview();
                case 0 -> {
                    System.out.println("Exit.");
                    return;
                }
                default -> System.out.println("Unknown option.");
            }
        }
    }


    private void printMainMenu() {
        System.out.println("\nIMAGE EDITOR (CLI version)");
        System.out.println("1. Load image");
        System.out.println("2. Filters");
        System.out.println("3. Adjustments");
        System.out.println("4. Render preview");
        System.out.println("0. Exit");
    }

    private void loadImage() {
        System.out.print("Enter image path: ");
        scanner.nextLine();
        String path = scanner.nextLine();

        try {
            editor = new ImageEditor(path);
            editor.state = new ImageState();
            editor.state.filtersUsed = new ArrayList<>();

            System.out.println("Image loaded successfully.");
        } catch (Exception e) {
            System.out.println("Failed to load image: " + e.getMessage());
            editor = null;
        }
    }

    private void filtersMenu() {
        if (!checkImageLoaded()) return;

        FilterType[] filters = FilterType.values();
        System.out.println("\n Filters ");

        for (int i = 0; i < filters.length; i++) {
            boolean active = editor.state.filtersUsed.contains(filters[i]);
            System.out.printf("%d %s %s%n",
                    i,
                    active ? "[X]" : "[ ]",
                    filters[i]);
        }

        System.out.println("Choose filter (-1 to back):");
        int choice = readInt();

        if (choice == -1) return;
        if (choice < 0 || choice >= filters.length) {
            System.out.println("Invalid filter.");
            return;
        }

        FilterType selected = filters[choice];

        if (editor.state.filtersUsed.contains(selected)) {
            editor.state.filtersUsed.remove(selected);
            System.out.println(selected + " removed.");
        } else {
            editor.applyFilter(selected);
            System.out.println(selected + " added.");
        }
    }

    private void adjustmentsMenu() {
        if (!checkImageLoaded()) return;

        AdjustmentType[] adjustments = AdjustmentType.values();
        System.out.println("\n Adjustments ");

        for (int i = 0; i < adjustments.length; i++) {
            int value = editor.state.adjustmentsUsed[i];
            System.out.printf("%d %s (current: %d)%n",
                    i, adjustments[i], value);
        }

        System.out.println("Choose adjustment (-1 to back):");
        int choice = readInt();

        if (choice == -1) return;
        if (choice < 0 || choice >= adjustments.length) {
            System.out.println("Invalid adjustment.");
            return;
        }

        System.out.print("Enter new value: ");
        int value = readInt();

        editor.applyAdjustment(adjustments[choice], value);
        System.out.println(adjustments[choice] + " set to " + value);
    }

    private void renderPreview() {
        if (!checkImageLoaded()) return;

        editor.renderPreview();
        System.out.println("Preview rendered.");
        System.out.println("Active filters: " + editor.state.filtersUsed);
    }


    private boolean checkImageLoaded() {
        if (editor == null) {
            System.out.println("No image loaded. Use option 1 first.");
            return false;
        }
        return true;
    }

    private int readInt() {
        while (!scanner.hasNextInt()) {
            scanner.next();
            System.out.print("Enter number: ");
        }
        return scanner.nextInt();
    }
}
