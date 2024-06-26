public class Card 
    implements Comparable<Card> {
    // card ranks are arranged according to value (A - highest, 2 - lowest)
    public static final char[] SUITS = {'c', 'd', 'h', 's'};
    public static final char[] RANKS = {'2', '3', '4', '5', '6', '7', '8', '9', 'X', 'J', 'Q', 'K', 'A'};

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

    @Override
    public String toString() {
        return "" + suit + rank ;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }

        final Card c = (Card) obj;

        if (suit !=  c.suit || rank != c.rank) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int hash = getClass().hashCode();
        hash = 31 * hash + suit + rank;
        return hash;
    }

    @Override
    public int compareTo(Card c) {
        int index = 0, c_index = 0;

        for (int i = 0; i < RANKS.length; i++) {
            if (RANKS[i] == rank) {
                index = i;
            }

            if (RANKS[i] == c.rank) {
                c_index = i;
            }
        }

        return index - c_index;
    }
}