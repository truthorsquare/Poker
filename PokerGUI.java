package ui;

import java.awt.*;
import javax.swing.*;

import model.Player;
import logic.TexasHoldem;

public class PokerGUI extends JFrame {
    private static final long serialVersionUID = 1L;
    
    private TexasHoldem game;
    private TablePanel tablePanel;
    private JPanel controlPanel;
    private JLabel messageLabel;
    private JButton foldButton;
    private JButton checkButton;
    private JButton callButton;
    private JButton raiseButton;
    private JSpinner raiseAmountSpinner;
    
    public PokerGUI() {
        initializeGame();
        setupUI();
    }
    
    private void initializeGame() {
        game = new TexasHoldem();
        
        // Add player (non-AI)
        game.addPlayer(new Player("You", 1000, false));
        
        // Add AI opponents with different personalities
        game.addPlayer(new Player("Alice", 1000, true));
        game.addPlayer(new Player("Bob", 1000, true));
        game.addPlayer(new Player("Charlie", 1000, true));
        
        game.startNewHand();
    }
    
    private void setupUI() {
        setTitle("Texas Hold'em Poker - Circular Table");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        
        // Main panel with green felt background
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(0, 100, 0));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Create circular table panel
        tablePanel = new TablePanel(game);
        mainPanel.add(tablePanel, BorderLayout.CENTER);
        
        // Setup control panel
        setupControlPanel();
        mainPanel.add(controlPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
        
        // Start the game flow
        SwingUtilities.invokeLater(() -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            processGameFlow();
        });
    }
    
    private void setupControlPanel() {
        controlPanel = new JPanel(new BorderLayout());
        controlPanel.setOpaque(false);
        
        // Message area
        messageLabel = new JLabel("Welcome to Texas Hold'em! Make your move.", JLabel.CENTER);
        messageLabel.setFont(new Font("Arial", Font.BOLD, 16));
        messageLabel.setForeground(Color.WHITE);
        messageLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setOpaque(false);
        
        foldButton = new JButton("Fold");
        checkButton = new JButton("Check");
        callButton = new JButton("Call");
        raiseButton = new JButton("Raise");
        
        // Style buttons
        for (JButton btn : new JButton[]{foldButton, checkButton, callButton, raiseButton}) {
            btn.setPreferredSize(new Dimension(120, 40));
            btn.setFont(new Font("Arial", Font.BOLD, 14));
        }
        
        foldButton.addActionListener(e -> handlePlayerAction("fold", 0));
        checkButton.addActionListener(e -> handlePlayerAction("check", 0));
        callButton.addActionListener(e -> handlePlayerAction("call", 0));
        raiseButton.addActionListener(e -> {
            int amount = (Integer) raiseAmountSpinner.getValue();
            handlePlayerAction("raise", amount);
        });
        
        raiseAmountSpinner = new JSpinner(new SpinnerNumberModel(50, 0, 10000, 10));
        raiseAmountSpinner.setPreferredSize(new Dimension(80, 30));
        
        buttonPanel.add(foldButton);
        buttonPanel.add(checkButton);
        buttonPanel.add(callButton);
        buttonPanel.add(raiseButton);
        buttonPanel.add(new JLabel("Amount: "));
        buttonPanel.add(raiseAmountSpinner);
        
        controlPanel.add(messageLabel, BorderLayout.NORTH);
        controlPanel.add(buttonPanel, BorderLayout.CENTER);
    }
    
    private void handlePlayerAction(String action, double amount) {
        Player currentPlayer = game.getCurrentPlayer();
        
        if (currentPlayer == null || currentPlayer.isAI()) {
            return;
        }
        
        game.processPlayerAction(action, amount);
        updateDisplay();
        
        // Process AI turns and game advancement
        SwingUtilities.invokeLater(() -> {
            processGameFlow();
        });
    }
    
    private void processGameFlow() {
        // Process AI players until betting round is complete or human player's turn
        while (true) {
            // Check if betting round is complete
            if (game.isBettingRoundComplete()) {
                advanceGame();
                break;
            }
            
            Player currentPlayer = game.getCurrentPlayer();
            if (currentPlayer == null) {
                break;
            }
            
            // If it's human player's turn, stop and wait
            if (!currentPlayer.isAI()) {
                break;
            }
            
            // Process AI action
            processSingleAITurn();
            updateDisplay();
            
            // Add small delay for visibility
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        
        updateDisplay();
    }
    
    private void processSingleAITurn() {
        Player aiPlayer = game.getCurrentPlayer();
        if (aiPlayer == null || !aiPlayer.isAI()) {
            return;
        }
        
        // Get AI decision with random personality
        double personality = 0.3 + (Math.random() * 0.6);
        ai.PokerAI pokerAI = new ai.PokerAI(aiPlayer.getName(), personality);
        ai.PokerAI.AIAction action = pokerAI.decideAction(
            aiPlayer, 
            game.getPlayers(), 
            game.getCommunityCards(), 
            game.getCurrentBetLevel() - aiPlayer.getCurrentBet(), 
            game.getPot()
        );
        
        // Process AI action
        String actionStr = action.getAction();
        int amount = 0;
        if (actionStr.equals("call")) {
            amount = (int) (game.getCurrentBetLevel() - aiPlayer.getCurrentBet());
        } else if (actionStr.equals("check")) {
            amount = 0;
        } else if (actionStr.equals("raise")) {
            amount = action.getAmount();
        }
        
        String actionMessage = aiPlayer.getName() + " " + actionStr;
        if (actionStr.equals("raise")) {
            actionMessage += " " + amount;
        }
        messageLabel.setText(actionMessage);
        
        game.processPlayerAction(actionStr, amount);
    }
    
    private void advanceGame() {
        // Reset bets
        game.resetBets();
        
        // Deal next community cards based on round
        int round = game.getCurrentRound();
        if (round == TexasHoldem.PRE_FLOP) {
            game.dealFlop();
            messageLabel.setText("Flop dealt! Betting round begins.");
        } else if (round == TexasHoldem.FLOP) {
            game.dealTurn();
            messageLabel.setText("Turn dealt! Betting round begins.");
        } else if (round == TexasHoldem.TURN) {
            game.dealRiver();
            messageLabel.setText("River dealt! Final betting round.");
        } else if (round == TexasHoldem.RIVER) {
            // Showdown
            Player winner = game.determineWinner();
            if (winner != null) {
                game.distributePot(winner);
                showGameResult(winner);
            }
            // Start new hand
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            game.startNewHand();
            messageLabel.setText("New hand started!");
        }
        
        updateDisplay();
        
        // Continue game flow
        SwingUtilities.invokeLater(() -> {
            try {
                Thread.sleep(1000);
                processGameFlow();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
    }
    
    private void showGameResult(Player winner) {
        String message = winner.getName() + " wins the pot of " + game.getPot() + " chips!";
        messageLabel.setText(message);
        JOptionPane.showMessageDialog(this, 
            winner.getName() + " wins with " + getHandDescription(winner),
            "Hand Complete",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private String getHandDescription(Player player) {
        double strength = player.getHandStrength(game.getCommunityCards());
        if (strength >= 0.95) {
            if (strength >= 1.0) return "Royal Flush!";
            return "Straight Flush";
        }
        if (strength >= 0.90) return "Four of a Kind";
        if (strength >= 0.85) return "Full House";
        if (strength >= 0.80) return "Flush";
        if (strength >= 0.75) return "Straight";
        if (strength >= 0.70) return "Three of a Kind";
        if (strength >= 0.65) return "Two Pair";
        if (strength >= 0.60) return "Pair";
        return "High Card";
    }
    
    private void updateDisplay() {
        // Repaint the table
        tablePanel.repaint();
        
        // Update buttons
        Player currentPlayer = game.getCurrentPlayer();
        if (currentPlayer != null && !currentPlayer.isAI()) {
            double betToCall = game.getCurrentBetLevel() - currentPlayer.getCurrentBet();
            
            foldButton.setEnabled(true);
            checkButton.setEnabled(betToCall == 0);
            callButton.setEnabled(betToCall > 0 && betToCall <= currentPlayer.getChips());
            raiseButton.setEnabled(currentPlayer.getChips() > 0);
            
            if (betToCall > 0) {
                callButton.setText("Call (" + (int)betToCall + ")");
            } else {
                callButton.setText("Call");
            }
        } else {
            foldButton.setEnabled(false);
            checkButton.setEnabled(false);
            callButton.setEnabled(false);
            raiseButton.setEnabled(false);
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PokerGUI gui = new PokerGUI();
            gui.setVisible(true);
        });
    }
}
