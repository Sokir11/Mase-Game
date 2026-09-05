import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ApiClient {
    private static final String BASE_URL = "https://shaitest-production-3066.up.railway.app/fm1";

    public RenderConfig getRenderConfig() throws Exception {
        String urlString = BASE_URL + "/get-render-config";
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("GET");
        connection.setRequestProperty("User-Agent", "Mozilla/5.0");
        connection.setRequestProperty("Accept", "application/json");

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            String json = response.toString();
            RenderConfig config = new RenderConfig();

            // שליפת הערכים עם גיבוי לברירת מחדל במקרה שהשדה ריק
            config.setWallColor(getOrDefault(extractValue(json, "wallColor"), "black"));
            config.setPathColor(getOrDefault(extractValue(json, "pathColor"), "red"));

            String drawGridStr = extractValue(json, "drawGrid");
            config.setDrawGrid(drawGridStr.isEmpty() ? true : Boolean.parseBoolean(drawGridStr));

            config.setGridColor(getOrDefault(extractValue(json, "gridColor"), "gray"));

            String delayStr = extractValue(json, "animationDelay");
            try {
                config.setAnimationDelay(delayStr.isEmpty() ? 50 : Integer.parseInt(delayStr));
            } catch (NumberFormatException e) {
                config.setAnimationDelay(50);
            }

            return config;
        } else {
            throw new RuntimeException("Failed : HTTP error code : " + responseCode);
        }
    }

    public BufferedImage getMazeImage(int width, int height) throws Exception {
        String urlString = BASE_URL + "/get-maze-image?width=" + width + "&height=" + height;
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("GET");
        connection.setRequestProperty("User-Agent", "Mozilla/5.0");

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            return ImageIO.read(connection.getInputStream());
        } else {
            throw new RuntimeException("Failed : HTTP error code : " + responseCode);
        }
    }

    private String extractValue(String json, String key) {
        try {
            String searchKey = "\"" + key + "\":";
            int startIndex = json.indexOf(searchKey);
            if (startIndex == -1) return "";

            startIndex += searchKey.length();
            int endIndex;

            // דילוג על רווחים לבנים במידת הצורך
            while (startIndex < json.length() && (json.charAt(startIndex) == ' ' || json.charAt(startIndex) == '\t')) {
                startIndex++;
            }

            if (startIndex < json.length() && json.charAt(startIndex) == '\"') {
                startIndex++;
                endIndex = json.indexOf("\"", startIndex);
            } else {
                endIndex = json.indexOf(",", startIndex);
                if (endIndex == -1) {
                    endIndex = json.indexOf("}", startIndex);
                }
            }

            if (startIndex == -1 || endIndex == -1 || startIndex > endIndex) return "";
            return json.substring(startIndex, endIndex).trim();
        } catch (Exception e) {
            return "";
        }
    }

    private String getOrDefault(String val, String defaultValue) {
        return (val == null || val.isEmpty()) ? defaultValue : val;
    }
}