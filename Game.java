import java.util.Scanner;
import java.util.TreeMap;
import java.util.Iterator;
import java.util.Collections;
import java.util.Random;

public class Game {

    // CONSTANTS
    static final int PLAYER_COUNT = 4; 
    static final int STARTING_CARD_NO = 7;

    static final char[][] LEAD_CARDS = {
        {'A', '5', '9', 'K'},  // Player 1
        {'2', '6', 'X'},       // Player 2
        {'3', '7', 'J'},       // Player 3
        {'4', '8', 'Q'}        // Player 4
    };

    // Create Random object
    static Random rand = new Random(System.currentTimeMillis());
    
    // The main deck, starting with 52 cards
    static Deck mainDeck;

    // The center deck, starting with one lead card
    static Deck centerDeck;

    // Create an array of 4 players
    static Player[] players = new Player[PLAYER_COUNT];

    static Player roundWinner;

    static int playerTurn, trickNum, roundNum, playerTurnCount;
    static boolean playerTurnEnd, roundEnd;

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        startNewGame();

        while (true) {
            try {
                // Display cards
                displayCards();
                
                // Get user command inputs
                System.out.print("> ");
                String cmd = input.next();

                // Check command and run
                if (cmd.length() == 1) {
                    switch (Character.toLowerCase(cmd.charAt(0))) {
                        // Start game command
                        case 's':
                            System.out.println();
                            System.out.println("*** A new game is initialized ***");
                            startNewGame();
                            break;
                        // Reset game command
                        case 'r':
                            System.out.println();
                            System.out.println("*** The game is resetted. All scores are set to 0 ***");
                            resetGame();
                            break;
                        // Resume game command
                        case 'l':
                            break;
                        // Quit game command
                        case 'x':
                            input.close();
                            return;
                        // Draw card command
                        case 'd':
                            playerDrawCard();
                            break;
                        default: 
                            throw new IllegalArgumentException("Invalid Command.");
                    }
                }
                else if (cmd.length() == 2) {
                    playerDealCard(cmd);
                    roundEnd = checkRoundEnd();
                }
                else {
                    throw new IllegalArgumentException("Invalid Command.");
                }
            }
            catch (IllegalArgumentException ex) {
                System.out.println("Input Error: " + ex.getMessage());
            }

            if (playerTurnEnd) {
                // If all players have played their turn
                if (playerTurnCount == PLAYER_COUNT - 1) {
                    endTrick();
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
                System.out.println();
                System.out.println("*** Player" + (roundWinner.getNumber() + 1) + " wins the game! A new game is initialized ***");
                roundNum++;
                // Start a new round
                startNewGame();
            }
            else if (playerTurnCount != 0) {
                while (!hasPlayableMove()) {
                    players[playerTurn].noPlayedCard();
                    System.out.println();
                    System.out.println("*** Player" + (playerTurn + 1) + " has no move to play. Their turn is skipped ***");

                    playerTurn = (playerTurn + 1) % PLAYER_COUNT;
                    playerTurnCount++;

                    if (playerTurnCount == PLAYER_COUNT) {
                        endTrick();
                        break;
                    }
                }
            }

            input.nextLine();
            System.out.println();
        }
    }

    static void endTrick() {
        // Determine the roundWinner of the trick, and set them as the lead player for the next trick
        playerTurn = determineTrickWinner();
        System.out.println();
        System.out.println("*** Player" + (playerTurn + 1) + " wins Trick #" + (trickNum + 1) + " ***");
        centerDeck.clear();
        playerTurnCount = 0;
        trickNum++;
    }

    static void resetGame() {
        players = new Player[PLAYER_COUNT];
        roundNum = 0;
        startNewGame();
    }

    static void startNewGame() {
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
        playerTurnEnd = false;
        roundEnd = false;
    }

    static void displayCards() {
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



    static int determineStartingPlayer(Card leadCard) {
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

    static boolean hasPlayableMove() {
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

    static void playerDrawCard() {
        // Draws card only if the mainDeck has card(s)
        if (mainDeck.getSize() > 0) {
            players[playerTurn].drawCard(mainDeck);
        }
        else {
            System.out.println();
            System.out.println("*** There's no more card in the deck ***");
        }
    }
                            
    static void playerDealCard(String cmd) throws IllegalArgumentException {
        Player p = players[playerTurn];
        char suit = Character.toLowerCase(cmd.charAt(0)), rank = Character.toUpperCase(cmd.charAt(1));
        boolean valid = false;
        
        // Check if the card is valid
        // 1. Check if the first char is a suit
        outer:
            for (char s : Card.SUITS) {
                if (suit == s) {
                    // 2. Check if the second char is a rank
                    for (char r : Card.RANKS) {
                        if (rank == r) {
                            valid = true;
                            break outer;
                        }
                    }
                }
            }
        if (!valid) {
            throw new IllegalArgumentException("Card does not exist.");
        }

        // Check if player has this card
        if (p.hasCard(suit, rank)) {
            // Check if the card is playable
            if (centerDeck.getSize() == 0 || suit == centerDeck.iterator().next().getSuit() || rank == centerDeck.iterator().next().getRank()) {
                Card card = p.dealCard(suit, rank);
                centerDeck.addCard(card);
                p.playedCard(card);
                playerTurnEnd = true;
            }
            else {
                throw new IllegalArgumentException("Card does not follow the suit or rank of the lead card.");
            }
        }
        else {
            throw new IllegalArgumentException("Player does not have this card.");
        }
        
    }


    static boolean checkRoundEnd() {
        // Returns true if a player's deck is empty
        for (int i = 0; i < PLAYER_COUNT; i++) {
            if (players[i].getDeckSize() == 0) {
                roundWinner = players[i];
                return true;
            }
        }
        return false;
    }


    static int determineTrickWinner() {
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
    

    static void calculateScores() {
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
    
    static int getCardValue(Card card) {
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
}
