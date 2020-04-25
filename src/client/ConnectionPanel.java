package client;

import controller.Controller;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class ConnectionPanel extends JPanel {

    private JButton connectButton;
    private JButton disconnectButton;
    private JButton checkIPButton;
    private JButton speedTestButton;
    private GridBagConstraints c;

    private Controller controller;

    public ConnectionPanel() {
        initLayout();
        initComponents();
        initListeners();
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    private void initLayout() {
        Border outerBorder = BorderFactory.createEmptyBorder(0, 8, 8, 8);
        Border innerBorder = BorderFactory.createEmptyBorder();
        setBorder(BorderFactory.createCompoundBorder(outerBorder, innerBorder));
        setLayout(new GridBagLayout());
    }

    private void initComponents() {
        c = new GridBagConstraints();
        addConnectButton();
        addDisconnectButton();
        addCheckIPButton();
        addSpeedButton();
    }

    private void addConnectButton() {
        connectButton = new JButton();
        connectButton.setEnabled(false);
        connectButton.setMargin(new Insets(2, 2, 2, 2));
        connectButton.setPreferredSize(new Dimension(98, 26));
        connectButton.setText("Connect");
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(0, 0, 0, 8);
        add(connectButton, c);
    }

    private void addDisconnectButton() {
        disconnectButton = new JButton();
        disconnectButton.setEnabled(false);
        disconnectButton.setMargin(new Insets(2, 2, 2, 2));
        disconnectButton.setPreferredSize(new Dimension(98, 26));
        disconnectButton.setText("Disconnect");
        c.anchor = GridBagConstraints.LINE_START;
        c.gridx = 1;
        c.gridy = 0;
        add(disconnectButton, c);
    }

    private void addCheckIPButton() {
        checkIPButton = new JButton();
        checkIPButton.setMargin(new Insets(2, 2, 2, 2));
        checkIPButton.setPreferredSize(new Dimension(98, 26));
        checkIPButton.setText("Check IP");
        c.anchor = GridBagConstraints.LINE_START;
        c.gridx = 2;
        c.gridy = 0;
        add(checkIPButton, c);
    }

    private void addSpeedButton() {
        speedTestButton = new JButton();
        speedTestButton.setMargin(new Insets(2, 2, 2, 2));
        speedTestButton.setPreferredSize(new Dimension(98, 26));
        speedTestButton.setText("Speed");
        c.anchor = GridBagConstraints.LINE_START;
        c.gridx = 3;
        c.gridy = 0;
        c.weightx = 0.1;
        add(speedTestButton, c);
    }

    private void initListeners() {
        connectButton.addActionListener(e -> connect());
        disconnectButton.addActionListener(e -> disconnect());
        checkIPButton.addActionListener(e -> checkIP());
        speedTestButton.addActionListener(e -> speedTest());
    }

    private void connect() {
        controller.connect();
    }

    private void disconnect() {
        controller.disconnect();
    }

    private void checkIP() {
        controller.checkIP();
    }

    private void speedTest() {
        controller.speedTest();
    }

    public void enableConnectButton(boolean enable) {
        SwingUtilities.invokeLater(() -> {
            connectButton.setEnabled(enable);
        });
    }

    public void enableDisconnectButton(boolean enable) {
        SwingUtilities.invokeLater(() -> {
            disconnectButton.setEnabled(enable);
        });
    }

    public void enableCheckIPButton(boolean enable) {
        SwingUtilities.invokeLater(() -> {
            checkIPButton.setEnabled(enable);
        });
    }

    public void enableSpeedTestButton(boolean enable) {
        SwingUtilities.invokeLater(() -> {
            speedTestButton.setEnabled(enable);
        });
    }
}

