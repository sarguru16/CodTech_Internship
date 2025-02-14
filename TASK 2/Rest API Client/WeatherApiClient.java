import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

public class WeatherApiClient {
    private static final String API_KEY = "YOUR_API_KEY"; // Replace with your OpenWeatherMap API key
    private static final String CITY = "London"; // Change city as needed
    private static final String API_URL = "https://api.openweathermap.org/data/2.5/weather?q=" + CITY + "&appid="
            + API_KEY + "&units=metric";

    public static void main(String[] args) {
        try {
            String response = sendGetRequest(API_URL);
            parseAndDisplayWeather(response);
        } catch (Exception e) {
            System.err.println("Error fetching weather data: " + e.getMessage());
        }
    }

    private static String sendGetRequest(String apiUrl) throws Exception {
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        if (responseCode != 200) {
            throw new RuntimeException("HTTP GET Request Failed with Error Code: " + responseCode);
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();
        return response.toString();
    }

    private static void parseAndDisplayWeather(String response) {
        JSONObject jsonResponse = new JSONObject(response);
        String cityName = jsonResponse.getString("name");
        JSONObject main = jsonResponse.getJSONObject("main");
        double temperature = main.getDouble("temp");
        int humidity = main.getInt("humidity");

        System.out.println("Weather Data for " + cityName + ":");
        System.out.println("Temperature: " + temperature + "°C");
        System.out.println("Humidity: " + humidity + "%");
    }
}
