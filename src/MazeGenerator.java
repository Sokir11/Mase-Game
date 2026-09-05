import java.awt.Point;
import java.util.*;

public class MazeGenerator {

    public static MazeModel generateMaze(int width, int height) {
        // ודאי שהמימדים אי-זוגיים כדי שהמבנה של קירות ומעברים יסתדר בצורה מושלמת
        if (width % 2 == 0) width++;
        if (height % 2 == 0) height++;

        MazeModel.CellType[][] grid = new MazeModel.CellType[height][width];

        // שלב 1: אתחול הכל כקירות
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                grid[y][x] = MazeModel.CellType.WALL;
            }
        }

        // שלב 2: יצירת המבוך בעזרת אלגוריתם Backtracking
        Random random = new Random();
        Stack<Point> stack = new Stack<>();

        // נקודת התחלה (למשל [1, 1])
        int startX = 1;
        int startY = 1;
        grid[startY][startX] = MazeModel.CellType.PATH;
        stack.push(new Point(startX, startY));

        int[][] directions = {
                {0, -2}, // למעלה
                {0, 2},  // למטה
                {-2, 0}, // שמאלה
                {2, 0}   // ימינה
        };

        while (!stack.isEmpty()) {
            Point current = stack.peek();
            List<int[]> neighbors = new ArrayList<>();

            // בדיקת שכנים במרחק 2 צעדים
            for (int[] dir : directions) {
                int nx = current.x + dir[0];
                int ny = current.y + dir[1];

                if (nx > 0 && nx < width - 1 && ny > 0 && ny < height - 1) {
                    if (grid[ny][nx] == MazeModel.CellType.WALL) {
                        neighbors.add(new int[]{nx, ny, dir[0] / 2, dir[1] / 2});
                    }
                }
            }

            if (!neighbors.isEmpty()) {
                // בחירת שכן אקראי
                int[] neighbor = neighbors.get(random.nextInt(neighbors.size()));
                int nx = neighbor[0];
                int ny = neighbor[1];
                int dx = neighbor[2];
                int dy = neighbor[3];

                // שבירת הקיר ביניים והפיכת השכן למעבר
                grid[current.y + dy][current.x + dx] = MazeModel.CellType.PATH;
                grid[ny][nx] = MazeModel.CellType.PATH;

                stack.push(new Point(nx, ny));
            } else {
                stack.pop();
            }
        }

        // וידוא שהכניסה (למעלה) והיציאה (למטה) פתוחות לחלוטין
        grid[0][1] = MazeModel.CellType.PATH;
        grid[height - 1][width - 2] = MazeModel.CellType.PATH;

        return new MazeModel(grid);
    }
}