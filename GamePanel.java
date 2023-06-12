import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.border.EmptyBorder;

public class GamePanel extends JPanel {
    private JButton startButton;
    private Image backgroundImage;

    public GamePanel() {
        initializeComponents();
        addComponentsToPanel();
        setBackgroundImage();
    }

    private void initializeComponents() {
        startButton = new JButton("Start");
        startButton.setFont(new Font("Georgia", Font.BOLD, 15));

        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //startNewGame();
            }
        });
    }

    private void addComponentsToPanel() {
        setLayout(new BorderLayout());

        // Create the title label
        JLabel titleLabel = new JLabel("Game Begin");
        titleLabel.setFont(new Font("Georgia", Font.BOLD, 25));
        titleLabel.setForeground(Color.BLACK);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Create the panel for the cards with a grid layout
        JPanel cardsPanel = new JPanel(new GridLayout(4, 13));
        ((GridLayout) cardsPanel.getLayout()).setHgap(0); // Set horizontal gap to 0
        ((GridLayout) cardsPanel.getLayout()).setVgap(0); // Set vertical gap to 0

        // Manually define the order of the card images
        String[] suits = { "clubs", "diamonds", "hearts", "spades" };
        String[] ranks = { "A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K" };

        // Add the card images to the panel
        for (String suit : suits) {
            for (String rank : ranks) {
                String cardPath = "C:\\Users\\user\\Downloads\\Playing Cards\\Playing Cards\\PNG-cards-1.3\\" + rank
                        + "_of_" + suit + ".png";
                ImageIcon cardIcon = new ImageIcon(cardPath);
                Image scaledCardImage = cardIcon.getImage().getScaledInstance(50, 80, Image.SCALE_SMOOTH);
                ImageIcon scaledCardIcon = new ImageIcon(scaledCardImage);
                JLabel cardLabel = new JLabel(scaledCardIcon);
                cardsPanel.add(cardLabel);
            }
        }

        // Create the start button panel
        JPanel startButtonPanel = new JPanel();
        startButtonPanel.add(startButton);

        // Add components to the panel
        add(titleLabel, BorderLayout.NORTH);
        add(cardsPanel, BorderLayout.CENTER);
        add(startButtonPanel, BorderLayout.SOUTH);

        // Set empty border around the panel for padding
        int padding = 100; // Adjust the padding size as needed
        setBorder(new EmptyBorder(padding, padding, padding, padding));
    }

    private void setBackgroundImage() {
        // Load the background image
        ImageIcon backgroundImageIcon = new ImageIcon("C:\\Users\\user\\Downloads\\Green Background.jpg");
        backgroundImage = backgroundImageIcon.getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw the background image
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
