package Model;

public class TestyTesty {
	
	public static void main(String[] args) {
		GameBoard board = new GameBoard();
		//System.out.println(board);
		
		//GamePiece a1 = GamePiece.getPiece("A1");
		//System.out.println(a1);
		
		//board.placeGamePiece(0, 1, a1);
		//System.out.println(board);
		
		Player p1 = new Player("Johnathan Alexander");
		System.out.println(p1.getPlayerHand());
	}

}
