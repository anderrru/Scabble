# Scabble

A moddified pass and play game of Scrabble. Play with up to 4 players, player with the highest score wins, make it to the top of the leaderboard by having the most wins.
To run the code from source, run the main method in GameVisualGUI and you will be prompted to enter the players and start the game of Scabble. The game ends when all players
have voted to end the game, and the person with the highest score gets the win while everyone else looses.

# The Design
This program uses a variety of data structures to solve the problems of implementing scrabble. For example a HashMap is used for the valid words since there is 200k + words, the hashmap was useful for indexing words directly. HashMaps were also used for the player move that represent the postion of the move with its value being the gamepiece at the position. ArrayLists were useful for a few things such as sorting the players moves (mentioned in strategy), and sorting the players by their score.

Flyweight
  -
  When program is ran, a static store is created the represent the gamepiece bag with all 98 pieces. The program uses this store for the gamepieces for any game played.

Iterator
  -
  The player class utilizes the iterator design pattern for being able to iterate through player hand without giving outside access to the player hand ArrayList

Strategy
  -
  The Strategy Design patter was useful for how player moves is implemented. In order to check the validity of a word, the moves at position x,y needs to be sorted by either x or y
  so strategy was useful for having multiple ways of sorting the player move.

Anti-Pattern Avoidance
  -
  Enums were very helpful to avoid the primitive obsession antipattern. For example, since point values of letters can only go up to 8 points, using int wouldnt make sense so an enum of points one-eight was used to    represent the point values.

Encapsulation
  -
  All classes instance variables were kept private, using setters and getters where it makes sense. getters are mainly used for immutable objects and for things that need to access objects like arraylists, iterator
  desing patter was used (like the player hand example)

Composition
  -
  * Player has a hand
  * GameBoard has 15 x 15 tiles
  * tile has 0 or 1 GamePiece
