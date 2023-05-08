public class Card {
    public static final char[] SUITS = {'H', 'D', 'C', 'S'};
    public static final char[] RANKS = {'A', '2', '3', '4', '5', '6', '7', '8', '9', 'T', 'J', 'Q', 'K'};
    
    private char suit;
    private char rank;
    
    public Card(char suit, char rank) {
        this.suit = suit;
        this.rank = rank;
    }
    
    public char getSuit() {
        return suit;
    }
    
    public char getRank() {
        return rank;
    }

    public int compareTo(Card otherCard) {
        if (this.rank > otherCard.rank) {
            return 1;
        } else if (this.rank < otherCard.rank) {
            return -1;
        } else {
            return 0;
        }
    }
    
    @Override
    public String toString() {
        return "" + suit + rank;
    }
}
