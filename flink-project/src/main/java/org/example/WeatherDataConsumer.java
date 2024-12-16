package org.example;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class WeatherDataConsumer {

    private static final String TOPIC = "weather_data";
    private static final String OUTPUT_FILE = "weather_dataa.csv";

    public static void main(String[] args) {

        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "weather-data-consumer");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("auto.offset.reset", "latest"); // Lire uniquement les nouveaux messages

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Collections.singletonList(TOPIC));

        try (FileWriter writer = new FileWriter(OUTPUT_FILE)) {
            // Écrire l'en-tête une seule fois
            writer.write("Time,City,Temperature (°C),Humidity (%),Pressure (hPa),Wind Speed (m/s),Clouds (%)\n");

            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
                records.forEach(record -> {
                    try {
                        writer.write(record.value() + "\n"); // Écrire chaque enregistrement sur une nouvelle ligne
                        System.out.println("Consumed: " + record.value());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                writer.flush(); // S'assurer que les données sont bien écrites
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
