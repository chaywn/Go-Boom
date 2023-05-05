import java.util.Random;

public class Game {

    // CONSTANTS
    static final int PLAYER_COUNT = 4; 

    static final char[][] LEAD_CARDS = {
        {'A', '5', '9', 'K'},  // Player 1
        {'2', '6', 'X'},       // Player 2
        {'3', '7', 'J'},       // Player 3
        {'4', '8', 'Q'}        // Player 4
    };

    static final char[] MAIN_COMMANDS = {'s', 'x', 'd'};

    public static void main(String[] args) {
        // Create Random object, using constant seed(0) for testing purposes
        Random rand = new Random(0 /**System.currentTimeMillis()**/);

        // The main deck, starting with 52 cards
        Deck mainDeck= new Deck();
        mainDeck.shuffle(rand);

        // The center deck, starting with one lead card
        Deck centerDeck = new Deck(1, mainDeck);

        // Create an array of 4 players
        Player[] players = new Player[PLAYER_COUNT];
        for (int i = 0; i < PLAYER_COUNT; i++) {
            players[i] = new Player(i);

            // Deal 7 cards to the player from the main deck
            players[i].retrieveCards(7, mainDeck);
        }
        


        // Determine the lead player based on the lead card (the first card in the center deck)
        int playerTurn = determineStartingPlayer(centerDeck.getCard(0)); 
        
        int trickCount = 0;


        // Display cards
        displayCards(players, centerDeck, mainDeck, playerTurn);
        
        // System.out.print("> ");
    
    }


    static void displayCards(Player[] players, Deck centerDeck, Deck mainDeck, int playerTurn) {
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

    // TO BE COMPLETED
    static int determineWinner(int playerTurn, Deck centerDeck) {
       
        Card firstCard = centerDeck.getCard(centerDeck.getSize() - PLAYER_COUNT);
        Card largestCard = firstCard;

        for (int i = 1; i < centerDeck.getSize(); i++) {
            Card card = centerDeck.getCard(i);
            
            if (card.compareTo(largestCard) > 0) {
                largestCard = card;
            }
        }

        int index = centerDeck.getCardIndex(largestCard); // 4

        return 0;
    }
}
