public class Player {
    private int number;
    private int score;
    private Deck deck;

    public Player(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    public int getScore() {
        return score;
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

    @Override
    public String toString() {
        return "Player" + (number+1) + ": " + deck;
    } 
}
