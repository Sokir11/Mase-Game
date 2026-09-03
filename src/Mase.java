import javax.swing.*;
import java.awt.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Mase extends JFrame {

    private static final String CONFIG_URL =
            "https://shaitest-production-3066.up.railway.app/fm1/get-render-config";

    private JLabel wallColorLabel;
    private JLabel pathColorLabel;
    private JLabel drawGridLabel;
    private JLabel gridColorLabel;
    private JLabel animationDelayLabel;

    private JTextField widthField;
    private JTextField heightField;

    private String wallCellColor;
    private String pathColor;
    private boolean drawGrid;
    private String gridColor;
    private int animationDelayMs;

    public Mase() {
        setTitle("Maze Game Configuration");
        setSize(500, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 2, 10, 10));

        panel.add(new JLabel("Render Configuration"));
        panel.add(new JLabel(""));

        panel.add(new JLabel("Wall Color:"));
        wallColorLabel = new JLabel("Loading...");
        panel.add(wallColorLabel);

        panel.add(new JLabel("Path Color:"));
        pathColorLabel = new JLabel("Loading...");
        panel.add(pathColorLabel);

        panel.add(new JLabel("Draw Grid:"));
        drawGridLabel = new JLabel("Loading...");
        panel.add(drawGridLabel);

        panel.add(new JLabel("Grid Color:"));
        gridColorLabel = new JLabel("Loading...");
        panel.add(gridColorLabel);

        panel.add(new JLabel("Animation Delay:"));
        animationDelayLabel = new JLabel("Loading...");
        panel.add(animationDelayLabel);

        JButton refreshButton = new JButton("Refresh Config");
        panel.add(refreshButton);
        panel.add(new JLabel(""));

        panel.add(new JLabel("Width (Cols):"));
        widthField = new JTextField("30");
        panel.add(widthField);

        panel.add(new JLabel("Height (Rows):"));
        heightField = new JTextField("30");
        panel.add(heightField);

        JButton getMazeButton = new JButton("GET MAZE");
        panel.add(getMazeButton);
        panel.add(new JLabel(""));

        refreshButton.addActionListener(e -> getRenderConfig());
        getMazeButton.addActionListener(e -> openMazeGameWindow());

        add(panel);
        getRenderConfig();
    }

    private void getRenderConfig() {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(CONFIG_URL))
                .GET()
                .build();

        try {
            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                String json = response.body();

                wallCellColor = getJsonString(json, "wallCellColor");
                pathColor = getJsonString(json, "pathColor");
                gridColor = getJsonString(json, "gridColor");
                drawGrid = getJsonBoolean(json, "drawGrid");
                animationDelayMs = getJsonInt(json, "animationDelayMs");

                wallColorLabel.setText(wallCellColor);
                pathColorLabel.setText(pathColor);
                drawGridLabel.setText(String.valueOf(drawGrid));
                gridColorLabel.setText(gridColor);
                animationDelayLabel.setText(animationDelayMs + " ms");

            } else {
                JOptionPane.showMessageDialog(this, "Failed to get render config. Status: " + response.statusCode());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error connecting to server:\n" + e.getMessage());
        }
    }

    private String getJsonString(String json, String key) {
        String search = "\"" + key + "\":\"";
        int start = json.indexOf(search);
        if (start == -1) return "";
        start += search.length();
        int end = json.indexOf("\"", start);
        return json.substring(start, end);
    }

    private boolean getJsonBoolean(String json, String key) {
        String search = "\"" + key + "\":";
        int start = json.indexOf(search);
        if (start == -1) return false;
        start += search.length();
        String value = json.substring(start).trim();
        return value.startsWith("true");
    }

    private int getJsonInt(String json, String key) {
        String search = "\"" + key + "\":";
        int start = json.indexOf(search);
        if (start == -1) return 0;
        start += search.length();
        String value = json.substring(start).trim();
        StringBuilder number = new StringBuilder();
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            if (Character.isDigit(c)) {
                number.append(c);
            } else {
                break;
            }
        }
        return Integer.parseInt(number.toString());
    }

    /**
     * פתיחת חלון המשחק החדש שבו יצוייר המבוך
     */
    private void openMazeGameWindow() {
        int width = getValidSize(widthField.getText());
        int height = getValidSize(heightField.getText());

        JFrame mazeFrame = new JFrame("Maze Game - Playing");
        mazeFrame.setSize(600, 600);
        mazeFrame.setLocationRelativeTo(this);

        // יצירת פאנל הציור והוספתו לחלון
        MazePanel mazePanel = new MazePanel(width, height, wallCellColor, pathColor, drawGrid, gridColor);
        mazeFrame.add(mazePanel);

        mazeFrame.setVisible(true);
    }

    private int getValidSize(String text) {
        try {
            int value = Integer.parseInt(text);
            if (value >= 5 && value <= 100) {
                return value;
            }
        } catch (NumberFormatException e) {
            // ברירת מחדל במקרה שגיאה
        }
        return 30;
    }
}