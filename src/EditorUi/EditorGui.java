package EditorUi;

import ImageEditor.ImageEditor;
import ImageEditor.OperationTags.AdjustmentType;
import ImageEditor.OperationTags.FilterPresets;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.util.EnumMap;

public class EditorGui extends JFrame {
    private ImageEditor editor;
    private JLabel imageLabel;
    private EnumMap<AdjustmentType, JSlider> sliders;
    private JComboBox<FilterPresets> filterBox;
    private JButton undoBtn;
    private JButton redoBtn;
    private int renderScale = 50;
    private SwingWorker<BufferedImage, Void> currentWorker;

    public EditorGui() {
        sliders = new EnumMap<>(AdjustmentType.class);
        imageLabel = new JLabel("No image loaded", SwingConstants.CENTER);
        filterBox = new JComboBox<>(FilterPresets.values());
        undoBtn = new JButton("Undo");
        redoBtn = new JButton("Redo");
        initUI();
    }

    private void initUI() {
        setTitle("Image Editor");
        setSize(1000, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(imageLabel, BorderLayout.CENTER);

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton openBtn = new JButton("Open");
        JButton exportBtn = new JButton("Export");

        openBtn.addActionListener(e -> openFile());
        exportBtn.addActionListener(e -> exportFile());



        toolbar.add(openBtn);
        toolbar.add(exportBtn);


        JPanel top = new JPanel(new BorderLayout());
        top.add(toolbar, BorderLayout.CENTER);


        JPanel left = new JPanel(new BorderLayout());
        JPanel leftTop = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftTop.add(undoBtn);
        leftTop.add(redoBtn);
        left.add(leftTop, BorderLayout.NORTH);


        JPanel slidersPanel = new JPanel();
        slidersPanel.setLayout(new BoxLayout(slidersPanel, BoxLayout.Y_AXIS));

        int LABEL_WIDTH = 120;
        int SLIDER_WIDTH = 200;
        for (AdjustmentType type : AdjustmentType.values()) {
            JPanel p = new JPanel(new BorderLayout());
            JLabel l = new JLabel(type.name());
            l.setPreferredSize(new Dimension(LABEL_WIDTH, l.getPreferredSize().height));
            JSlider s = new JSlider(-100, 100, 0);
            s.setPaintTicks(true);
            s.setPaintLabels(true);
            s.setPreferredSize(new Dimension(SLIDER_WIDTH, s.getPreferredSize().height));
            s.addChangeListener(new AdjustmentChangeListener(type));
            sliders.put(type, s);
            p.add(l, BorderLayout.WEST);
            p.add(s, BorderLayout.CENTER);
            p.setBorder(BorderFactory.createEmptyBorder(4,4,4,4));
            slidersPanel.add(p);
        }


        JPanel filterPanel = new JPanel(new BorderLayout());
        filterPanel.add(new JLabel("Filter:"), BorderLayout.WEST);
        filterPanel.add(filterBox, BorderLayout.CENTER);

        JPanel leftCenter = new JPanel(new BorderLayout());
        JScrollPane scroll = new JScrollPane(slidersPanel);
        leftCenter.add(scroll, BorderLayout.CENTER);
        leftCenter.add(filterPanel, BorderLayout.SOUTH);

        left.add(leftCenter, BorderLayout.CENTER);



        add(top, BorderLayout.NORTH);

        add(left, BorderLayout.WEST);
        add(mainPanel, BorderLayout.CENTER);


        undoBtn.addActionListener(e -> doUndo());
        redoBtn.addActionListener(e -> doRedo());
        filterBox.addActionListener(e -> applySelectedFilter());




        setControlsEnabled(false);
        setVisible(true);
    }

    private void setControlsEnabled(boolean enabled) {
        undoBtn.setEnabled(enabled);
        redoBtn.setEnabled(enabled);
        filterBox.setEnabled(enabled);
        for (JSlider s : sliders.values()) s.setEnabled(enabled);
    }

    private void openFile() {
        JFileChooser chooser = new JFileChooser();
        int res = chooser.showOpenDialog(this);
        if (res == JFileChooser.APPROVE_OPTION) {
            String path = chooser.getSelectedFile().getAbsolutePath();
            try {
                editor = new ImageEditor(path);
                scheduleRender();
                setControlsEnabled(true);
            } catch (IOException | IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, "Cannot open image: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void scheduleRender() { // funkcja do renderowania obrazu na osobnym wątku

        if (currentWorker != null && !currentWorker.isDone()) { //musimy sprawdzic czy nie jest w trakcie innego zadania, dzieki temu nie bedzie renderów na raz
            currentWorker.cancel(true);
        }

        imageLabel.setText("Rendering...");
        imageLabel.setIcon(null);
        currentWorker = new SwingWorker<BufferedImage, Void>() {
            @Override
            protected BufferedImage doInBackground() throws Exception {
                return editor.getPreview(EditorGui.this.renderScale);
            }

            @Override
            protected void done() {
                if (isCancelled()) return;
                try {
                    BufferedImage img = get();
                    imageLabel.setIcon(new ImageIcon(img));
                    imageLabel.setText(null);
                } catch (Exception ex) {
                    imageLabel.setText("Error rendering preview");
                }
            }
        };
        currentWorker.execute();
    }

    private void doUndo() {
        if (editor == null) return;
        editor.undo();
        scheduleRender();
    }

    private void doRedo() {
        if (editor == null) return;
        editor.redo();
        scheduleRender();
    }

    private void applySelectedFilter() {
        if (editor == null) return;
        FilterPresets preset = (FilterPresets) filterBox.getSelectedItem();
        if (preset == null) return;
        editor.applyFilter(preset);
        scheduleRender();
    }

    private class AdjustmentChangeListener implements ChangeListener { //klasa pomocnicza do nasłuchiwania zmian suwaka
        private AdjustmentType type;

        AdjustmentChangeListener(AdjustmentType type) { this.type = type; }

        @Override
        public void stateChanged(ChangeEvent e) {
            if (editor == null) return;
            JSlider s = (JSlider) e.getSource();
            if (!s.getValueIsAdjusting()) {
                int val = s.getValue();
                editor.applyAdjustment(type, val);
                scheduleRender();
            }
        }
    }

    public ImageEditor getEditor() {
        return editor;
    }

    private void exportFile() {
        if (editor == null) {
            JOptionPane.showMessageDialog(this, "No image loaded to export", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Export image");
        chooser.setSelectedFile(new File("export.png"));
        chooser.addChoosableFileFilter(new FileNameExtensionFilter("PNG image", "png"));
        chooser.addChoosableFileFilter(new FileNameExtensionFilter("JPEG image", "jpg", "jpeg"));

        int res = chooser.showSaveDialog(this);
        if (res != JFileChooser.APPROVE_OPTION) return;

        File f = chooser.getSelectedFile();
        String path = f.getAbsolutePath();
        String lower = path.toLowerCase();
        String format;
        if (lower.endsWith(".jpg") || lower.endsWith(".jpeg")) format = "jpg";
        else if (lower.endsWith(".png")) format = "png";
        else { format = "png"; path = path + ".png"; }

        String Path = path;
        String Format = format;

        new SwingWorker<Void, Void>() { // tutaj tworzymy workera na biezaca bo export to jednorazowa akcja
            @Override
            protected Void doInBackground() throws Exception {
                editor.exportPreview(Format, Path, 100);
                return null;
            }

            @Override
            protected void done() {
                try {
                    get();
                    JOptionPane.showMessageDialog(EditorGui.this, "Exported: " + Path, "Export", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(EditorGui.this, "Export failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }.execute();
    }
}
