# EDGESEC — Secure Data Transmission Between Edge and Cloud Using AES Encryption

EDGESEC is a Java-based project that demonstrates secure sensor-data processing using AES-256-GCM encryption and models an edge–cloud computing environment using iFogSim2.

## Overview

Data generated at the edge can contain sensitive information. EDGESEC demonstrates how encryption and authenticated decryption can help protect data confidentiality and integrity, alongside a simulation of edge–cloud resource usage.

## Features

- **AES-256-GCM encryption:** Encrypts data using AES with Galois/Counter Mode.
- **Authenticated decryption:** Detects modified or tampered encrypted data.
- **Random initialization vector:** Uses a fresh 12-byte IV for encryption.
- **Integrity verification:** Tests whether decrypted data matches the original and whether tampering is rejected.
- **Sensor-data generation:** Generates sample temperature and humidity readings.
- **Performance testing:** Measures local encryption and decryption execution times.
- **Edge–cloud simulation:** Uses iFogSim2 to model edge–cloud computing behavior and resource metrics.
- **Java dashboard:** Provides a graphical interface for running demonstrations.

## Technology Stack

- Java
- AES-256-GCM
- Java Cryptography Architecture (JCA)
- iFogSim2
- CloudSim
- Swing (dashboard UI)
- PowerShell (demo launcher)

## Project Structure

```text
EDGESEC/
├── src/
│   ├── encryption/
│   │   └── AesEncryption.java
│   ├── edge/
│   │   └── EdgeDevice.java
│   ├── cloud/
│   │   └── CloudReceiver.java
│   ├── simulation/
│   │   └── Main.java
│   ├── AesTest.java
│   ├── AesPerformanceTest.java
│   ├── AesComparisonTest.java
│   ├── SensorDataGenerator.java
│   ├── SecureDemoLauncher.java
│   └── SecurityDashboard.java
├── RunDemo.ps1
└── README.md
```

## Requirements

- Java Development Kit (JDK)
- Windows PowerShell to run the provided PowerShell launcher
- iFogSim2 and its required dependencies for the simulation component

## Running the Project

1. Clone or download this repository.
2. Ensure the required JDK and dependencies are installed.
3. Open PowerShell in the project directory.
4. Run the provided demo launcher:

```powershell
.\RunDemo.ps1
```

If PowerShell blocks script execution, review your local execution policy and the script before changing any security settings. You can also compile and run individual Java tests using the appropriate classpath and dependencies.

## Security Design

The encryption component uses:

- AES with a 256-bit key
- GCM authenticated encryption
- A 12-byte initialization vector (IV)
- A 128-bit authentication tag
- Secure random key generation

The encryption output is Base64-encoded and includes the IV alongside the encrypted data and authentication tag. The AES key is generated in memory for the demonstration.

## Scope and Limitations

This is an academic demonstration and simulation project. AES encryption and decryption are performed locally in Java. iFogSim2 models the edge–cloud environment; the current implementation does not establish a real network connection or deploy encryption and decryption across separate physical edge and cloud devices. Sensor readings are generated sample data rather than readings from physical sensors.

Performance measurements reflect local Java execution and depend on the runtime environment.

## Future Improvements

- Integrate actual edge devices or sensor sources.
- Transmit encrypted payloads between separate edge and cloud services.
- Add persistent key management and secure key exchange.
- Evaluate security and performance under real network conditions.

## Author

**Mohammed Nawaz**

GitHub: [@Nawazz1905](https://github.com/Nawazz1905)
