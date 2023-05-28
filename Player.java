import java.util.Iterator;

public class Player {
    private int number;
    private int score;
    private Deck deck;
    private Card playedCard;
    
    public Player(int number, int score, Deck deck) {
       this.number = number;
       this.score = score;
       this.deck = deck;
    }
    
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

    public boolean hasCard(char suit, char rank) {
        return deck.containCard(suit, rank);
    }

    public Card dealCard(char suit, char rank) {
        Card c = deck.getCard(suit, rank);
        return deck.popCard(c);
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
        Iterator<Card> itr = mainDeck.iterator();
        deck.addCard(itr.next());
        itr.remove();
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
