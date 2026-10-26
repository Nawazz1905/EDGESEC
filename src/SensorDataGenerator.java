import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class SensorDataGenerator {

    private final Random random = new Random();

    public String generateReading() {
        double temperature = 25.0 + random.nextDouble() * 10.0;
        double humidity = 40.0 + random.nextDouble() * 40.0;

        String timestamp = LocalTime.now().format(
                DateTimeFormatter.ofPattern("HH:mm:ss"));

        return String.format(
                "Device: Edge-01, Time: %s, Temperature: %.1f C, Humidity: %.1f%%, Status: Operational",
                timestamp, temperature, humidity);
    }
}