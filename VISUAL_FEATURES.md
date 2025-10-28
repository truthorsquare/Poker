# Visual Features Documentation

## Circular Table Layout

The poker game features a beautiful circular/oval table with players arranged evenly around it using **polar coordinates**.

### Table Design

- **Green Felt Table**: Semi-transparent green oval in the center
- **Brown Table Edge**: Dark brown border around the table
- **Dynamic Sizing**: Table adapts to window size

### Player Positioning

Players are positioned around the table using polar coordinates:

```java
// Calculate position
angle = (2 * PI / number_of_players) * player_index
x = centerX + radius * cos(angle)
y = centerY + radius * sin(angle)
```

**Layout Examples**:
- 2 players → Opposite sides (180° apart)
- 3 players → Triangle (120° apart)
- 4 players → Diamond/square (90° apart)
- 6 players → Hexagon (60° apart)

### Player Display Elements

Each player has:
1. **Name Label**: Player's name in bold
2. **Chip Count**: Yellow text showing remaining chips
3. **Card Display**: Two hole cards below player info
4. **Status Indicators**: 
   - "FOLDED" in red if folded
   - "ALL IN" if all-in
5. **Active Player Highlight**: Cyan border when it's their turn

### AI vs Human Player Cards

- **Human Player Cards**: Always visible, face-up with rank and suit
- **AI Player Cards**: Face-down blue card backs until showdown
  - Blue background with white border
  - Diagonal lines pattern
  - Revealed at showdown for all players

### Community Cards

- **Location**: Center of the table, horizontally aligned
- **Appearance**: White cards with black border
- **Progression**: 
  - Flop: First 3 cards appear
  - Turn: 4th card added
  - River: 5th card added (complete hand)

### Pot Display

- **Location**: Center of table, below community cards
- **Style**: Large yellow text, bold font
- **Updates**: Real-time as players bet

### Color Scheme

- **Table Background**: Green felt (#006400)
- **Table Edge**: Brown (#8B5A2B)
- **Player Backgrounds**: 
  - Human: Dark red with transparency
  - AI: Dark blue with transparency
- **Card Colors**:
  - Hearts/Diamonds: Red text
  - Spades/Clubs: Black text
- **Status Colors**:
  - Active player: Cyan border
  - Folded: Red border + red text
  - Pot/Chips: Yellow

### Responsive Design

- Table and player positions automatically adjust to window size
- Cards scale proportionally
- All text scales appropriately

### Game Flow Visualization

1. **Pre-Flop**: Players see only their hole cards + community cards empty
2. **Flop**: 3 community cards appear in center
3. **Turn**: 4th community card added
4. **River**: Final community card added
5. **Showdown**: All AI cards revealed, winner announced

### Visual Feedback

- **Button States**: Buttons grayed out when not player's turn
- **Action Messages**: Top of screen shows current action
- **Call Amount**: Displayed on call button (e.g., "Call (20)")
- **Highlight System**: Active player highlighted with cyan border
- **Card Reveal**: Smooth transition from face-down to face-up at showdown

