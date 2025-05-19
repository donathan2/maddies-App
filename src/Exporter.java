
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.*;

public class Exporter {

    public static String chatGPT(String message) {
        String url = "https://api.openai.com/v1/chat/completions";
        String key = System.getenv("OPENAI_API_KEY"); // get API key from environment
        if (key == null || key.isEmpty()) {
            throw new RuntimeException("OPENAI_API_KEY environment variable not set");
        }
        String model = "gpt-3.5-turbo";

        try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Authorization", "Bearer " + key);
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("User-Agent", "MyJavaApp");

            String body = """
                {
                  "model": "%s",
                  "messages": [{"role": "user", "content": "%s"}]
                }
                """.formatted(model, escapeJson(message));

            con.setDoOutput(true);
            try (OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream())) {
                writer.write(body);
            }

            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getResponseCode() == 200 ? con.getInputStream() : con.getErrorStream()
            ));
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            System.out.println("Raw response: " + response.toString());
            return extractContentFromResponse(response.toString());

        } catch (IOException e) {
            return "Error: " + e.getMessage();
        }
    }

    private static String extractContentFromResponse(String response) {
        JSONObject json = new JSONObject(response);
        return json.getJSONArray("choices")
                .getJSONObject(0)
                .getJSONObject("message")
                .getString("content");
    }

    private static String escapeJson(String text) {
        return text.replace("\"", "\\\"");
    }
}