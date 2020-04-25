package helpers;

import controller.Controller;
import data.Data;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.URL;

public class SpeedTest {

    private final Controller controller;

    public SpeedTest(Controller controller) {
        this.controller = controller;
    }

    private boolean stopTest;
    private long downloadedData;
    private long downloadStartTime;
    private final long maxDownloadTestTime = 30 * 1000;
    private final int timerInterval = 250;
    private Timer t;

    public void runTest() {
        controller.enableSpeedTestButton(false);
        controller.overwriteConsole("Running speed test...\n");
        stopTest = false;
        downloadedData = 0;
        Data data = Data.getInstance();
        BufferedInputStream bufferedInputStream = null;
        FileOutputStream fileOutputStream = null;
        DownloadTimer downloadTimer = new DownloadTimer();
        downloadTimer.start();
        try {
            bufferedInputStream = new BufferedInputStream(new URL(data.getSpeedTestDownloadFile()).openStream());
            fileOutputStream = new FileOutputStream(data.getSpeedTestSaveFile());
            byte[] dataBuffer = new byte[1024];
            int bytesRead = 0;
            while ((bytesRead = bufferedInputStream.read(dataBuffer, 0, 1024)) != -1) {
                downloadedData += bytesRead;
                fileOutputStream.write(dataBuffer, 0, bytesRead);
                if (stopTest) {
                    break;
                }
            }
        } catch (IOException ex) {
            System.out.println("runText() error (" + ex.getMessage() + ")");
        } finally {
            downloadTimer.stop();
            try {
                if (bufferedInputStream != null) {
                    bufferedInputStream.close();
                }
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                System.out.println("runTest()-finally error (" + e.getMessage() + ")");
            }
            if (stopTest) {
                controller.overwriteConsole("\nSpeed test terminated (time limit reached)\n");
            } else {
                controller.overwriteConsole("\nSpeed test finished\n");
            }
            controller.enableSpeedTestButton(true);
        }
    }

    class DownloadTimer implements ActionListener {

        public void start() {
            downloadStartTime = System.currentTimeMillis();
            t = new Timer(timerInterval, this);
            t.start();
        }

        public void stop() {
            timerTick();
            t.stop();
        }

        private void timerTick() {
            long timeNow = System.currentTimeMillis();
            long elapsedTime = timeNow - downloadStartTime;
            double downloadSpeed = (downloadedData / 1024.0 / 1024.0) / (elapsedTime / 1000.0);
            String fileSize = formatSize(downloadedData);
            String seconds = String.format("%.2f", elapsedTime / 1000.0);
            String speed = String.format("%.2f", downloadSpeed);
            controller.overwriteConsole(String.format("Downloaded %s bytes (%s) in %s seconds at %s MB/second", downloadedData, fileSize, seconds, speed));
            if (elapsedTime >= maxDownloadTestTime) {
                stopTest = true;
            }
        }

        private String formatSize(long size) {
            if (size < 1024) {
                return size + " B";
            }
            int zeros = (63 - Long.numberOfLeadingZeros(size)) / 10;
            return String.format("%.1f %sB", (double) size / (1L << (zeros * 10)), " KMGTPE".charAt(zeros));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            timerTick();
        }
    }

}


