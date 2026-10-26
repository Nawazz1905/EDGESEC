
package edge;

import encryption.AesEncryption;

public class EdgeDevice {

    private final AesEncryption aes;

    public EdgeDevice(AesEncryption aes) {
        this.aes = aes;
    }

    public String generateSensorData() {
        return "Device: Edge-01, Temperature: 28.5C, Humidity: 65%";
    }

    public String encryptData(String sensorData) throws Exception {
        return aes.encrypt(sensorData);
    }
}
