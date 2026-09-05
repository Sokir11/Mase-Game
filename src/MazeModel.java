public class MazeModel {
    public enum CellType { WALL, PATH }

    private CellType[][] grid;

    // קונסטרוקטור מקורי המבוסס על תמונה (אם תרצי לשמור גיבוי)
    public MazeModel(java.awt.image.BufferedImage image) {
        // ... הקוד הקודם שלך לפענוח תמונה ...
    }

    // קונסטרוקטור חדש המקבל מטריצה ישירות ממחולל המבוך
    public MazeModel(CellType[][] grid) {
        this.grid = grid;
    }

    public CellType[][] getGrid() {
        return grid;
    }

    public int getWidth() {
        return grid[0].length;
    }

    public int getHeight() {
        return grid.length;
    }
}