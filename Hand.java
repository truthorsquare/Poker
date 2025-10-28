package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Hand {
    private ArrayList<Card> cards;
    
    public Hand() {
        cards = new ArrayList<>();
    }
    
    public void addCard(Card card) {
        cards.add(card);
    }
    
    public ArrayList<Card> getCards() {
        return cards;
    }
    
    public void clear() {
        cards.clear();
    }
    
    public int size() {
        return cards.size();
    }
    
    public double evaluate() {
        if (cards.size() < 2) {
            return 0.0;
        }
        
        // Sort cards by value in descending order
        ArrayList<Card> sortedCards = new ArrayList<>(cards);
        sortedCards.sort(Comparator.comparing(Card::getValue).reversed());
        
        // Check for different hand types and return normalized strength (0.0 - 1.0)
        if (isRoyalFlush(sortedCards)) return 1.0;
        if (isStraightFlush(sortedCards)) return 0.95;
        if (isFourOfAKind(sortedCards)) return 0.90;
        if (isFullHouse(sortedCards)) return 0.85;
        if (isFlush(sortedCards)) return 0.80;
        if (isStraight(sortedCards)) return 0.75;
        if (isThreeOfAKind(sortedCards)) return 0.70;
        if (isTwoPair(sortedCards)) return 0.65;
        if (isPair(sortedCards)) return 0.60;
        
        // High card
        return 0.30 + (sortedCards.get(0).getValue() / 100.0);
    }
    
    // Helper methods for hand evaluation
    
    private boolean isRoyalFlush(ArrayList<Card> cards) {
        return isStraightFlush(cards) && cards.get(0).getValue() == 14;
    }
    
    private boolean isStraightFlush(ArrayList<Card> cards) {
        return isStraight(cards) && isFlush(cards);
    }
    
    private boolean isFourOfAKind(ArrayList<Card> cards) {
        if (cards.size() < 4) return false;
        for (int i = 0; i < cards.size(); i++) {
            int count = 1;
            for (int j = i + 1; j < cards.size(); j++) {
                if (cards.get(i).getValue() == cards.get(j).getValue()) {
                    count++;
                }
            }
            if (count >= 4) return true;
        }
        return false;
    }
    
    private boolean isFullHouse(ArrayList<Card> cards) {
        return isThreeOfAKind(cards) && isPair(cards);
    }
    
    private boolean isFlush(ArrayList<Card> cards) {
        if (cards.size() < 5) return false;
        String suit = cards.get(0).getSuit();
        int count = 0;
        for (Card card : cards) {
            if (card.getSuit().equals(suit)) {
                count++;
            }
        }
        return count >= 5;
    }
    
    private boolean isStraight(ArrayList<Card> cards) {
        if (cards.size() < 5) return false;
        
        ArrayList<Integer> values = new ArrayList<>();
        for (Card card : cards) {
            values.add(card.getValue());
        }
        Collections.sort(values);
        
        for (int i = 0; i < values.size() - 4; i++) {
            boolean isStraight = true;
            for (int j = 1; j < 5; j++) {
                if (values.get(i + j) != values.get(i) + j) {
                    isStraight = false;
                    break;
                }
            }
            if (isStraight) return true;
        }
        
        // Check for A-2-3-4-5 straight
        if (values.contains(14) && values.contains(2) && values.contains(3) && 
            values.contains(4) && values.contains(5)) {
            return true;
        }
        
        return false;
    }
    
    private boolean isThreeOfAKind(ArrayList<Card> cards) {
        if (cards.size() < 3) return false;
        for (int i = 0; i < cards.size(); i++) {
            int count = 1;
            for (int j = i + 1; j < cards.size(); j++) {
                if (cards.get(i).getValue() == cards.get(j).getValue()) {
                    count++;
                }
            }
            if (count >= 3) return true;
        }
        return false;
    }
    
    private boolean isTwoPair(ArrayList<Card> cards) {
        if (cards.size() < 4) return false;
        int pairCount = 0;
        ArrayList<Integer> checkedValues = new ArrayList<>();
        for (int i = 0; i < cards.size(); i++) {
            if (checkedValues.contains(cards.get(i).getValue())) continue;
            int count = 1;
            for (int j = i + 1; j < cards.size(); j++) {
                if (cards.get(i).getValue() == cards.get(j).getValue()) {
                    count++;
                }
            }
            if (count >= 2) {
                pairCount++;
                checkedValues.add(cards.get(i).getValue());
            }
        }
        return pairCount >= 2;
    }
    
    private boolean isPair(ArrayList<Card> cards) {
        if (cards.size() < 2) return false;
        for (int i = 0; i < cards.size(); i++) {
            for (int j = i + 1; j < cards.size(); j++) {
                if (cards.get(i).getValue() == cards.get(j).getValue()) {
                    return true;
                }
            }
        }
        return false;
    }
    
    // Find the best 5-card hand from 7 cards (2 hole cards + 5 community cards)
    public double evaluateBestHand(ArrayList<Card> communityCards) {
        ArrayList<Card> allCards = new ArrayList<>(cards);
        allCards.addAll(communityCards);
        
        if (allCards.size() < 5) {
            return evaluate();
        }
        
        double bestStrength = 0.0;
        // Try all combinations of 5 cards
        for (int i = 0; i < allCards.size(); i++) {
            for (int j = i + 1; j < allCards.size(); j++) {
                for (int k = j + 1; k < allCards.size(); k++) {
                    for (int l = k + 1; l < allCards.size(); l++) {
                        for (int m = l + 1; m < allCards.size(); m++) {
                            Hand tempHand = new Hand();
                            tempHand.addCard(allCards.get(i));
                            tempHand.addCard(allCards.get(j));
                            tempHand.addCard(allCards.get(k));
                            tempHand.addCard(allCards.get(l));
                            tempHand.addCard(allCards.get(m));
                            double strength = tempHand.evaluate();
                            if (strength > bestStrength) {
                                bestStrength = strength;
                            }
                        }
                    }
                }
            }
        }
        
        return bestStrength;
    }
}

