package client;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class ConsolePanel extends JPanel {

    private JTextArea consoleTextArea;
    private GridBagConstraints c;

    private int overwriteStart;
    private int overwriteEnd;

    public ConsolePanel() {
        initLayout();
        initComponents();
    }

    private void initLayout() {
        Border outerBorder = BorderFactory.createEmptyBorder(0, 8, 8, 8);
        Border innerBorder = BorderFactory.createEmptyBorder();
        setBorder(BorderFactory.createCompoundBorder(outerBorder, innerBorder));
        setLayout(new GridBagLayout());
    }

    private void initComponents() {
        c = new GridBagConstraints();
        addConsoleTextArea();
    }

    private void addConsoleTextArea() {
        consoleTextArea = new JTextArea();
        consoleTextArea.setDisabledTextColor(Color.DARK_GRAY);
        consoleTextArea.setEnabled(false);
        consoleTextArea.setMargin(new Insets(0, 2, 0, 0));
        c.gridx = 0;
        c.gridy = 0;
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setPreferredSize(new Dimension(1024, 512));
        scrollPane.setViewportView(consoleTextArea);
        add(scrollPane, c);
    }

    public void updateConsole(String text) {
        SwingUtilities.invokeLater(() -> {
            consoleTextArea.append(text);
            consoleTextArea.setCaretPosition(consoleTextArea.getDocument().getLength());
        });
    }

    public void overwriteConsole(String text) {
        SwingUtilities.invokeLater(() -> {
            if ("Running speed test...\n".equals(text)) {
                consoleTextArea.append(text);
                overwriteStart = consoleTextArea.getDocument().getLength();
            } else if (text.startsWith("\nSpeed test ")) {
                consoleTextArea.append(text);
                overwriteEnd = 0;
                consoleTextArea.setCaretPosition(consoleTextArea.getDocument().getLength());
            } else {
                if (overwriteEnd == 0) {
                    consoleTextArea.append(text);
                    overwriteEnd = consoleTextArea.getDocument().getLength();
                } else {
                    consoleTextArea.replaceRange(text, overwriteStart, overwriteEnd);
                    overwriteEnd = consoleTextArea.getDocument().getLength();
                }
            }
        });
    }

}

