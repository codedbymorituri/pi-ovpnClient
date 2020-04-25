package helpers;

import controller.Controller;
import data.Data;

import javax.swing.*;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Arrays;

public class Sudo {

    private final String optionsError = "Options error:";
    private final String fatalError = "Exiting due to fatal error";
    private final String invalidLoginResponse = "AUTH_FAILED";
    private final String connectedResponse = "Initialization Sequence Completed";
    private final String disconnectedResponse = "process exiting";

    private final Controller controller;
    private final Data data;

    public Sudo(Controller controller) {
        this.controller = controller;
        this.data = Data.getInstance();
    }

    public void sudoCommand(String arguments) {
        OutputStreamWriter sudoOutput = null;
        InputStreamReader sudoInput = null;
        try {
            String pbString = "/usr/bin/sudo -S " + arguments + " 2>&1";
            ProcessBuilder pb = new ProcessBuilder("/bin/bash", "-c", pbString);
            Process p = pb.start();
            sudoOutput = new OutputStreamWriter(p.getOutputStream());
            sudoInput = new InputStreamReader(p.getInputStream());
            int bytes;
            char[] buffer = new char[1024];
            while ((bytes = sudoInput.read(buffer, 0, 1024)) != -1) {
                if (bytes == 0) {
                    continue;
                }
                String dataReceived = String.valueOf(buffer, 0, bytes);
                controller.updateConsole(dataReceived);
                if (dataReceived.contains("[sudo] password")) {
                    //use the line below to hard code your sudo password so that you are not prompted for it
                    //char password[] = new char[]{'p','a','s','s','w','o','r','d'};
                    //or
                    //use the line below to be prompted to supply your sudo password
                    char[] password = JOptionPane.showInputDialog("[sudo] password:").toCharArray();
                    sudoOutput.write(password);
                    sudoOutput.write('\n');
                    sudoOutput.flush();
                    Arrays.fill(password, '\0');
                } else if (dataReceived.contains(optionsError)) {
                    disconnected();
                } else if (dataReceived.contains(fatalError)) {
                    disconnected();
                } else if (dataReceived.contains(invalidLoginResponse)) {
                    controller.updateConsole("\nInvalid username or password\n");
                    disconnected();
                } else if (dataReceived.contains(connectedResponse)) {
                    data.setVpnConnected(true);
                    controller.checkIP();
                } else if (dataReceived.contains(disconnectedResponse)) {
                    controller.updateConsole("\nDisconnected");
                    disconnected();
                    controller.checkIP();
                }
            }
        } catch (Exception ex) {
            controller.updateConsole("\n sudoCommand() error (" + ex.getMessage() + ")");
        } finally {
            if (sudoInput != null) {
                try {
                    sudoInput.close();
                } catch (Exception e) {
                    //e.getMessage();
                }
            }
            if (sudoOutput != null) {
                try {
                    sudoOutput.close();
                } catch (Exception e) {
                    //e.getMessage();
                }
            }
        }
    }

    private void disconnected() {
        data.setVpnConnected(false);
        controller.enableConnectButton(true);
        controller.enableDisconnectButton(false);
    }
}


