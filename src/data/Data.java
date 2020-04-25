package data;

import certificates.CertificateData;
import helpers.Json;
import main.Main;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Data {

    private final String appPath = Main.class.getProtectionDomain().getCodeSource().getLocation().getPath();
    private final String certificatesFolderPath = appPath.substring(0, appPath.lastIndexOf('/') + 1) + "vpnCertificates/";
    private final String dataFolderPath = certificatesFolderPath + "data/";
    private final String certificateDataFilePath = dataFolderPath + "certificates.data";
    private final String credentialsFilePath = dataFolderPath + "credentials.txt";
    private final String speedTestSaveFile = dataFolderPath + "speedTest.data";
    private final String authUserPass = "auth-user-pass " + credentialsFilePath;
    private final String speedTestDownloadFile = "https://download.microsoft.com/download/8/4/A/84A35BF1-DAFE-4AE8-82AF-AD2AE20B6B14/directx_Jun2010_redist.exe";
    private final String ipInfoURL = "https://api.ipify.org/";
    private final String connectCommand = "openvpn";
    private final String disconnectCommand = "killall -SIGINT openvpn";

    private Json json;
    private String[] certificates;
    private List<CertificateData> certificatesData;
    private boolean vpnConnected;

    private static final Data instance = new Data();

    private Data() {
        initData();
    }

    private void initData() {
        json = new Json();
        if (!Files.isDirectory(Paths.get(certificatesFolderPath))) {
            new File(certificatesFolderPath).mkdir();
            new File(dataFolderPath).mkdir();
        }
        File file = new File(certificateDataFilePath);
        if (!file.exists()) {
            createDummyCertificateFile();
        } else {
            readCertificateData();
        }
        importCertificates();
        if (!Files.isDirectory(Paths.get(dataFolderPath))) {
            new File(dataFolderPath).mkdir();
        }
    }

    public static Data getInstance() {
        return instance;
    }

    public boolean isVpnConnected() {
        return vpnConnected;
    }

    public void setVpnConnected(boolean vpnConnected) {
        this.vpnConnected = vpnConnected;
    }

    public String[] getCertificates() {
        return certificates;
    }

    public List<CertificateData> getCertificatesData() {
        return certificatesData;
    }

    public String getCertificatesFolderPath() {
        return certificatesFolderPath;
    }

    public String getConnectCommand() {
        return connectCommand;
    }

    public String getDisconnectCommand() {
        return disconnectCommand;
    }

    public String getIpInfoURL() {
        return ipInfoURL;
    }

    public String getSpeedTestDownloadFile() {
        return speedTestDownloadFile;
    }

    public String getSpeedTestSaveFile() {
        return speedTestSaveFile;
    }

    public void saveCertificateData(List<CertificateData> certificatesData) {
        this.certificatesData = certificatesData;
        writeCertificateData();
    }

    private void importCertificates() {
        File file = new File(certificatesFolderPath);
        certificates = file.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(".ovpn");
            }
        });
        assert certificates != null;
        if (certificates.length > 1) {
            Arrays.sort(certificates);
        }
    }

    @SuppressWarnings("unchecked")
    private void readCertificateData() {
        certificatesData = new ArrayList<>();
        if (json.readFromJsonFile(certificateDataFilePath, CertificateData.class)) {
            certificatesData = (List<CertificateData>) json.getJsonClassObject();
        } else {
            System.out.println("readCertificateData error (" + json.getErrorMessage() + ")");
        }
    }

    public void createCredentialsFile(List<String> credentials) {
        try {
            Files.write(Paths.get(credentialsFilePath), credentials);
        } catch (IOException ex) {
            System.out.println("createCredentials() error (" + ex.getMessage() + ")");
        }
    }

    public void addCredentialsFileLocationToCertificate(String certificateName) {
        Path filePath = Paths.get(certificatesFolderPath + certificateName);
        try {
            List<String> fileContent = new ArrayList<>(Files.readAllLines(filePath, StandardCharsets.UTF_8));
            for (int i = 0; i < fileContent.size(); i++) {
                if (fileContent.get(i).equals("auth-user-pass")) {
                    fileContent.set(i, authUserPass);
                    break;
                } else if (fileContent.get(i).equals(authUserPass)) {
                    break;
                } else if (fileContent.get(i).contains("auth-user-pass") && !fileContent.get(i).equals(authUserPass)) {
                    fileContent.set(i, authUserPass);
                    break;
                }
            }
            Files.write(filePath, fileContent, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            System.out.println("addCredentialsFileToCertificate() error (" + ex.getMessage() + ")");
        }
    }

    private void createDummyCertificateFile() {
        certificatesData = new ArrayList<>();
        CertificateData dummyCertificateData = new CertificateData("dummyCertificate.ovpn", "dummyUsername", "dummyPassword");
        certificatesData.add(dummyCertificateData);
        writeCertificateData();
    }

    private void writeCertificateData() {
        if (!json.writeJsonToFile(certificatesData, certificateDataFilePath)) {
            System.out.println("writeCertificateData error (" + json.getErrorMessage() + ")");
        }
    }

}

