public class Player {
    private int number;
    private int score;
    private Deck deck;
    private Card playedCard;

    public Player(int number) {
        this.number = number;
        this.playedCard = null;
    }

    public int getNumber() {
        return number;
    }

    public int getScore() {
        return score;
    }
    
    public int getDeckSize() {
        return deck.getSize();
    }

    public Deck getDeck() {
        return deck;
    } 

    public void setScore(int score) {
        this.score = score;
    }

    public void playedCard (Card card) {
        this.playedCard = card;
    }

    public void noPlayedCard () {
        this.playedCard = null;
    }

    public boolean containCard(char suit, char rank) {
        for (int i = 0; i < deck.getSize(); i++) {
            if (suit == deck.getCard(i).getSuit() && rank == deck.getCard(i).getRank()) {
                return true;
            }
        }

        return false;
    }

    public Card dealCard(char suit, char rank) {
        for (int i = 0; i < deck.getSize(); i++) {
            if (suit == deck.getCard(i).getSuit() && rank == deck.getCard(i).getRank()) {
                Card card = deck.getCard(i);

                deck.removeCard(card);
                return card;
            }
        }

        return null;
    }

    public void retrieveCards(int noOfCards, Deck mainDeck) {
        if (deck == null) {
            deck = new Deck(noOfCards, mainDeck);
        }
        else {
            deck.addCards(noOfCards, mainDeck);
        }
        
    }

    public void drawCard(Deck mainDeck) {
        deck.addCard(mainDeck.popCard(0));
    }

    public void resetDeck(int noOfCards, Deck mainDeck) {
        deck.clear();
        deck.addCards(noOfCards, mainDeck);
    }

    @Override
    public String toString() {
        return "Player" + (number+1) + ": " + deck;
    }

    public Card getPlayedCard() {
        return playedCard;
    } 

    
}
