package EditorUi;

import Exceptions.NonExistentPresetException;
import Exceptions.PresetAlreadyExistsException;
import ImageEditor.Core.EdgeDetectionSafetyObserver;
import ImageEditor.Core.ExposureSafetyObserver;
import ImageEditor.OperationTags.AdjustmentType;
import ImageEditor.OperationTags.FilterType;
import ImageEditor.ImageEditor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumMap;
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
                case 5 -> presetsMenu();
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
        System.out.println("5. Presets");
        System.out.println("0. Exit");
        System.out.print("Choice: ");
    }

    private void presetsMenu() {
        if (!checkImageLoaded()) return;

        System.out.println("Presets options");
        System.out.println("1. Apply preset");
        System.out.println("2. Save current options as a preset");
        System.out.println("3. Remove preset");

        System.out.print("Choice (-1 to go back): ");
        int choice = readInt();

        switch (choice) {
            case 1 -> applyPresetMenu();
            case 2 -> savePresetMenu();
            case 3 -> deletePresetMenu();
            case -1 -> {}  // nic nie wykonuje -> funkcja sie konczy i wraca do menu glownego
            default -> System.out.println("Invalid option.");
        }
    }

    private void applyPresetMenu() {
        ArrayList<String> presets = new ArrayList<>(editor.getPresetNames());
        if (presets.isEmpty()) {
            System.out.println("No presets saved.");
            return;
        }

        System.out.println("Available Presets:");
        for (int i = 0; i < presets.size(); i++) {
            System.out.println((i + 1) + ". " + presets.get(i));
        }
        System.out.print("Choose preset (-1 to back): ");

        int choice = readInt();
        if (choice == 0) return;

        if (choice > 0 && choice <= presets.size()) {
            String selectedName = presets.get(choice - 1);
            try {
                editor.applyPreset(selectedName);
                System.out.println("Preset '" + selectedName + "' applied.");
            } catch (NonExistentPresetException e) {
                System.out.println("[Error]: " + e.getMessage());
            }
        } else {
            System.out.println("Invalid preset.");
        }
    }

    private void savePresetMenu() {
        System.out.print("Enter name for new preset: ");
        scanner.nextLine();
        String name = scanner.nextLine().trim();

        if (name.isEmpty()) {
            System.out.println("Name can't be an empty string'.");
            return;
        }

        try {
            editor.savePreset(name);
            System.out.println("Preset '" + name + "' saved.");
        } catch (PresetAlreadyExistsException e) {
            System.out.println("[Error]: " + e.getMessage());
        }
    }

    private void deletePresetMenu() {
        ArrayList<String> presets = new ArrayList<>(editor.getPresetNames());
        if (presets.isEmpty()) {
            System.out.println("No presets available to delete.");
            return;
        }

        for (int i = 0; i < presets.size(); i++) {
            System.out.println(i + ". " + presets.get(i));
        }
        System.out.print("Choose preset (-1 to back): ");

        int choice = readInt();
        if (choice == -1) return;

        if (choice > -1 && choice <= presets.size()) {
            String selectedName = presets.get(choice - 1);
            editor.removePreset(selectedName);
            System.out.println("Preset '" + selectedName + "' deleted.");
        } else {
            System.out.println("Invalid preset.");
        }
    }

    private void loadImage() {
        System.out.print("Enter image path: ");
        scanner.nextLine();
        String path = scanner.nextLine();

        try {
            editor = new ImageEditor(path);
            System.out.println("Image loaded successfully.");

            // obserwatorzy
            editor.registerObserver(new EdgeDetectionSafetyObserver(editor, false));
            editor.registerObserver(new ExposureSafetyObserver(editor, false));
        } catch (Exception e) {
            System.out.println("Failed to load image: " + e.getMessage());
            editor = null;
        }
    }

    private void filtersMenu() {
        if (!checkImageLoaded()) return;

        FilterType[] filters = FilterType.values();
        System.out.println("\n Filters ");

        FilterType curFilter = editor.getState().getFilter();

        for (int i = 0; i < filters.length; i++) {
            boolean active = curFilter == filters[i];
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

        if (selected != curFilter) {
            System.out.println("Filer " + curFilter + " changed to " + selected);
            editor.applyFilter(selected);
        }
        else {
            System.out.println("No filter changed.");
        }
    }

    private void adjustmentsMenu() {
        if (!checkImageLoaded()) return;

        AdjustmentType[] adjustments = AdjustmentType.values();
        EnumMap<AdjustmentType, Integer> curAdjustments = editor.getState().getAdjustmentState();
        System.out.println("\n Adjustments ");

        for (int i = 0; i < adjustments.length; i++) {
            System.out.printf("%d %s (current: %d)%n",
                    i, adjustments[i], curAdjustments.get(adjustments[i]));
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
