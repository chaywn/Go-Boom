import java.util.Objects;

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

    public void retrieveCards(int noOfCards, Deck mainDeck) {
        if (Objects.isNull(deck)) {
            deck = new Deck(noOfCards, mainDeck);
        }
        else {
            deck.addCards(noOfCards, mainDeck);
        }
        
    }

    @Override
    public String toString() {
        return "Player" + (number+1) + ": " + deck;
    } 
}
