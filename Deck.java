package model;

import java.util.ArrayList;
import java.util.Collections;

public class Deck {
    private ArrayList<Card> cards;
    
    public Deck() {
        cards = new ArrayList<>();
        initializeDeck();
    }
    
    private void initializeDeck() {
        String[] suits = {Card.SPADES, Card.HEARTS, Card.DIAMONDS, Card.CLUBS};
        String[] ranks = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"};
        int[] values = {2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14};
        
        for (int i = 0; i < suits.length; i++) {
            for (int j = 0; j < ranks.length; j++) {
                cards.add(new Card(suits[i], ranks[j], values[j]));
            }
        }
    }
    
    public void shuffle() {
        Collections.shuffle(cards);
    }
    
    public Card deal() {
        if (cards.isEmpty()) {
            return null;
        }
        return cards.remove(0);
    }
    
    public int size() {
        return cards.size();
    }
    
    public void reset() {
        cards.clear();
        initializeDeck();
    }
}

