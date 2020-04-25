package client;

import controller.Controller;
import data.Data;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Client extends JFrame {

    private Controller controller;
    private Data data;

    private CredentialsPanel credentialsPanel;
    private ConnectionPanel connectionPanel;
    private ConsolePanel consolePanel;
    private GridBagConstraints c;

    public Client() {
        initLayout();
        initComponents();
        initController();
        initListeners();
    }

    public void setController(Controller controller) {
        this.controller = controller;
        this.data = Data.getInstance();
    }

    private void initLayout() {
        setTitle("pi-ovpnClient (v0.3.1)");
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/icon.png")));
        setResizable(false);
        setLayout(new GridBagLayout());
    }

    private void initComponents() {
        c = new GridBagConstraints();
        addCredentialsPanel();
        addConnectionPanel();
        addConsolePanel();
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initController() {
        new Controller(new ClientComponents(this, credentialsPanel, connectionPanel, consolePanel));
    }

    private void addCredentialsPanel() {
        credentialsPanel = new CredentialsPanel();
        c.anchor = GridBagConstraints.LINE_START;
        c.gridx = 0;
        c.gridy = 0;
        add(credentialsPanel, c);
    }

    private void addConnectionPanel() {
        connectionPanel = new ConnectionPanel();
        c.gridx = 0;
        c.gridy = 1;
        add(connectionPanel, c);
    }

    private void addConsolePanel() {
        consolePanel = new ConsolePanel();
        c.gridx = 0;
        c.gridy = 2;
        add(consolePanel, c);
    }

    private void initListeners() {
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                handleWindowClosing();
            }
        });
    }

    private void handleWindowClosing() {
        if (data.isVpnConnected()) {
            controller.disconnect();
        }
        dispose();
        System.gc();
    }

}

