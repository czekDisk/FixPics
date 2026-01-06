package EditorUi;

import ImageEditor.OperationTags.AdjustmentType;
import ImageEditor.OperationTags.FilterPresets;
import ImageEditor.ImageEditor;

import java.io.IOException;
import java.util.Scanner;

public class EditorCli {

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
                case 4 -> exportImage();
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
        System.out.println("4. Export image");
        System.out.println("0. Exit");
        System.out.print("Choice: ");
    }

    private void loadImage() {
        System.out.print("Enter image path: ");
        scanner.nextLine();
        String path = scanner.nextLine();

        try {
            editor = new ImageEditor(path);
            System.out.println("Image loaded successfully.");
        } catch (Exception e) {
            System.out.println("Failed to load image: " + e.getMessage());
            editor = null;
        }
    }

    private void filtersMenu() {
        if (!checkImageLoaded()) return;

        FilterPresets[] filters = FilterPresets.values();
        System.out.println("\n Filters ");

        for (int i = 0; i < filters.length; i++) {
            System.out.printf("%d %s%n",
                    i,
                    filters[i]);
        }

        System.out.println("Choose filter (-1 to back):");
        int choice = readInt();

        if (choice == -1) return;
        if (choice < 0 || choice >= filters.length) {
            System.out.println("Invalid filter.");
            return;
        }

        FilterPresets selected = filters[choice];
        editor.applyFilter(selected);
        System.out.println(selected + " applied.");
    }

    private void adjustmentsMenu() {
        if (!checkImageLoaded()) return;

        AdjustmentType[] adjustments = AdjustmentType.values();
        System.out.println("\n Adjustments ");

        for (int i = 0; i < adjustments.length; i++) {
            System.out.printf("%d %s%n",
                    i, adjustments[i]);
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

    private void exportImage() {
        if (!checkImageLoaded()) return;

        scanner.nextLine();
        System.out.print("Enter image path: ");
        String path = scanner.nextLine();
        System.out.print("Enter format (jpg/png/...): ");
        String format = scanner.nextLine();

        try {
            editor.export(format, path);
            System.out.println("Image exported successfully to " + path);
        } catch (IOException e) {
            System.out.println("Failed to export image.");
        }
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
