
import encryption.AesEncryption;

public class AesTest {
    public static void main(String[] args) {
        try {
            AesEncryption aes = new AesEncryption();

            String original = "Hello from Edge Device 01!";
            String encrypted = aes.encrypt(original);
            String decrypted = aes.decrypt(encrypted);

            System.out.println("=== AES-GCM INTEGRITY TEST ===");
            System.out.println("Original Data: " + original);
            System.out.println("Encrypted Data: " + encrypted);
            System.out.println("Decrypted Data: " + decrypted);
            System.out.println("Integrity Check: "
                    + original.equals(decrypted));

            // Tamper with the encrypted payload.
            char replacement = encrypted.charAt(encrypted.length() - 1) == 'A'
                    ? 'B' : 'A';

            String tampered = encrypted.substring(0, encrypted.length() - 1)
                    + replacement;

            System.out.println("\n=== TAMPER DETECTION TEST ===");

            
boolean tamperingRejected = false;

try {
    aes.decrypt(tampered);
} catch (Exception expected) {
    tamperingRejected = true;
}

if (!tamperingRejected) {
    throw new IllegalStateException(
            "Security test failed: tampered data was accepted.");
}

System.out.println(
        "Tampered data rejected: authentication failed.");
System.out.println("Tamper detection test: PASSED");


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
