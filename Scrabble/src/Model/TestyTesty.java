package Model;


import java.util.Scanner;
import java.util.ArrayList;

public class TestyTesty {
	
	public static void main(String[] args) {
		GameBoard board = new GameBoard();
		Player p1 = new Player("Johnathan Alexander");
		Player p2 = new Player("Erik Picazzo");
		Scanner myScanner = new Scanner(System.in);
		
		
		//GamePiece a1 = GamePiece.getPiece("A1");
		//System.out.println(a1);
		
		//board.placeGamePiece(0, 1, a1);
		//System.out.println(board);
		
		System.out.println(board);
		System.out.println(p1.getPlayerHand());
		//String input = myScanner.nextLine().toLowerCase().trim();
		
		String word = "";
		int x = 0;
		int y = 0;
		Move.Directions direction = null;
		
		boolean playerTurn = false;
		boolean isFirstMove = true;
		
		while (!word.equals("exit")) {
			word = myScanner.nextLine().toUpperCase().trim();
			x = Integer.valueOf(myScanner.nextLine()) - 1;
			y = Integer.valueOf(myScanner.nextLine()) - 1;
			String dir = myScanner.nextLine().toLowerCase().trim();
			
			if (dir.equals("horizontal")) direction = Move.Directions.Horizontal;
			else direction = Move.Directions.Vertical;
			
			Move playerMove = new Move();
			playerMove.setPlacement(x, y, direction);
			
			ArrayList<GamePiece> playerPieces;
			GamePiece g;
			int invalidMove = 0;
			ArrayList<GamePiece> usedLetters = new ArrayList<>();
			
			if (playerTurn == false) playerPieces = p1.getPlayerPieces();
			else playerPieces = p2.getPlayerPieces();
			
			for (String s : word.split("")) {
				if (playerTurn == false) g = p1.getPiece(s);
				else g = p2.getPiece(s);
				
				if (g != null) {
					playerMove.addPiece(g, x, y);
					usedLetters.add(g);
					System.out.println(usedLetters);
				}
				else {
					// Add a pre-check here to see if the board at position contains the letter matching s, and continue from there
					if ((board.getTile(y, x).getPiece() != null) && (board.getTile(y, x).getPiece().getLetter().equals(s))) {
						System.out.println("Existing tile same as desired placement");
						System.out.println(board.getTile(y, x).getPiece());
						playerMove.addPiece(board.getTile(y, x).getPiece(), x, y);
						System.out.println(playerMove.getMove());
						for (GamePiece n : usedLetters) {
							if (n.getLetter().equals(board.getTile(y, x).getPiece().getLetter())) {
								if (playerTurn == false) p1.addToHand(n);
								else p2.addToHand(n);
							}
						}
					}
					else {
						System.out.println("You don't have the letter " + s + " in your hand");
						invalidMove = 1;
						playerMove.clear();
						
						for (GamePiece p : usedLetters) {
							if (playerTurn == false) p1.addToHand(p);
							else p2.addToHand(p);
						}
						
						// Add a little tidbit here that lets the player try again if this happens. I.e. it doesn't switch the
						// player turn
						break;
					}
					
				}
		
				if (direction.equals(Move.Directions.Horizontal)) x++;
				else y++;
			}
			
			if (invalidMove == 0) {
				board.playerMove(playerMove, isFirstMove);
				isFirstMove = false;
				
				System.out.println(board);
				if (playerTurn == false) {
					p1.fillPlayerHand();
					System.out.println(p2.getPlayerHand());
				}
				else {
					p2.fillPlayerHand();
					System.out.println(p1.getPlayerHand());
				}
				
				// Changes player turn
				playerTurn = !playerTurn;
			}
			
			usedLetters.clear();
			
			
		}
		
		
	}

}
