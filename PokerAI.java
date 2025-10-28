package ai;

import java.util.ArrayList;
import java.util.Random;
import model.Card;
import model.Player;

public class PokerAI {
    private static final Random random = new Random();
    private double personality; // 0.3 (cautious) to 0.9 (aggressive)
    private String name;
    
    public PokerAI(String name, double personality) {
        this.name = name;
        this.personality = personality;
    }
    
    public AIAction decideAction(Player aiPlayer, ArrayList<Player> players, 
                                  ArrayList<Card> communityCards, 
                                  double currentBetToCall, double pot) {
        if (aiPlayer.isFolded() || aiPlayer.isAllIn()) {
            return new AIAction("check", 0);
        }
        
        // Evaluate hand strength
        double handStrength = aiPlayer.getHandStrength(communityCards);
        
        // Adjust hand strength based on community cards
        if (communityCards.size() > 0) {
            handStrength = adjustStrengthForCommunity(handStrength, communityCards.size());
        }
        
        // Add some randomness based on personality
        double adjustedStrength = handStrength + (personality - 0.5) * 0.2;
        
        // Make decision based on hand strength
        if (adjustedStrength > 0.8) {
            // Very strong hand - aggressive betting
            double raiseAmount = pot * (0.10 + (personality - 0.5) * 0.4);
            return new AIAction("raise", (int) Math.min(raiseAmount, aiPlayer.getChips()));
        } 
        else if (adjustedStrength > 0.6) {
            // Strong hand - call or small raise
            if (random.nextDouble() < personality) {
                double raiseAmount = pot * (0.10 + (personality - 0.6) * 0.2);
                return new AIAction("raise", (int) Math.min(raiseAmount, aiPlayer.getChips()));
            } else {
                return new AIAction("call", (int) currentBetToCall);
            }
        }
        else if (adjustedStrength > 0.4) {
            // Medium hand - occasional bluff or call
            if (random.nextDouble() < (personality * 0.3)) {
                // Bluff with small raise
                double bluffAmount = pot * 0.15;
                return new AIAction("raise", (int) Math.min(bluffAmount, aiPlayer.getChips()));
            }
            else if (random.nextDouble() < 0.7) {
                return new AIAction("call", (int) currentBetToCall);
            } else {
                return new AIAction("fold", 0);
            }
        }
        else {
            // Weak hand - fold or occasional bluff
            if (random.nextDouble() < (personality * 0.2)) {
                // Aggressive bluff
                double bluffAmount = pot * 0.2;
                return new AIAction("raise", (int) Math.min(bluffAmount, aiPlayer.getChips()));
            } else {
                return new AIAction("fold", 0);
            }
        }
    }
    
    private double adjustStrengthForCommunity(double strength, int communityCount) {
        // Early game (pre-flop) is more uncertain
        if (communityCount == 0) {
            return strength * 0.7; // Reduce confidence pre-flop
        }
        // After flop, more confident
        if (communityCount == 3) {
            return strength * 0.95;
        }
        // After turn/river, very confident
        if (communityCount >= 4) {
            return strength * 1.0;
        }
        return strength;
    }
    
    public String getName() {
        return name;
    }
    
    public double getPersonality() {
        return personality;
    }
    
    // Inner class to represent AI action
    public static class AIAction {
        private String action; // "fold", "call", "raise", "check"
        private int amount;
        
        public AIAction(String action, int amount) {
            this.action = action;
            this.amount = amount;
        }
        
        public String getAction() {
            return action;
        }
        
        public int getAmount() {
            return amount;
        }
    }
}

