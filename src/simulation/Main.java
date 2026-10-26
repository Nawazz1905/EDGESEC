package simulation;

import encryption.AesEncryption;
import edge.EdgeDevice;
import cloud.CloudReceiver;

public class Main {
    public static void main(String[] args) {
        try {
            AesEncryption aes = new AesEncryption();
            EdgeDevice edge = new EdgeDevice(aes);
            CloudReceiver cloud = new CloudReceiver(aes);

            String originalData = edge.generateSensorData();

            long encryptionStart = System.nanoTime();
            String encryptedData = edge.encryptData(originalData);
            long encryptionEnd = System.nanoTime();

            long decryptionStart = System.nanoTime();
            String receivedData = cloud.receiveAndDecrypt(encryptedData);
            long decryptionEnd = System.nanoTime();

            double encryptionMs =
                    (encryptionEnd - encryptionStart) / 1_000_000.0;
            double decryptionMs =
                    (decryptionEnd - decryptionStart) / 1_000_000.0;

            System.out.println("=== SECURE EDGE-TO-CLOUD TRANSMISSION ===");
            System.out.println("Original Sensor Data: " + originalData);
            System.out.println("Encrypted Data: " + encryptedData);
            System.out.println("Cloud Decrypted Data: " + receivedData);
            System.out.println("Integrity Check: "
                    + originalData.equals(receivedData));

            System.out.println("\n=== PERFORMANCE METRICS ===");
            System.out.printf("Encryption Time: %.6f ms%n", encryptionMs);
            System.out.printf("Decryption Time: %.6f ms%n", decryptionMs);
            System.out.printf("Total Processing Time: %.6f ms%n",
                    encryptionMs + decryptionMs);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
