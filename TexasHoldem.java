package logic;

import java.util.ArrayList;
import model.Card;
import model.Deck;
import model.Player;

public class TexasHoldem {
    private ArrayList<Player> players;
    private Deck deck;
    private ArrayList<Card> communityCards;
    private int pot;
    private int currentRound;
    private int currentPlayerIndex;
    private double currentBetLevel;
    private int dealerIndex = 0;
    private int smallBlindIndex;
    private int bigBlindIndex;
    
    public static final int PRE_FLOP = 0;
    public static final int FLOP = 1;
    public static final int TURN = 2;
    public static final int RIVER = 3;
    public static final int SHOWDOWN = 4;
    
    public static final int SMALL_BLIND = 10;
    public static final int BIG_BLIND = 20;
    
    public TexasHoldem() {
        players = new ArrayList<>();
        deck = new Deck();
        communityCards = new ArrayList<>();
        pot = 0;
        currentRound = PRE_FLOP;
    }
    
    public void addPlayer(Player player) {
        players.add(player);
    }
    
    public void startNewHand() {
        // Remove players with no chips
        players.removeIf(p -> !p.hasChips());
        
        if (players.size() < 2) {
            return;
        }
        
        // Reset for new hand
        deck.reset();
        deck.shuffle();
        communityCards.clear();
        pot = 0;
        currentRound = PRE_FLOP;
        currentBetLevel = 0;
        
        // Set dealer position (rotate)
        dealerIndex = (dealerIndex + 1) % players.size();
        smallBlindIndex = (dealerIndex + 1) % players.size();
        bigBlindIndex = (smallBlindIndex + 1) % players.size();
        
        // Reset all players for new hand
        for (Player player : players) {
            player.newHand();
        }
        
        // Post blinds
        if (players.size() > 2) {
            players.get(smallBlindIndex).bet(SMALL_BLIND);
            players.get(bigBlindIndex).bet(BIG_BLIND);
            pot += SMALL_BLIND + BIG_BLIND;
            currentBetLevel = BIG_BLIND;
        }
        
        // Deal hole cards
        dealHoleCards();
        
        // Start first betting round
        currentPlayerIndex = (bigBlindIndex + 1) % players.size();
    }
    
    private void dealHoleCards() {
        for (Player player : players) {
            player.getHand().addCard(deck.deal());
            player.getHand().addCard(deck.deal());
        }
    }
    
    public void dealFlop() {
        deck.deal(); // Burn card
        communityCards.add(deck.deal());
        communityCards.add(deck.deal());
        communityCards.add(deck.deal());
        currentRound = FLOP;
    }
    
    public void dealTurn() {
        deck.deal(); // Burn card
        communityCards.add(deck.deal());
        currentRound = TURN;
    }
    
    public void dealRiver() {
        deck.deal(); // Burn card
        communityCards.add(deck.deal());
        currentRound = RIVER;
    }
    
    public void processPlayerAction(String action, double amount) {
        Player player = players.get(currentPlayerIndex);
        
        if (player.isFolded() || player.isAllIn()) {
            moveToNextPlayer();
            return;
        }
        
        double betToCall = currentBetLevel - player.getCurrentBet();
        
        switch (action.toLowerCase()) {
            case "fold":
                player.fold();
                break;
            case "check":
                // Check is only valid if there's no bet to call
                // If there is a bet to call, just leave current bet as is
                break;
            case "call":
                double callAmount = Math.min(betToCall, player.getChips());
                player.call(callAmount);
                pot += callAmount;
                if (callAmount < betToCall) {
                    player.isAllIn(); // All-in on call
                }
                break;
            case "raise":
                double totalRaise = betToCall + amount;
                double actualRaise = Math.min(totalRaise, player.getChips());
                player.bet(actualRaise);
                pot += actualRaise;
                currentBetLevel = player.getCurrentBet();
                break;
        }
        
        moveToNextPlayer();
    }
    
    private void moveToNextPlayer() {
        int startIndex = currentPlayerIndex;
        int attempts = 0;
        
        do {
            currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
            attempts++;
            
            // Prevent infinite loop
            if (attempts >= players.size()) {
                break;
            }
        } while ((players.get(currentPlayerIndex).isFolded() || 
                 players.get(currentPlayerIndex).isAllIn()) && 
                 currentPlayerIndex != startIndex);
    }
    
    public boolean isBettingRoundComplete() {
        int activePlayers = 0;
        int playersAtCurrentBet = 0;
        
        for (Player player : players) {
            if (!player.isFolded()) {
                activePlayers++;
                if (Math.abs(player.getCurrentBet() - currentBetLevel) < 0.01 || player.isAllIn()) {
                    playersAtCurrentBet++;
                }
            }
        }
        
        return activePlayers <= 1 || playersAtCurrentBet == activePlayers;
    }
    
    public Player getCurrentPlayer() {
        if (currentPlayerIndex < players.size()) {
            return players.get(currentPlayerIndex);
        }
        return null;
    }
    
    public ArrayList<Player> getPlayers() {
        return players;
    }
    
    public ArrayList<Card> getCommunityCards() {
        return communityCards;
    }
    
    public int getPot() {
        return pot;
    }
    
    public int getCurrentRound() {
        return currentRound;
    }
    
    public Player determineWinner() {
        ArrayList<Player> eligiblePlayers = new ArrayList<>();
        for (Player player : players) {
            if (!player.isFolded()) {
                eligiblePlayers.add(player);
            }
        }
        
        if (eligiblePlayers.size() == 0) {
            return null;
        }
        
        if (eligiblePlayers.size() == 1) {
            return eligiblePlayers.get(0);
        }
        
        // Compare hands
        Player winner = eligiblePlayers.get(0);
        double bestStrength = winner.getHandStrength(communityCards);
        
        for (int i = 1; i < eligiblePlayers.size(); i++) {
            double strength = eligiblePlayers.get(i).getHandStrength(communityCards);
            if (strength > bestStrength) {
                bestStrength = strength;
                winner = eligiblePlayers.get(i);
            }
        }
        
        return winner;
    }
    
    public void resetBets() {
        for (Player player : players) {
            player.resetBet();
        }
        currentBetLevel = 0;
    }
    
    public void distributePot(Player winner) {
        winner.winPot(pot);
        pot = 0;
    }
    
    public int getActivePlayerCount() {
        int count = 0;
        for (Player player : players) {
            if (!player.isFolded()) {
                count++;
            }
        }
        return count;
    }
    
    public double getCurrentBetLevel() {
        return currentBetLevel;
    }
}

