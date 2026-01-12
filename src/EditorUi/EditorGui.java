package EditorUi;

import ImageEditor.ImageEditor;
import ImageEditor.ImageState;
import ImageEditor.OperationTags.AdjustmentType;
import ImageEditor.OperationTags.FilterType;
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
    private JComboBox<FilterType> filterBox;
    private JButton undoBtn;
    private JButton redoBtn;
    private int renderScale = 50;
    private SwingWorker<BufferedImage, Void> currentWorker;

    private JPanel mainPanel;
    private JPanel toolbar;
    private JPanel left;
    private JPanel slidersPanel;
    private JPanel filterPanel;
    private JLabel filterLabel;
    private JToggleButton darkToggle;
    private boolean darkTheme = false;

    public EditorGui() {
        sliders = new EnumMap<>(AdjustmentType.class);
        imageLabel = new JLabel("No image loaded", SwingConstants.CENTER);
        filterBox = new JComboBox<>(FilterType.values());
        filterBox.setBackground(Color.WHITE);
        filterBox.setForeground(Color.BLACK);
        undoBtn = new RoundedButton("Undo");
        redoBtn = new RoundedButton("Redo");
        initUI();
    }

    private void initUI() {
        setTitle("Image Editor");
        setSize(1000, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(imageLabel, BorderLayout.CENTER);

        toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        RoundedButton openBtn = new RoundedButton("Open");
        RoundedButton exportBtn = new RoundedButton("Export");

        openBtn.addActionListener(e -> openFile());
        exportBtn.addActionListener(e -> exportFile());

        styleToolbarButton(openBtn);
        styleToolbarButton(exportBtn);
        styleToolbarButton(undoBtn);
        styleToolbarButton(redoBtn);

        toolbar.add(openBtn);
        toolbar.add(exportBtn);
        toolbar.add(undoBtn);
        toolbar.add(redoBtn);
        darkToggle = new RoundedToggleButton("Dark");
        darkToggle.setFocusPainted(false);
        styleToolbarButton(darkToggle);
        darkToggle.addActionListener(e -> applyTheme(darkToggle.isSelected()));
        toolbar.add(Box.createHorizontalStrut(8));
        toolbar.add(darkToggle);

        JPanel top = new JPanel(new BorderLayout());
        top.add(toolbar, BorderLayout.CENTER);

        left = new JPanel(new BorderLayout());

        slidersPanel = new JPanel();
        slidersPanel.setLayout(new BoxLayout(slidersPanel, BoxLayout.Y_AXIS));

        int LABEL_WIDTH = 120;
        int SLIDER_WIDTH = 200;
        for (AdjustmentType type : AdjustmentType.values()) {
            JPanel p = new JPanel(new BorderLayout());
            p.setBackground(Color.WHITE);

            JLabel l = new JLabel(type.name());
            l.setPreferredSize(new Dimension(LABEL_WIDTH, l.getPreferredSize().height));
            l.setForeground(Color.BLACK);

            JSlider s = new JSlider(-100, 100, 0);
            s.setUI(new CustomSliderUI(s));
            s.setFocusable(false);
            s.setMajorTickSpacing(50);
            s.setMinorTickSpacing(25);
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


        filterPanel = new JPanel(new BorderLayout());
        filterLabel = new JLabel("Filter:");
        filterPanel.add(filterLabel, BorderLayout.WEST);
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
        applyTheme(darkTheme);
        setVisible(true);
    }

    private void setControlsEnabled(boolean enabled) {
        undoBtn.setEnabled(enabled);
        redoBtn.setEnabled(enabled);
        filterBox.setEnabled(enabled);
        for (JSlider s : sliders.values()) s.setEnabled(enabled);
    }

    private void styleToolbarButton(AbstractButton b) {
        Color bg = darkTheme ? Color.BLACK : Color.WHITE;
        Color fg = darkTheme ? Color.WHITE : Color.BLACK;
        b.setBackground(bg);
        b.setForeground(fg);
        b.setFocusPainted(false);
        b.setContentAreaFilled(false);
        b.setOpaque(false);
        b.setPreferredSize(new Dimension(100, 28));
    }

    private void applyTheme(boolean dark) {
        darkTheme = dark;
        Color bg = darkTheme ? Color.BLACK : Color.WHITE;
        Color fg = darkTheme ? Color.WHITE : Color.BLACK;
        getContentPane().setBackground(bg);
        if (mainPanel != null) mainPanel.setBackground(bg);
        darkToggle.setText(darkTheme ? "Light" : "Dark");
        imageLabel.setOpaque(true);
        imageLabel.setBackground(bg);
        imageLabel.setForeground(fg);
        if (toolbar != null) toolbar.setBackground(bg);
        if (left != null) left.setBackground(bg);
        if (slidersPanel != null) slidersPanel.setBackground(bg);
        if (filterPanel != null) filterPanel.setBackground(bg);
        if (filterLabel != null) filterLabel.setForeground(fg);
        if (filterBox != null) { filterBox.setBackground(bg); filterBox.setForeground(fg); }
        if (toolbar != null) {
            for (Component c : toolbar.getComponents()) {
                if (c instanceof AbstractButton) styleToolbarButton((AbstractButton) c);
            }
        }

        styleToolbarButton(undoBtn);
        styleToolbarButton(redoBtn);

        if (slidersPanel != null) {
            for (Component comp : slidersPanel.getComponents()) {
                if (comp instanceof JPanel) {
                    JPanel p = (JPanel) comp;
                    p.setBackground(bg);
                    if (p.getComponentCount() > 0 && p.getComponent(0) instanceof JLabel) {
                        JLabel lab = (JLabel) p.getComponent(0);
                        lab.setForeground(fg);
                        lab.setBackground(bg);
                    }
                }
            }
        }
        for (JSlider s : sliders.values()) {
            s.setBackground(bg);
            s.setForeground(fg);
            s.repaint();
        }
        CustomSliderUI.setColors(fg, fg);
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
                refreshControls();
            } catch (IOException | IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, "Cannot open image: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void scheduleRender() {

        if (currentWorker != null && !currentWorker.isDone()) {
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

    // Flag used to avoid reacting to programmatic control updates
    private boolean programmaticUpdate = false;

    private void doUndo() {
        if (editor == null) return;
        editor.undo();
        scheduleRender();
        refreshControls();
    }

    private void doRedo() {
        if (editor == null) return;
        editor.redo();
        scheduleRender();
        refreshControls();
    }

    private void applySelectedFilter() {
        if (programmaticUpdate) return; // ignore programmatic selections
        if (editor == null) return;
        FilterType preset = (FilterType) filterBox.getSelectedItem();
        if (preset == null) return;
        editor.applyFilter(preset);
        scheduleRender();
        refreshControls();
    }

    // Sync UI controls with editor state (doesn't trigger change listeners)
    private void refreshControls(){
        if (editor == null) return;
        // Update undo/redo availability
        undoBtn.setEnabled(editor.isUndoable());
        redoBtn.setEnabled(editor.isRedoable());

        programmaticUpdate = true;
        // Update sliders
        ImageState state = editor.getState();
        for (AdjustmentType type : AdjustmentType.values()){
            JSlider s = sliders.get(type);
            if (s != null){
                Integer val = state.getAdjustmentState().get(type);
                if (val != null) s.setValue(val);
            }
        }

        // Update filter combobox; if non-preset filter is active, show NOFILTER
        if (state.getFilter() instanceof FilterType){
            filterBox.setSelectedItem((FilterType) state.getFilter());
        } else {
            filterBox.setSelectedItem(FilterType.NOFILTER);
        }

        programmaticUpdate = false;
    }

    private class AdjustmentChangeListener implements ChangeListener {
        private AdjustmentType type;

        AdjustmentChangeListener(AdjustmentType type) { this.type = type; }

        @Override
        public void stateChanged(ChangeEvent e) {
            if (programmaticUpdate) return; // ignore when controls are being updated from state
            if (editor == null) return;
            JSlider s = (JSlider) e.getSource();
            s.repaint();  // niestety konieczne jest przerysowywanie przy zmianie, inaczej jest bug paska
            if (!s.getValueIsAdjusting()) {
                int val = s.getValue();
                editor.applyAdjustment(type, val);
                scheduleRender();
                refreshControls();
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

        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                editor.export(Format, Path);
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
