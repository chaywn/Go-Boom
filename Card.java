public class Card 
    implements Comparable<Card> {
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