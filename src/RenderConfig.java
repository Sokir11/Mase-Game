public class RenderConfig {
    private String wallColor;
    private String pathColor;
    private boolean drawGrid;
    private String gridColor;
    private int animationDelay;

    // Constructors, Getters, and Setters
    public RenderConfig() {}

    public RenderConfig(String wallColor, String pathColor, boolean drawGrid, String gridColor, int animationDelay) {
        this.wallColor = wallColor;
        this.pathColor = pathColor;
        this.drawGrid = drawGrid;
        this.gridColor = gridColor;
        this.animationDelay = animationDelay;
    }

    public String getWallColor() { return wallColor; }
    public void setWallColor(String wallColor) { this.wallColor = wallColor; }

    public String getPathColor() { return pathColor; }
    public void setPathColor(String pathColor) { this.pathColor = pathColor; }

    public boolean isDrawGrid() { return drawGrid; }
    public void setDrawGrid(boolean drawGrid) { this.drawGrid = drawGrid; }

    public String getGridColor() { return gridColor; }
    public void setGridColor(String gridColor) { this.gridColor = gridColor; }

    public int getAnimationDelay() { return animationDelay; }
    public void setAnimationDelay(int animationDelay) { this.animationDelay = animationDelay; }
}