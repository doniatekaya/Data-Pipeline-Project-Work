package org.example;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.ProducerConfig;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

public class WeatherDataProducer {

    // Liste des villes à surveiller
    private static final String[] CITIES = {
        "Rome", "Paris", "Madrid", "Istanbul", "Sydney", "Tokyo",
        "New York", "Los Angeles", "Beijing", "Rio de Janeiro",
        "Cape Town", "Mumbai", "Berlin", "Seoul", "Singapore"
    };
    private static final String API_KEY = "9ad42e4331030fd4c17744170c84d008"; // Remplacez par votre clé API
    private static final String UNITS = "metric";
    private static final String TOPIC = "weather_data";

    public static void main(String[] args) {

        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");

        KafkaProducer<String, String> producer = new KafkaProducer<>(props);

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                for (String city : CITIES) {
                    try {
                        String data = fetchWeatherData(city);
                        if (data != null) {
                            producer.send(new ProducerRecord<>(TOPIC, city, data));
                            System.out.println("Sent data for city: " + city);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }, 0, 200); // Exécution toutes les 5 minutes
    }

    private static String fetchWeatherData(String city) throws Exception {
        String encodedCity = URLEncoder.encode(city, "UTF-8");
        String urlString = String.format(
                "https://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s&units=%s",
                encodedCity, API_KEY, UNITS);

        HttpURLConnection connection = (HttpURLConnection) new URL(urlString).openConnection();
        connection.setRequestMethod("GET");

        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            InputStreamReader reader = new InputStreamReader(connection.getInputStream());
            JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();
            double temperature = json.getAsJsonObject("main").get("temp").getAsDouble();
            double humidity = json.getAsJsonObject("main").get("humidity").getAsDouble();
            double pressure = json.getAsJsonObject("main").get("pressure").getAsDouble();
            double windSpeed = json.getAsJsonObject("wind").get("speed").getAsDouble();
            double clouds = json.getAsJsonObject("clouds").get("all").getAsDouble();

            // Récupérer la date/heure actuelle
            String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

            // Inclure Time dans les données
            return String.format("%s,%s,%.2f,%.2f,%.2f,%.2f,%.2f",
                    currentTime, city, temperature, humidity, pressure, windSpeed, clouds);
        } else {
            System.err.println("Failed to fetch data for city: " + city);
            return null;
        }
    }
}
