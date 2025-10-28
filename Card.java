package model;

public class Card {
    private String suit;
    private String rank;
    private int value;
    
    // Suit symbols
    public static final String SPADES = "♠";
    public static final String HEARTS = "♥";
    public static final String DIAMONDS = "♦";
    public static final String CLUBS = "♣";
    
    public Card(String suit, String rank, int value) {
        this.suit = suit;
        this.rank = rank;
        this.value = value;
    }
    
    public String getSuit() {
        return suit;
    }
    
    public String getRank() {
        return rank;
    }
    
    public int getValue() {
        return value;
    }
    
    @Override
    public String toString() {
        return rank + suit;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Card card = (Card) obj;
        return suit.equals(card.suit) && rank.equals(card.rank);
    }
}

