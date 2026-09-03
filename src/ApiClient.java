import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.imageio.ImageIO;

public class ApiClient {
//    // כתובת מעודכנת לקבלת ההגדרות
//    private static final String CONFIG_URL = "https://shaitest-production-3066.up.railway.app/fm1/get-render-config";
//
//    // כתובת מעודכנת לשליפת תמונת המבוך (תוקן מ-Render ל-Railway)
//    private static final String MAZE_IMAGE_URL = "https://shaitest-production-3066.up.railway.app/fm1/get-maze-image";
//
//    public static String fetchConfigJson() {
//        try {
//            HttpURLConnection conn = (HttpURLConnection) new URL(CONFIG_URL).openConnection();
//            conn.setRequestMethod("GET");
//            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//            StringBuilder response = new StringBuilder();
//            String line;
//            while ((line = reader.readLine()) != null) {
//                response.append(line);
//            }
//            reader.close();
//            return response.toString();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    public static BufferedImage fetchMazeImage(int width, int height) {
//        try {
//            String urlStr = MAZE_IMAGE_URL + "?width=" + width + "&height=" + height;
//            System.out.println("Sending request to: " + urlStr);
//            HttpURLConnection conn = (HttpURLConnection) new URL(urlStr).openConnection();
//            conn.setRequestMethod("GET");
//
//            // בדיקת סטטוס התשובה כדי לוודא שהבקשה הצליחה
//            int responseCode = conn.getResponseCode();
//            System.out.println("Response status code: " + responseCode);
//
//            if (responseCode == 200) {
//                return ImageIO.read(conn.getInputStream());
//            } else {
//                System.err.println("Failed to fetch maze image. Server returned code: " + responseCode);
//                return null;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
}