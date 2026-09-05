import java.awt.Point;
import java.util.*;

public class MazeSolver {

    public static List<Point> solve(MazeModel maze) {
        int height = maze.getHeight();
        int width = maze.getWidth();
        MazeModel.CellType[][] grid = maze.getGrid();

        // התאמה מדויקת למיקום הפתחים שהמחולל מייצר:
        // כניסה למעלה ב- (1, 0) ויציאה למטה ב- (width - 2, height - 1)
        Point start = new Point(1, 0);
        Point end = new Point(width - 2, height - 1);

        // בדיקה האם נקודת ההתחלה או הסיום הן קירות
        if (grid[start.y][start.x] == MazeModel.CellType.WALL ||
                grid[end.y][end.x] == MazeModel.CellType.WALL) {
            return Collections.emptyList(); // אין מסלול אפשרי
        }

        boolean[][] visited = new boolean[height][width];
        Point[][] parent = new Point[height][width];

        Queue<Point> queue = new LinkedList<>();
        queue.add(start);
        visited[start.y][start.x] = true;

        // 4 כיוונים: למעלה, למטה, ימינה, שמאלה
        int[] dx = {0, 0, 1, -1};
        int[] dy = {1, -1, 0, 0};
        boolean found = false;

        while (!queue.isEmpty()) {
            Point current = queue.poll();

            if (current.equals(end)) {
                found = true;
                break;
            }

            for (int i = 0; i < 4; i++) {
                int nx = current.x + dx[i];
                int ny = current.y + dy[i];

                // בדיקת גבולות המבוך, האם התא הוא מעבר פנוי, והאם טרם ביקרנו בו
                if (nx >= 0 && nx < width && ny >= 0 && ny < height) {
                    if (!visited[ny][nx] && grid[ny][nx] == MazeModel.CellType.PATH) {
                        visited[ny][nx] = true;
                        parent[ny][nx] = current;
                        queue.add(new Point(nx, ny));
                    }
                }
            }
        }

        if (!found) {
            return Collections.emptyList(); // לא נמצא מסלול
        }

        // שחזור המסלול מהסוף להתחלה בעזרת טבלת ההורים
        List<Point> path = new ArrayList<>();
        Point curr = end;
        while (curr != null) {
            path.add(curr);
            curr = parent[curr.y][curr.x];
        }

        // הפיכת הסדר כך שהרשימה תתחיל מנקודת ההתחלה ועד לסיום
        Collections.reverse(path);
        return path;
    }
}