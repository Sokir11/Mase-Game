import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ConfigFrame extends JFrame {
    private ApiClient apiClient;
    private RenderConfig currentConfig;

    // שדות ממשק להגדרות
    private JTextField wallColorField;
    private JTextField pathColorField;
    private JCheckBox drawGridCheckbox;
    private JTextField gridColorField;
    private JTextField animationDelayField;

    // שדות למימדי המבוך
    private JTextField widthField;
    private JTextField heightField;

    public ConfigFrame() {
        apiClient = new ApiClient();

        setTitle("Maze Configuration & Settings");
        setSize(400, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // פאנל טפסים להגדרות השרת
        JPanel configPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        configPanel.setBorder(BorderFactory.createTitledBorder("Render Configuration (API)"));

        wallColorField = new JTextField();
        pathColorField = new JTextField();
        drawGridCheckbox = new JCheckBox("Draw Grid");
        gridColorField = new JTextField();
        animationDelayField = new JTextField();

        configPanel.add(new JLabel("Wall Color:"));
        configPanel.add(wallColorField);
        configPanel.add(new JLabel("Path Color:"));
        configPanel.add(pathColorField);
        configPanel.add(new JLabel("Draw Grid:"));
        configPanel.add(drawGridCheckbox);
        configPanel.add(new JLabel("Grid Color:"));
        configPanel.add(gridColorField);
        configPanel.add(new JLabel("Animation Delay (ms):"));
        configPanel.add(animationDelayField);

        JButton refreshButton = new JButton("Refresh Config");
        refreshButton.addActionListener(e -> loadConfigFromApi());
        configPanel.add(refreshButton);

        add(configPanel, BorderLayout.CENTER);

        // פאנל תחתית לבחירת מימדי המבוך ומעבר
        JPanel bottomPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        bottomPanel.setBorder(BorderFactory.createTitledBorder("Maze Dimensions"));

        widthField = new JTextField("30");
        heightField = new JTextField("30");

        bottomPanel.add(new JLabel("Width:"));
        bottomPanel.add(widthField);
        bottomPanel.add(new JLabel("Height:"));
        bottomPanel.add(heightField);

        JButton getMazeButton = new JButton("GET MAZE");
        bottomPanel.add(getMazeButton);

        add(bottomPanel, BorderLayout.SOUTH);

        // טעינת נתונים ראשונית מה-API בפתיחת החלון
        loadConfigFromApi();

        // מאזין לכפתור GET MAZE - יצירת מבוך מקומי, פתרון ופתיחת חלון הצגת המבוך
        getMazeButton.addActionListener(e -> {
            try {
                int width = 30;
                int height = 30;

                // בדיקת תקינות רוחב (טווח 5 עד 100, אחרת איפוס ל-30)
                try {
                    int parsedWidth = Integer.parseInt(widthField.getText());
                    if (parsedWidth >= 5 && parsedWidth <= 100) {
                        width = parsedWidth;
                    } else {
                        widthField.setText("30");
                    }
                } catch (NumberFormatException ignored) {
                    widthField.setText("30");
                }

                // בדיקת תקינות גובה (טווח 5 עד 100, אחרת איפוס ל-30)
                try {
                    int parsedHeight = Integer.parseInt(heightField.getText());
                    if (parsedHeight >= 5 && parsedHeight <= 100) {
                        height = parsedHeight;
                    } else {
                        heightField.setText("30");
                    }
                } catch (NumberFormatException ignored) {
                    heightField.setText("30");
                }

                // עדכון אובייקט ההגדרות מהשדות במידה והמשתמש שינה ידנית
                updateConfigFromUI();

                // יצירת המבוך מקומית בעזרת המחולל החדש
                MazeModel mazeModel = MazeGenerator.generateMaze(width, height);

                // מציאת הפתרון למבוך באמצעות BFS
                List<Point> solutionPath = MazeSolver.solve(mazeModel);

                // פתיחת חלון הצגת המבוך והאנימציה עם כפתור הפתרון
                SwingUtilities.invokeLater(() -> {
                    new MazeWindow(mazeModel, currentConfig, solutionPath).setVisible(true);
                });

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Failed to generate or solve maze:\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    // פונקציה לטעינת ההגדרות דרך ה-API והצגתן בממשק
    private void loadConfigFromApi() {
        try {
            currentConfig = apiClient.getRenderConfig();
            wallColorField.setText(currentConfig.getWallColor());
            pathColorField.setText(currentConfig.getPathColor());
            drawGridCheckbox.setSelected(currentConfig.isDrawGrid());
            gridColorField.setText(currentConfig.getGridColor());
            animationDelayField.setText(String.valueOf(currentConfig.getAnimationDelay()));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to load config from API:\n" + e.getMessage(), "API Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // פונקציה לעדכון אובייקט ההגדרות מתוך שדות הממשק
    private void updateConfigFromUI() {
        if (currentConfig != null) {
            currentConfig.setWallColor(wallColorField.getText());
            currentConfig.setPathColor(pathColorField.getText());
            currentConfig.setDrawGrid(drawGridCheckbox.isSelected());
            currentConfig.setGridColor(gridColorField.getText());
            try {
                currentConfig.setAnimationDelay(Integer.parseInt(animationDelayField.getText()));
            } catch (NumberFormatException ignored) {}
        }
    }

    public RenderConfig getCurrentConfig() {
        updateConfigFromUI();
        return currentConfig;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ConfigFrame().setVisible(true));
    }
}