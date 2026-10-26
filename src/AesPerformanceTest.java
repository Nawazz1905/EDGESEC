
import encryption.AesEncryption;
import java.util.Arrays;

public class AesPerformanceTest {

    private static final int WARMUP_RUNS = 1000;
    private static final int MEASURED_RUNS = 5000;

    public static void main(String[] args) throws Exception {
        AesEncryption aes = new AesEncryption();

        String data =
                "Device: Edge-01, Temperature: 28.5C, Humidity: 65%, "
                + "Pressure: 1013 hPa, Battery: 87%, Location: Building A, "
                + "Status: Operational, Alert: None";

        for (int i = 0; i < WARMUP_RUNS; i++) {
            String encrypted = aes.encrypt(data);
            aes.decrypt(encrypted);
        }

        long[] encryptionTimes = new long[MEASURED_RUNS];
        long[] decryptionTimes = new long[MEASURED_RUNS];

        for (int i = 0; i < MEASURED_RUNS; i++) {
            long startEncrypt = System.nanoTime();
            String encrypted = aes.encrypt(data);
            long endEncrypt = System.nanoTime();

            long startDecrypt = System.nanoTime();
            String decrypted = aes.decrypt(encrypted);
            long endDecrypt = System.nanoTime();

            encryptionTimes[i] = endEncrypt - startEncrypt;
            decryptionTimes[i] = endDecrypt - startDecrypt;

            if (!data.equals(decrypted)) {
                throw new IllegalStateException(
                        "Integrity verification failed at run " + i);
            }
        }

        Arrays.sort(encryptionTimes);
        Arrays.sort(decryptionTimes);

        double avgEncrypt = averageMs(encryptionTimes);
        double avgDecrypt = averageMs(decryptionTimes);

        System.out.println("=== AES PERFORMANCE TEST ===");
        System.out.println("Warm-up runs: " + WARMUP_RUNS);
        System.out.println("Measured runs: " + MEASURED_RUNS);
        System.out.printf("Average encryption: %.6f ms%n", avgEncrypt);
        System.out.printf("Median encryption: %.6f ms%n",
                percentileMs(encryptionTimes, 50));
        System.out.printf("95th percentile encryption: %.6f ms%n",
                percentileMs(encryptionTimes, 95));
        System.out.printf("Average decryption: %.6f ms%n", avgDecrypt);
        System.out.printf("Median decryption: %.6f ms%n",
                percentileMs(decryptionTimes, 50));
        System.out.printf("95th percentile decryption: %.6f ms%n",
                percentileMs(decryptionTimes, 95));
        System.out.printf("Average combined time: %.6f ms%n",
                avgEncrypt + avgDecrypt);
        System.out.println("Integrity checks passed: " + MEASURED_RUNS);
    }

    private static double averageMs(long[] sortedTimes) {
        long total = 0;
        for (long time : sortedTimes) {
            total += time;
        }
        return total / (double) sortedTimes.length / 1_000_000.0;
    }

    private static double percentileMs(long[] sortedTimes, int percentile) {
        int index = (int) Math.ceil(
                percentile / 100.0 * sortedTimes.length) - 1;
        index = Math.max(0, Math.min(index, sortedTimes.length - 1));
        return sortedTimes[index] / 1_000_000.0;
    }
}
