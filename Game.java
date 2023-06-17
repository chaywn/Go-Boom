import java.util.Scanner;
import java.util.TreeMap;

import javax.swing.SwingUtilities;

import java.util.Iterator;
import java.util.Collections;
import java.util.Random;
import java.io.*; 

public class Game {

    // CONSTANTS
    static final int PLAYER_COUNT = 4; 
    static final int STARTING_CARD_NO = 3;

    static final char[][] LEAD_CARDS = {
        {'A', '5', '9', 'K'},  // Player 1
        {'2', '6', 'X'},       // Player 2
        {'3', '7', 'J'},       // Player 3
        {'4', '8', 'Q'}        // Player 4
    };

    // Create Random object
    static Random rand = new Random(System.currentTimeMillis());

    // Scanner object
    static Scanner input = new Scanner(System.in);
    
    // The main deck, starting with 52 cards
    static Deck mainDeck;

    // The center deck, starting with one lead card
    static Deck centerDeck;

    // Create an array of 4 players
    static Player[] players = new Player[PLAYER_COUNT];

    static Player roundWinner;

    static int playerTurn, trickNum, roundNum, playerTurnCount;
    static boolean playerTurnEnd, roundEnd, playerHasMove, trickEnd;
    static boolean displayRoundEndGUI = false, displayTrickEndGUI = false;
    static final String SAVE_FILE_NAME = "savegame.txt"; // savefile name

    public static void main(String[] args) {
        System.out.println("Welcome to Go Boom!");
        System.out.println("Press play to begin :)");
        SwingUtilities.invokeLater(() -> new MainFrame());
        System.out.println();
    }
    
    public static String update() {
        String message = "";

        if (playerTurnEnd) {
            // If all players have played their turn
            if (playerTurnCount == PLAYER_COUNT - 1) {
                trickEnd = true;
                displayTrickEndGUI = true;
                message = endTrick();
            }
            // Else, switch to next player
            else {
                playerTurn = (playerTurn + 1) % PLAYER_COUNT;
                playerTurnCount++;
            }
            playerTurnEnd = false;
        }

        // Check if the game has ended when a player runs out of cards to play
        if (roundEnd) {
            calculateScores();
            message = ("Player" + (roundWinner.getNumber() + 1) + " wins the game! A new game is initialized");
            System.out.println("*** " + message + " ***\n");
            roundNum++;
            displayRoundEndGUI = true;
            // Start a new round
            startNewGame();
        }
        // If the next player is not the lead player
        else if (playerTurnCount != 0) {
            // Check if the next player has playable move
            playerHasMove = hasPlayableMove();

            if (!playerHasMove) {
                players[playerTurn].noPlayedCard();
                message = "Player" + (playerTurn + 1) + " has no move to play. Their turn is skipped";
                System.out.println("*** " + message + " ***\n");

                playerTurnEnd = true;
            }

            // while (!hasPlayableMove()) {
            //     players[playerTurn].noPlayedCard();
            //     System.out.println();
            //     System.out.println("*** Player" + (playerTurn + 1) + " has no move to play. Their turn is skipped ***");

            //     playerTurn = (playerTurn + 1) % PLAYER_COUNT;
            //     playerTurnCount++;

            //     if (playerTurnCount == PLAYER_COUNT) {
            //         endTrick();
            //         break;
            //     }
            // }
        }

        // Display cards in console
        displayCards();

        System.out.println();
        return message;
    }

    private static String endTrick() {
        // Determine the roundWinner of the trick, and set them as the lead player for the next trick
        playerTurn = determineTrickWinner();
        String message = "Player" + (playerTurn + 1) + " wins Trick #" + (trickNum + 1);
        System.out.println("*** " + message + "***\n");
        centerDeck.clear();
        playerTurnCount = 0;
        trickNum++;
        trickEnd = false;
        return message;
    }

    public static String quitGame() {
        String result = saveGame();
        System.out.println("*** " + result + " ***\n");
        input.close();
        return result;
    }

    public static void resetGame() {
        players = new Player[PLAYER_COUNT];
        roundNum = 0;
        deleteSaveFile();
        startNewGame();
        System.out.println("*** The game is resetted. All scores are set to 0 ***\n");
    }

    public static void startNewGame() {
        mainDeck= new Deck(rand);

        centerDeck = new Deck(1, mainDeck);

        if (players[0] == null) {
            for (int i = 0; i < PLAYER_COUNT; i++) {
                players[i] = new Player(i);
                players[i].retrieveCards(STARTING_CARD_NO, mainDeck);
            }
        }
        else {
            for (Player p: players) {
                p.resetDeck(STARTING_CARD_NO, mainDeck);
            }
        }

        // Determine the lead player based on the lead card (the first card in the center deck)
        playerTurn = determineStartingPlayer(centerDeck.iterator().next()); 

        trickNum = 0;
        playerTurnCount = 0;
        playerHasMove = true;
        playerTurnEnd = false;
        roundEnd = false;
        trickEnd = false;
    }

    private static void displayCards() {
        System.out.println("Round #" + (roundNum + 1) + ", Trick #" + (trickNum + 1));

        for (Player p: players) {
            System.out.println(p);
        }
        System.out.println("Center : " + centerDeck);
        System.out.println("Deck   : " + mainDeck);
        System.out.print("Score: ");
        for (int i = 0; i < PLAYER_COUNT; i++) {
            Player p = players[i];

            if (i != 0) {
                System.out.print(" | ");
            }

            System.out.print("Player" + (p.getNumber() + 1) + " = " + p.getScore());
        }
        System.out.println();
        System.out.println("Turn : Player" + (playerTurn + 1));
    }



    private static int determineStartingPlayer(Card leadCard) {
        char leadCardRank = leadCard.getRank();
        
        for (int i = 0; i < LEAD_CARDS.length; i++) {
            for (char rank : LEAD_CARDS[i]) {
                if (rank == leadCardRank) {
                    return i;
                }
            }            
        }
        return 0;
    }

    private static boolean hasPlayableMove() {
        // If the main deck has no more cards, check if the player has playable card
        if (mainDeck.getSize() == 0) {
            Card leaCard = centerDeck.iterator().next();
            Iterator<Card> deckItr = players[playerTurn].getDeck().iterator();

            while(deckItr.hasNext()) {
                Card c = deckItr.next();
                if (c.getSuit() == leaCard.getSuit() || c.getRank() == leaCard.getRank()) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }

    public static void playerDrawCard() {
        // Draws card only if the mainDeck has card(s)
        if (mainDeck.getSize() > 0) {
            players[playerTurn].drawCard(mainDeck);
        }
        else {
            System.out.println();
            System.out.println("*** There's no more card in the deck ***");
        }
    }
                            
    public static boolean playerDealCard(Card c) {
        Player p = players[playerTurn];
        char suit = c.getSuit();
        char rank = c.getRank();
        
        if (!players[playerTurn].hasCard(suit, rank)) {
            return false;
        }

        // Check if the card is playable
        if (centerDeck.getSize() == 0 || suit == centerDeck.iterator().next().getSuit() || rank == centerDeck.iterator().next().getRank()) {
            Card card = p.dealCard(c.getSuit(), c.getRank());
            centerDeck.addCard(card);
            p.playedCard(card);
            playerTurnEnd = true;

            roundEnd = checkRoundEnd();
            return true;
        }
        return false;
    }


    private static boolean checkRoundEnd() {
        // Returns true if a player's deck is empty
        for (int i = 0; i < PLAYER_COUNT; i++) {
            if (players[i].getDeckSize() == 0) {
                roundWinner = players[i];
                return true;
            }
        }
        return false;
    }


    private static int determineTrickWinner() {
        Card leadCard = centerDeck.iterator().next();
        char leadSuit = leadCard.getSuit();

        TreeMap<Card, Integer> map = new TreeMap<>(Collections.reverseOrder());
        
        for (int i = 0; i < PLAYER_COUNT; i++) {
            Card playedCard = players[i].getPlayedCard();

            if (playedCard != null && playedCard.getSuit() == leadSuit) {
                map.put(playedCard, i);
            }
        }
    
        return map.get(map.firstKey());
    }
    

    private static void calculateScores() {
        for (Player player: players){
            int score = 0;
            Deck deck = player.getDeck();

            Iterator<Card> itr = deck.iterator();

            if (deck.getSize() > 0){
                while (itr.hasNext()) {
                    Card card = itr.next();
                    int cardValue = getCardValue(card);
                    score += cardValue;
                }
            }

            player.setScore(player.getScore() + score);
        }
    }
    
    private static int getCardValue(Card card) {
        char rank = card.getRank();

        switch (rank) {
            case '2':
                return 2;
            case '3':
                return 3;
            case '4':
                return 4;
            case '5':
                return 5;
            case '6':
                return 6;
            case '7':
                return 7;
            case '8':
                return 8;
            case '9':
                return 9;
            case 'A':
                return 1;
            case 'X','J','Q','K':
                return 10;
            default:
                return -1;
        }
    }
    
    private static String deckToString(Deck deck) {
        StringBuilder sb = new StringBuilder();
    
        Iterator<Card> iterator = deck.iterator();

        // if the deck is empty
        if (!iterator.hasNext()) {
            return "\n";
        }

        while (iterator.hasNext()) {
            Card card = iterator.next();
            sb.append(card.getSuit()).append(card.getRank());
    
            if (iterator.hasNext()) {
                sb.append(",");
            }
        }
    
        return sb.toString();
    }

    private static Deck stringToDeck(String deckString) {
        Deck deck = new Deck();
    
        String[] cards = deckString.split(",");

        // If deckString is not empty
        if (!cards[0].equals("")) {
            for (String card : cards) {
                char suit = card.charAt(0);
                char rank = card.charAt(1);
                deck.addCard(new Card(suit, rank));
            }
        }
    
        return deck;
    }
    
    public static String saveGame() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(SAVE_FILE_NAME))) {
            // Save player information
            for (Player player : players) {
                writer.println(player.getNumber());
                writer.println(player.getScore());
                writer.println(deckToString(player.getDeck()));
            }
    
            // Save round information
            writer.println(roundNum);
            writer.println(trickNum);
            writer.println(playerTurn);
            writer.println(playerTurnCount);
            writer.println(playerTurnEnd);
            writer.println(roundEnd);
    
            // Save deck information
            writer.println(deckToString(mainDeck));
            writer.println(deckToString(centerDeck));
    
            writer.flush();

            return "Game Saved and Exited";
        } catch (IOException e) {
            return "Error saving game: " + e.getMessage();
        }
    }
    

    public static String loadGame() {
        try (BufferedReader reader = new BufferedReader(new FileReader(SAVE_FILE_NAME))) {
            // Load player information
            for (int i = 0; i < PLAYER_COUNT; i++) {
                int number = Integer.parseInt(reader.readLine());
                int score = Integer.parseInt(reader.readLine());
                Deck deck = stringToDeck(reader.readLine());
                players[i] = new Player(number, score, deck);
            }
    
            // Load round information
            roundNum = Integer.parseInt(reader.readLine());
            trickNum = Integer.parseInt(reader.readLine());
            playerTurn = Integer.parseInt(reader.readLine());
            playerTurnCount = Integer.parseInt(reader.readLine());
            playerTurnEnd = Boolean.parseBoolean(reader.readLine());
            roundEnd = Boolean.parseBoolean(reader.readLine());
    
            // Load deck information
            mainDeck = stringToDeck(reader.readLine());
            centerDeck = stringToDeck(reader.readLine());

            System.out.println("*** Previous game resumed ***\n");

            return "Previous game resumed";
        } catch (IOException e) {
            return "Error loading game: " + e.getMessage();
        }
    }
    
    
    private static void deleteSaveFile() {
        File file = new File(SAVE_FILE_NAME);
        file.delete();
    }
}
