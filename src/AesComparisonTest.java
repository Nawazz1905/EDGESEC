
import encryption.AesEncryption;
import java.nio.charset.StandardCharsets;

public class AesComparisonTest {

    private static final int WARMUP_RUNS = 1000;
    private static final int MEASURED_RUNS = 5000;

    public static void main(String[] args) throws Exception {
        AesEncryption aes = new AesEncryption();

        String data =
                "Device: Edge-01, Temperature: 28.5C, Humidity: 65%";

        byte[] plainBytes = data.getBytes(StandardCharsets.UTF_8);

        String sampleEncrypted = aes.encrypt(data);
        byte[] encryptedBytes =
                sampleEncrypted.getBytes(StandardCharsets.UTF_8);

        long plainTotal = 0;
        long aesTotal = 0;

        for (int i = 0; i < WARMUP_RUNS; i++) {
            String plainResult = new String(
                    data.getBytes(StandardCharsets.UTF_8),
                    StandardCharsets.UTF_8);

            String encryptedResult = aes.decrypt(aes.encrypt(data));

            if (!data.equals(plainResult)
                    || !data.equals(encryptedResult)) {
                throw new IllegalStateException(
                        "Warm-up verification failed.");
            }
        }

        for (int i = 0; i < MEASURED_RUNS; i++) {
            long startPlain = System.nanoTime();

            String plainResult = new String(
                    data.getBytes(StandardCharsets.UTF_8),
                    StandardCharsets.UTF_8);

            long endPlain = System.nanoTime();

            long startAes = System.nanoTime();

            String encryptedResult = aes.decrypt(aes.encrypt(data));

            long endAes = System.nanoTime();

            plainTotal += endPlain - startPlain;
            aesTotal += endAes - startAes;

            if (!data.equals(plainResult)
                    || !data.equals(encryptedResult)) {
                throw new IllegalStateException(
                        "Verification failed at run " + i);
            }
        }

        double avgPlainMs =
                plainTotal / (double) MEASURED_RUNS / 1_000_000.0;

        double avgAesMs =
                aesTotal / (double) MEASURED_RUNS / 1_000_000.0;

        System.out.println("=== PLAINTEXT VS AES COMPARISON ===");
        System.out.println("Warm-up runs: " + WARMUP_RUNS);
        System.out.println("Measured runs: " + MEASURED_RUNS);
        System.out.println("Plaintext payload size: "
                + plainBytes.length + " bytes");
        System.out.println("Encrypted Base64 payload size: "
                + encryptedBytes.length + " bytes");

        System.out.printf("Average plaintext encode/decode: %.6f ms%n",
                avgPlainMs);
        System.out.printf("Average AES encrypt/decrypt: %.6f ms%n",
                avgAesMs);

        System.out.println("Verification checks passed: "
                + MEASURED_RUNS);
        System.out.println(
                "Note: Processing-time comparison only; "
                + "network latency is not measured.");
    }
}
