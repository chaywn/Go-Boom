import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MainFrame extends JFrame {
    private JButton playButton;

    public MainFrame() {
        initializeComponents();
        addComponentsToFrame();
        setStartPageProperties();
    }

    private void initializeComponents() {
        playButton = new JButton("Play");
        playButton.setFont(new Font("Georgia", Font.BOLD, 15));

        playButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Start Game
                Game game = new Game();
                game.startNewGame();

                GamePanel gamePanel = new GamePanel(MainFrame.this, game);
                getContentPane().removeAll();
                getContentPane().add(gamePanel);
                revalidate();
                repaint();

                game.update();
            }
        });
    }

    private void addComponentsToFrame() {
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Draw the poker card image as the background
                Image backgroundImage = Toolkit.getDefaultToolkit()
                        .getImage("images\\PokerBackground.jpg");
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };

        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBackground(new Color(39, 121, 39));

        // Create the title label
        JLabel titleLabel = new JLabel("Go Boom");
        titleLabel.setFont(new Font("Georgia", Font.BOLD, 60));
        titleLabel.setForeground(Color.BLACK);

        // Add the title label to the main panel
        GridBagConstraints gbcTitle = new GridBagConstraints();
        gbcTitle.gridx = 0;
        gbcTitle.gridy = 0;
        gbcTitle.insets = new Insets(150, 0, 50, 0); // Adjust padding if needed
        mainPanel.add(titleLabel, gbcTitle);

        // Add the start button to the main panel
        GridBagConstraints gbcButton = new GridBagConstraints();
        gbcButton.gridx = 0;
        gbcButton.gridy = 1;
        gbcButton.insets = new Insets(0, 0, 100, 0); // Adjust padding if needed
        mainPanel.add(playButton, gbcButton);

        getContentPane().add(mainPanel);
    }

    private void setStartPageProperties() {
        setTitle("Go Boom");
        setMinimumSize(new Dimension(1000, 650)); // Set the minimum size of the window
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window on the screen
        setVisible(true);
    }

    public static void main(String[] args) {
        System.out.println("Welcome to Go Boom!");
        System.out.println("Press play to begin :)");
        SwingUtilities.invokeLater(() -> new MainFrame());
        System.out.println();
    }
}
