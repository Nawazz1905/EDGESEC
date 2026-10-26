
import encryption.AesEncryption;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class SecurityDashboard extends JFrame {

    private final AesEncryption aes;
    private final SensorDataGenerator sensorGenerator =
        new SensorDataGenerator();
    private final JTextArea inputArea = new JTextArea(5, 30);
    private final JTextArea encryptedArea = new JTextArea(5, 30);
    private final JTextArea outputArea = new JTextArea(4, 30);
    private final JLabel statusLabel = new JLabel("SYSTEM READY");
    private final JLabel payloadLabel = new JLabel("Plaintext: 0 B | Ciphertext: 0 B");
    private final JTextArea simulationArea = new JTextArea(8, 30);
private final JLabel simulationStatus = new JLabel("SIMULATION NOT RUN");
private final JButton simulationButton =
        new JButton("Run iFogSim Simulation");
        
private final JLabel networkMetric =
        new JLabel("Network Usage: --");

private final JLabel edgeEnergyMetric =
        new JLabel("Edge Energy: --");

private final JLabel cloudEnergyMetric =
        new JLabel("Cloud Energy: --");

private final JLabel executionCostMetric =
        new JLabel("Cloud Execution Cost: --");


    private String lastCiphertext = "";
    private double encryptionTimeMs = 0.0;
private double decryptionTimeMs = 0.0;

private final JLabel encryptionMetric =
        new JLabel("Encryption: -- ms");
private final JLabel decryptionMetric =
        new JLabel("Decryption: -- ms");
private final JLabel overheadMetric =
        new JLabel("Payload overhead: --%");

    private static final Color BACKGROUND = new Color(11, 17, 32);
    private static final Color PANEL = new Color(21, 30, 49);
    private static final Color GREEN = new Color(65, 230, 174);
    private static final Color BLUE = new Color(92, 169, 255);
    private static final Color TEXT = new Color(235, 241, 255);
    private static final Color MUTED = new Color(151, 166, 193);

    public SecurityDashboard() throws Exception {
        aes = new AesEncryption();

        setTitle("EDGESEC | Secure Edge-to-Cloud Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1150, 950);
        setMinimumSize(new Dimension(850, 650));
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout(0, 18));
        root.setBackground(BACKGROUND);
        root.setBorder(new EmptyBorder(24, 28, 24, 28));
        setContentPane(root);

        root.add(createHeader(), BorderLayout.NORTH);

        JPanel content = new JPanel(new GridBagLayout());
content.setOpaque(false);

GridBagConstraints gbc = new GridBagConstraints();
gbc.gridx = 0;
gbc.fill = GridBagConstraints.BOTH;
gbc.weightx = 1.0;

gbc.gridy = 0;
gbc.weighty = 0.9;
content.add(createDataPanel(), gbc);
        gbc.gridy = 1;
gbc.weighty = 1.1;
gbc.insets = new Insets(18, 0, 0, 0);
content.add(createOutputPanel(), gbc);

        root.add(content, BorderLayout.CENTER);
        root.add(createFooter(), BorderLayout.SOUTH);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JPanel titles = new JPanel();
        titles.setOpaque(false);
        titles.setLayout(new BoxLayout(titles, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("EDGESEC");
        title.setForeground(GREEN);
        title.setFont(new Font("SansSerif", Font.BOLD, 30));

        JLabel subtitle = new JLabel(
                "SECURE EDGE-TO-CLOUD TRANSMISSION");
        subtitle.setForeground(MUTED);
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 12));

        titles.add(title);
        titles.add(Box.createVerticalStrut(5));
        titles.add(subtitle);

        JLabel badge = new JLabel("  AES-256-GCM  ");
        badge.setForeground(GREEN);
        badge.setOpaque(true);
        badge.setBackground(new Color(23, 57, 54));
        badge.setFont(new Font("SansSerif", Font.BOLD, 12));

        header.add(titles, BorderLayout.WEST);
        header.add(badge, BorderLayout.EAST);
        return header;
    }

    private JPanel createDataPanel() {
        JPanel panel = createPanel("01  /  SENSOR DATA & ENCRYPTION");

        inputArea.setText(
                "Device: Edge-01, Temperature: 28.5C, "
                + "Humidity: 65%, Status: Operational");
        styleTextArea(inputArea);

        encryptedArea.setEditable(false);
        styleTextArea(encryptedArea);

        JPanel fields = new JPanel(new GridLayout(1, 2, 16, 0));
        fields.setOpaque(false);
        fields.add(createField("PLAINTEXT INPUT", inputArea));
        fields.add(createField("ENCRYPTED PAYLOAD", encryptedArea));
        JButton generateButton =
        createButton("Generate Sensor Data", BLUE);

generateButton.addActionListener(e -> {
    inputArea.setText(sensorGenerator.generateReading());
    statusLabel.setText("NEW SENSOR READING GENERATED");
    statusLabel.setForeground(BLUE);
});

        JButton encryptButton = createButton("Encrypt Data", GREEN);
        JButton decryptButton = createButton("Decrypt Data", BLUE);
        JButton tamperButton = createButton("Test Tampering",
                new Color(255, 190, 90));

        encryptButton.addActionListener(e -> encryptData());
        decryptButton.addActionListener(e -> decryptData());
        tamperButton.addActionListener(e -> testTampering());

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        buttons.setOpaque(false);
        buttons.add(generateButton);
        buttons.add(encryptButton);
        buttons.add(decryptButton);
        buttons.add(tamperButton);


        panel.add(fields, BorderLayout.CENTER);
        panel.add(buttons, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createOutputPanel() {
        JPanel panel = createPanel("02  /  DECRYPTION & INTEGRITY");

        outputArea.setEditable(false);
        styleTextArea(outputArea);

        JPanel field = createField("DECRYPTED DATA / TEST RESULTS", outputArea);

        

JPanel metricsPanel = new JPanel(new GridLayout(1, 3, 12, 0));
metricsPanel.setOpaque(false);
metricsPanel.setBorder(new EmptyBorder(10, 0, 10, 0));

for (JLabel metric : new JLabel[]{
        encryptionMetric, decryptionMetric, overheadMetric}) {
    metric.setForeground(GREEN);
    metric.setFont(new Font("SansSerif", Font.BOLD, 12));
    metricsPanel.add(metric);
}


JPanel resultSection = new JPanel(new BorderLayout(0, 8));
resultSection.setOpaque(false);
resultSection.add(field, BorderLayout.CENTER);
resultSection.add(metricsPanel, BorderLayout.SOUTH);

panel.add(resultSection, BorderLayout.CENTER);


JPanel simulationPanel = new JPanel(new BorderLayout(0, 8));
simulationPanel.setOpaque(false);

simulationStatus.setForeground(BLUE);
simulationStatus.setFont(
        new Font("SansSerif", Font.BOLD, 12));

simulationArea.setEditable(false);
simulationArea.setText(
        "iFogSim results will appear here.\n"
        + "Encryption module: EDGE\n"
        + "Receiver module: CLOUD");
styleTextArea(simulationArea);

JScrollPane simulationScroll = new JScrollPane(simulationArea);
simulationScroll.setPreferredSize(new Dimension(400, 130));

simulationButton.setForeground(GREEN);
simulationButton.setBackground(new Color(31, 43, 64));
simulationButton.setFocusPainted(false);

JPanel simulationHeader = new JPanel(new BorderLayout());
simulationHeader.setOpaque(false);
simulationHeader.add(simulationStatus, BorderLayout.WEST);
simulationHeader.add(simulationButton, BorderLayout.EAST);

simulationPanel.add(simulationHeader, BorderLayout.NORTH);
simulationPanel.add(simulationScroll, BorderLayout.CENTER);
JPanel simulationMetrics = new JPanel(new GridLayout(2, 2, 12, 8));
simulationMetrics.setOpaque(false);
simulationMetrics.setBorder(new EmptyBorder(10, 0, 4, 0));

for (JLabel metric : new JLabel[]{
        networkMetric,
        edgeEnergyMetric,
        cloudEnergyMetric,
        executionCostMetric
}) {
    metric.setForeground(GREEN);
    metric.setFont(new Font("SansSerif", Font.BOLD, 12));
    simulationMetrics.add(metric);
}

simulationPanel.add(simulationMetrics, BorderLayout.SOUTH);

panel.add(simulationPanel, BorderLayout.SOUTH);
simulationButton.addActionListener(e -> runSimulation());
return panel;

    }

    private JPanel createFooter() {
        JPanel footer = new JPanel(new BorderLayout(10, 0));
        footer.setOpaque(false);

        statusLabel.setForeground(GREEN);
        statusLabel.setFont(new Font("SansSerif", Font.BOLD, 12));

        payloadLabel.setForeground(MUTED);
        payloadLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));

        footer.add(statusLabel, BorderLayout.WEST);
        footer.add(payloadLabel, BorderLayout.EAST);
        return footer;
    }

    private JPanel createPanel(String heading) {
        JPanel panel = new JPanel(new BorderLayout(0, 14));
        panel.setBackground(PANEL);
        panel.setBorder(new EmptyBorder(16, 18, 16, 18));

        JLabel label = new JLabel(heading);
        label.setForeground(TEXT);
        label.setFont(new Font("SansSerif", Font.BOLD, 13));

        panel.add(label, BorderLayout.NORTH);
        return panel;
    }

    private JPanel createField(String heading, JTextArea area) {
        JPanel field = new JPanel(new BorderLayout(0, 8));
        field.setOpaque(false);

        JLabel label = new JLabel(heading);
        label.setForeground(MUTED);
        label.setFont(new Font("SansSerif", Font.BOLD, 11));

        JScrollPane scroll = new JScrollPane(area);
        scroll.setBorder(BorderFactory.createLineBorder(
                new Color(45, 59, 82)));
        scroll.getViewport().setBackground(BACKGROUND);

        field.add(label, BorderLayout.NORTH);
        field.add(scroll, BorderLayout.CENTER);
        return field;
    }

    private void styleTextArea(JTextArea area) {
        area.setBackground(BACKGROUND);
        area.setForeground(TEXT);
        area.setCaretColor(GREEN);
        area.setFont(new Font("Monospaced", Font.PLAIN, 13));
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setBorder(new EmptyBorder(10, 10, 10, 10));
    }

    private JButton createButton(String text, Color accent) {
        JButton button = new JButton(text);
        button.setForeground(accent);
        button.setBackground(new Color(31, 43, 64));
        button.setFont(new Font("SansSerif", Font.BOLD, 12));
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(10, 15, 10, 15));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void encryptData() {
        try {
            String plaintext = inputArea.getText();

            if (plaintext.isBlank()) {
                showError("Enter some sensor data first.");
                return;
            }

            
long start = System.nanoTime();
lastCiphertext = aes.encrypt(plaintext);
encryptionTimeMs =
        (System.nanoTime() - start) / 1_000_000.0;
double elapsedMs = encryptionTimeMs;

encryptionMetric.setText(
        String.format("Encryption: %.4f ms", encryptionTimeMs));


            encryptedArea.setText(lastCiphertext);
            outputArea.setText(
                    "Encryption successful.\n"
                    + "Algorithm: AES-256-GCM\n"
                    + String.format("Encryption time: %.4f ms%n", elapsedMs)
                    + "Ciphertext authentication: GCM tag included.");

            int plainBytes = plaintext.getBytes(
                    StandardCharsets.UTF_8).length;
            int cipherBytes = Base64.getDecoder()
                    .decode(lastCiphertext).length;

            payloadLabel.setText(
                    "Plaintext: " + plainBytes
                    + " B | Binary encrypted payload: "
                    + cipherBytes + " B | Base64: "
                    + lastCiphertext.getBytes(
                            StandardCharsets.UTF_8).length + " B");
double overheadPercent =
        ((double) (lastCiphertext.getBytes(
                StandardCharsets.UTF_8).length - plainBytes)
                / plainBytes) * 100.0;

overheadMetric.setText(
        String.format("Payload overhead: %.2f%%", overheadPercent));

            statusLabel.setText("ENCRYPTION SUCCESSFUL");
            statusLabel.setForeground(GREEN);

        } catch (Exception ex) {
            showError("Encryption failed: " + ex.getMessage());
        }
    }

    private void decryptData() {
        if (lastCiphertext.isEmpty()) {
            showError("Encrypt data first.");
            return;
        }

        try {
            
long start = System.nanoTime();
String decrypted = aes.decrypt(lastCiphertext);
decryptionTimeMs =
        (System.nanoTime() - start) / 1_000_000.0;

double elapsedMs = decryptionTimeMs;

decryptionMetric.setText(
        String.format("Decryption: %.4f ms", decryptionTimeMs));


            outputArea.setText(
                    "Decryption successful.\n"
                    + "Recovered data:\n" + decrypted
                    + String.format("%n%nDecryption time: %.4f ms", elapsedMs));

            statusLabel.setText("INTEGRITY VERIFIED");
            statusLabel.setForeground(GREEN);

        } catch (Exception ex) {
            outputArea.setText("Decryption failed: " + ex.getMessage());
            statusLabel.setText("DECRYPTION FAILED");
            statusLabel.setForeground(Color.RED);
        }
    }

    private void testTampering() {
        if (lastCiphertext.isEmpty()) {
            showError("Encrypt data before testing tampering.");
            return;
        }

        try {
            byte[] bytes = Base64.getDecoder().decode(lastCiphertext);
            bytes[bytes.length - 1] ^= 1;

            String tampered = Base64.getEncoder().encodeToString(bytes);

            try {
                aes.decrypt(tampered);
                outputArea.setText(
                        "WARNING: Modified ciphertext was accepted.");
                statusLabel.setText("TAMPER TEST FAILED");
                statusLabel.setForeground(Color.RED);
            } catch (Exception expected) {
                outputArea.setText(
                        "Tampered ciphertext rejected.\n"
                        + "AES-GCM authentication verification failed.\n"
                        + "Integrity test: PASSED");
                statusLabel.setText("TAMPERING DETECTED");
                statusLabel.setForeground(GREEN);
            }

        } catch (Exception ex) {
            showError("Tamper test failed: " + ex.getMessage());
        }
    }

private void runSimulation() {
    simulationButton.setEnabled(false);
    simulationStatus.setText("SIMULATION RUNNING...");
    simulationStatus.setForeground(BLUE);
    simulationArea.setText("Starting iFogSim...\n");

    SwingWorker<String, String> worker = new SwingWorker<>() {
        @Override
        protected String doInBackground() throws Exception {
            String ifogDirectory =
                    "C:\\Users\\moham\\Downloads\\iFogSim-main\\iFogSim-main";

            File workingDirectory = new File(ifogDirectory);
            if (!workingDirectory.isDirectory()) {
                throw new IllegalStateException(
                        "iFogSim directory not found: " + ifogDirectory);
            }

            int payloadBytes;
            if (lastCiphertext.isEmpty()) {
                payloadBytes = 1000;
            } else {
                payloadBytes = Base64.getDecoder()
                        .decode(lastCiphertext).length;
            }

            String classpath =
                    "out\\production\\iFogSim2;jars\\*;"
                    + "jars\\commons-math3-3.5\\*";

            String javaExecutable = System.getProperty("java.home")
                    + File.separator + "bin"
                    + File.separator + "java.exe";

            ProcessBuilder builder = new ProcessBuilder(
                    javaExecutable,
                    "-cp", classpath,
                    "org.fog.test.perfeval.SecureEdgeCloudSimulation",
                    Integer.toString(payloadBytes));

            builder.directory(workingDirectory);
            builder.redirectErrorStream(true);

            Process process = builder.start();

            StringBuilder log = new StringBuilder();
            try (java.io.BufferedReader reader =
                    new java.io.BufferedReader(
                            new java.io.InputStreamReader(
                                    process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    log.append(line).append("\n");
                    publish(line);
                }
            }

            int exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new IllegalStateException(
                        "iFogSim exited with code " + exitCode
                        + "\n" + log);
            }

            return log.toString();
        }

        @Override
        protected void process(java.util.List<String> lines) {
            for (String line : lines) {
                simulationArea.append(line + "\n");
            }
        }

        @Override
        protected void done() {
            simulationButton.setEnabled(true);
            try {
                String log = get();
                networkMetric.setText(
    "Network Usage: " +
    extractMetric(log, "Total network usage =")
);

edgeEnergyMetric.setText(
    "Edge Energy: " +
    extractMetric(log, "edge : Energy Consumed =")
);

cloudEnergyMetric.setText(
    "Cloud Energy: " +
    extractMetric(log, "cloud : Energy Consumed =")
);

executionCostMetric.setText(
    "Cloud Execution Cost: " +
    extractMetric(log, "Cost of execution in cloud =")
);
                simulationStatus.setText("SIMULATION COMPLETED");
                simulationStatus.setForeground(GREEN);

                if (log.contains("Total network usage =")) {
    simulationArea.append(
            "\nSimulation results detected successfully.");
} else {
    simulationArea.append(
            "\nWarning: expected simulation metrics were not found.");
}
            } catch (Exception ex) {
                simulationStatus.setText("SIMULATION FAILED");
                simulationStatus.setForeground(Color.RED);
                simulationArea.append(
                        "\nERROR: " + ex.getMessage() + "\n");
            }
        }
    };

    worker.execute();
}
private String extractMetric(String log, String prefix) {
    for (String line : log.split("\\R")) {
        if (line.startsWith(prefix)) {
            return line.substring(prefix.length()).trim();
        }
    }
    return "--";
}
    private void showError(String message) {
        JOptionPane.showMessageDialog(
                this, message, "EDGESEC",
                JOptionPane.WARNING_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(
                        UIManager.getSystemLookAndFeelClassName());
                new SecurityDashboard().setVisible(true);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                        null, "Unable to start dashboard: "
                        + ex.getMessage());
            }
        });
    }
}
