package Model;

import java.util.ArrayList;
import java.util.List;

public class Hand {

    private static final int MAX_SIZE = 7;
    private final List<GamePiece> tiles;

    public Hand() {
        tiles = new ArrayList<>();
        fillFromBag();
    }

    // Fills hand with up to 7 GamePieces
    public void fillFromBag() {
        while (tiles.size() < MAX_SIZE) {
            GamePiece piece = GamePiece.getPiece();
            if (piece != null) {
                tiles.add(piece);
            } else {
                break; 
            }
        }
    }

    // Swap selected tiles for new ones (with pool check)
    public boolean swapTiles(List<GamePiece> toSwap) {
        if (toSwap.size() > GamePiece.remainingTiles()) {
            return false; 
        }

        for (GamePiece g : toSwap) {
            if (!tiles.contains(g)) {
                return false; 
            }
        }

        for (GamePiece g : toSwap) {
            tiles.remove(g);
           
        }

        fillFromBag(); 
        return true;
    }

    // Add a tile to hand if there's space
    public boolean add(GamePiece piece) {
        if (tiles.size() < MAX_SIZE) {
            tiles.add(piece);
            return true;
        }
        return false;
    }

    // Remove a specific tile
    public boolean remove(GamePiece piece) {
        return tiles.remove(piece);
    }

    // Return number of tiles in hand
    public int size() {
        return tiles.size();
    }

    // Clear all tiles from hand
    public void clear() {
        tiles.clear();
    }

    // Return current tiles in hand
    public List<GamePiece> getTiles() {
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
