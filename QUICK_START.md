# Quick Start Guide - Texas Hold'em Poker

## Run the Game

### Option 1: Run the JAR file (Easiest)
```powershell
java -jar Poker.jar
```

### Option 2: Use the run script
Double-click `run.bat` or run from command line:
```powershell
.\run.bat
```

### Option 3: Run directly from compiled classes
```powershell
java -cp bin Poker
```

## Build from Source

### Option 1: Use the build script
Double-click `build.bat` or run from command line:
```powershell
.\build.bat
```

### Option 2: Manual compilation
```powershell
javac -d bin src/*.java src/model/*.java src/ai/*.java src/logic/*.java src/ui/*.java
```

## Game Controls

- **Fold**: Give up your hand (lose current bet)
- **Check**: Pass when there's no bet to call
- **Call**: Match the current highest bet
- **Raise**: Increase the bet by your selected amount

## Game Flow

1. Each hand starts with blinds (small blind: 10, big blind: 20)
2. You and 3 AI opponents receive 2 hole cards
3. Betting rounds:
   - Pre-flop (with only your cards)
   - Flop (after 3 community cards)
   - Turn (after 4th community card)
   - River (after 5th community card)
4. Showdown: Best hand wins the pot
5. New hand starts automatically

## Tips

- The AI opponents have different personalities (cautious to aggressive)
- Watch their betting patterns to guess their hand strength
- Your cards are shown, AI cards are hidden until showdown
- Each player starts with 1000 chips

Enjoy playing!

