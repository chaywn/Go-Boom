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

    public int getCardIndex(Card card) {
        return cards.indexOf(card);
    }

    public Card dealCard() {
        int size = cards.size();
        Card[] tempCards = new Card[size];
        tempCards = cards.toArray(tempCards);
        Card card = tempCards[size-1];
        cards.remove(card);
        return card;
    }
    
    
    public void shuffle(Random rnd) {
        Collections.shuffle(cards, rnd);
    }

    public void addCards(int noOfCards, Deck mainDeck) {
        for (int i = 0; i < noOfCards; i++) {
            Card card = mainDeck.getCard(0);

            addCard(card);
            mainDeck.removeCard(card);
        }
    }

    public void addCard(Card card) {
        cards.add(card);
    }

    public void removeCard(Card card) {
        cards.remove(card);
    }

    public ArrayList<Card> getCards() {
        return cards;
    }

    @Override
    public String toString() {
        return cards + "";
    }
}
