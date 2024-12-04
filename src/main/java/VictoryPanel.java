/**
 * vitcory panel will show after the winner wins
 *
 * @author Shenaho Gong
 * @Version 2024-12-04
 *
 */

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;

public class VictoryPanel extends JPanel {
    private String winnerMessage;

    public VictoryPanel(String winnerMessage) {
        this.winnerMessage = winnerMessage;
        setLayout(new BorderLayout());
        setBackground(Color.BLACK);

        // Add celebratory message
        JLabel messageLabel = new JLabel(winnerMessage, SwingConstants.CENTER);
        messageLabel.setFont(new Font("Arial", Font.BOLD, 48));
        messageLabel.setForeground(Color.WHITE);
        add(messageLabel, BorderLayout.CENTER);

        // Start confetti animation
        ConfettiPanel confettiPanel = new ConfettiPanel();
        add(confettiPanel, BorderLayout.NORTH);

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                confettiPanel.repaint();
            }
        }, 0, 50);

        playVictorySound();
    }

    // Confetti animation panel
    static class ConfettiPanel extends JPanel {
        private int[] x = new int[100];
        private int[] y = new int[100];
        private Color[] colors = new Color[100];

        public ConfettiPanel() {
            setPreferredSize(new Dimension(800, 200));
            for (int i = 0; i < x.length; i++) {
                x[i] = (int) (Math.random() * 800);
                y[i] = (int) (Math.random() * 200);
                colors[i] = new Color((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255));
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            for (int i = 0; i < x.length; i++) {
                g.setColor(colors[i]);
                g.fillOval(x[i], y[i], 10, 10);
                y[i] += 5;
                if (y[i] > 200) {
                    y[i] = 0;
                }
            }
        }
    }

    private void playVictorySound() {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("winning.wav")) {
            if (inputStream == null) {
                System.out.println("Sound file not found in resources.");
                return;
            }
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(inputStream);
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
