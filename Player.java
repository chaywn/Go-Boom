import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Player {
    private int number;
    private ArrayList<Card> cards;
    private int score;
    private Deck deck;
    private Card leadCard;

    public Player(int number) {
        this.number = number;
        this.cards = new ArrayList<Card>();
        this.score = 0;
    }

    public int getNumber() {
        return this.number;
    }

    public int getScore() {
        return this.score;
    }

    public void retrieveCards(int count, Deck deck) {
        for (int i = 0; i < count; i++) {
            cards.add(deck.dealCard());
        }
    }

    public Card playCard(char leadSuit) {
        List<Card> playableCards = new ArrayList<Card>();
        for (Card card : deck.getCards()) {
            if (leadCard == null || card.getSuit() == leadSuit) {
                playableCards.add(card);
            }
        }
        if (playableCards.isEmpty()) {
            // implement logic to play any card if player
            // doesn't have any playable cards in their hand
        }
        // implement logic to choose which playable card
        // to play based on strategy
        Card playedCard = playableCards.get(0);
        deck.removeCard(playedCard);
        return playedCard;
    }

    public void playCard(Card card, Deck centerDeck) {
        this.cards.remove(card);
        centerDeck.addCard(card);
    }

    public List<Card> getHand() {
        return deck.getCards();
    }

    public Card getLeadCard() {
        return leadCard;
    }

    public void setLeadCard(Card leadCard) {
        this.leadCard = leadCard;
    }

    @Override
    public String toString() {
        return "Player " + (this.number + 1) + " (Score: " + this.score + ")";
    } 
}
