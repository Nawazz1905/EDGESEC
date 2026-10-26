
package cloud;

import encryption.AesEncryption;

public class CloudReceiver {

    private final AesEncryption aes;

    public CloudReceiver(AesEncryption aes) {
        this.aes = aes;
    }

    public String receiveAndDecrypt(String encryptedData) throws Exception {
        return aes.decrypt(encryptedData);
    }
}
