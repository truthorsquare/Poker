package model;

import java.util.ArrayList;

public class Player {
    private String name;
    private double chips;
    private Hand hand;
    private boolean isAI;
    private double currentBet;
    private boolean isFolded;
    private boolean isAllIn;
    private int position; // For AI decision making
    
    public Player(String name, double chips, boolean isAI) {
        this.name = name;
        this.chips = chips;
        this.hand = new Hand();
        this.isAI = isAI;
        this.currentBet = 0;
        this.isFolded = false;
        this.isAllIn = false;
        this.position = 0;
    }
    
    public String getName() {
        return name;
    }
    
    public double getChips() {
        return chips;
    }
    
    public Hand getHand() {
        return hand;
    }
    
    public boolean isAI() {
        return isAI;
    }
    
    public double getCurrentBet() {
        return currentBet;
    }
    
    public boolean isFolded() {
        return isFolded;
    }
    
    public boolean isAllIn() {
        return isAllIn;
    }
    
    public int getPosition() {
        return position;
    }
    
    public void setPosition(int position) {
        this.position = position;
    }
    
    public void bet(double amount) {
        if (amount >= chips) {
            currentBet = chips;
            chips = 0;
            isAllIn = true;
        } else {
            currentBet += amount;
            chips -= amount;
        }
    }
    
    public void call(double amount) {
        double callAmount = Math.min(amount, chips);
        if (callAmount >= chips) {
            isAllIn = true;
        }
        bet(callAmount);
    }
    
    public void fold() {
        isFolded = true;
    }
    
    public void newHand() {
        hand.clear();
        isFolded = false;
        isAllIn = false;
        currentBet = 0;
    }
    
    public void resetBet() {
        currentBet = 0;
    }
    
    public void winPot(double pot) {
        chips += pot;
    }
    
    public double getHandStrength(ArrayList<Card> communityCards) {
        if (hand.size() < 2) {
            return 0.0;
        }
        if (communityCards.isEmpty()) {
            return hand.evaluate();
        }
        return hand.evaluateBestHand(communityCards);
    }
    
    public boolean hasChips() {
        return chips > 0;
    }
}

