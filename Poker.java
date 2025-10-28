import javax.swing.SwingUtilities;
import ui.PokerGUI;

/**
 * Texas Hold'em Poker Game
 * Main entry point for the poker game application
 */
public class Poker {
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			PokerGUI gui = new PokerGUI();
			gui.setVisible(true);
		});
	}
}
