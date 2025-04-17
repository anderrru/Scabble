package Model;

public class Player {

    private final String name;
    private final Hand hand;

    public Player(String name) {
        this.name = name;
        this.hand = new Hand(); 
    }

    // Returns the player's name
    public String getName() {
        return name;
    }

    // Returns the full Hand object 
    public Hand getHand() {
        return hand;
    }

    // Returns a string display of the player's hand 
    public String getPlayerHand() {
        return hand.toString();
    }
}
