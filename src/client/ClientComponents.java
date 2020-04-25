package client;

public class ClientComponents {

    private final Client client;
    private final CredentialsPanel credentialsPanel;
    private final ConnectionPanel connectionPanel;
    private final ConsolePanel consolePanel;

    public ClientComponents(Client client, CredentialsPanel credentialsPanel, ConnectionPanel connectionPanel, ConsolePanel consolePanel) {
        this.client = client;
        this.credentialsPanel = credentialsPanel;
        this.connectionPanel = connectionPanel;
        this.consolePanel = consolePanel;
    }

    public Client getClient() {
        return client;
    }

    public CredentialsPanel getCredentialsPanel() {
        return credentialsPanel;
    }

    public ConnectionPanel getConnectionPanel() {
        return connectionPanel;
    }

    public ConsolePanel getConsolePanel() {
        return consolePanel;
    }

}

