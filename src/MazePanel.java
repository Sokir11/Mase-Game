import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MazePanel extends JPanel {
    private MazeModel mazeModel;
    private RenderConfig config;
    private List<Point> solutionPath;
    private int currentStepIndex = -1;

    private static final int CELL_SIZE = 20; // גודל קבוע לכל ריבוע: 20x20 פיקסלים

    public MazePanel(MazeModel mazeModel, RenderConfig config, List<Point> solutionPath) {
        this.mazeModel = mazeModel;
        this.config = config;
        this.solutionPath = solutionPath;
        setBackground(Color.WHITE);

        // קביעת הגודל המועדף של הפאנל כך שיתאים בדיוק לחישוב של 20 פיקסל לכל תא
        if (mazeModel != null) {
            int totalWidth = mazeModel.getWidth() * CELL_SIZE;
            int totalHeight = mazeModel.getHeight() * CELL_SIZE;
            setPreferredSize(new Dimension(totalWidth, totalHeight));
        }
    }

    public void setCurrentStepIndex(int index) {
        this.currentStepIndex = index;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (mazeModel == null) return;

        int cols = mazeModel.getWidth();
        int rows = mazeModel.getHeight();
        MazeModel.CellType[][] grid = mazeModel.getGrid();

        // ציור הקירות והמעברים בגודל מדויק של 20x20
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                if (grid[y][x] == MazeModel.CellType.WALL) {
                    g.setColor(parseColor(config.getWallColor(), Color.BLACK));
                } else {
                    g.setColor(Color.WHITE);
                }
                g.fillRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
            }
        }

        // ציור מסלול הפתרון באנימציה
        if (solutionPath != null && currentStepIndex >= 0) {
            g.setColor(parseColor(config.getPathColor(), Color.RED));
            int limit = Math.min(currentStepIndex, solutionPath.size());
            for (int i = 0; i < limit; i++) {
                Point p = solutionPath.get(i);
                // ציור מודגש בתוך הריבוע של ה-20x20
                g.fillRect((p.x * CELL_SIZE) + 4, (p.y * CELL_SIZE) + 4, CELL_SIZE - 8, CELL_SIZE - 8);
            }
        }

        // ציור רשת (Grid) אם מוגדר
        if (config.isDrawGrid()) {
            g.setColor(parseColor(config.getGridColor(), Color.LIGHT_GRAY));
            for (int x = 0; x <= cols; x++) {
                g.drawLine(x * CELL_SIZE, 0, x * CELL_SIZE, rows * CELL_SIZE);
            }
            for (int y = 0; y <= rows; y++) {
                g.drawLine(0, y * CELL_SIZE, cols * CELL_SIZE, y * CELL_SIZE);
            }
        }
    }

    private Color parseColor(String colorStr, Color defaultColor) {
        if (colorStr == null || colorStr.trim().isEmpty()) return defaultColor;
        try {
            if (colorStr.startsWith("#")) {
                return Color.decode(colorStr);
            }
            switch (colorStr.toLowerCase()) {
                case "black": return Color.BLACK;
                case "white": return Color.WHITE;
                case "red": return Color.RED;
                case "blue": return Color.BLUE;
                case "green": return Color.GREEN;
                case "yellow": return Color.YELLOW;
                case "gray": return Color.GRAY;
                case "orange": return Color.ORANGE;
                case "pink": return Color.PINK;
                case "cyan": return Color.CYAN;
                case "magenta": return Color.MAGENTA;
            }
        } catch (Exception ignored) {}
        return defaultColor;
    }
}