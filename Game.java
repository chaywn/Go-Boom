import java.util.Scanner;
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

    // Create Random object, using constant seed(0) for testing purposes
    static Random rand = new Random(0 /**System.currentTimeMillis()**/);
    
    // The main deck, starting with 52 cards
    static Deck mainDeck = new Deck();

    // The center deck, starting with one lead card
    static Deck centerDeck = new Deck(1, mainDeck);

    // Create an array of 4 players
    static Player[] players = new Player[PLAYER_COUNT];

    // Determine the lead player based on the lead card (the first card in the center deck)
    static int playerTurn = determineStartingPlayer(centerDeck.getCard(0)); 

    static int trickCount = 0;
    static int playerTurnCount = 0;
    static boolean playerTurnEnd = false;

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        mainDeck.shuffle(rand);

        // Create Player objects and deal 7 cards to the player from the main deck
        for (int i = 0; i < PLAYER_COUNT; i++) {
            players[i] = new Player(i);
            players[i].retrieveCards(7, mainDeck);
        }

        while (true) {
            try {
                // Display cards
                displayCards();
                
                // Get user command input
                System.out.print("> ");
                String cmd = input.next();

                // Check command and run
                if (cmd.length() == 1) {
                    switch (Character.toLowerCase(cmd.charAt(0))) {
                        // Start game command
                        case 's':
                            restartGame();
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
                    // Determine the winner of the trick, and set them as the lead player for the next trick
                    playerTurn = determineTrickWinner();
                    System.out.println();
                    System.out.println("*** Player" + (playerTurn + 1) + " wins Trick #" + (trickCount + 1) + " ***");
                    centerDeck.clear();
                    playerTurnCount = 0;
                    trickCount++;

                    // Check if the game has ended when a player runs out of cards to play
                    if (checkGameEnd()) {
                        System.out.println("*** Player" + (determineGameWinner() + 1) + " wins the game! A new game is initialized ***");
                        // Start a new game
                        restartGame();
                    }
                }
                // Else, switch to next player
                else {
                    playerTurn = (playerTurn + 1) % PLAYER_COUNT;
                    playerTurnCount++;
                }
                playerTurnEnd = false;
            }

            input.nextLine();
            System.out.println();
            
            boolean gameWinner = checkGameEnd();
            if (gameWinner == true){
                determineGameWinner();
                break;
            }
        }
    }

    static void restartGame() {
        mainDeck= new Deck();
        mainDeck.shuffle(rand);

        centerDeck = new Deck(1, mainDeck);

        players = new Player[PLAYER_COUNT];
        for (int i = 0; i < PLAYER_COUNT; i++) {
            players[i] = new Player(i);
            players[i].retrieveCards(7, mainDeck);
        }

        playerTurn = determineStartingPlayer(centerDeck.getCard(0)); 

        trickCount = 0;
        playerTurnCount = 0;
        playerTurnEnd = false;
    }

    static void displayCards() {
        System.out.println("Trick #" + (trickCount + 1));
        System.out.println();

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

    static void playerDrawCard() {
        // Draws card only if the mainDeck has card(s)
        if (mainDeck.getSize() > 0) {
            players[playerTurn].drawCard(mainDeck);
        }
        else {
            System.out.println("There's no more cards in the deck. Player" + (playerTurn + 1) + "'s turn is skipped.");
            playerTurnEnd = true;
        }
    }
                            
    static void playerDealCard(String cmd) throws IllegalArgumentException {
        Player p = players[playerTurn];
        char suit = Character.toLowerCase(cmd.charAt(0)), rank = Character.toUpperCase(cmd.charAt(1));
        boolean valid = false;
        
        // Check if the card is valid
        // 1. Check if the first char is a suit
        for (char s : Card.SUITS) {
            if (suit == s) {
                // 2. Check if the second char is a rank
                for (char r : Card.RANKS) {
                    if (rank == r) {
                        valid = true;
                        break;
                    }
                }
                break;
            }
        }
        if (!valid) {
            throw new IllegalArgumentException("Card does not exist.");
        }

        // Check if player has this card
        if (p.containCard(suit, rank)) {
            // Check if the card is playable
            if (centerDeck.getSize() == 0 || suit == centerDeck.getCard(0).getSuit() || rank == centerDeck.getCard(0).getRank()) {
                Card card = p.dealCard(suit, rank);
                centerDeck.addCard(card);
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


    // TO-DO:
    // 1. Check whether the game has ended when one of the players run out of cards to play
    // 2. Get each player's deck size and check if they're 0, meaning they have no cards left in their deck
    // 3. Return true if there is player who has 0 deck size, else return false
    // HINT: Deck class has getSize() method, you may want to implement a method in Player class to return the size of a particular player's deck size
    static boolean checkGameEnd() {
        boolean gotWinner = false;
        for (int i = 0; i < 4; i++) {
            if (players[i].getDeckSize() == 0) {
                gotWinner = true;
            }
        }
        if (gotWinner == true) {
            return true;
        } else {
            return false;
        }
    }

    // TO-DO: 
    // 1. Determine the winner of each trick after every player has made their turn 
    //    *You do not need to check whether all players have finished playing because I already did it
    // 2. Player with the highest ranked card (and same suit as lead card) wins the trick
    // 3. Return the index of the winner (0 = Player1, 1 = Player2, 2 = Player3, 3 = Player4)
    // HINT: 
    // 1. You need to way to identify which card is played by which player (e.g. using Array index? -> requires extra code outside of this method to add and clear array)
    // 2. Card class has a compareTo() method which compares cards based on their rank. You may use it or modify it further. 
    static int determineTrickWinner() {

        return 0;
    }

    // TO-DO: 
    // 1. Calculate the score for all players once a game has finished
    // 2. Calculate the score of each player based on their remaining cards (the winner will have 0 score since they have no cards left)
    //     *watch the go boom tutorial on how the scores are calculated
    // 3. Update each player's score
    // HINT: Player class has a score attribute, you may want to implement a new method in Player to update their scores
    static void calculateScores() {
        for (Player player: players){
            int score = 0;
            Deck deck = player.getDeck();

            if(deck.getSize() > 0){
                for (int i = 0; i < deck.getSize(); i++){
                    Card card = deck.getCard(i);
                    int cardValue = getCardValue(card);
                    score += cardValue;
                }
            }

            player.setScore(score);
        }
    }
    
    static int getCardValue(Card card){
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
            case 'X':
            case 'J':
            case 'Q':
            case 'K':
                return 10;
            default:
                throw new IllegalArgumentException("Invalid rank: " + rank);
        }
    }

    // TO-DO: 
    // 1. Determine the winner of the game based on the players' score after a game has finished
    // 2. Player with the lowest score wins the game
    // 3. Return the index of the winner (0 = Player1, 1 = Player2, 2 = Player3, 3 = Player4)
    // HINT: use player.getScore() to retrieve a player's score
    static int determineGameWinner() {

        return 0;
    }
}
