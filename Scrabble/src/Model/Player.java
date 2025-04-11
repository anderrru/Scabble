package Model;

public class Player {
	private String name;
	private GamePiece[] playerHand;
	
	public Player(String name) {
		this.name = name;
		this.playerHand = new GamePiece[7];
		fillPlayerHand();
	}
	
	private void fillPlayerHand() {
		for (int i = 0; i < 7; i++) {
			playerHand[i] = GamePiece.getPiece();
		}
	}
	
	public String getName() {
		return name;
	}
	
	public String getPlayerHand() {
		String output = "| ";
		for (GamePiece g : playerHand) {
			output += g.getLetter() + " | ";
		}
		
		return output.trim();
	}
}
