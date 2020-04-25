package client;

import certificates.CertificateData;
import controller.Controller;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.List;

public class CredentialsPanel extends JPanel {

    private JComboBox<String> certificatesComboBox;
    private JTextField usernameTextField;
    private JTextField passwordTextField;
    private JButton saveButton;
    private GridBagConstraints c;

    private Controller controller;
    private List<CertificateData> certificatesData;
    private CertificateData currentCertificate;

    public CredentialsPanel() {
        initLayout();
        initComponents();
        initListeners();
        setCredentials();
    }

    private void initLayout() {
        Border outerBorder = BorderFactory.createEmptyBorder(8, 8, 8, 8);
        Border innerBorder = BorderFactory.createEmptyBorder();
        setBorder(BorderFactory.createCompoundBorder(outerBorder, innerBorder));
        setLayout(new GridBagLayout());
    }

    private void initComponents() {
        c = new GridBagConstraints();
        addCertificatesComboBox();
        addUsernameLabel();
        addUsernameTextField();
        addPasswordLabel();
        addPasswordTextField();
        addSaveButton();
    }

    private void addCertificatesComboBox() {
        certificatesComboBox = new JComboBox<>();
        certificatesComboBox.setPreferredSize(new Dimension(310, 26));
        c.gridx = 0;
        c.gridy = 0;
        add(certificatesComboBox, c);
    }

    private void addUsernameLabel() {
        JLabel usernameLabel = new JLabel();
        usernameLabel.setText("Username");
        c.gridx = 1;
        c.gridy = 0;
        c.insets = new Insets(0, 32, 0, 0);
        add(usernameLabel, c);
    }

    private void addUsernameTextField() {
        usernameTextField = new JTextField();
        usernameTextField.setMargin(new Insets(0, 2, 0, 0));
        usernameTextField.setPreferredSize(new Dimension(120, 26));
        c.gridx = 2;
        c.gridy = 0;
        c.insets = new Insets(0, 4, 0, 0);
        add(usernameTextField, c);
    }

    private void addPasswordLabel() {
        JLabel passwordLabel = new JLabel();
        passwordLabel.setText("Password");
        c.gridx = 3;
        c.gridy = 0;
        c.insets = new Insets(0, 16, 0, 0);
        add(passwordLabel, c);
    }

    private void addPasswordTextField() {
        passwordTextField = new JTextField();
        passwordTextField.setMargin(new Insets(0, 2, 0, 0));
        passwordTextField.setPreferredSize(new Dimension(120, 26));
        c.gridx = 4;
        c.gridy = 0;
        c.insets = new Insets(0, 4, 0, 0);
        add(passwordTextField, c);
    }

    private void addSaveButton() {
        saveButton = new JButton();
        saveButton.setEnabled(false);
        saveButton.setMargin(new Insets(2, 2, 2, 2));
        saveButton.setPreferredSize(new Dimension(98, 26));
        saveButton.setText("Save");
        c.anchor = GridBagConstraints.LINE_START;
        c.gridx = 5;
        c.gridy = 0;
        c.insets = new Insets(0, 8, 0, 0);
        add(saveButton, c);
    }

    private void initListeners() {
        certificatesComboBox.addActionListener(e -> certificatesComboBoxItemChanged());
        saveButton.addActionListener(e -> saveButtonClicked());
    }

    private void certificatesComboBoxItemChanged() {
        if (certificatesComboBox.getSelectedIndex() > -1) {
            setCredentials();
        }
    }

    private void saveButtonClicked() {
        if (currentCertificate != null) {
            certificatesData.remove(currentCertificate);
            currentCertificate.setUsername(usernameTextField.getText());
            currentCertificate.setPassword(passwordTextField.getText());
            certificatesData.add(currentCertificate);
        } else {
            String certificateName = (String) certificatesComboBox.getSelectedItem();
            CertificateData certificateData = new CertificateData(certificateName, usernameTextField.getText(), passwordTextField.getText());
            certificatesData.add(certificateData);
        }
        controller.saveCertificateData(certificatesData);
    }

    public String getCertificate() {
        if (certificatesComboBox.getSelectedIndex() == -1) {
            return null;
        }
        return (String) certificatesComboBox.getSelectedItem();
    }

    public String getUsername() {
        return usernameTextField.getText().trim();
    }

    public String getPassword() {
        return passwordTextField.getText().trim();
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void setCertificatesData(List<CertificateData> certificatesData) {
        this.certificatesData = certificatesData;
    }

    public void setCertificates(String[] certificates) {
        DefaultComboBoxModel<String> certificatesComboBoxModel = new DefaultComboBoxModel<>(certificates);
        certificatesComboBox.setModel(certificatesComboBoxModel);
        if (certificatesComboBoxModel.getSize() > 0) {
            certificatesComboBox.setSelectedIndex(0);
            saveButton.setEnabled(true);
            controller.enableConnectButton(true);
        }
    }

    public void setCredentials() {
        currentCertificate = null;
        if (certificatesComboBox.getModel().getSize() > 0) {
            usernameTextField.setText("");
            passwordTextField.setText("");
            for (CertificateData certificateData : certificatesData) {
                if (certificateData.getName().equals(certificatesComboBox.getSelectedItem())) {
                    currentCertificate = certificateData;
                    usernameTextField.setText(currentCertificate.getUsername());
                    passwordTextField.setText((currentCertificate.getPassword()));
                    break;
                }
            }
        }
    }

}

