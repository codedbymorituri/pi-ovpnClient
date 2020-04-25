package controller;

import certificates.CertificateData;
import certificates.CertificateSorter;
import client.*;
import data.Data;
import helpers.SpeedTest;
import helpers.Sudo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class Controller {

    private final Client client;
    private final CredentialsPanel credentialsPanel;
    private final ConnectionPanel connectionPanel;
    private final ConsolePanel consolePanel;
    private final Data data;
    private final String newLine = System.lineSeparator();

    public Controller(ClientComponents clientComponents) {
        this.client = clientComponents.getClient();
        this.credentialsPanel = clientComponents.getCredentialsPanel();
        this.connectionPanel = clientComponents.getConnectionPanel();
        this.consolePanel = clientComponents.getConsolePanel();
        this.data = Data.getInstance();
        initControls();
    }

    private void initControls() {
        client.setController(this);
        credentialsPanel.setController(this);
        credentialsPanel.setCertificatesData(data.getCertificatesData());
        credentialsPanel.setCertificates(data.getCertificates());
        connectionPanel.setController(this);
    }

    public void enableConnectButton(boolean enable) {
        connectionPanel.enableConnectButton(enable);
    }

    public void enableDisconnectButton(boolean enable) {
        connectionPanel.enableDisconnectButton(enable);
    }

    public void enableCheckIPButton(boolean enable) {
        connectionPanel.enableCheckIPButton(enable);
    }

    public void enableSpeedTestButton(boolean enable) {
        connectionPanel.enableSpeedTestButton(enable);
    }

    public void updateConsole(String text) {
        consolePanel.updateConsole(text);
    }

    public void overwriteConsole(String text) {
        consolePanel.overwriteConsole(text);
    }

    public void saveCertificateData(List<CertificateData> certificatesData) {
        if (certificatesData.size() > 1) {
            certificatesData.sort(new CertificateSorter());
        }
        data.saveCertificateData(certificatesData);
        updateConsole("Username and Password saved" + newLine);
    }

    public void connect() {
        String certificate = credentialsPanel.getCertificate();
        if (certificate != null) {
            if (checkCredentials()) {
                createCredentialsFile();
                addCredentialsFileLocationToCertificate();
                updateConsole(newLine);
                enableConnectButton(false);
                enableDisconnectButton(true);
                Thread tConnect = new Thread() {
                    public void run() {
                        tConnect(certificate);
                    }
                };
                tConnect.start();
            }
        }
    }

    public void disconnect() {
        Thread tDisconnect = new Thread() {
            public void run() {
                tDisconnect();
            }
        };
        tDisconnect.start();
    }

    public void checkIP() {
        Thread tCheckIP = new Thread() {
            public void run() {
                tCheckIP();
            }
        };
        tCheckIP.start();
    }

    public void speedTest() {
        Thread tSpeedTest = new Thread() {
            public void run() {
                tSpeedCheck();
            }
        };
        tSpeedTest.start();
    }

    private void tConnect(String certificate) {
        Sudo sudo = new Sudo(this);
        sudo.sudoCommand(data.getDisconnectCommand());
        String connection = String.format("%s %s%s", data.getConnectCommand(), data.getCertificatesFolderPath(), certificate);
        updateConsole(connection + newLine + newLine);
        sudo.sudoCommand(connection);
    }

    private void tDisconnect() {
        Sudo sudo = new Sudo(this);
        sudo.sudoCommand(data.getDisconnectCommand());
    }

    private void tCheckIP() {
        enableCheckIPButton(false);
        updateConsole(newLine + "Retrieving IP address... ");
        String remoteIPAddress = "";
        String line;
        try {
            URL remoteIP = new URL(data.getIpInfoURL());
            BufferedReader reader = new BufferedReader(new InputStreamReader(remoteIP.openStream()));
            while ((line = reader.readLine()) != null)
                remoteIPAddress = line;
            reader.close();
            updateConsole(remoteIPAddress);
        } catch (Exception ex) {
            updateConsole("failed (" + ex.getMessage() + ")");
        } finally {
            updateConsole(newLine);
            enableCheckIPButton(true);
        }
    }

    private void tSpeedCheck() {
        SpeedTest speedTest = new SpeedTest(this);
        speedTest.runTest();
    }

    private boolean checkCredentials() {
        if ("".equals(credentialsPanel.getUsername()) || "".equals(credentialsPanel.getPassword())) {
            updateConsole("Username and Password required" + newLine);
            return false;
        }
        return true;
    }

    private void createCredentialsFile() {
        List<String> credentials = Arrays.asList(credentialsPanel.getUsername(), credentialsPanel.getPassword());
        data.createCredentialsFile(credentials);
    }

    private void addCredentialsFileLocationToCertificate() {
        data.addCredentialsFileLocationToCertificate(credentialsPanel.getCertificate());
    }

}


