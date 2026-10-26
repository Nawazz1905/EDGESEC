
import encryption.AesEncryption;
import java.nio.charset.StandardCharsets;
import java.io.File;
import java.util.Scanner;

public class SecureDemoLauncher {

    public static void main(String[] args) throws Exception {
        String sensorData =
        "Device: Edge-01, Temperature: 28.5C, Humidity: 65%, "
        + "Pressure: 1013 hPa, Battery: 87%, Location: Building A, "
        + "Status: Operational, Alert: None";

        AesEncryption aes = new AesEncryption();

        long encryptionStart = System.nanoTime();
String encryptedPayload = aes.encrypt(sensorData);
long encryptionEnd = System.nanoTime();

double encryptionTimeMs =
        (encryptionEnd - encryptionStart) / 1_000_000.0;
        System.out.printf("AES encryption time: %.6f ms%n",
        encryptionTimeMs);
        String decryptedData = aes.decrypt(encryptedPayload);

        int plaintextBytes =
                sensorData.getBytes(StandardCharsets.UTF_8).length;

        int encryptedPayloadBytes =
                encryptedPayload.getBytes(StandardCharsets.UTF_8).length;

        System.out.println("=== AES → iFogSim INTEGRATION ===");
        System.out.println("Original sensor data: " + sensorData);
        System.out.println("Plaintext size: " + plaintextBytes + " bytes");
        System.out.println("Encrypted Base64 payload size: "
                + encryptedPayloadBytes + " bytes");
        double overheadPercent =
        ((double) (encryptedPayloadBytes - plaintextBytes)
        / plaintextBytes) * 100.0;

System.out.printf("Payload size increase: %.2f%%%n",
        overheadPercent);
        System.out.println("AES decryption verified: "
                + sensorData.equals(decryptedData));

        if (!sensorData.equals(decryptedData)) {
            throw new IllegalStateException("AES verification failed.");
        }

        String javaExecutable = System.getProperty("java.home")
                + File.separator + "bin"
                + File.separator + "java.exe";

        String ifogDirectory =
                "C:\\Users\\moham\\Downloads\\iFogSim-main\\iFogSim-main";

        String classpath =
                "out\\production\\iFogSim2;jars\\*;jars\\commons-math3-3.5\\*";

        ProcessBuilder builder = new ProcessBuilder(
                javaExecutable,
                "-cp", classpath,
                "org.fog.test.perfeval.SecureEdgeCloudSimulation",
                Integer.toString(encryptedPayloadBytes)
        );

        builder.directory(new File(ifogDirectory));
        builder.inheritIO();

        System.out.println("\nPassing the actual encrypted payload size "
                + "to the iFogSim model...");

        Process process = builder.start();
        int exitCode = process.waitFor();

        if (exitCode != 0) {
            throw new IllegalStateException(
                    "iFogSim exited with code " + exitCode);
        }

        System.out.println("\nAES → iFogSim integration completed.");
    }
}
