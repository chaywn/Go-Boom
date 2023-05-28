import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Random;
import java.util.Iterator;

public class Deck {
    private LinkedHashSet<Card> cards = new LinkedHashSet<>();

    // Create 52 cards
    public Deck(Random rnd) {
        Card[] cardArr = new Card[52];
        int i = 0;
        for (char suit: Card.SUITS) {
            for (char rank: Card.RANKS) {
                cardArr[i] = new Card(suit, rank);
                i++;
            }
        }

        Collections.shuffle(Arrays.asList(cardArr), rnd);
        cards.addAll(Arrays.asList(cardArr));
    }

    public Deck(int noOfCards, Deck mainDeck) {
        addCards(noOfCards, mainDeck);
    }

    public int getSize() {
        return cards.size();
    }

    public void clear() {
        cards.clear();
    }

    public void addCards(int noOfCards, Deck mainDeck) {
        Iterator<Card> itr = mainDeck.iterator();
        int i = 0;

        while (itr.hasNext() && i < noOfCards) {
            Card c = itr.next();
            addCard(c);
            itr.remove();
            i++;
        }
    }

    public void addCard(Card card) {
        cards.add(card);
    }

    public Card getCard(char suit, char rank) {
        for (Card c: cards) {
            if (c.getSuit() == suit && c.getRank() == rank) {
                return c;
            }
        }
        return null;
    }

    public boolean containCard(char suit, char rank) {
        if (getCard(suit, rank) != null) {
            return true;
        }
        return false;
    }

    public void removeCard(Card card) {
        cards.remove(card);
    }

    public Card popCard(Card card) {
        cards.remove(card);
        return card;
    }

    public Iterator<Card> iterator() {
        return cards.iterator();
    }

    @Override
    public String toString() {
        return cards.toString();
    }
}
