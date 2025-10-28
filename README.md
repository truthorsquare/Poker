# Texas Hold'em Poker Game

A fully functional Texas Hold'em Poker game built in Java with Swing GUI and AI opponents.

## Features

- **Complete Poker Implementation**: Full Texas Hold'em rules with hole cards, flop, turn, river, and betting rounds
- **AI Opponents**: Multiple AI players with different personalities (cautious to aggressive)
- **Circular Table Layout**: Beautiful circular/oval poker table with players arranged using polar coordinates
- **Visual Design**: Green felt table, face-down cards for AI, community cards in center
- **Hand Evaluation**: Advanced hand strength calculation for proper poker gameplay
- **Bluffing AI**: AI players can bluff and make strategic decisions based on hand strength
- **Active Player Highlighting**: Current player highlighted with cyan border

## Project Structure

```
src/
├── Poker.java           # Main entry point
├── model/               # Core data classes
│   ├── Card.java       # Playing card representation
│   ├── Deck.java       # 52-card deck with shuffle
│   ├── Hand.java       # Hand evaluation and management
│   └── Player.java     # Player with chips, hand, and status
├── logic/               # Game logic
│   └── TexasHoldem.java # Main game controller
├── ai/                  # AI decision-making
│   └── PokerAI.java    # AI opponent logic
└── ui/                  # User interface
    └── PokerGUI.java   # Swing-based graphical interface
```

## How to Compile

### Windows (PowerShell)
```powershell
cd "path\to\Poker"
javac -d bin src/*.java src/model/*.java src/ai/*.java src/logic/*.java src/ui/*.java
```

### Mac/Linux
```bash
cd path/to/Poker
javac -d bin src/*.java src/model/*.java src/ai/*.java src/logic/*.java src/ui/*.java
```

## How to Run

### Direct Execution
```powershell
java -cp bin Poker
```

Or:
```powershell
java -cp bin ui.PokerGUI
```

## Creating an Executable JAR

### Method 1: Using jar command
1. Compile the project as shown above
2. Create a manifest file `manifest.txt`:
```
Main-Class: Poker
```

3. Create the JAR:
```powershell
jar cvfm Poker.jar manifest.txt -C bin .
```

4. Run the JAR:
```powershell
java -jar Poker.jar
```

### Method 2: Using jpackage (Java 17+)
```powershell
jpackage --input . --name Poker --main-jar Poker.jar --main-class Poker
```

### Method 3: Using Launch4j (Windows only)
1. Install Launch4j from http://launch4j.sourceforge.net/
2. Configure Launch4j to point to your JAR file
3. Export as .exe file

## How to Play

1. **Start**: The game begins with 4 players (you + 3 AI opponents)
2. **Blinds**: Small blind (10 chips) and big blind (20 chips) are posted
3. **Your Turn**: Choose an action:
   - **Fold**: Give up your hand
   - **Check**: Pass (only if no bet to call)
   - **Call**: Match the current bet
   - **Raise**: Increase the bet by your chosen amount
4. **Game Flow**:
   - Pre-flop: Betting with your 2 hole cards
   - Flop: 3 community cards dealt, betting round
   - Turn: 4th community card, betting round
   - River: 5th community card, final betting round
   - Showdown: Best hand wins the pot
5. **Winning**: The player with the best 5-card combination wins
6. **New Hand**: Automatically starts after each showdown

## AI Behavior

The AI opponents use personality-based decision making:
- **Hand Strength**: Evaluates their best possible hand (0.0 - 1.0)
- **Personality**: Each AI has an aggression level (0.3 cautious to 0.9 aggressive)
- **Strategy**:
  - Strong hands (>0.8): Aggressive betting
  - Good hands (>0.6): Call or small raise
  - Medium hands (>0.4): Call or occasional bluff
  - Weak hands (<0.4): Fold or rare aggressive bluff
- **Bluffing**: AI occasionally bluffs with weak hands based on personality

## Requirements

- Java 17 or later
- Java Swing (included in JDK)
- No external libraries required

## Technical Details

### Hand Evaluation
The game evaluates all possible 5-card combinations from hole cards + community cards and returns the best hand strength normalized to 0.0-1.0.

### Betting Rounds
- Proper bet matching and raising logic
- All-in support
- Side pot calculations (basic)
- Betting round completion detection

### Card Display
- Unicode card suit symbols: ♠ ♥ ♦ ♣
- Face-down blue card backs for AI players until showdown
- Community cards displayed horizontally in center of table
- Cards color-coded by suit (red hearts/diamonds, black spades/clubs)
- Circular table layout with players evenly spaced around the table

## Future Enhancements

- [ ] Sound effects
- [ ] Leaderboard tracking
- [ ] Save/load system
- [ ] Card reveal animations
- [ ] Multiplayer support (network play)
- [ ] Tournaments mode
- [ ] Better visual card graphics
- [ ] Statistics tracking

## License

This project is provided as-is for educational purposes.

