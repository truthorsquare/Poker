package ui;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import model.Card;
import model.Player;
import logic.TexasHoldem;

public class TablePanel extends JPanel {
    private static final long serialVersionUID = 1L;
    
    private TexasHoldem game;
    private int centerX;
    private int centerY;
    private int radius = 200;
    
    public TablePanel(TexasHoldem game) {
        this.game = game;
        setOpaque(false);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        centerX = getWidth() / 2;
        centerY = getHeight() / 2;
        
        // Draw table (green oval)
        g2d.setColor(new Color(0, 100, 0, 150)); // Semi-transparent green
        g2d.fillOval(centerX - radius - 50, centerY - radius - 50, 
                     (radius + 50) * 2, (radius + 50) * 2);
        
        // Draw table border
        g2d.setColor(new Color(139, 90, 43)); // Brown table edge
        g2d.setStroke(new BasicStroke(8));
        g2d.drawOval(centerX - radius - 50, centerY - radius - 50, 
                     (radius + 50) * 2, (radius + 50) * 2);
        
        // Draw community cards in center
        drawCommunityCards(g2d);
        
        // Draw pot display in center
        drawPot(g2d);
        
        // Draw players around the table
        drawPlayers(g2d);
    }
    
    private void drawCommunityCards(Graphics2D g2d) {
        ArrayList<Card> communityCards = game.getCommunityCards();
        if (communityCards.isEmpty()) return;
        
        int cardWidth = 40;
        int cardHeight = 60;
        int spacing = 10;
        int totalWidth = communityCards.size() * cardWidth + (communityCards.size() - 1) * spacing;
        int startX = centerX - totalWidth / 2;
        
        for (int i = 0; i < communityCards.size(); i++) {
            int x = startX + i * (cardWidth + spacing);
            drawCard(g2d, x, centerY - 20, cardWidth, cardHeight, communityCards.get(i));
        }
    }
    
    private void drawPot(Graphics2D g2d) {
        g2d.setColor(Color.YELLOW);
        g2d.setFont(new Font("Arial", Font.BOLD, 24));
        FontMetrics fm = g2d.getFontMetrics();
        String potText = "Pot: " + game.getPot();
        int textWidth = fm.stringWidth(potText);
        g2d.drawString(potText, centerX - textWidth / 2, centerY + 80);
    }
    
    private void drawPlayers(Graphics2D g2d) {
        ArrayList<Player> players = game.getPlayers();
        int numPlayers = players.size();
        
        for (int i = 0; i < numPlayers; i++) {
            Player player = players.get(i);
            Player currentPlayer = game.getCurrentPlayer();
            
            // Calculate position using polar coordinates
            double angle = 2 * Math.PI * i / numPlayers;
            int x = centerX + (int)(radius * Math.cos(angle));
            int y = centerY + (int)(radius * Math.sin(angle));
            
            // Check if this player is the current player (highlight)
            if (currentPlayer != null && currentPlayer.equals(player)) {
                g2d.setColor(Color.CYAN);
                g2d.setStroke(new BasicStroke(3));
                g2d.drawOval(x - 50, y - 40, 100, 80);
            }
            
            // Draw player info
            drawPlayerInfo(g2d, x, y, player);
        }
    }
    
    private void drawPlayerInfo(Graphics2D g2d, int x, int y, Player player) {
        // Draw player background
        Color bgColor = player.isAI() ? new Color(50, 50, 150, 200) : new Color(150, 50, 50, 200);
        g2d.setColor(bgColor);
        g2d.fillRoundRect(x - 70, y - 30, 140, 80, 10, 10);
        
        // Draw border
        if (player.isFolded()) {
            g2d.setColor(Color.RED);
        } else {
            g2d.setColor(Color.WHITE);
        }
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRoundRect(x - 70, y - 30, 140, 80, 10, 10);
        
        // Draw player name
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 14));
        FontMetrics fm = g2d.getFontMetrics();
        String name = player.getName();
        int nameWidth = fm.stringWidth(name);
        g2d.drawString(name, x - nameWidth / 2, y - 10);
        
        // Draw chip count
        g2d.setColor(Color.YELLOW);
        g2d.setFont(new Font("Arial", Font.PLAIN, 12));
        fm = g2d.getFontMetrics();
        String chips = (int)player.getChips() + " chips";
        int chipsWidth = fm.stringWidth(chips);
        g2d.drawString(chips, x - chipsWidth / 2, y + 10);
        
        // Draw status
        String status = "";
        if (player.isFolded()) {
            status = "FOLDED";
        } else if (player.isAllIn()) {
            status = "ALL IN";
        }
        if (!status.isEmpty()) {
            g2d.setColor(Color.RED);
            g2d.setFont(new Font("Arial", Font.BOLD, 10));
            fm = g2d.getFontMetrics();
            int statusWidth = fm.stringWidth(status);
            g2d.drawString(status, x - statusWidth / 2, y + 25);
        }
        
        // Draw player cards
        if (!player.isFolded() && player.getHand().size() >= 2) {
            int cardY = y + 40;
            int currentRound = game.getCurrentRound();
            boolean isShowdown = (currentRound >= 4); // After river
            if (player.isAI() && !isShowdown && currentRound <= TexasHoldem.RIVER) {
                // Draw face-down cards for AI
                drawCardBack(g2d, x - 30, cardY, 30, 40);
                drawCardBack(g2d, x + 5, cardY, 30, 40);
            } else {
                // Draw actual cards
                Card card1 = player.getHand().getCards().get(0);
                Card card2 = player.getHand().getCards().get(1);
                drawCard(g2d, x - 30, cardY, 30, 40, card1);
                drawCard(g2d, x + 5, cardY, 30, 40, card2);
            }
        }
    }
    
    private void drawCard(Graphics2D g2d, int x, int y, int width, int height, Card card) {
        // Draw white card background
        g2d.setColor(Color.WHITE);
        g2d.fillRoundRect(x, y, width, height, 5, 5);
        
        // Draw card border
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRoundRect(x, y, width, height, 5, 5);
        
        // Draw card content
        String cardText = card.toString();
        g2d.setFont(new Font("Arial", Font.BOLD, Math.min(width / 3, height / 3)));
        
        // Determine card color based on suit
        Color textColor = (card.getSuit().equals(Card.HEARTS) || card.getSuit().equals(Card.DIAMONDS)) ? 
                         Color.RED : Color.BLACK;
        g2d.setColor(textColor);
        
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(cardText);
        int textHeight = fm.getAscent();
        g2d.drawString(cardText, x + (width - textWidth) / 2, y + (height + textHeight) / 2);
    }
    
    private void drawCardBack(Graphics2D g2d, int x, int y, int width, int height) {
        // Draw blue card back
        g2d.setColor(new Color(0, 0, 150));
        g2d.fillRoundRect(x, y, width, height, 5, 5);
        
        // Draw border
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRoundRect(x, y, width, height, 5, 5);
        
        // Draw pattern
        g2d.setColor(new Color(0, 0, 200));
        g2d.drawLine(x + 3, y + 3, x + width - 3, y + height - 3);
        g2d.drawLine(x + width - 3, y + 3, x + 3, y + height - 3);
    }
}

