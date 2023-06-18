import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Deck {
    private ArrayList<Card> cards = new ArrayList<>();

    // Create 52 cards
    public Deck() {
        for (char suit: Card.SUITS) {
            for (char rank: Card.RANKS) {
                cards.add(new Card(suit, rank));
            }
        }
    }

    public Deck(int noOfCards, Deck mainDeck) {
        addCards(noOfCards, mainDeck);
    }

    public int getSize() {
        return cards.size();
    }

    public Card getCard(int index) {
        return cards.get(index);
    }

    public void shuffle(Random rnd) {
        Collections.shuffle(cards, rnd);
    }

    public void clear() {
        cards.clear();
    }

    public void addCards(int noOfCards, Deck mainDeck) {
        for (int i = 0; i < noOfCards; i++) {
            addCard(mainDeck.popCard(i));
        }
    }

    public void addCard(Card card) {
        cards.add(card);
    }

    public Card popCard(int index) {
        Card card = getCard(index);
        removeCard(card);
        return card;
    }

    public void removeCard(Card card) {
        cards.remove(card);
    }

    @Override
    public String toString() {
        return cards + "";
    }
}
