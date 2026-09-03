import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

public class MazePanel extends JPanel {

    private int cols;
    private int rows;
    private String wallColorHex;
    private String pathColorHex;
    private boolean drawGrid;
    private String gridColorHex;

    // מטריקס למבוך (0 = נתיב פתוח, 1 = קיר)
    private int[][] mazeData;

    public MazePanel(int cols, int rows, String wallColorHex, String pathColorHex, boolean drawGrid, String gridColorHex) {
        // דאגה שמידות המסגרת יהיו אי-זוגיות כמפורט באלגוריתם כדי למנוע בעיות חישוב
        this.cols = (cols % 2 == 0) ? cols + 1 : cols;
        this.rows = (rows % 2 == 0) ? rows + 1 : rows;

        this.wallColorHex = wallColorHex;
        this.pathColorHex = pathColorHex;
        this.drawGrid = drawGrid;
        this.gridColorHex = gridColorHex;

        // יצירת מבוך אמיתי לפי אלגוריתם Recursive Backtracking
        generateRecursiveBacktrackingMaze();
    }

    private void generateRecursiveBacktrackingMaze() {
        mazeData = new int[rows][cols];

        // שלב 1: הכל מלא בקירות (1)
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                mazeData[r][c] = 1;
            }
        }

        Stack<Point> stack = new Stack<>();

        // שלב ההתחלה: מתחילים מתא (1, 1) והופכים אותו למעבר (0)
        int startR = 1;
        int startC = 1;
        mazeData[startR][startC] = 0;
        stack.push(new Point(startC, startR)); // Point מקבל (x=col, y=row)

        while (!stack.isEmpty()) {
            Point current = stack.peek();
            int r = current.y;
            int c = current.x;

            // בדיקת 4 השכנים במרחק 2 משבצות (למעלה, למטה, שמאלה, ימינה)
            java.util.List<int[]> neighbors = new ArrayList<>();

            int[] dRows = {-2, 2, 0, 0};
            int[] dCols = {0, 0, -2, 2};

            for (int i = 0; i < 4; i++) {
                int nr = r + dRows[i];
                int nc = c + dCols[i];

                // בדיקה שהשכן בתוך גבולות המבוך ושטרם ביקרו בו (הוא עדיין קיר 1)
                if (nr > 0 && nr < rows - 1 && nc > 0 && nc < cols - 1) {
                    if (mazeData[nr][nc] == 1) {
                        neighbors.add(new int[]{nr, nc, i}); // שומרים גם את כיוון הצעד
                    }
                }
            }

            if (!neighbors.isEmpty()) {
                // שבירת קירות: בחירת שכן אקראי מבין השכנים הלא מבוקרים
                Collections.shuffle(neighbors);
                int[] chosen = neighbors.get(0);
                int nr = chosen[0];
                int nc = chosen[1];
                int direction = chosen[2];

                // הפיכת התא החדש למעבר (0)
                mazeData[nr][nc] = 0;

                // שבירת הקיר שבינו לבין התא הנוכחי (התא האמצעי ביניהם)
                int wallR = r + (nr - r) / 2;
                int wallC = c + (nc - c) / 2;
                mazeData[wallR][wallC] = 0;

                // דחיפה למחסנית כדי להמשיך ממנו
                stack.push(new Point(nc, nr));
            } else {
                // חזרה אחורה (Backtracking) כשנתקעים
                stack.pop();
            }
        }

        // נקודת סיום מובטחת בפינה הימנית-תחתונה
        mazeData[rows - 2][cols - 2] = 0;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int panelWidth = getWidth();
        int panelHeight = getHeight();

        int cellWidth = panelWidth / cols;
        int cellHeight = panelHeight / rows;

        Color wallColor = parseColor(wallColorHex, Color.BLACK);
        Color pathColor = parseColor(pathColorHex, Color.WHITE);
        Color gridColor = parseColor(gridColorHex, Color.LIGHT_GRAY);

        // ציור המבוך על המסך
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (mazeData[r][c] == 1) {
                    g.setColor(wallColor);
                } else {
                    g.setColor(pathColor);
                }
                g.fillRect(c * cellWidth, r * cellHeight, cellWidth, cellHeight);

                if (drawGrid) {
                    g.setColor(gridColor);
                    g.drawRect(c * cellWidth, r * cellHeight, cellWidth, cellHeight);
                }
            }
        }
    }

    private Color parseColor(String colorStr, Color defaultColor) {
        if (colorStr == null || colorStr.isEmpty()) {
            return defaultColor;
        }
        try {
            if (colorStr.startsWith("#")) {
                return Color.decode(colorStr);
            } else {
                Field field = Color.class.getField(colorStr.toUpperCase());
                return (Color) field.get(null);
            }
        } catch (Exception e) {
            return defaultColor;
        }
    }
}