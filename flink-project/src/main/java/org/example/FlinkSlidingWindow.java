package org.example;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.windowing.WindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.SlidingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.util.Collector;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;

public class FlinkSlidingWindow {

    public static void main(String[] args) throws Exception {
        // Créez l'environnement Flink
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        // Configurez la source Kafka
        KafkaSource<String> kafkaSource = KafkaSource.<String>builder()
                .setBootstrapServers("localhost:9092")
                .setTopics("weather_data")
                .setGroupId("flink-weather-consumer")
                .setStartingOffsets(OffsetsInitializer.latest())
                .setValueOnlyDeserializer(new SimpleStringSchema())
                .build();

        // Lisez les données depuis Kafka
        DataStream<String> stream = env.fromSource(kafkaSource, WatermarkStrategy.noWatermarks(), "Kafka Source");

        // Mappez les données JSON pour en extraire la température et la ville
        DataStream<WeatherData> weatherDataStream = stream.map(new MapFunction<String, WeatherData>() {
            @Override
            public WeatherData map(String value) throws Exception {
                JsonObject json = JsonParser.parseString(value).getAsJsonObject();
                String time = json.get("Time").getAsString();
                String city = json.get("City").getAsString();
                double temperature = json.get("Temperature (°C)").getAsDouble();
                return new WeatherData(time, city, temperature);
            }
        });

        // Appliquez une fenêtre glissante pour calculer la moyenne des températures par ville
        DataStream<String> resultStream = weatherDataStream
                .keyBy(weather -> weather.city) // Groupement par ville
                .window(SlidingProcessingTimeWindows.of(Time.minutes(10), Time.minutes(5))) // Fenêtre glissante
                .apply(new WindowFunction<WeatherData, String, String, TimeWindow>() {
                    @Override
                    public void apply(String city, TimeWindow window, Iterable<WeatherData> input, Collector<String> out) {
                        int count = 0;
                        double sum = 0;
                        for (WeatherData weather : input) {
                            sum += weather.temperature;
                            count++;
                        }
                        double avgTemperature = sum / count;
                        out.collect(String.format("City: %s, Avg Temperature: %.2f°C", city, avgTemperature));
                    }
                });

        // Affichez les résultats
        resultStream.print();

        // Exécutez l'application Flink
        env.execute("Flink Sliding Window Example");
    }

    // Classe interne pour modéliser les données météo
    public static class WeatherData {
        public String time;
        public String city;
        public double temperature;

        public WeatherData(String time, String city, double temperature) {
            this.time = time;
            this.city = city;
            this.temperature = temperature;
        }
    }
}
