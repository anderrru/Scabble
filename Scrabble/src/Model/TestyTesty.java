package Model;

import java.util.Scanner;

public class TestyTesty {
	
	public static void main(String[] args) {
		GameBoard board = new GameBoard();
		Player p1 = new Player("Johnathan Alexander");
		
		System.out.println(board);
		System.out.println(p1.getPlayerHand());
		
		//GamePiece a1 = GamePiece.getPiece("A1");
		//System.out.println(a1);
		
		//board.placeGamePiece(0, 1, a1);
		//System.out.println(board);
		Scanner userScanner = new Scanner(System.in);
		/*String letter = "";
		int x = 0;
		int y = 0;
		
		while (!letter.equals("exit")) {
			letter = userScanner.nextLine().trim();
			x = Integer.valueOf(userScanner.nextLine()) - 1;
			y = Integer.valueOf(userScanner.nextLine()) - 1;
			
			board.placeGamePiece(x, y, p1.getLetter(letter), p1);
			
			System.out.println(board);
			System.out.println(p1.getPlayerHand());
			//input = userScanner.nextLine();
		}*/
		
		// Now lets try for a whole word placement.
		
		
		String word = "";
		int x = 0;
		int y = 0;
		Move.Directions direction = null;
		
		while (!word.equals("exit")) {
			word = userScanner.nextLine().trim();
			x = Integer.valueOf(userScanner.nextLine()) - 1;
			y = Integer.valueOf(userScanner.nextLine()) - 1;
			String dir = userScanner.nextLine();
			if (dir.toLowerCase().equals("horizontal")) direction = Move.Directions.Horizontal;
			else if (dir.toLowerCase().equals("vertical")) direction = Move.Directions.Vertical;
			
			for (String c : word.split("")) {
				board.placeGamePiece(x, y, p1.getLetter(c), p1);
				
				if (direction.equals(Move.Directions.Horizontal)) x++;
				else if (direction.equals(Move.Directions.Vertical)) y++;
				
			}
			
			System.out.println(board);
			System.out.println(p1.getPlayerHand());
			
			p1.fillPlayerHand();
			
			System.out.println(p1.getPlayerHand());
			
		}
		
		
	}

}
