import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Iterator;
import java.awt.event.ActionEvent;
import javax.swing.border.EmptyBorder;

public class GamePanel extends JPanel {

    private Image backgroundImage;
    private MainFrame mainFrame;

    // Buttons
    private JButton restartButton;
    private JButton resetButton;
    private JButton loadButton;
    private JButton quitButton;

    // Labels
    private JLabel roundNumLabel;
    private JLabel trickNumLabel;
    private JLabel playerTurnLabel;
    private JLabel[] playerScoreLabels = new JLabel[Game.PLAYER_COUNT];
    private HashMap<Card, JLabel> cardToLabelMap = new HashMap<>();
    private HashMap<JLabel, Card> labelToCardMap = new HashMap<>();
    private JLabel mainDeckLabel;

    // Panels
    private JPanel[] playerPanels = new JPanel[Game.PLAYER_COUNT];

    // Path
    private String holeCardPath = "Go-Boom-main\\images\\cards\\hole_card.png";

    public GamePanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        initializeCards();
        initializeComponents();
        addComponentsToPanel();
        setBackgroundImage();
    }

    private JLabel createCardLabel(String path) {
        ImageIcon cardIcon = new ImageIcon(path);
        Image scaledCardImage = cardIcon.getImage().getScaledInstance(50, 80, Image.SCALE_SMOOTH);
        ImageIcon scaledCardIcon = new ImageIcon(scaledCardImage);
        JLabel cardLabel = new JLabel(scaledCardIcon);

        cardLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // If the main deck is clicked, draw a card
                if (e.getSource() == mainDeckLabel) {
                    Game.playerDrawCard();
                    Game.update();

                    refreshPanel();
                }
                // Else if a faced-up card is click, deal the card
                else if (labelToCardMap.containsKey(e.getSource())) {
                    boolean played = Game.playerDealCard(labelToCardMap.get(e.getSource()));

                    if (played) {
                        String message = Game.update();
                        refreshPanel();

                        if (Game.displayTrickEndGUI) {
                            popUpMessage(message, "Game Status");
                            Game.displayTrickEndGUI = false;
                        }

                        if (Game.displayRoundEndGUI) {
                            popUpMessage(message, "Game Status");
                            Game.displayRoundEndGUI = false;
                        }
                    }
                }
            }
        });

        return cardLabel;
    }

    private void initializeCards() {
        // Follow the same order of the suit and rank of the game cards so that they
        // have the same index
        String[] suits = { "clubs", "diamonds", "hearts", "spades" };
        String[] ranks = { "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A" };

        for (int s = 0; s < Card.SUITS.length; s++) {
            for (int r = 0; r < Card.RANKS.length; r++) {
                Card card = new Card(Card.SUITS[s], Card.RANKS[r]);
                String cardPath = "Go-Boom-main\\images\\cards\\" + ranks[r] + "_of_" + suits[s] + ".png";
                JLabel cardLabel = createCardLabel(cardPath);

                cardToLabelMap.put(card, cardLabel);
                labelToCardMap.put(cardLabel, card);
            }
        }

        mainDeckLabel = createCardLabel(holeCardPath);
    }

    private void refreshPanel() {
        // Update label text
        roundNumLabel.setText("Round #" + (Game.roundNum + 1));
        trickNumLabel.setText("Trick #" + (Game.trickNum + 1));
        ;
        playerTurnLabel.setText("Turn: Player" + (Game.playerTurn + 1));
        for (int i = 0; i < Game.PLAYER_COUNT; i++) {
            playerScoreLabels[i].setText("Player " + (i + 1) + " = " + Game.players[i].getScore());
        }

        // Rebuild panel
        removeAll();
        addComponentsToPanel();
        revalidate();
        repaint();
    }

    private void popUpMessage(String message, String title) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    private void initializeComponents() {
        roundNumLabel = new JLabel("Round #" + (Game.roundNum + 1));
        trickNumLabel = new JLabel("Trick #" + (Game.trickNum + 1));
        playerTurnLabel = new JLabel("Turn: Player" + (Game.playerTurn + 1));
        for (int i = 0; i < Game.PLAYER_COUNT; i++) {
            playerScoreLabels[i] = new JLabel("Player " + (i + 1) + " = " + Game.players[i].getScore());
        }

        restartButton = new JButton("Start New Game");
        restartButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Display message
                popUpMessage("A new game is initialized", "Game Status");
                System.out.println("*** A new game is initialized ***\n");

                // Start new game
                Game.startNewGame();
                Game.roundEnd = false;
                Game.update();

                refreshPanel();
            }
        });

        resetButton = new JButton("Reset Game");
        resetButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Display pop-up message
                popUpMessage("The game is resetted. All scores are set to 0", "Game Status");

                // Start new game
                Game.resetGame();
                Game.roundEnd = false;
                Game.update();

                refreshPanel();
            }
        });

        loadButton = new JButton("Load Game");
        loadButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Load saved file and display returned message
                popUpMessage(Game.loadGame(), "Load Status");

                Game.update();

                refreshPanel();
            }
        });

        quitButton = new JButton("Quit Game");
        quitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Quit Game and display returned message
                popUpMessage(Game.quitGame(), "Save Status");

                mainFrame.dispose();
            }
        });

    }

    private void addComponentsToPanel() {
        setLayout(new BorderLayout());

        // Create main Panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setOpaque(false);

        // Create a panel for holding the player panels
        JPanel playerPanelsContainer = new JPanel(new GridLayout(2, 1));
        playerPanelsContainer.setOpaque(false);

        // Add card labels into respective player's panel
        for (int i = 0; i < Game.PLAYER_COUNT; i++) {
            playerPanels[i] = new JPanel();
            playerPanels[i].setOpaque(false);

            if (i == Game.playerTurn) {
                Deck playerDeck = Game.players[i].getDeck();

                Iterator<Card> itr = playerDeck.iterator();
                while (itr.hasNext()) {
                    playerPanels[i].add(cardToLabelMap.get(itr.next()));
                }
            } else {
                int deckSize = Game.players[i].getDeck().getSize();
                for (int j = 0; j < deckSize; j++) {
                    playerPanels[i].add(createCardLabel(holeCardPath));
                }
            }

            // Set the layout manager for player panels
            FlowLayout playerPanelLayout = new FlowLayout(FlowLayout.CENTER, 5, 5);
            playerPanels[i].setLayout(playerPanelLayout);

            // Create a panel for the name label and player label
            JPanel labelPanel = new JPanel(new BorderLayout());
            labelPanel.setOpaque(false);

            // Create a name label for the player
            JLabel nameLabel = new JLabel("Player " + (i + 1));
            nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
            nameLabel.setFont(new Font("Georgia", Font.BOLD, 15));
            nameLabel.setForeground(Color.BLACK);

            // Add the name label to the label panel
            labelPanel.add(nameLabel, BorderLayout.NORTH);

            // Add the player label to the label panel
            labelPanel.add(playerPanels[i], BorderLayout.CENTER);

            // Create a scroll pane for the label panel
            JScrollPane scrollPane = new JScrollPane(labelPanel);
            scrollPane.setOpaque(false);
            scrollPane.getViewport().setOpaque(false);
            scrollPane.setPreferredSize(new Dimension(200, 200));

            // Add the scroll pane to the player panels container
            playerPanelsContainer.add(scrollPane);

        }

        // Create a scroll pane for the player panels container
        JScrollPane playerPanelsScrollPane = new JScrollPane(playerPanelsContainer);
        playerPanelsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        playerPanelsScrollPane.setOpaque(false);
        playerPanelsScrollPane.getViewport().setOpaque(false);

        // Add the scroll pane to the main panel
        mainPanel.add(playerPanelsContainer, BorderLayout.NORTH);

        // Add decks cards to center panel
        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);

        // Create a label for the deck
        JLabel deckLabel = new JLabel("Deck");
        deckLabel.setHorizontalAlignment(SwingConstants.CENTER);
        deckLabel.setFont(new Font("Georgia", Font.BOLD, 15));
        deckLabel.setForeground(Color.BLACK);

        // Add the deck label to the center panel
        centerPanel.add(deckLabel);

        if (Game.mainDeck.getSize() > 0) {
            centerPanel.add(mainDeckLabel);
        }

        if (Game.centerDeck.getSize() > 0) {
            Iterator<Card> itr = Game.centerDeck.iterator();
            while (itr.hasNext()) {
                centerPanel.add(cardToLabelMap.get(itr.next()));
            }
        }

        // Create a wrapper panel for the centerPanel
        JPanel centerWrapperPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 100, 100));
        centerWrapperPanel.setOpaque(false);
        centerWrapperPanel.add(centerPanel);

        // Add center panel to main panel
        mainPanel.add(centerWrapperPanel, BorderLayout.CENTER);

        // Create side Panel
        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new BorderLayout());
        sidePanel.setBackground(new Color(255, 255, 255, 150));
        sidePanel.setFont(new Font("Georgia", Font.BOLD, 15));

        // Create text panel
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);
        textPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Add labels into text panel
        textPanel.add(roundNumLabel);
        textPanel.add(trickNumLabel);
        textPanel.add(playerTurnLabel);
        textPanel.add(new JLabel("Scores: "));
        for (JLabel label : playerScoreLabels) {
            textPanel.add(label);
        }

        // Create the start button panel
        JPanel buttonPanel = new JPanel(new GridLayout(4, 1));
        buttonPanel.setOpaque(false);
        buttonPanel.add(restartButton);
        buttonPanel.add(resetButton);
        buttonPanel.add(loadButton);
        buttonPanel.add(quitButton);

        // Add components to side panel
        sidePanel.add(textPanel, BorderLayout.NORTH);
        sidePanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add main panel and side panel to game panel
        add(mainPanel, BorderLayout.CENTER);
        add(sidePanel, BorderLayout.EAST);

        // Set empty border around the panel for padding
        int padding = 10; // Adjust the padding size as needed
        setBorder(new EmptyBorder(padding, padding, padding, padding));

        if (!Game.playerHasMove) {
            popUpMessage("Player" + (Game.playerTurn + 1) + " has no move to play. Their turn is skipped",
                    "Game Status");
            Game.playerHasMove = true;
            String message = Game.update();
            refreshPanel();

            if (Game.displayTrickEndGUI) {
                popUpMessage(message, "Game Status");
                Game.displayTrickEndGUI = false;
            }

            if (Game.displayRoundEndGUI) {
                popUpMessage(message, "Game Status");
                Game.displayRoundEndGUI = false;
            }
        }
    }

    private void setBackgroundImage() {
        // Load the background image
        ImageIcon backgroundImageIcon = new ImageIcon("Go-Boom-main\\images\\Green Background.jpg");
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
