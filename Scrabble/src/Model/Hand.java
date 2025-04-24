/*
* Authors: Johnathan Alexander
*
* Description: This file simulates a player's hand of Scrabble tiles. Each player hand
*              contains seven GamePiece objects, equal to the number of Scrabble tiles
*              a player would have in a real game of Scrabble. It also contains functionality
*              to swap tiles from a player's hand, in the case they can't form words with
*              their given tiles. 
*/

package Model;

import java.util.ArrayList;
import java.util.List;

public class Hand {

    private static final int MAX_SIZE = 7;
    private final List<GamePiece> tiles;

    // Constructor
    public Hand() {
        tiles = new ArrayList<>();
        fillFromBag(); 
    }

    public void fillFromBag() {
        // This method fills the hand with up to 7 GamePiece objects
        while (tiles.size() < MAX_SIZE) {
            GamePiece piece = GamePiece.getPiece(); // Gets a random GamePiece
            if (piece != null) {
                tiles.add(piece);
            } else {
                break; 
            }
        }
    }

    public boolean swapTiles(List<GamePiece> toSwap) {
        /*
        * This method allows a player to swap tiles in their hand
        * for new ones from the static store, and checks
        * to ensure that the static store has enough pieces to re-fill
        * the player hand afterward. 
        */

        // Checks the static store to ensure sufficient GamePieces remain
        if (toSwap.size() > GamePiece.remainingTiles()) {
            return false; 
        }

        // Checks to ensure all pieces the player desire to swap exist within their hand
        for (GamePiece g : toSwap) {
            if (!tiles.contains(g)) {
                return false; 
            }
        }

        // Removes all tiles the player desires to swap from the player hand
        for (GamePiece g : toSwap) {
            tiles.remove(g);
           
        }

        // Refills the player hand
        fillFromBag(); 
        return true;
    }

    public boolean add(GamePiece piece) {
        // This method adds a tile to the hand if there's space
        if (tiles.size() < MAX_SIZE) {
            tiles.add(piece);
            return true;
        }
        return false;
    }

    public boolean remove(GamePiece piece) {
        // This method removes a GamePiece from the hand
        return tiles.remove(piece);
    }

    public int size() {
        // This method returns the number of tiles in hand
        return tiles.size();
    }

    public void clear() {
        // This method clears all GamePiece objects from hand
        tiles.clear();
    }

    public List<GamePiece> getTiles() {
        // This method returns a List of the current GamePiece objects in hand
        return tiles;
    }

    // String display of hand
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Hand: ");
        for (GamePiece g : tiles) {
            sb.append(g.getLetter()).append(" ");
        }
        return sb.toString().trim();
    }
}
